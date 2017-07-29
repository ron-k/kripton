/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.abubusoft.kripton.processor.sqlite;

import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.typeName;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.annotation.BindSqlDelete;
import com.abubusoft.kripton.android.annotation.BindSqlUpdate;
import com.abubusoft.kripton.android.sqlite.ConflictAlgorithmType;
import com.abubusoft.kripton.common.One;
import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import com.abubusoft.kripton.processor.BaseProcessor;
import com.abubusoft.kripton.processor.core.AnnotationAttributeType;
import com.abubusoft.kripton.processor.core.AssertKripton;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.reflect.AnnotationUtility;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.exceptions.InvalidMethodSignException;
import com.abubusoft.kripton.processor.sqlite.core.GenerationPartMarks;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQL.JQLDynamicStatementType;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQL.JQLType;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker.JQLParameterName;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLPlaceHolder;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLReplaceVariableStatementListenerImpl;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLReplacerListenerImpl;
import com.abubusoft.kripton.processor.sqlite.grammars.jsql.JqlParser.Where_stmtContext;
import com.abubusoft.kripton.processor.sqlite.grammars.uri.ContentUriPlaceHolder;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteDatabaseSchema;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import android.content.ContentValues;
import android.net.Uri;

/**
 * @author Francesco Benincasa (abubusoft@gmail.com)
 *
 *
 * @since 05/mag/2016
 */
public abstract class SqlModifyBuilder {

	public enum ModifyType {
		UPDATE_BEAN(ModifyBeanHelper.class, true), UPDATE_RAW(ModifyRawHelper.class,
				true), DELETE_BEAN(ModifyBeanHelper.class, false), DELETE_RAW(ModifyRawHelper.class, false);

		private ModifyCodeGenerator codeGenerator;

		private boolean update;

		/**
		 * if true, map cursor fields to bean attributes.
		 * 
		 * @return the mapFields
		 */
		public boolean isUpdate() {
			return update;
		}

		private ModifyType(Class<? extends ModifyCodeGenerator> codeGenerator, boolean updateValue) {
			try {
				this.update = updateValue;
				this.codeGenerator = codeGenerator.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new KriptonRuntimeException(e);
			}
		}

		public void generate(Elements elementUtils, MethodSpec.Builder methodBuilder, SQLiteModelMethod method,
				TypeName returnType) {
			codeGenerator.generate(elementUtils, methodBuilder, isUpdate(), method, returnType);

		}
	}

	public interface ModifyCodeGenerator {
		void generate(Elements elementUtils, MethodSpec.Builder methodBuilder, boolean mapFields,
				SQLiteModelMethod method, TypeName returnType);
	}

	/**
	 * 
	 * @param elementUtils
	 * @param builder
	 * @param method
	 * @param updateMode
	 */
	public static void generate(Elements elementUtils, Builder builder, SQLiteModelMethod method) {

		ModifyType updateResultType = detectModifyType(method, method.jql.operationType);

		// if true, field must be associate to ben attributes
		TypeName returnType = method.getReturnClass();

		if (updateResultType == null) {
			throw (new InvalidMethodSignException(method));
		}

		// generate method code
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName()).addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC);
		ParameterSpec parameterSpec;
		for (Pair<String, TypeName> item : method.getParameters()) {
			parameterSpec = ParameterSpec.builder(item.value1, item.value0).build();
			methodBuilder.addParameter(parameterSpec);
		}
		methodBuilder.returns(returnType);

		// generate inner code
		updateResultType.generate(elementUtils, methodBuilder, method, returnType);

		MethodSpec methodSpec = methodBuilder.build();
		builder.addMethod(methodSpec);

		if (method.contentProviderEntryPathEnabled) {
			// we need to generate UPDATE or DELETE for content provider to
			generateModifierForContentProvider(elementUtils, builder, method, updateResultType);
		}

	}

	/**
	 * Detect method type
	 * 
	 * @param method
	 * @param jqlType
	 *            jql type is necessary because method.jql can be not properly
	 *            initialized
	 * @return
	 */
	public static ModifyType detectModifyType(SQLiteModelMethod method, JQLType jqlType) {
		Elements elementUtils = BaseProcessor.elementUtils;
		SQLDaoDefinition daoDefinition = method.getParent();
		SQLEntity entity = daoDefinition.getEntity();

		ModifyType updateResultType = null;

		// check type of arguments
		int count = 0;
		for (Pair<String, TypeName> param : method.getParameters()) {
			if (method.isThisDynamicWhereConditionsName(param.value0)) {
				// if current parameter is dynamic where, skip it
				continue;
			}

			if (TypeUtility.isEquals(param.value1, typeName(entity.getElement()))) {
				count++;
			}
		}

		if (count == 0) {
			// method to update raw data: no bean is used
			updateResultType = jqlType == JQLType.UPDATE ? ModifyType.UPDATE_RAW : ModifyType.DELETE_RAW;

			ModelAnnotation annotation;

			if (jqlType == JQLType.UPDATE) {
				annotation = method.getAnnotation(BindSqlUpdate.class);

				AssertKripton
						.assertTrueOrInvalidMethodSignException(
								AnnotationUtility.extractAsStringArray(elementUtils, method, annotation,
										AnnotationAttributeType.FIELDS).size() == 0,
								method, " can not use attribute %s in this kind of query definition",
								AnnotationAttributeType.FIELDS.getValue());
				AssertKripton.assertTrueOrInvalidMethodSignException(
						AnnotationUtility.extractAsStringArray(elementUtils, method, annotation,
								AnnotationAttributeType.EXCLUDED_FIELDS).size() == 0,
						method, " can not use attribute %s in this kind of query definition",
						AnnotationAttributeType.EXCLUDED_FIELDS.getValue());

			} else {
				annotation = method.getAnnotation(BindSqlDelete.class);
			}

			// check if there is only one parameter
			AssertKripton.failWithInvalidMethodSignException(method.getParameters().size() > 1
					&& TypeUtility.isEquals(method.getParameters().get(0).value1, daoDefinition.getEntityClassName()),
					method);

		} else if (count == 1) {
			updateResultType = jqlType == JQLType.UPDATE ? ModifyType.UPDATE_BEAN : ModifyType.DELETE_BEAN;

			// with dynamic where conditions, we have to add 1 to parameter
			// check size (one parameter is a bean and one is the where
			// conditions)
			AssertKripton.assertTrueOrInvalidMethodSignException(
					method.getParameters().size() == 1 + (method.hasDynamicWhereConditions() ? 1 : 0), method,
					" expected only one parameter of %s type", daoDefinition.getEntityClassName());
		} else {
			throw (new InvalidMethodSignException(method,
					"only one parameter of type " + typeName(entity.getElement()) + " can be used"));
		}
		return updateResultType;
	}

	/**
	 * <p>
	 * Generate update and delete used in content provider class.
	 * </p>
	 * 
	 * @param elementUtils
	 * @param builder
	 * @param method
	 * @param updateResultType
	 */
	private static void generateModifierForContentProvider(Elements elementUtils, Builder builder,
			final SQLiteModelMethod method, ModifyType updateResultType) {
		final SQLDaoDefinition daoDefinition = method.getParent();
		final SQLEntity entity = daoDefinition.getEntity();
		final Set<String> columns = new LinkedHashSet<>();

		JQLChecker jqlChecker = JQLChecker.getInstance();

		// parameters extracted from query
		final One<String> whereStatement = new One<>();

		// put in whereStatement value of where statement.
		jqlChecker.replaceVariableStatements(method.jql.value, new JQLReplaceVariableStatementListenerImpl() {

			@Override
			public String onWhere(String statement) {
				whereStatement.value0 = statement;
				return "";
			}

		});

		List<JQLPlaceHolder> placeHolders = jqlChecker.extractFromVariableStatement(whereStatement.value0);
		// remove placeholder for dynamic where, we are not interested here
		placeHolders = SqlBuilderHelper.removeDynamicPlaceHolder(placeHolders);
		AssertKripton.assertTrue(placeHolders.size() == method.contentProviderUriVariables.size(),
				"In '%s.%s' content provider URI path variables and variables in where conditions are different",
				daoDefinition.getName(), method.getName());

		// detect column used for content value
		jqlChecker.replace(method.jql, new JQLReplacerListenerImpl() {

			@Override
			public String onColumnNameToUpdate(String columnName) {
				SQLProperty tempProperty = entity.get(columnName);
				AssertKripton.assertTrueOrUnknownPropertyInJQLException(tempProperty != null, method, columnName);

				columns.add(tempProperty.columnName);
				return null;
			}

		});

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.contentProviderMethodName);

		// params
		methodBuilder.addParameter(ParameterSpec.builder(Uri.class, "uri").build());

		if (updateResultType == ModifyType.UPDATE_BEAN || updateResultType == ModifyType.UPDATE_RAW) {
			methodBuilder.addParameter(ParameterSpec.builder(ContentValues.class, "contentValues").build());
		}

		methodBuilder.addParameter(ParameterSpec.builder(String.class, "selection").build());
		methodBuilder.addParameter(ParameterSpec.builder(ArrayTypeName.of(String.class), "selectionArgs").build());
		methodBuilder.returns(Integer.TYPE);

		SqlBuilderHelper.generateLogForContentProviderBeginning(method, methodBuilder);

		// query builder
		methodBuilder.addStatement("$T _sqlBuilder=new $T()", StringBuilder.class, StringBuilder.class);
		generateInitForDynamicWhereVariables(method, methodBuilder, "selection", "selectionArgs");

		SqlBuilderHelper.generateWhereCondition(methodBuilder, method, false);

		int i = 0;
		// extract pathVariables
		// every controls was done in constructor of SQLiteModelMethod
		for (ContentUriPlaceHolder variable : method.contentProviderUriVariables) {
			AssertKripton.assertTrue(SqlBuilderHelper.validate(variable.value, placeHolders, i),
					"In '%s.%s' content provider URI path variables and variables in where conditions are different",
					daoDefinition.getName(), method.getName());

			JQLParameterName paramName = JQLParameterName.parse(variable.value);
			SQLProperty entityProperty = entity.get(paramName.getValue());

			if (entityProperty != null) {
				methodBuilder.addCode("// Add parameter $L at path segment $L\n", variable.value,
						variable.pathSegmentIndex);
				methodBuilder.addStatement("_sqlWhereParams.add(uri.getPathSegments().get($L))",
						variable.pathSegmentIndex);
				AssertKripton.assertTrue(
						TypeUtility.isTypeIncludedIn(entityProperty.getPropertyType().getTypeName(), String.class,
								Long.class, Long.TYPE),
						"In '%s.%s' content provider URI path variables %s must be String of Long type",
						daoDefinition.getName(), method.getName(), entityProperty.getName());
			}

			i++;
		}

		if (method.hasDynamicWhereConditions() && method.hasDynamicWhereArgs()) {
			// ASSERT: only with dynamic where conditions
			methodBuilder.beginControlFlow("if ($T.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null)",
					StringUtils.class);

			if (method.hasDynamicWhereConditions()) {
				methodBuilder.beginControlFlow("for (String _arg: _sqlDynamicWhereArgs)");
				methodBuilder.addStatement("_sqlWhereParams.add(_arg)");
				methodBuilder.endControlFlow();
			}

			methodBuilder.endControlFlow();
		}

		// column checj
		switch (updateResultType) {
		case UPDATE_BEAN:
		case UPDATE_RAW:
			SqlBuilderHelper.generateColumnCheckSet(elementUtils, builder, method, columns);
			SqlBuilderHelper.forEachColumnInContentValue(method, methodBuilder, "contentValues.keySet()", true, null);
			break;
		default:
			break;
		}

		generateLogForModifiers(method, methodBuilder);

		if (method.jql.operationType == JQLType.UPDATE) {
			// generate log for content values
			SqlBuilderHelper.generateLogForContentValues(method, methodBuilder);
		}

		// log for where parames
		SqlBuilderHelper.generateLogForWhereParameters(method, methodBuilder);

		methodBuilder.addCode("\n// execute SQL\n");
		switch (updateResultType) {
		case DELETE_BEAN:
		case DELETE_RAW:
			methodBuilder.addStatement(
					"int result = database().delete($S, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]))",
					daoDefinition.getEntity().getTableName());
			break;
		case UPDATE_BEAN:
		case UPDATE_RAW:
			if (method.jql.conflictAlgorithmType == ConflictAlgorithmType.NONE) {
				methodBuilder.addStatement(
						"int result = database().update($S, contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]))",
						daoDefinition.getEntity().getTableName());
			} else {
				methodBuilder.addCode("// conflict algorithm $L\n", method.jql.conflictAlgorithmType);
				methodBuilder.addStatement(
						"int result = database().updateWithOnConflict($S, contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]), $L)",
						daoDefinition.getEntity().getTableName(),
						method.jql.conflictAlgorithmType.getConflictAlgorithm());
			}
			break;
		}

		methodBuilder.addStatement("return result");

		// we add at last javadoc, because need info is built at last.
		SqlBuilderHelper.generateJavaDocForContentProvider(method, methodBuilder);

		methodBuilder.addJavadoc("@param uri $S\n", method.contentProviderUriTemplate.replace("*", "[*]"));
		switch (updateResultType) {
		case UPDATE_BEAN:
		case UPDATE_RAW:
			methodBuilder.addJavadoc("@param contentValues content values\n");
			break;
		default:
			break;
		}
		methodBuilder.addJavadoc("@param selection dynamic part of <code>where</code> statement $L\n",
				method.hasDynamicWhereConditions() ? "" : "<b>NOT USED</b>");
		methodBuilder.addJavadoc("@param selectionArgs arguments of dynamic part of <code>where</code> statement $L\n",
				method.hasDynamicWhereConditions() ? "" : "<b>NOT USED</b>");

		methodBuilder.addJavadoc("@return number of effected rows\n");

		builder.addMethod(methodBuilder.build());
	}

	/**
	 * @param method
	 * @param methodBuilder
	 */
	static void generateInitForDynamicWhereVariables(SQLiteModelMethod method, MethodSpec.Builder methodBuilder,
			String dynamiWhereName, String dynamicWhereArgsName) {
		GenerationPartMarks.begin(methodBuilder, GenerationPartMarks.CODE_001);
		// where _sqlDynamicWhere and _sqlDynamicWhereArgs
		if (method.hasDynamicWhereConditions()) {
			methodBuilder.addCode("// initialize dynamic where\n");
			methodBuilder.addStatement("String _sqlDynamicWhere=$L", dynamiWhereName);
		}
		if (method.hasDynamicWhereArgs()) {
			methodBuilder.addCode("// initialize dynamic where args\n");
			methodBuilder.addStatement("String[] _sqlDynamicWhereArgs=$L", dynamicWhereArgsName);
		}
		GenerationPartMarks.end(methodBuilder, GenerationPartMarks.CODE_001);

	}

	/**
	 * @param method
	 * @param methodBuilder
	 * @param schema
	 * @param entity
	 * @param jqlChecker
	 */
	public static void generateLogForModifiers(final SQLiteModelMethod method, MethodSpec.Builder methodBuilder) {
		final SQLiteDatabaseSchema schema = method.getParent().getParent();

		// display log
		if (schema.generateLog) {

			//final SQLEntity entity = method.getParent().getEntity();
			JQLChecker jqlChecker = JQLChecker.getInstance();

			final One<Boolean> usedInWhere = new One<Boolean>(false);

			methodBuilder.addCode("\n// display log\n");

			String sqlForLog = jqlChecker.replace(method.jql, new JQLReplacerListenerImpl() {

				@Override
				public String onBindParameter(String bindParameterName) {
					if (!usedInWhere.value0) {
						// String
						// realName=method.findParameterNameByAlias(bindParameterName);
						// String columnName = entity.get(realName).columnName;
						return ":" + bindParameterName;
					} else {
						return "?";
					}
				}

				@Override
				public String onTableName(String tableName) {
					SQLEntity entity = schema.getEntityBySimpleName(tableName);
					AssertKripton.assertTrueOrUnknownClassInJQLException(entity != null, method, tableName);
					return entity.getTableName();
				}

				@Override
				public void onWhereStatementBegin(Where_stmtContext ctx) {
					usedInWhere.value0 = true;
				}

				public void onWhereStatementEnd(Where_stmtContext ctx) {
					usedInWhere.value0 = false;
				};

			});

			if (method.jql.dynamicReplace.containsKey(JQLDynamicStatementType.DYNAMIC_WHERE)) {
				methodBuilder.addStatement("$T.info($S, $L)", Logger.class,
						sqlForLog.replace(method.jql.dynamicReplace.get(JQLDynamicStatementType.DYNAMIC_WHERE), "%s"),
						"StringUtils.ifNotEmptyAppend(_sqlDynamicWhere,\" AND \")");
			} else {
				methodBuilder.addStatement("$T.info($S)", Logger.class, sqlForLog);
			}

		}
	}

}
