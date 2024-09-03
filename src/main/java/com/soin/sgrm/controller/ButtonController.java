package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.ButtonFile;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.DetailButtonFile;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PButtonFile;
import com.soin.sgrm.model.pos.PCrontab;
import com.soin.sgrm.model.pos.PDetailButtonCommand;
import com.soin.sgrm.model.pos.PDetailButtonFile;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PTypeDetail;
import com.soin.sgrm.service.ButtonCommandService;
import com.soin.sgrm.service.ButtonFileService;
import com.soin.sgrm.service.CrontabService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.pos.PButtonCommandService;
import com.soin.sgrm.service.pos.PButtonFileService;
import com.soin.sgrm.service.pos.PCrontabService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PTypeDetailService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/button")
public class ButtonController extends BaseController {

	public static final Logger logger = Logger.getLogger(ButtonController.class);

	@Autowired
	ReleaseService releaseService;
	@Autowired
	TypeDetailService typeDetailService;
	@Autowired
	ButtonCommandService buttonService;
	@Autowired
	ButtonFileService buttonFileService;
	@Autowired
	CrontabService crontabService;

	@Autowired
	PReleaseService preleaseService;
	@Autowired
	PTypeDetailService ptypeDetailService;
	@Autowired
	PButtonCommandService pbuttonService;
	@Autowired
	PButtonFileService pbuttonFileService;
	@Autowired
	PCrontabService pcrontabService;

	private final Environment environment;

	@Autowired
	public ButtonController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = "/saveButtonCommand-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveButtonCommand(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("ButtonCommand") ButtonCommand bc, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
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
//				bc.setHaveCrontab(false);
				bc = buttonService.saveButton(bc);
				for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(bc);
			} else if (profileActive().equals("postgres")) {
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				PButtonCommand pbc = new PButtonCommand();

				List<PDetailButtonCommand> listDetail = new ArrayList<PDetailButtonCommand>();
				for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
					PDetailButtonCommand pdetail = new PDetailButtonCommand();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					pdetail.setDescription(detail.getDescription());
					pdetail.setTypeName(detail.getTypeName());
					pdetail.setTypeText(detail.getTypeText());
					pdetail.setIsRequired(detail.getIsRequired());
					PTypeDetail ptypeDetail = ptypeDetailService.findByName(detail.getTypeName());
					pdetail.setTypeDetail(ptypeDetail);
					pdetail.setQuotationMarks(detail.getQuotationMarks());
					PButtonCommand pbuttonSon = new PButtonCommand();
					pbuttonSon.setId(bc.getId());
					pdetail.setButton(pbuttonSon);
					listDetail.add(pdetail);
				}
				pbc.setDetailsButtonCommands(listDetail);
				pbc.setId(bc.getId());
				pbc.setModule(bc.getModule());
				pbc.setDescription(bc.getDescription());
				pbc.setName(bc.getName());
				pbc.setCommand(bc.getCommand());
				pbc.setExecuteDirectory(bc.getExecuteDirectory());
				pbc.setExecuteUser(bc.getExecuteUser());
				pbc.setDirectoryName(bc.getDirectoryName());
				pbc.setUserName(bc.getUserName());
				pbc.setUseUserEnvironment(bc.getUseUserEnvironment());
				pbc.setHaveHTML(bc.getHaveHTML());
				pbc.setHideExecute(bc.getHideExecute());
				pbc.setUserminAvailability(bc.getUserminAvailability());
				pbc.setClearVariables(bc.getClearVariables());
				pbc.setWaitCommand(bc.getWaitCommand());
				pbc.setTimeCommand(bc.getTimeCommand());
				pbc.setPrincipalPage(bc.getPrincipalPage());
				pbc.setPageName(bc.getPageName());
				pbc.setDetailQuotationMarks(bc.getDetailQuotationMarks());
				pbc.setDetailName(bc.getDetailName());
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
				for (PDetailButtonCommand detail : pbc.getDetailsButtonCommands()) {
					typeDetail = ptypeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				pbc.setRelease(release);
//				bc.setHaveCrontab(false);
				pbc = pbuttonService.saveButton(pbc);
				for (PDetailButtonCommand detail : pbc.getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(pbc);
			}

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
			if (profileActive().equals("oracle")) {
				ButtonCommand button = buttonService.findById(id);
				/* TODO: Hacer la validación por el ContrabID */
				if (button.getHaveCrontab()) {
					res.setStatus("fail");
					Crontab crontab;
					crontab = crontabService.findByIdButton(button.getId());
					res.setException("El Botón esta asociado al Crontab " + crontab.getDescriptionCron());
					return res;
				}
				buttonService.deleteButton(button);
				res.setData(id + "");
			} else if (profileActive().equals("postgres")) {
				PButtonCommand button = pbuttonService.findById(id);
				/* TODO: Hacer la validación por el ContrabID */
				if (button.getHaveCrontab()) {
					res.setStatus("fail");
					PCrontab crontab;
					crontab = pcrontabService.findByIdButton(button.getId());
					res.setException("El Botón esta asociado al Crontab " + crontab.getDescriptionCron());
					return res;
				}
				pbuttonService.deleteButton(button);
				res.setData(id + "");
			}
			
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
	public @ResponseBody Object findButtonCommand(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		
		try {
			
			if (profileActive().equals("oracle")) {
				ButtonCommand button = null;
				button = buttonService.findById(id);
				for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				return button;
			} else if (profileActive().equals("postgres")) {
				PButtonCommand button = null;
				button = pbuttonService.findById(id);
				for (PDetailButtonCommand detail : button.getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				return button;
			}
			
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
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				PButtonCommand pbc=new PButtonCommand();
				List<PDetailButtonCommand> listDetail = new ArrayList<PDetailButtonCommand>();
				for (DetailButtonCommand detail : bc.getDetailsButtonCommands()) {
					PDetailButtonCommand pdetail = new PDetailButtonCommand();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					pdetail.setDescription(detail.getName());
					pdetail.setTypeName(detail.getTypeName());
					pdetail.setTypeText(detail.getTypeText());
					pdetail.setIsRequired(detail.getIsRequired());
					PTypeDetail ptypeDetail = ptypeDetailService.findByName(detail.getTypeName());
					pdetail.setTypeDetail(ptypeDetail);
					pdetail.setQuotationMarks(detail.getQuotationMarks());
					PButtonCommand pbuttonSon = new PButtonCommand();
					pbuttonSon.setId(bc.getId());
					pdetail.setButton(pbuttonSon);
					listDetail.add(pdetail);
				}
				pbc.setDetailsButtonCommands(listDetail);
				pbc.setId(bc.getId());
				pbc.setModule(bc.getModule());
				pbc.setDescription(bc.getDescription());
				pbc.setName(bc.getName());
				pbc.setCommand(bc.getCommand());
				pbc.setExecuteDirectory(bc.getExecuteDirectory());
				pbc.setExecuteUser(bc.getExecuteUser());
				pbc.setDirectoryName(bc.getDirectoryName());
				pbc.setUserName(bc.getUserName());
				pbc.setUseUserEnvironment(bc.getUseUserEnvironment());
				pbc.setHaveHTML(bc.getHaveHTML());
				pbc.setHideExecute(bc.getHideExecute());
				pbc.setUserminAvailability(bc.getUserminAvailability());
				pbc.setClearVariables(bc.getClearVariables());
				pbc.setWaitCommand(bc.getWaitCommand());
				pbc.setTimeCommand(bc.getTimeCommand());
				pbc.setPrincipalPage(bc.getPrincipalPage());
				pbc.setPageName(bc.getPageName());
				pbc.setDetailQuotationMarks(bc.getDetailQuotationMarks());
				pbc.setDetailName(bc.getDetailName());
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
				for (PDetailButtonCommand detail : pbc.getDetailsButtonCommands()) {
					typeDetail = ptypeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				pbc.setRelease(release);
				pbc = pbuttonService.updateButton(pbc);
				for (PDetailButtonCommand detail : pbc.getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(pbc);
			}
			
			
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
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				PButtonFile pbf=new PButtonFile();
				pbf.setId(bf.getId());
				pbf.setDescription(bf.getDescription());
				pbf.setDescriptionHtml(bf.getDescriptionHtml());
				pbf.setModule(bf.getModule());
				pbf.setFileEdit(bf.getFileEdit());
				pbf.setOwner(bf.getOwner());
				pbf.setPermissions(bf.getPermissions());
				pbf.setReplaceVariables(bf.isReplaceVariables());
				pbf.setUserminAvailability(bf.isUserminAvailability());
				pbf.setCommandBeforeEditing(bf.getCommandBeforeEditing());
				pbf.setCommandBeforeSaving(bf.getCommandBeforeSaving());
				pbf.setCommandBeforeExecuting(bf.getCommandBeforeExecuting());
				List<PDetailButtonFile> listDetail = new ArrayList<PDetailButtonFile>();
				for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
					PDetailButtonFile pdetail = new PDetailButtonFile();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					pdetail.setDescription(detail.getName());
					pdetail.setTypeName(detail.getTypeName());
					pdetail.setTypeText(detail.getTypeText());
					pdetail.setIsRequired(detail.getIsRequired());
					PTypeDetail ptypeDetail = ptypeDetailService.findByName(detail.getTypeName());
					pdetail.setTypeDetail(ptypeDetail);
					pdetail.setQuotationMarks(detail.getQuotationMarks());
					PButtonFile pbuttonSon = new PButtonFile();
					pbuttonSon.setId(pbf.getId());
					pdetail.setButton(pbuttonSon);
					listDetail.add(pdetail);
				}
				pbf.setDetailsButtonFiles(listDetail);
				
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
				for (PDetailButtonFile detail : pbf.getDetailsButtonFiles()) {
					typeDetail = ptypeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				pbf.setRelease(release);
				pbf = pbuttonFileService.saveButton(pbf);
				for (PDetailButtonFile detail : pbf.getDetailsButtonFiles()) {
					detail.setButton(null);
				}
				res.setObj(bf);
			}
		
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
			if (profileActive().equals("oracle")) {
				ButtonFile button = buttonFileService.findById(id);
				if (button == null) {
					res.setStatus("fail");
					res.setException("El Botón no existe.");
					return res;
				}
				buttonFileService.deleteButton(button);
				res.setData(id + "");
			} else if (profileActive().equals("postgres")) {
				PButtonFile button = pbuttonFileService.findById(id);
				if (button == null) {
					res.setStatus("fail");
					res.setException("El Botón no existe.");
					return res;
				}
				pbuttonFileService.deleteButton(button);
				res.setData(id + "");
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/findButtonFile/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findButtonFile(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		
		try {
			if (profileActive().equals("oracle")) {
				ButtonFile button = null;
				button = buttonFileService.findById(id);
				for (DetailButtonFile detail : button.getDetailsButtonFiles()) {
					detail.setButton(null);
				}
				return button;
			} else if (profileActive().equals("postgres")) {
				PButtonFile button = null;
				button = pbuttonFileService.findById(id);
				for (PDetailButtonFile detail : button.getDetailsButtonFiles()) {
					detail.setButton(null);
				}
				return button;
			}
			
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
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				PButtonFile pbf=new PButtonFile();
				pbf.setId(bf.getId());
				pbf.setDescription(bf.getDescription());
				pbf.setDescriptionHtml(bf.getDescriptionHtml());
				pbf.setModule(bf.getModule());
				pbf.setFileEdit(bf.getFileEdit());
				pbf.setOwner(bf.getOwner());
				pbf.setPermissions(bf.getPermissions());
				pbf.setReplaceVariables(bf.isReplaceVariables());
				pbf.setUserminAvailability(bf.isUserminAvailability());
				pbf.setCommandBeforeEditing(bf.getCommandBeforeEditing());
				pbf.setCommandBeforeSaving(bf.getCommandBeforeSaving());
				pbf.setCommandBeforeExecuting(bf.getCommandBeforeExecuting());
				List<PDetailButtonFile> listDetail = new ArrayList<PDetailButtonFile>();
				for (DetailButtonFile detail : bf.getDetailsButtonFiles()) {
					PDetailButtonFile pdetail = new PDetailButtonFile();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					pdetail.setDescription(detail.getName());
					pdetail.setTypeName(detail.getTypeName());
					pdetail.setTypeText(detail.getTypeText());
					pdetail.setIsRequired(detail.getIsRequired());
					PTypeDetail ptypeDetail = ptypeDetailService.findByName(detail.getTypeName());
					pdetail.setTypeDetail(ptypeDetail);
					pdetail.setQuotationMarks(detail.getQuotationMarks());
					PButtonFile pbuttonSon = new PButtonFile();
					pbuttonSon.setId(pbf.getId());
					pdetail.setButton(pbuttonSon);
					listDetail.add(pdetail);
				}
				pbf.setDetailsButtonFiles(listDetail);
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
				for (PDetailButtonFile detail : pbf.getDetailsButtonFiles()) {
					typeDetail = ptypeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				pbf.setRelease(release);
				pbf = pbuttonFileService.updateButton(pbf);
				for (PDetailButtonFile detail : pbf.getDetailsButtonFiles()) {
					detail.setButton(null);
				}
				res.setObj(pbf);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "button");
			res.setStatus("exception");
			res.setException("Error al actualizar el botón: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
