package com.soin.sgrm.utils;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.soin.sgrm.exception.Sentry;

public class EnviromentConfig {

	public EnviromentConfig() {
	}

	public String getEntry(String nameEnv) {
		String returnStr = null;
		try {
			Context context = new InitialContext();
			Context envCtx = (Context) context.lookup("java:comp/env");
			returnStr = (String) envCtx.lookup(nameEnv);
		} catch (NamingException e) {
			Sentry.capture(e, "environmentConfig");
		} catch (Exception e) {
			Sentry.capture(e, "environmentConfig");
		}
		return returnStr;
	}

	public Map<String, String> getEntryProperties(String nameEnv) {

		Map<String, String> properties = new HashMap<String, String>();
		String chain = null;
		try {
			Context context = new InitialContext();
			Context envCtx = (Context) context.lookup("java:comp/env");
			chain = (String) envCtx.lookup(nameEnv);

			String[] chainProperties = chain.split(";");

			for(String keyValue : chainProperties) {
				properties.put(keyValue.split("=")[0].trim(),keyValue.split("=")[1].trim());
			}
		} catch (NamingException e) {
			Sentry.capture(e, "environmentConfig");
		} catch (Exception e) {
			Sentry.capture(e, "environmentConfig");
		}
		return properties;
	}

}
