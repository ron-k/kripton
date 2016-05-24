package com.abubusoft.kripton.processor.sqlite;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.util.Elements;

import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.ModelMethod;
import com.abubusoft.kripton.processor.core.reflect.AnnotationUtility;
import com.abubusoft.kripton.processor.sqlite.exceptions.IncompatibleAttributesInAnnotationException;
import com.abubusoft.kripton.processor.sqlite.exceptions.PropertyInAnnotationNotFoundException;
import com.abubusoft.kripton.processor.sqlite.model.AnnotationAttributeType;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteDatabaseSchema;
import com.abubusoft.kripton.processor.sqlite.transform.Transformer;
import com.squareup.javapoet.MethodSpec.Builder;

public class CodeBuilderUtility {

	/**
	 * Generate code necessary to put bean properties in content values map. Return primary key
	 * 
	 * @param elementUtils
	 * @param model
	 * @param daoDefinition
	 * @param method
	 *            used to code generation
	 * @param checkProperty
	 *            if true, check if property used in query is present as attribute in bean
	 * @param alreadyUsedBeanPropertiesNames
	 *            optional
	 * @return primary key.
	 */
	public static Pair<String, List<SQLProperty>> generatePropertyList(Elements elementUtils, SQLiteDatabaseSchema model, SQLDaoDefinition daoDefinition, ModelMethod method, Class<? extends Annotation> annotationClazz,
			boolean checkProperty, Set<String> alreadyUsedBeanPropertiesNames) {
		Pair<String, List<SQLProperty>> result = new Pair<String, List<SQLProperty>>();
		result.value1 = new ArrayList<SQLProperty>();

		SQLEntity entity = model.getEntity(daoDefinition.getEntityClassName());

		ModelAnnotation annotation = method.getAnnotation(annotationClazz);

		// check included and excluded fields
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_VALUE);
		if (alreadyUsedBeanPropertiesNames != null) {
			includedFields.removeAll(alreadyUsedBeanPropertiesNames);
		}
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_EXCLUDED_FIELDS));

		if (includedFields.size() > 0 && excludedFields.size() > 0) {
			throw (new IncompatibleAttributesInAnnotationException(daoDefinition, method, annotation, AnnotationAttributeType.ATTRIBUTE_VALUE, AnnotationAttributeType.ATTRIBUTE_EXCLUDED_FIELDS));
		}

		StringBuilder buffer = new StringBuilder();
		String separator = "";

		if (checkProperty) {
			// check included
			for (String item : includedFields) {
				if (!entity.contains(item)) {
					throw (new PropertyInAnnotationNotFoundException(daoDefinition, method, item));
				}
			}
			// check excluded
			for (String item : excludedFields) {
				if (!entity.contains(item)) {
					throw (new PropertyInAnnotationNotFoundException(daoDefinition, method, item));
				}
			}

			// methodBuilder.addCode("contentValues.clear();\n\n");
			// for each property in entity except primaryKey and excluded properties
			for (SQLProperty item : entity.getCollection()) {
				if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
					continue;
				if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
					continue;

				buffer.append(separator + model.columnNameConverter.convert(item.getName()));
				result.value1.add(item);
				separator = ", ";
			}
		} else {
			// get fields from property
			if (includedFields.size() == 0) {
				for (SQLProperty item : entity.getCollection()) {
					includedFields.add(item.getName());
				}
				;
			}
			for (String item : includedFields) {
				buffer.append(separator + model.columnNameConverter.convert(item));
				result.value1.add(null);
				separator = ", ";
			}
		}

		result.value0 = buffer.toString();

		return result;

	}

	/**
	 * Generate code necessary to put bean properties in content values map. Return primary key
	 * 
	 * @param elementUtils
	 * @param model
	 * @param daoDefinition
	 * @param method
	 * @param methodBuilder
	 *            used to code generation
	 * @param alreadyUsedBeanPropertiesNames
	 *            optional
	 * @return primary key.
	 */
	public static List<SQLProperty> populateContentValuesFromEntity(Elements elementUtils, SQLiteDatabaseSchema model, SQLDaoDefinition daoDefinition, ModelMethod method, Class<? extends Annotation> annotationClazz, Builder methodBuilder,
			List<String> alreadyUsedBeanPropertiesNames) {
		List<SQLProperty> listPropertyInContentValue=new ArrayList<SQLProperty>();
		
		SQLEntity entity = model.getEntity(daoDefinition.getEntityClassName());
		// check included and excluded fields
		ModelAnnotation annotation = method.getAnnotation(annotationClazz);
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_VALUE);
		if (includedFields==null) includedFields=new ArrayList<String>();
		if (alreadyUsedBeanPropertiesNames != null) {
			includedFields.removeAll(alreadyUsedBeanPropertiesNames);
		}
		
		List<String> temp=AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_EXCLUDED_FIELDS);
		if (temp==null) temp=new ArrayList<String>();
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(temp);

		if (includedFields.size() > 0 && excludedFields.size() > 0) {
			throw (new IncompatibleAttributesInAnnotationException(daoDefinition, method, annotation, AnnotationAttributeType.ATTRIBUTE_VALUE, AnnotationAttributeType.ATTRIBUTE_EXCLUDED_FIELDS));
		}
		// check included
		for (String item : includedFields) {
			if (!entity.contains(item)) {
				throw (new PropertyInAnnotationNotFoundException(daoDefinition, method, item));
			}
		}
		// check excluded
		for (String item : excludedFields) {
			if (!entity.contains(item)) {
				throw (new PropertyInAnnotationNotFoundException(daoDefinition, method, item));
			}
		}

		// initialize contentValues
		SQLProperty primaryKey = entity.getPrimaryKey();
		// for each property in entity except primaryKey and excluded properties
		for (SQLProperty item : entity.getCollection()) {
			if (item.equals(primaryKey) || excludedFields.contains(item.getName()))
				continue;
			if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
				continue;
			if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
				continue;

			// add property to list of used properties
			listPropertyInContentValue.add(item);
		}
		
		return listPropertyInContentValue;

	}
	
	public static void generateContentValuesFromEntity(Elements elementUtils, SQLiteDatabaseSchema model, SQLDaoDefinition daoDefinition, ModelMethod method, Class<? extends Annotation> annotationClazz, Builder methodBuilder,
			List<String> alreadyUsedBeanPropertiesNames) {
		// all check is already done
		
		SQLEntity entity = model.getEntity(daoDefinition.getEntityClassName());
		// check included and excluded fields
		ModelAnnotation annotation = method.getAnnotation(annotationClazz);
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_VALUE);
		if (alreadyUsedBeanPropertiesNames != null) {
			includedFields.removeAll(alreadyUsedBeanPropertiesNames);
		}
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.ATTRIBUTE_EXCLUDED_FIELDS));

	
		// initialize contentValues
		SQLProperty primaryKey = entity.getPrimaryKey();
		methodBuilder.addCode("contentValues.clear();\n\n");
		// for each property in entity except primaryKey and excluded properties
		for (SQLProperty item : entity.getCollection()) {
			if (item.equals(primaryKey) || excludedFields.contains(item.getName()))
				continue;
			if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
				continue;
			if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
				continue;

			// add property to list of used properties
			methodBuilder.addCode("contentValues.put($S, ", model.columnNameConverter.convert(item.getName()));
			Transformer.java2ContentValues(methodBuilder, item, method.getParameters().get(0).value0);
			methodBuilder.addCode(");\n", model.columnNameConverter.convert(item.getName()));

		}

	}

}