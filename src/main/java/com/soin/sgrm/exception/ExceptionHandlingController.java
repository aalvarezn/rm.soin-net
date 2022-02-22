package com.soin.sgrm.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;


@ControllerAdvice
public class ExceptionHandlingController extends BaseController {

	@ExceptionHandler(MultipartException.class)
	public @ResponseBody JsonResponse handleErrorMultipart(HttpServletRequest req, Exception ex) {
		JsonResponse res = new JsonResponse();
		res.setStatus("exception");
		res.setException("Tamaño máximo de archivos " + Constant.MAXFILEUPLOADSIZE + "MB.");
		return res;
	}
}
