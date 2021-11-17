package com.soin.sgrm.utils;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}
	
	public Map getEntryProperties(String nameEnv) {
		
		Map<String, String> properties = new HashMap<String, String>();
		String chain = null;
		try {
			Context context = new InitialContext();
			Context envCtx = (Context) context.lookup("java:comp/env");
			chain = (String) envCtx.lookup(nameEnv);
			
			for(String keyValue : chain.split(" *; *")) {
			   String[] pairs = keyValue.split(" *= *", 2);
			   properties.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
			}
				
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

}
