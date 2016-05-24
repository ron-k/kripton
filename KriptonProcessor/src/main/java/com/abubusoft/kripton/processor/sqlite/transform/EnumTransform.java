package com.abubusoft.kripton.processor.sqlite.transform;

import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.setter;

import com.abubusoft.kripton.processor.core.ModelProperty;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.MethodSpec.Builder;

/**
 * Transformer between a string and a Java5 Enum object
 * 
 * @author bulldog
 * 
 */
class EnumTransform  extends AbstractTransform {

	public EnumTransform(TypeName typeName) {
		
	}

	@Override
	public void generateReadProperty(MethodSpec.Builder methodBuilder, ModelProperty property, String beanName, String cursorName, String indexName)  {		
		//methodBuilder.addCode("$L."+setter(property, "T$.valueOf($L.getString($L))")+";", beanName,property.getModelType().getName(), cursorName, indexName);
		methodBuilder.addCode("$L."+setter(property, "$T.valueOf($L.getString($L))"), beanName,property.getModelType().getName(), cursorName, indexName);	
	}
	
	@Override
	public void generateRead(Builder methodBuilder, String cursorName, String indexName) {
		methodBuilder.addCode("$L.getString($L)", cursorName, indexName);		
	}

	@Override
	public void generateDefaultValue(Builder methodBuilder)
	{
		methodBuilder.addCode("null");		
	}
	
	@Override
	public void generateResetProperty(Builder methodBuilder, ModelProperty property, String beanName, String cursorName, String indexName) {
		methodBuilder.addCode("$L."+setter(property, "null"), beanName);
	}
	
	@Override
	public String generateColumnType(ModelProperty property) {
		return "TEXT";
	}
}