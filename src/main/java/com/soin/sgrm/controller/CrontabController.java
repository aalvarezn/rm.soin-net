package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.service.ButtonCommandService;
import com.soin.sgrm.service.CrontabService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/crontab")
public class CrontabController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(CrontabController.class);

	@Autowired
	private ReleaseService releaseService;
	@Autowired
	TypeDetailService typeDetailService;
	@Autowired
	ButtonCommandService buttonService;
	@Autowired
	CrontabService crontabService;

	@RequestMapping(value = "/saveCrontab-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveCrontab(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("Crontab") Crontab crontab, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseUser release = releaseService.findReleaseUserById(Integer.parseInt(id));
			TypeDetail typeDetail = null;
			res.setStatus("success");

			if (errors.hasErrors()) {
				res.setStatus("fail");
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
			}
			if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
				res.addError("directoryName", Constant.EMPTY);
			}

			if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
				res.addError("userName", Constant.EMPTY);
			}

			if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
				res.addError("timeCommand", Constant.EMPTY);
			}

			if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
				res.addError("pageName", Constant.EMPTY);
			}

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				return res;
			}
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			crontab.getButton().setRelease(release);
			crontab.setRelease(release);
			crontab.getButton().setHaveCrontab(true);
			crontab = crontabService.saveCrontab(crontab);
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			res.setObj(crontab);
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteCrontab/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteCrontab(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			res.setStatus("success");

			Crontab crontab = crontabService.findById(id);
			crontabService.deleteCrontab(crontab);
			res.setData(crontab.getButton().getId()+"");
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				detail.setButton(null);
			}

			
			res.setObj(crontab);
			
			
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		
		return res;
	}

	@RequestMapping(value = "/findCrontab/{id}", method = RequestMethod.GET)
	public @ResponseBody Crontab findCrontab(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		Crontab crontab = null;
		try {
			
			crontab = crontabService.findById(id);
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			return crontab;
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			logger.log(MyLevel.RELEASE_ERROR, ex.toString());
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;
	}

	@RequestMapping(value = "/updateCrontab-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateCrontab(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("Crontab") Crontab crontab, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseUser release = releaseService.findReleaseUserById(Integer.parseInt(id));
			TypeDetail typeDetail = null;
			res.setStatus("success");

			if (errors.hasErrors()) {
				res.setStatus("fail");
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
			}
			if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
				res.addError("directoryName", Constant.EMPTY);
			}

			if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
				res.addError("userName", Constant.EMPTY);
			}

			if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
				res.addError("timeCommand", Constant.EMPTY);
			}

			if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
				res.addError("pageName", Constant.EMPTY);
			}

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				return res;
			}
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			crontab.getButton().setRelease(release);
			crontab.getButton().setHaveCrontab(true);
			crontab.setRelease(release);
			ButtonCommand old = buttonService.findById(crontab.getButton().getId());
			crontab.getButton().setRelease(release);
			crontab = crontabService.updateCrontab(crontab, old);
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			res.setObj(crontab);
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
