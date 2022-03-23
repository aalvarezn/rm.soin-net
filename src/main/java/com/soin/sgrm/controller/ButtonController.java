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
import com.soin.sgrm.model.ButtonFile;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.DetailButtonFile;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.service.ButtonCommandService;
import com.soin.sgrm.service.ButtonFileService;
import com.soin.sgrm.service.CrontabService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/button")
public class ButtonController extends BaseController {

	public static final Logger logger = Logger.getLogger(ButtonController.class);

	@Autowired
	private ReleaseService releaseService;
	@Autowired
	TypeDetailService typeDetailService;
	@Autowired
	ButtonCommandService buttonService;
	@Autowired
	ButtonFileService buttonFileService;
	
	@Autowired
	CrontabService crontabService;

	@RequestMapping(value = "/saveButtonCommand-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveButtonCommand(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("ButtonCommand") ButtonCommand bc, BindingResult errors, ModelMap model) {
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
			if (bc.getExecuteDirectory() && bc.getDirectoryName().trim().equals("")) {
				res.addError("directoryName", Constant.EMPTY);
			}

			if (bc.getExecuteUser() && bc.getUserName().trim().equals("")) {
				res.addError("userName", Constant.EMPTY);
			}

			if (bc.getWaitCommand() && bc.getTimeCommand().trim().equals("")) {
				res.addError("timeCommand", Constant.EMPTY);
			}

			if (bc.getPrincipalPage() && bc.getPageName().trim().equals("")) {

				res.addError("pageName", Constant.EMPTY);
			}

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				res.setStatus("fail");
				return res;
			}
			for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			bc.setRelease(release);
//			bc.setHaveCrontab(false);
			bc = buttonService.saveButton(bc);
			for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			res.setObj(bc);
		} catch (SQLException ex) {
			Sentry.capture(ex, "button");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("Error al guardar el botón: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteButtonCommand/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteButtonCommand(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			ButtonCommand button = buttonService.findById(id);
			/* TODO: Hacer la validación por el ContrabID */
			if (button.getHaveCrontab()) {
	 		res.setStatus("fail");
	 			Crontab crontab;
	 			crontab= crontabService.findByIdButton(button.getId());
				res.setException("El Botón esta asociado al Crontab "+crontab.getDescriptionCron());
			return res;
		}
			buttonService.deleteButton(button);
			res.setData(id + "");
		} catch (SQLException ex) {
			Sentry.capture(ex, "button");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/findButtonCommand/{id}", method = RequestMethod.GET)
	public @ResponseBody ButtonCommand findButtonCommand(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		ButtonCommand button = null;
		try {
			button = buttonService.findById(id);
			for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			return button;
		} catch (SQLException ex) {
			Sentry.capture(ex, "button");
			logger.log(MyLevel.RELEASE_ERROR, ex.toString());
		} catch (Exception e) {
			Sentry.capture(e, "button");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return null;
	}

	@RequestMapping(value = "/updateButtonCommand-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateButtonCommand(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("ButtonCommand") ButtonCommand bc, BindingResult errors, ModelMap model) {
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
			if (bc.getExecuteDirectory() && bc.getDirectoryName().trim().equals("")) {
				res.addError("directoryName", Constant.EMPTY);
			}

			if (bc.getExecuteUser() && bc.getUserName().trim().equals("")) {
				res.addError("userName", Constant.EMPTY);
			}

			if (bc.getWaitCommand() && bc.getTimeCommand().trim().equals("")) {
				res.addError("timeCommand", Constant.EMPTY);
			}

			if (bc.getPrincipalPage() && bc.getPageName().trim().equals("")) {
				res.addError("pageName", Constant.EMPTY);
			}

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				res.setStatus("fail");
				return res;
			}
			for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			bc.setRelease(release);
			bc = buttonService.updateButton(bc);
			for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
				detail.setButton(null);
			}
			res.setObj(bc);
		} catch (SQLException ex) {
			Sentry.capture(ex, "button");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("Error al actualizar el botón: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	// -------------------------------------------------------------------------------------
	@RequestMapping(value = "/saveButtonFile-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveButtonFile(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("ButtonFile") ButtonFile bf, BindingResult errors, ModelMap model)
			throws SQLException {
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

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				res.setStatus("fail");
				return res;
			}
			for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			bf.setRelease(release);
			bf = buttonFileService.saveButton(bf);
			for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
				detail.setButton(null);
			}
			res.setObj(bf);
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("Error al guardar el botón: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteButtonFile/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteButtonFile(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			ButtonFile button = buttonFileService.findById(id);
			if (button == null) {
				res.setStatus("fail");
				res.setException("El Botón no existe.");
				return res;
			}
			buttonFileService.deleteButton(button);
			res.setData(id + "");
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/findButtonFile/{id}", method = RequestMethod.GET)
	public @ResponseBody ButtonFile findButtonFile(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		ButtonFile button = null;
		try {
			button = buttonFileService.findById(id);
			for (DetailButtonFile detail : button.getDetailsButtonFiles()) {
				detail.setButton(null);
			}
			return button;
		} catch (Exception e) {
			Sentry.capture(e, "button");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;
	}

	@RequestMapping(value = "/updateButtonFile-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateButtonFile(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("ButtonFile") ButtonFile bf, BindingResult errors, ModelMap model) {
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

			if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
				res.setStatus("fail");
				return res;
			}
			for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
				typeDetail = typeDetailService.findByName(detail.getTypeName());
				detail.setTypeDetail(typeDetail);
			}
			bf.setRelease(release);
			bf = buttonFileService.updateButton(bf);
			for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
				detail.setButton(null);
			}
			res.setObj(bf);
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("Error al actualizar el botón: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
