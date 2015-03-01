package com.abubusoft.kripton.binder.annotation.schema;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.abubusoft.kripton.binder.annotation.BindAnyElement;
import com.abubusoft.kripton.binder.annotation.BindAttribute;
import com.abubusoft.kripton.binder.annotation.BindDatabase;
import com.abubusoft.kripton.binder.annotation.BindDefault;
import com.abubusoft.kripton.binder.annotation.BindElement;
import com.abubusoft.kripton.binder.annotation.BindOrder;
import com.abubusoft.kripton.binder.annotation.BindRoot;
import com.abubusoft.kripton.binder.annotation.BindValue;
import com.abubusoft.kripton.binder.exception.MappingException;
import com.abubusoft.kripton.binder.transform.Transformer;
import com.abubusoft.kripton.common.LRUCache;
import com.abubusoft.kripton.common.StringUtil;
import com.abubusoft.kripton.reflect.GenericClass;
import com.abubusoft.kripton.reflect.TypeReflector;

/**
 * Factory class for OX mapping schema
 * 
 * 
 * @author bulldog
 * 
 */
public class MappingSchema {

	private RootElementSchema rootElementSchema;
	private Map<String, Object> field2SchemaMapping;
	private Map<String, Object> xml2SchemaMapping;

	private Map<String, AttributeSchema> field2AttributeSchemaMapping;
	private ValueSchema valueSchema;
	private Map<String, AttributeSchema> xml2AttributeSchemaMapping;
	private AnyElementSchema anyElementSchema;

	private Class<?> type;
	private boolean isDefault;
	private GenericClass genericsResolver;
	/**
	 * elenco dei wrapper di lista di questo schema
	 */
	private Map<String, Object> xmlWrapper2SchemaMapping;

	public Map<String, Object> getXmlWrapper2SchemaMapping() {
		return xmlWrapper2SchemaMapping;
	}

	private static final int CACHE_SIZE = 100;
	// use LRU cache to limit memory consumption.
	private static Map<Class<?>, MappingSchema> schemaCache = Collections.synchronizedMap(new LRUCache<Class<?>, MappingSchema>(CACHE_SIZE));

	private MappingSchema(Class<?> type) throws MappingException {
		this.type = type;

		genericsResolver = GenericClass.forClass(type);

		isDefault = type.isAnnotationPresent(BindDefault.class);

		// step 1
		this.buildRootElementSchema();
		// step 2
		this.buildField2SchemaMapping();
		// step 3
		this.buildXml2SchemaMapping();
		// step 4
		this.buildField2AttributeSchemaMapping();
	}

	private void buildRootElementSchema() {
		rootElementSchema = new RootElementSchema();

		if (type.isAnnotationPresent(BindRoot.class)) {
			BindRoot xre = type.getAnnotation(BindRoot.class);
			if (StringUtil.isEmpty(xre.name())) {
				rootElementSchema.setName(StringUtil.lowercaseFirstLetter(type.getSimpleName()));
			} else {
				rootElementSchema.setName(xre.name());
			}
			String namespace = StringUtil.isEmpty(xre.namespace()) ? null : xre.namespace();
			rootElementSchema.setNamespace(namespace);
			rootElementSchema.setOnlyChildren(xre.onlyChildren());
		} else { // if no BinderRoot, use class name instead
			rootElementSchema.setName(StringUtil.lowercaseFirstLetter(type.getSimpleName()));
			rootElementSchema.setNamespace(null);
			rootElementSchema.setOnlyChildren(false);
		}

	}

	private void buildField2SchemaMapping() throws MappingException {
		field2SchemaMapping = this.scanFieldSchema(type);

		Class<?> superType = type.getSuperclass();
		// scan super class fields
		while (superType != Object.class && superType != null) {
			Map<String, Object> parentField2SchemaMapping = this.scanFieldSchema(superType);
			// redefined fields in sub-class will overwrite corresponding fields
			// in super-class.
			parentField2SchemaMapping.putAll(field2SchemaMapping);
			field2SchemaMapping = parentField2SchemaMapping;
			superType = superType.getSuperclass();
		}
	}

	private void buildXml2SchemaMapping() {
		xml2SchemaMapping = new LinkedHashMap<String, Object>();
		xmlWrapper2SchemaMapping = new LinkedHashMap<String, Object>();
		xml2AttributeSchemaMapping = new LinkedHashMap<String, AttributeSchema>();

		for (String fieldName : field2SchemaMapping.keySet()) {
			Object schemaObj = field2SchemaMapping.get(fieldName);
			if (schemaObj instanceof AttributeSchema) {
				AttributeSchema as = (AttributeSchema) schemaObj;
				xml2SchemaMapping.put(as.getName(), as);
				// build xml2AttributeSchemaMapping at the same time.
				xml2AttributeSchemaMapping.put(as.getName(), as);
			} else if (schemaObj instanceof ElementSchema) {
				ElementSchema es = (ElementSchema) schemaObj;
				xml2SchemaMapping.put(es.getName(), es);

				// se esiste un wrapper lo mettiamo nell'apposita mappa
				if (es.hasWrapperName()) {
					xmlWrapper2SchemaMapping.put(es.getWrapperName(), es);
				}
			}
		}
	}

	private void buildField2AttributeSchemaMapping() {
		field2AttributeSchemaMapping = new LinkedHashMap<String, AttributeSchema>();

		for (String fieldName : field2SchemaMapping.keySet()) {
			Object schemaObj = field2SchemaMapping.get(fieldName);
			if (schemaObj instanceof AttributeSchema) {
				field2AttributeSchemaMapping.put(fieldName, (AttributeSchema) schemaObj);
			}
		}
	}

	private Map<String, Object> scanFieldSchema(Class<?> type) throws MappingException {
		// usiamo la linkedhashmap per tenere traccia dell'ordine
		Map<String, Object> fieldsMap = new LinkedHashMap<String, Object>();
		Field[] fields = type.getDeclaredFields();

		// sort fields according to order annotaions
		Arrays.sort(fields, new Comparator<Field>() {

			@Override
			public int compare(Field field1, Field field2) {
				BindOrder order1 = field1.getAnnotation(BindOrder.class);
				BindOrder order2 = field2.getAnnotation(BindOrder.class);
				if (order1 != null && order2 != null) {
					return order1.value() - order2.value();
				}
				if (order1 != null && order2 == null) {
					return -1;
				}
				if (order1 == null && order2 != null) {
					return 1;
				}
				return field1.getName().compareTo(field2.getName());
			}

		});

		// used for validation
		int valueSchemaCount = 0;
		int anyElementSchemaCount = 0;
		int elementSchemaCount = 0;

		for (Field field : fields) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			if (field.isAnnotationPresent(BindElement.class)) {

				elementSchemaCount++;

				BindElement xmlElement = field.getAnnotation(BindElement.class);
				ElementSchema elementSchema = new ElementSchema();

				if (StringUtil.isEmpty(xmlElement.name())) {
					elementSchema.setName(field.getName());
				} else {
					elementSchema.setName(xmlElement.name());
				}

				// se esiste un nome per gli elementi di una lista/array, il nome dell'elemento
				// diventa il nome del wrapper
				if (!StringUtil.isEmpty(xmlElement.elementName()))
				{
					// invertiamo i nomi
					elementSchema.setWrapperName(xmlElement.elementName());
					
					String temp=elementSchema.getName();
					elementSchema.setName(elementSchema.getWrapperName());
					elementSchema.setWrapperName(temp);
				}

				// List validation
				handleList(field, elementSchema);
				handleArray(field, elementSchema);

				elementSchema.setData(xmlElement.data());
				elementSchema.setField(field);

				fieldsMap.put(field.getName(), elementSchema);

				// database section
				if (field.isAnnotationPresent(BindDatabase.class)) {
					BindDatabase databaseAnnotation = field.getAnnotation(BindDatabase.class);
					elementSchema.setPrimaryKey(databaseAnnotation.primaryKey());
					elementSchema.setNullable(databaseAnnotation.nullable());
					elementSchema.setUnique(databaseAnnotation.unique());
				}
			} else if (field.isAnnotationPresent(BindAttribute.class)) {
				// validation
				if (!Transformer.isTransformable(field.getType())) {
					throw new MappingException("BinderAttribute annotation can't annotate complex type field, "
							+ "only primivte type or frequently used java type or enum type field is allowed, " + "field = " + field.getName() + ", type = "
							+ type.getName());
				}

				BindAttribute xmlAttribute = field.getAnnotation(BindAttribute.class);
				AttributeSchema attributeSchema = new AttributeSchema();
				// if attribute name was not provided, use field name instead
				if (StringUtil.isEmpty(xmlAttribute.name())) {
					attributeSchema.setName(field.getName());
				} else {
					attributeSchema.setName(xmlAttribute.name());
				}
				attributeSchema.setField(field);

				fieldsMap.put(field.getName(), attributeSchema);

				// database section
				if (field.isAnnotationPresent(BindDatabase.class)) {
					BindDatabase databaseAnnotation = field.getAnnotation(BindDatabase.class);
					attributeSchema.setPrimaryKey(databaseAnnotation.primaryKey());
					attributeSchema.setNullable(databaseAnnotation.nullable());
					attributeSchema.setUnique(databaseAnnotation.unique());
				}
			} else if (field.isAnnotationPresent(BindValue.class)) {
				valueSchemaCount++;

				// validation
				if (!Transformer.isTransformable(field.getType())) {
					throw new MappingException("BinderValue annotation can't annotate complex type field, "
							+ "only primivte type or frequently used java type or enum type field is allowed, " + "field = " + field.getName() + ", type = "
							+ type.getName());
				}

				BindValue xmlValue = field.getAnnotation(BindValue.class);

				valueSchema = new ValueSchema();
				valueSchema.setData(xmlValue.data());
				valueSchema.setField(field);

				// set the name
				valueSchema.setName(field.getName());

				// database section
				if (field.isAnnotationPresent(BindDatabase.class)) {
					BindDatabase databaseAnnotation = field.getAnnotation(BindDatabase.class);
					valueSchema.setPrimaryKey(databaseAnnotation.primaryKey());
					valueSchema.setNullable(databaseAnnotation.nullable());
					valueSchema.setUnique(databaseAnnotation.unique());
				}

			} else if (field.isAnnotationPresent(BindAnyElement.class)) {
				anyElementSchemaCount++;

				if (!TypeReflector.collectionAssignable(field.getType())) {
					throw new MappingException("Current nano framework only supports java.util.List<T> as container of any type, " + "field = "
							+ field.getName() + ", type = " + type.getName());
				}

				Class<?> fieldType = field.getType();
				if (!TypeReflector.isList(fieldType)) {
					throw new MappingException("Current nano framework only supports java.util.List<T> as collection type, " + "field = " + field.getName()
							+ ", type = " + type.getName());
				}

				anyElementSchema = new AnyElementSchema();
				anyElementSchema.setField(field);

				// set the name
				anyElementSchema.setName(field.getName());

				// database section
				if (field.isAnnotationPresent(BindDatabase.class)) {
					BindDatabase databaseAnnotation = field.getAnnotation(BindDatabase.class);
					anyElementSchema.setPrimaryKey(databaseAnnotation.primaryKey());
					anyElementSchema.setNullable(databaseAnnotation.nullable());
					anyElementSchema.setUnique(databaseAnnotation.unique());
				}

			} else if (isDefault) { // default to Node

				// if serialId, continue
				if (field.getName().equals("serialVersionUID"))
					continue;

				elementSchemaCount++;

				ElementSchema elementSchema = new ElementSchema();

				elementSchema.setName(field.getName());

				// List validation
				handleList(field, elementSchema);
				handleArray(field, elementSchema);

				elementSchema.setField(field);

				fieldsMap.put(field.getName(), elementSchema);

				// database section
				if (field.isAnnotationPresent(BindDatabase.class)) {
					BindDatabase databaseAnnotation = field.getAnnotation(BindDatabase.class);
					elementSchema.setPrimaryKey(databaseAnnotation.primaryKey());
					elementSchema.setNullable(databaseAnnotation.nullable());
					elementSchema.setUnique(databaseAnnotation.unique());
				}
			}
		}

		// more validation
		if (valueSchemaCount > 1) {
			throw new MappingException("BinderValue annotation can't annotate more than one fields in same class," + " type = " + type.getName());
		}

		if (anyElementSchemaCount > 1) {
			throw new MappingException("BinderAnyElement annotation can't annotate more than one fields in same class," + " type = " + type.getName());
		}

		if (valueSchemaCount == 1 && elementSchemaCount >= 1) {
			throw new MappingException("BinderValue and Node annotations can't coexist in same class," + " type = " + type.getName());
		}

		return fieldsMap;

	}

	private void handleArray(Field field, ElementSchema elementSchema) throws MappingException {
		if (field.getType().isArray()) {
			Class<?> type = field.getType().getComponentType();

			if (!elementSchema.hasWrapperName())
			{
				//elementSchema.setWrapperName(elementSchema.getName());
				//elementSchema.setName(elementSchema.getName()+"Element");
			}
			
			elementSchema.setArray(true);
			elementSchema.setParameterizedType(type);
		}
	}

	private void handleList(Field field, ElementSchema elementSchema) throws MappingException {
		if (TypeReflector.collectionAssignable(field.getType())) {
			Class<?> type = field.getType();

			if (TypeReflector.isList(type) || TypeReflector.isArrayList(type)) {
				elementSchema.setList(true);
				Class<?> paramizedType = TypeReflector.getParameterizedType(field, genericsResolver);
				if (paramizedType == null) {
					throw new MappingException("Can't get parameterized type of a List field, "
							+ "Framework only supports collection field of List<T> type, and T must be a Nano bindable type, " + "field = " + field.getName()
							+ ", type = " + type.getName());
				} else {
					elementSchema.setParameterizedType(paramizedType);
				}
			} else {
				throw new MappingException("Current framework only supports java.util.List<T> as collection type, " + "field = " + field.getName()
						+ ", type = " + type.getName());
			}
		}
	}

	public Class<?> getType() {
		return this.type;
	}

	/**
	 * Factory method.
	 * 
	 * @param object
	 *            an object to get mapping schema from.
	 * @return a MappingSchema instance.
	 */
	public static MappingSchema fromObject(Object object) throws MappingException {
		return fromClass(object.getClass());
	}

	/**
	 * Factory method.
	 * 
	 * @param type
	 *            a Class type to get mapping schema from.
	 * @return a MappingSchema instance.
	 */
	public static MappingSchema fromClass(Class<?> type) throws MappingException {
		if (schemaCache.containsKey(type)) {
			return schemaCache.get(type);
		} else {
			MappingSchema mappingSchema = new MappingSchema(type);
			schemaCache.put(type, mappingSchema);
			return mappingSchema;
		}
	}

	public Map<String, Object> getField2SchemaMapping() {
		return field2SchemaMapping;
	}

	public Map<String, Object> getXml2SchemaMapping() {
		return xml2SchemaMapping;
	}

	public RootElementSchema getRootElementSchema() {
		return rootElementSchema;
	}

	public Map<String, AttributeSchema> getField2AttributeSchemaMapping() {
		return field2AttributeSchemaMapping;
	}

	public ValueSchema getValueSchema() {
		return valueSchema;
	}

	public AnyElementSchema getAnyElementSchema() {
		return anyElementSchema;
	}

	public Map<String, AttributeSchema> getXml2AttributeSchemaMapping() {
		return xml2AttributeSchemaMapping;
	}

}
