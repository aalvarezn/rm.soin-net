package com.soin.sgrm.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

import com.soin.sgrm.exception.Sentry;

public class CommonUtils {

	public static final Logger logger = Logger.getLogger(CommonUtils.class);

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String getSystemDate() {
		long time = System.currentTimeMillis();
		java.sql.Timestamp date = new java.sql.Timestamp(time);
		return date.toString();
	}

	public static String getSystemDate(String format) {
		String dateInString = new SimpleDateFormat(format).format(new Date());
		return dateInString;

	}

	public static java.sql.Date getSqlDate(String dateString) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			java.util.Date utilDate = sdf1.parse(dateString);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (Exception e) {
			throw new Exception("Formato de Fecha Inválida.");
		}
	}
	
	public static java.sql.Date getSqlDateNew(String dateString) throws Exception{
        String inputPattern = "yyyy-MM-dd HH:mm:ss.SSS";
        String outputPattern = "yyyy-MM-dd 00:00:00.000";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            java.util.Date utilDate = inputFormat.parse(dateString);

            String formattedDateString = outputFormat.format(utilDate);
            java.util.Date formattedUtilDate = outputFormat.parse(formattedDateString);

            java.sql.Date sqlDate = new java.sql.Date(formattedUtilDate.getTime());
            
            return sqlDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return null;

	}

	public static java.sql.Date getSqlDate() {
		long time = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(time);
		return date;
	}

	public static java.sql.Date isSqlDate(String dateString) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			java.util.Date utilDate = sdf1.parse(dateString);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (Exception e) {
			Sentry.capture(e, "baseController");
			return null;
		}
	}

	public static Timestamp getSystemTimestamp() {
		long time = System.currentTimeMillis();
		java.sql.Timestamp date = new java.sql.Timestamp(time);
		return date;
	}

	public static Timestamp getSystemTimestamp(String dateTime) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			java.util.Date utilDate = sdf1.parse(dateTime);
			Timestamp sqlDate = new Timestamp(utilDate.getTime());
			return sqlDate;
		} catch (Exception e) {
			Sentry.capture(e, "baseController");
			return null;
		}
	}

	public static boolean isValidEmailAddress(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	public static String getRandom() {
		// define the range
		int max = 9, min = 1;
		int range = max - min + 1;

		String code = "";
		// generate random numbers within 1 to 10
		for (int i = 0; i < 4; i++) {
			int rand = (int) (Math.random() * range) + min;
			code += rand;
		}
		return code;
	}

	public static boolean equalsWithNulls(Object a, Object b) {
		if (a == b)
			return true;
		if ((a == null) || (b == null))
			return false;
		return a.equals(b);
	}

	public static Timestamp convertStringToTimestamp(String str_date, String format) {
        if (str_date == null || str_date.length() == 0)
            return null;
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            // you can change format of date
            Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate;
        } catch (ParseException e) {
            return null;
        }
    }
}
