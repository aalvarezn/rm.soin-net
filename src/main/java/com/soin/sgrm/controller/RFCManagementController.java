package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ntp.TimeStamp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.service.pos.RFCService;
import com.soin.sgrm.service.pos.StatusService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/rfc")
public class RFCManagementController extends BaseController{
	public static final Logger logger = Logger.getLogger(RFCManagementController.class);
	
	@Autowired
	RFCService rfcService;
	@Autowired 
	StatusService statusService;
	
	@RequestMapping(value = "/statusRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			PRFC rfc = rfcService.findById(idRFC);
			PStatus status = statusService.findById(idStatus);
			if (status != null && status.getName().equals("Borrador")) {
				/*if (release.getStatus().getId() != status.getId())
					release.setRetries(release.getRetries() + 1);*/
			}
			rfc.setStatus(status);
			rfc.setOperator(getUserLogin().getName());
			rfc.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			rfc.setMotive(motive);
			rfcService.update(rfc);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del RFC: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/cancelRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC) {
		JsonResponse res = new JsonResponse();
		try {
			PUser userLogin = getUserLogin();
			PRFC rfc = rfcService.findById(idRFC);
			PStatus status = statusService.findByKey("name", "Anulado");
			rfc.setStatus(status);
			rfc.setOperator(userLogin.getName());
			rfc.setMotive(status.getReason());
			rfcService.update(rfc);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar el RFC: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
