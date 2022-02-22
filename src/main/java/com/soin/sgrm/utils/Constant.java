package com.soin.sgrm.utils;

import java.util.HashMap;
import java.util.Map;

public class Constant {

	public static final String EMPTY = "Ingrese un valor";

	public static final String EMPTYVARDOC = "No Aplica";

	public static final String[] FILTRED = new String[] { "Anulado", "Rollback" };

	public static final String[] STATISTICBAR = new String[] { "Completados", "Certificacion", "En Revision",
			"Borrador" };

	public static final Boolean SEND_EMAIL = true;

	public static final String APP_PROPERTIES = "application.properties";

	public static final int MAXFILEUPLOADSIZE = 250; // MB

	public static final String getCharacterEmail(String body) {
		String[] characters = { "Á", "É", "Í", "Ó", "Ú", "Ü", "Ñ", "á", "é", "í", "ó", "ú", "ü", "ñ" };
		for (int i = 0; i < characters.length; i++) {
			char c = characters[i].charAt(0);
			int value = c;
			body = body.replace(characters[i], "&#" + value + ";");
		}
		return body;
	}

}
