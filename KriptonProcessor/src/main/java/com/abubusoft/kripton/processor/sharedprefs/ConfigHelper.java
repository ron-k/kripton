package com.abubusoft.kripton.processor.sharedprefs;

import java.util.Date;

import org.abubu.elio.util.ElioCodec;

public class ConfigHelper {

	private static final String SPLIT_SEPARATOR = "\\|\\|\\|";
	public static final String SEPARATOR = "|||";

	/**
	 * Codifica una stringa, aggiungendo informazioni di contorno, in modo da rendere più difficile la comprensione.
	 * 
	 * @param stringa
	 *            da codificare
	 * @return
	 */
	public static String encodedString(String secret) {

		long t1 = ((new Date()).getTime());

		String code = "" + t1 + SEPARATOR + secret + SEPARATOR + (t1 + 1001);

		String code1 = ElioCodec.encode(code);

		return code1;
	}

	/**
	 * Data una stringa, decodifica il token in essa contenuto.
	 * 
	 * @param input
	 * @return
	 */
	public static String decodeString(String input) {
		try {
			String temp = ElioCodec.decode(input);

			String[] temp1;
			temp1 = temp.split(SPLIT_SEPARATOR);

			String secret = temp1[1];

			return secret;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
