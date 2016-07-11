package com.abubusoft.kripton.processor.sharedprefs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Viene messo sui campi di un oggetto di tipo config per renderli persistenti e fare in modo che possano essere modificabili anche da preference.
 * </p>
 * 
 * @author Francesco Benincasa
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferenceConfig {
	/**
	 * rappresenta il nome della preference da associare alla configurazione. Di default viene utilizzato il nome dell'attributo.
	 * 
	 * @return
	 */
	String key() default ConfigBase.DEFAULT_KEY;

	/**
	 * converter da utilizzare per la conversione tra preference e config e viceversa.
	 * 
	 * @return
	 */
	Class<? extends Converter> converter() default DefaultConverter.class;

	/**
	 * Tipo di preferenza. Normalmente è string, ma può essere anche altro.
	 * 
	 * @return
	 */
	PreferenceType preferenceType() default PreferenceType.STRING;

	/**
	 * Se true il campo viene ricopiato durante il reset, altrimenti viene ignorato.
	 * 
	 * @return
	 */
	boolean copyOnReset() default true;

	/**
	 * <p>
	 * Se true indica che il campo deve essere criptato. <b>Gli attributi di config possono essere di qualunque tipo convertibile in stringa. Funziona solo con le preference di
	 * tipo Stringa</b>
	 * </p>
	 * 
	 * @return
	 */
	boolean crypted() default false;
}
