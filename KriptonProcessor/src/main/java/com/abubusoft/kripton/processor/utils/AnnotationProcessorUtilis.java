package com.abubusoft.kripton.processor.utils;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import com.abubusoft.kripton.processor.BaseProcessor;

public class AnnotationProcessorUtilis {

	/**
	 * display info about generated classes
	 * 
	 * @param annotation
	 * @param className
	 */
	public static void infoOnGeneratedClasses(Class<? extends Annotation> annotation, String packageName, String className) {
		String msg = String.format("%s annotation processor generates %s class in package %s", annotation.getSimpleName(), className, packageName);
		if (BaseProcessor.DEVELOP_MODE) {
			logger.info(msg);
		}
		messager.printMessage(Diagnostic.Kind.NOTE, msg);
	}

	/**
	 * logger
	 */
	static Logger logger = Logger.getGlobal();
	
	private static Messager messager;

	public static void init(Messager value) {
		messager=value;		
	}
}
