package com.soin.sgrm.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.soin.sgrm.utils.AppProperties;
import com.soin.sgrm.utils.Constant;

public class AppProperties { 

	private Properties properties;
//	private final static Logger logger = Logger.getLogger(AppProperties.class);

	public AppProperties() { 
		this.properties = new Properties();
		try {
			InputStream resourceInputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(Constant.APP_PROPERTIES);
			this.properties.load(resourceInputStream);
			resourceInputStream.close();
		} catch (FileNotFoundException fnfe) {
//			logger.error("No se logro cargar el archivo de propiedades", fnfe);
		} catch (IOException e) {
//			logger.error(e);
		}
	}

	/**
	 * Obtiene el valor según el parametro ingreso
	 * 
	 * @param property Nombre del parámetro que se desea obtener
	 */
	public String getProperty(String property) {
		return properties.getProperty(property);
	}

}
