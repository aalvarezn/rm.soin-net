package com.soin.sgrm.utils;


import org.apache.log4j.Level;

/*
 * OFF(0) FATAL(100) ERROR(200) WARN(300) INFO(400) DEBUG(500) TRACE(600)
 * ALL(MAX)
 */

@SuppressWarnings("serial")
public class MyLevel extends Level {

	public static final int WEBSERVICE_INT = 40001;
	public static final int CONFIG_INT = 40002;
	public static final int SYSTEM_ERROR_INT = 40003;
	public static final int RELEASE_ERROR_INT = 40004;
	public static final int FILE_READ_INT = 40005;
	public static final int ADMIN_ERROR_INT = 40007;

	public static final Level WEBSERVICE = new MyLevel(WEBSERVICE_INT, "WEBSERVICE", 10);
	public static final Level CONFIG = new MyLevel(CONFIG_INT, "CONFIG", 10);
	public static final Level SYSTEM_ERROR = new MyLevel(SYSTEM_ERROR_INT, "SYSTEM_ERROR", 10);
	public static final Level RELEASE_ERROR = new MyLevel(RELEASE_ERROR_INT, "RELEASE_ERROR", 10);
	public static final Level FILE_READ = new MyLevel(FILE_READ_INT, "FILE_READ", 10);
	public static final Level ADMIN_ERROR = new MyLevel(ADMIN_ERROR_INT, "ADMIN_ERROR", 10);

	protected MyLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}

	public static Level toLevel(String logArgument) {
		if (logArgument != null) {
			if (logArgument.toUpperCase().equals("WEBSERVICE")) {
				return WEBSERVICE;
			}
			if (logArgument.toUpperCase().equals("CONFIG")) {
				return CONFIG;
			}
			if (logArgument.toUpperCase().equals("SYSTEM_ERROR")) {
				return SYSTEM_ERROR;
			}

			if (logArgument.toUpperCase().equals("RELEASE_ERROR")) {
				return RELEASE_ERROR;
			}

			if (logArgument.toUpperCase().equals("ADMIN_ERROR")) {
				return ADMIN_ERROR;
			}

			if (logArgument.toUpperCase().equals("FILE_READ")) {
				return FILE_READ;
			}

		}
		return (Level) toLevel(logArgument, Level.DEBUG);
	}

	public static Level toLevel(int val) {
		switch (val) {
		case WEBSERVICE_INT:
			return WEBSERVICE;
		case CONFIG_INT:
			return CONFIG;
		case SYSTEM_ERROR_INT:
			return SYSTEM_ERROR;
		case RELEASE_ERROR_INT:
			return RELEASE_ERROR;
		case ADMIN_ERROR_INT:
			return ADMIN_ERROR;
		case FILE_READ_INT:
			return FILE_READ;
		}

		return (Level) toLevel(val, Level.DEBUG);
	}

	public static Level toLevel(int val, Level defaultLevel) {
		switch (val) {
		case WEBSERVICE_INT:
			return WEBSERVICE;
		case CONFIG_INT:
			return CONFIG;
		case SYSTEM_ERROR_INT:
			return SYSTEM_ERROR;
		case RELEASE_ERROR_INT:
			return RELEASE_ERROR;
		case ADMIN_ERROR_INT:
			return ADMIN_ERROR;
		case FILE_READ_INT:
			return FILE_READ;
		}
		return Level.toLevel(val, defaultLevel);
	}

	public static Level toLevel(String logArgument, Level defaultLevel) {
		if (logArgument != null) {
			if (logArgument.toUpperCase().equals("WEBSERVICE")) {
				return WEBSERVICE;
			}
			if (logArgument.toUpperCase().equals("CONFIG")) {
				return CONFIG;
			}
			if (logArgument.toUpperCase().equals("SYSTEM_ERROR")) {
				return SYSTEM_ERROR;
			}

			if (logArgument.toUpperCase().equals("RELEASE_ERROR")) {
				return RELEASE_ERROR;
			}
			if (logArgument.toUpperCase().equals("ADMIN_ERROR")) {
				return ADMIN_ERROR;
			}

			if (logArgument.toUpperCase().equals("FILE_READ")) {
				return FILE_READ;
			}

		}
		return Level.toLevel(logArgument, defaultLevel);
	}

}