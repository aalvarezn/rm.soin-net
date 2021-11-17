package com.soin.sgrm.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.commons.validator.routines.EmailValidator;

import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.UserInfoService;

public class BaseController {

	@Autowired
	private UserInfoService loginService;

	public static final Logger logger = Logger.getLogger(BaseController.class);

	public String getUserName() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails.getUsername();
	}

	public int getUserId() {
		UserInfo user = loginService.getUserByUsername(getUserName());
		return user.getId();
	}

	public UserInfo getUseInfo() {
		return loginService.getUserByUsername(getUserName());
	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void logs(String log, String message) {
		if (log.equals("WEBSERVICE"))
			logger.log(MyLevel.WEBSERVICE, message);
		if (log.equals("CONFIG"))
			logger.log(MyLevel.CONFIG, message);
		if (log.equals("SYSTEM_ERROR"))
			logger.log(MyLevel.SYSTEM_ERROR, message);
		if (log.equals("RELEASE_ERROR"))
			logger.log(MyLevel.RELEASE_ERROR, message);
		if (log.equals("FILE_READ"))
			logger.log(MyLevel.FILE_READ, message);
		if (log.equals("ADMIN_ERROR"))
			logger.log(MyLevel.ADMIN_ERROR, message);
	}

	public String getSystemDate() {
		long time = System.currentTimeMillis();
		java.sql.Timestamp date = new java.sql.Timestamp(time);
		return date.toString();
	}

	public String getSystemDate(String format) {
		String dateInString = new SimpleDateFormat(format).format(new Date());
		return dateInString;

	}

	public java.sql.Date getSqlDate(String dateString) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			java.util.Date utilDate = sdf1.parse(dateString);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (Exception e) {
			throw new Exception("Formato de Fecha Inválida.");
		}
	}

	public java.sql.Date isSqlDate(String dateString) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			java.util.Date utilDate = sdf1.parse(dateString);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (Exception e) {
			return null;
		}
	}

	public Timestamp getSystemTimestamp() {
		long time = System.currentTimeMillis();
		java.sql.Timestamp date = new java.sql.Timestamp(time);
		return date;
	}

	public String getErrorFormat(Exception e) {
		return "\n" + " CAUSE: " + e.getCause().getMessage() + " Linea Error: " + e.getStackTrace()[0].getLineNumber()
				+ " CLASSNAME: " + e.getClass().getName() + "\n" + e;
	}

	public boolean isValidEmailAddress(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	public String getRandom() {
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
}
