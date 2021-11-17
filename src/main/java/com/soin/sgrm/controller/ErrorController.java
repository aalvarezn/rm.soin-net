package com.soin.sgrm.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {

	@RequestMapping(value = "/500", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
	public String renderErrorPage(HttpServletResponse response, HttpServletRequest httpRequest, Locale locale, Model model) throws IOException {
		String errorMsg = "";
		boolean existError = false;

		String errorType = (String) httpRequest.getParameter("errorType");
		
		if(errorType == null) {
			errorType = (String) ((httpRequest.getAttribute("errorType") == null) ? "": httpRequest.getAttribute("errorType"));
		}
		
		if (errorType.contains("ArithmeticException")) {
			errorMsg = "Error del servidor, operación aritmetica incorrecta.";
			existError = true;
		}
		if (errorType.contains("GenericJDBCException")) {
			errorMsg = "La base de datos no se encuentra disponible, favor notificar a soporte técnico.";
			existError = true;
		}

		if (errorType.contains("SQLException")) {
			errorMsg = "Problemas de conexión con la base de datos, favor intente más tarde.";
			existError = true;
		}

		if (!existError) {
			errorMsg = "Error del servidor, favor comunicar a soporte técnico e intente más tarde.";
		}
		
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(errorMsg);
		
		model.addAttribute("errorCode", 500);
		model.addAttribute("errorMsg", errorMsg);
		return "/plantilla/500";
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}
}