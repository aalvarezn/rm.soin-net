package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.BaseKnowledgeService;
import com.soin.sgrm.service.ComponentService;
import com.soin.sgrm.service.StatusKnowlegeService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PBaseKnowledgeService;
import com.soin.sgrm.service.pos.PComponentService;
import com.soin.sgrm.service.pos.PStatusKnowlegeService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.model.StatusKnowlege;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PBaseKnowledge;
import com.soin.sgrm.model.pos.PComponent;
import com.soin.sgrm.model.pos.PStatusKnowlege;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/baseKnowledge")
public class BaseKnowledgeController extends BaseController {

	@Autowired
	BaseKnowledgeService baseKnowledgeService;

	@Autowired
	StatusKnowlegeService statusService;

	@Autowired
	ComponentService componentService;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	@Autowired
	AttentionGroupService attentionGroupService;

	@Autowired
	SystemService systemService;

	@Autowired
	PBaseKnowledgeService pbaseKnowledgeService;

	@Autowired
	PStatusKnowlegeService pstatusService;

	@Autowired
	PComponentService pcomponentService;

	@Autowired
	PUserService puserService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	@Autowired
	PSystemService psystemService;

	private final Environment environment;

	@Autowired
	public BaseKnowledgeController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	public static final Logger logger = Logger.getLogger(BaseKnowledgeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {

			if (profileActive().equals("oracle")) {
				Integer userLogin = getUserLogin().getId();
				boolean rolRM = false;
				List<System> systemList = new ArrayList<System>();
				if (request.isUserInRole("ROLE_Release Manager")) {
					rolRM = true;
				}
				if (rolRM) {
					systemList = systemService.list();
				} else {
					List<AttentionGroup> attentionGroups = attentionGroupService.findGroupByUserId(userLogin);
					List<Long> listAttentionGroupId = new ArrayList<Long>();
					for (AttentionGroup attentionGroup : attentionGroups) {
						listAttentionGroupId.add(attentionGroup.getId());
					}
					systemList = systemService.findByGroupIncidence(listAttentionGroupId);
					Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
					systemList.clear();
					systemList.addAll(systemWithRepeat);
				}

				loadCountsRelease(request, userLogin, rolRM);
				List<Component> components = componentService.findAll();
				List<StatusKnowlege> statuses = statusService.findAll();

				model.addAttribute("statuses", statuses);
				model.addAttribute("system", systemList);
				model.addAttribute("components", components);
			} else if (profileActive().equals("postgres")) {
				Integer userLogin = getUserLogin().getId();
				boolean rolRM = false;
				List<PSystem> systemList = new ArrayList<PSystem>();
				if (request.isUserInRole("ROLE_Release Manager")) {
					rolRM = true;
				}
				if (rolRM) {
					systemList = psystemService.list();
				} else {
					List<PAttentionGroup> attentionGroups = pattentionGroupService.findGroupByUserId(userLogin);
					List<Long> listAttentionGroupId = new ArrayList<Long>();
					for (PAttentionGroup attentionGroup : attentionGroups) {
						listAttentionGroupId.add(attentionGroup.getId());
					}
					systemList = psystemService.findByGroupIncidence(listAttentionGroupId);
					Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
					systemList.clear();
					systemList.addAll(systemWithRepeat);
				}

				loadCountsRelease(request, userLogin, rolRM);
				List<PComponent> components = pcomponentService.findAll();
				List<PStatusKnowlege> statuses = pstatusService.findAll();

				model.addAttribute("statuses", statuses);
				model.addAttribute("system", systemList);
				model.addAttribute("components", components);
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			e.printStackTrace();
		}
		return "/incidence/baseKnowledge/baseKnowledge";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			Integer userLogin = getUserLogin().getId();
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			Long componentId;
			Integer systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("componentId").equals("")) {
				componentId = null;
			} else {
				componentId = (long) Integer.parseInt(request.getParameter("componentId"));
			}
			if (request.getParameter("systemId").equals("")) {
				systemId = null;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			if (systemId == 0) {
				componentId = (long) 0;
			}
			String dateRange = request.getParameter("dateRange");
			boolean rolRM = false;
			if (request.isUserInRole("ROLE_Release Manager")) {
				rolRM = true;
			}

			if (profileActive().equals("oracle")) {
				JsonSheet<BaseKnowledge> baseKnowledges = new JsonSheet<>();
				baseKnowledges = baseKnowledgeService.findAll2(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, componentId, systemId, userLogin, rolRM);
				return baseKnowledges;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PBaseKnowledge> baseKnowledges = new JsonSheet<>();
				baseKnowledges = pbaseKnowledgeService.findAll2(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, componentId, systemId, userLogin, rolRM);
				return baseKnowledges;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody BaseKnowledge addBaseKnowledge) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				StatusKnowlege status = statusService.findByKey("code", "draft");
				if (status != null) {
					addBaseKnowledge.setStatus(status);
					addBaseKnowledge.setUser(user);
					addBaseKnowledge.setPublicate(false);
					SystemInfo system = new SystemInfo();
					system.setId(addBaseKnowledge.getSystemId());
					addBaseKnowledge.setSystem(system);
					addBaseKnowledge.setUrl("/wrk2/app_docs_gestInc");
					addBaseKnowledge.setRequestDate(CommonUtils.getSystemTimestamp());
					res.setStatus("success");
					addBaseKnowledge.setMotive("Inicio ");
					addBaseKnowledge.setOperator(user.getFullName());
					Component component = componentService.findById(addBaseKnowledge.getComponentId());
					addBaseKnowledge.setComponent(component);
					addBaseKnowledge.setNumError((baseKnowledgeService.generateErrorNumber(component.getName())));
					baseKnowledgeService.save(addBaseKnowledge);
					res.setData(addBaseKnowledge.getId().toString());
					res.setMessage("Se creo correctamente el error!");

				} else {

					res.setStatus("exception");
					res.setMessage("Error al crear error comunicarse con los administradores!");
				}
			} else if (profileActive().equals("postgres")) {
				PUser puser = puserService.getUserByUsername(getUserLogin().getUsername());
				PStatusKnowlege status = pstatusService.findByKey("code", "draft");
				PBaseKnowledge paddBaseKnowledge = new PBaseKnowledge();
				paddBaseKnowledge.setDataRequired(addBaseKnowledge.getDataRequired());
				paddBaseKnowledge.setDescription(addBaseKnowledge.getDescription());
				paddBaseKnowledge.setNote(addBaseKnowledge.getNote());
				paddBaseKnowledge.setPublicate(addBaseKnowledge.getPublicate());
				if (status != null) {
					paddBaseKnowledge.setStatus(status);
					paddBaseKnowledge.setUser(puser);
					paddBaseKnowledge.setPublicate(false);
					PSystemInfo system = new PSystemInfo();
					system.setId(addBaseKnowledge.getSystemId());
					paddBaseKnowledge.setSystem(system);
					paddBaseKnowledge.setUrl("/wrk2/app_docs_gestInc");
					paddBaseKnowledge.setRequestDate(CommonUtils.getSystemTimestamp());
					res.setStatus("success");
					paddBaseKnowledge.setMotive("Inicio ");
					paddBaseKnowledge.setOperator(puser.getFullName());
					PComponent pcomponent = pcomponentService.findById(addBaseKnowledge.getComponentId());
					paddBaseKnowledge.setComponent(pcomponent);
					paddBaseKnowledge.setNumError((pbaseKnowledgeService.generateErrorNumber(pcomponent.getName())));
					pbaseKnowledgeService.save(paddBaseKnowledge);
					res.setData(paddBaseKnowledge.getId().toString());
					res.setMessage("Se creo correctamente el error!");

				} else {

					res.setStatus("exception");
					res.setMessage("Error al crear error comunicarse con los administradores!");
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			res.setStatus("exception");
			res.setMessage("Error al crear baseKnowledge!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}


	@RequestMapping(value = "/saveBaseKnowledge", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request,
			@RequestBody BaseKnowledge addBaseKnowledge) {

		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {

			if (profileActive().equals("oracle")) {
				errors = validSections(addBaseKnowledge, errors);
				BaseKnowledge baseKnowledgeMod = baseKnowledgeService.findById(addBaseKnowledge.getId());
				addBaseKnowledge.setSystem(baseKnowledgeMod.getSystem());
				addBaseKnowledge.setUser(baseKnowledgeMod.getUser());
				addBaseKnowledge.setNumError(baseKnowledgeMod.getNumError());
				addBaseKnowledge.setComponent(baseKnowledgeMod.getComponent());
				addBaseKnowledge.setStatus(baseKnowledgeMod.getStatus());
				addBaseKnowledge.setMotive(baseKnowledgeMod.getMotive());
				addBaseKnowledge.setUrl(baseKnowledgeMod.getUrl());
				addBaseKnowledge.setOperator(baseKnowledgeMod.getUser().getFullName());
				addBaseKnowledge.setRequestDate(baseKnowledgeMod.getRequestDate());
				addBaseKnowledge.setFiles(baseKnowledgeMod.getFiles());
				if (addBaseKnowledge.getPublicateValidate() == 1) {
					addBaseKnowledge.setPublicate(true);
				} else {
					addBaseKnowledge.setPublicate(false);
				}

				if (addBaseKnowledge.getDescription().length() < 256) {
					addBaseKnowledge.setDescription(addBaseKnowledge.getDescription());
				} else {
					addBaseKnowledge.setDescription(baseKnowledgeMod.getDescription());
				}

				baseKnowledgeService.update(addBaseKnowledge);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
			} else if (profileActive().equals("postgres")) {
				errors = validSections(addBaseKnowledge, errors);
				PBaseKnowledge pbaseKnowledgeMod = pbaseKnowledgeService.findById(addBaseKnowledge.getId());
				PBaseKnowledge paddBaseKnowledge = new PBaseKnowledge();
				paddBaseKnowledge.setDescription(addBaseKnowledge.getDescription());
				paddBaseKnowledge.setId(addBaseKnowledge.getId());
				paddBaseKnowledge.setNote(addBaseKnowledge.getNote());
				paddBaseKnowledge.setDataRequired(addBaseKnowledge.getDataRequired());
				paddBaseKnowledge.setSystem(pbaseKnowledgeMod.getSystem());
				paddBaseKnowledge.setUser(pbaseKnowledgeMod.getUser());
				paddBaseKnowledge.setNumError(pbaseKnowledgeMod.getNumError());
				paddBaseKnowledge.setComponent(pbaseKnowledgeMod.getComponent());
				paddBaseKnowledge.setStatus(pbaseKnowledgeMod.getStatus());
				paddBaseKnowledge.setMotive(pbaseKnowledgeMod.getMotive());
				paddBaseKnowledge.setUrl(pbaseKnowledgeMod.getUrl());
				paddBaseKnowledge.setOperator(pbaseKnowledgeMod.getUser().getFullName());
				paddBaseKnowledge.setRequestDate(pbaseKnowledgeMod.getRequestDate());
				paddBaseKnowledge.setFiles(pbaseKnowledgeMod.getFiles());
				if (addBaseKnowledge.getPublicateValidate() == 1) {
					paddBaseKnowledge.setPublicate(true);
				} else {
					paddBaseKnowledge.setPublicate(false);
				}

				if (addBaseKnowledge.getDescription().length() < 256) {
					paddBaseKnowledge.setDescription(addBaseKnowledge.getDescription());
				} else {
					paddBaseKnowledge.setDescription(pbaseKnowledgeMod.getDescription());
				}

				pbaseKnowledgeService.update(paddBaseKnowledge);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			res.setStatus("exception");
			res.setException("Error al guardar el error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/editBaseKnowledge-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		try {

			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledgeEdit = new BaseKnowledge();
				if (id == null) {
					return "redirect:/";
				}

				baseKnowledgeEdit = baseKnowledgeService.findById(id);

				if (baseKnowledgeEdit == null) {
					return "/plantilla/404";
				}

				if (!baseKnowledgeEdit.getStatus().getName().equals("Borrador")) {
					redirectAttributes.addFlashAttribute("data", "error no disponible para editar.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}

				model.addAttribute("baseKnowledge", baseKnowledgeEdit);
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledgeEdit = new PBaseKnowledge();
				if (id == null) {
					return "redirect:/";
				}

				baseKnowledgeEdit = pbaseKnowledgeService.findById(id);

				if (baseKnowledgeEdit == null) {
					return "/plantilla/404";
				}

				if (!baseKnowledgeEdit.getStatus().getName().equals("Borrador")) {
					redirectAttributes.addFlashAttribute("data", "error no disponible para editar.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}

				model.addAttribute("baseKnowledge", baseKnowledgeEdit);
			}
			return "/incidence/baseKnowledge/editBaseKnowledge";

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/getBaseKnowledge-{id}", method = RequestMethod.GET)
	public @ResponseBody Object getBaseKnowledge(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		try {

			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledgeEdit = new BaseKnowledge();
				baseKnowledgeEdit = baseKnowledgeService.findById(id);

				return baseKnowledgeEdit;
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledgeEdit = new PBaseKnowledge();
				baseKnowledgeEdit = pbaseKnowledgeService.findById(id);

				return baseKnowledgeEdit;
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return null;
	}

	@RequestMapping(value = "/summaryBaseKnowledge-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {

			if (profileActive().equals("oracle")) {
				model.addAttribute("parameter", status);
				BaseKnowledge baseKnowledge = null;
				if (CommonUtils.isNumeric(status)) {
					baseKnowledge = baseKnowledgeService.findById(Long.parseLong(status));
				}

				if (baseKnowledge == null) {
					return "redirect:/";
				}
				model.addAttribute("statuses", statusService.findAll());
				model.addAttribute("baseKnowledge", baseKnowledge);
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("parameter", status);
				PBaseKnowledge baseKnowledge = null;
				if (CommonUtils.isNumeric(status)) {
					baseKnowledge = pbaseKnowledgeService.findById(Long.parseLong(status));
				}

				if (baseKnowledge == null) {
					return "redirect:/";
				}
				model.addAttribute("statuses", pstatusService.findAll());
				model.addAttribute("baseKnowledge", baseKnowledge);
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen baseKnowledge." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/incidence/baseKnowledge/summaryBaseKnowledge";
	}

	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			if (profileActive().equals("oracle")) {
				model.addAttribute("parameter", status);
				BaseKnowledge baseKnowledge = null;
				if (CommonUtils.isNumeric(status)) {
					baseKnowledge = baseKnowledgeService.findById(Long.parseLong(status));
				}

				if (baseKnowledge == null) {
					return "redirect:/";
				}
				model.addAttribute("baseKnowledge", baseKnowledge);
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("parameter", status);
				PBaseKnowledge baseKnowledge = null;
				if (CommonUtils.isNumeric(status)) {
					baseKnowledge = pbaseKnowledgeService.findById(Long.parseLong(status));
				}

				if (baseKnowledge == null) {
					return "redirect:/";
				}
				model.addAttribute("baseKnowledge", baseKnowledge);
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen error." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/incidence/baseKnowledge/tinySummaryBaseKnowledge";
	}

	@RequestMapping(value = "/updateBaseKnowledge/{baseKnowledgeId}", method = RequestMethod.GET)
	public String updateBaseKnowledge(@PathVariable String baseKnowledgeId, HttpServletRequest request, Locale locale,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {

			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledge = null;

				if (CommonUtils.isNumeric(baseKnowledgeId)) {
					baseKnowledge = baseKnowledgeService.findById((long) Integer.parseInt(baseKnowledgeId));
				}
				// Si el release no existe se regresa al inicio.
				if (baseKnowledge == null) {
					return "redirect:/homeBaseKnowledge";
				}
				// Verificar si existe un flujo para el sistema

				StatusKnowlege status = statusService.findByKey("code", "valid");

				baseKnowledge.setStatus(status);
				baseKnowledge.setMotive(status.getReason());
				baseKnowledge.setRequestDate((CommonUtils.getSystemTimestamp()));

				baseKnowledge.setOperator(getUserLogin().getFullName());

				baseKnowledgeService.update(baseKnowledge);

				return "redirect:/baseKnowledge/summaryBaseKnowledge-" + baseKnowledge.getId();
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledge = null;

				if (CommonUtils.isNumeric(baseKnowledgeId)) {
					baseKnowledge = pbaseKnowledgeService.findById((long) Integer.parseInt(baseKnowledgeId));
				}
				// Si el release no existe se regresa al inicio.
				if (baseKnowledge == null) {
					return "redirect:/homeBaseKnowledge";
				}
				// Verificar si existe un flujo para el sistema

				PStatusKnowlege status = pstatusService.findByKey("code", "valid");

				baseKnowledge.setStatus(status);
				baseKnowledge.setMotive(status.getReason());
				baseKnowledge.setRequestDate((CommonUtils.getSystemTimestamp()));

				baseKnowledge.setOperator(getUserLogin().getFullName());

				pbaseKnowledgeService.update(baseKnowledge);

				return "redirect:/baseKnowledge/summaryBaseKnowledge-" + baseKnowledge.getId();
			}

		} catch (Exception e) {
			Sentry.capture(e, "BaseKnowledge");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeBaseKnowledge";
	}

	public ArrayList<MyError> validSections(BaseKnowledge baseKnowledge, ArrayList<MyError> errors) {

		if (baseKnowledge.getDescription() != null) {
			if (baseKnowledge.getDescription().trim().equals("")) {

				errors.add(new MyError("description", "Debe ingresar algun dato"));

			} else {
				if (baseKnowledge.getDescription().length() > 256) {
					errors.add(new MyError("description", "La cantidad de caracteres no puede ser mayor a 256"));
				}
			}
		} else {
			errors.add(new MyError("description", "Debe ingresar algun dato"));
		}

		if (baseKnowledge.getDataRequired() != null) {
			if (baseKnowledge.getDataRequired().trim().equals("")) {

				errors.add(new MyError("data", "Debe ingresar algun dato"));

			}
		} else {
			errors.add(new MyError("data", "Debe ingresar algun dato"));
		}

		return errors;
	}

	public MyError getErrorSenders(String senders) {

		String[] listSenders = senders.split(",");
		String to_invalid = "";
		for (int i = 0; i < listSenders.length; i++) {
			if (!CommonUtils.isValidEmailAddress(listSenders[i])) {
				if (to_invalid.equals("")) {
					to_invalid += listSenders[i];
				} else {
					to_invalid += "," + listSenders[i];
				}

			}
		}
		if (!to_invalid.equals("")) {
			return new MyError("senders", "dirección(es) inválida(s) " + to_invalid);
		}
		return null;
	}

	@RequestMapping(value = "/cancelError", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idError", required = true) Long idError) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledge = baseKnowledgeService.findById(idError);
				StatusKnowlege status = statusService.findByKey("name", "Obsoleto");
				baseKnowledge.setStatus(status);
				baseKnowledge.setOperator(getUserLogin().getFullName());
				baseKnowledge.setMotive(status.getReason());
				baseKnowledgeService.update(baseKnowledge);
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledge = pbaseKnowledgeService.findById(idError);
				PStatusKnowlege status = pstatusService.findByKey("name", "Obsoleto");
				baseKnowledge.setStatus(status);
				baseKnowledge.setOperator(getUserLogin().getFullName());
				baseKnowledge.setMotive(status.getReason());
				pbaseKnowledgeService.update(baseKnowledge);
			}

			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "base");
			res.setStatus("exception");
			res.setException("Error al cancelar el error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/statusError", method = RequestMethod.GET)
	public @ResponseBody JsonResponse changeStatusError(HttpServletRequest request, Model model,
			@RequestParam(value = "idError", required = true) Long idError,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = true) String dateChange,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledge = baseKnowledgeService.findById(idError);
				StatusKnowlege status = statusService.findById(idStatus);
				baseKnowledge.setStatus(status);
				baseKnowledge.setOperator(getUserLogin().getFullName());
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				baseKnowledge.setRequestDate(dateFormat);
				baseKnowledge.setMotive(motive);
				baseKnowledgeService.update(baseKnowledge);
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledge = pbaseKnowledgeService.findById(idError);
				PStatusKnowlege status = pstatusService.findById(idStatus);
				baseKnowledge.setStatus(status);
				baseKnowledge.setOperator(getUserLogin().getFullName());
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				baseKnowledge.setRequestDate(dateFormat);
				baseKnowledge.setMotive(motive);
				pbaseKnowledgeService.update(baseKnowledge);
			}

			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteBaseKnowledge/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteBaseKnowledge(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				BaseKnowledge baseKnowledge = baseKnowledgeService.findById(id);
				if (baseKnowledge.getStatus().getName().equals("Borrador")) {
					if (baseKnowledge.getUser().getUsername().equals(getUserLogin().getUsername())) {
						StatusKnowlege status = statusService.findByKey("name", "Obsoleto");
						baseKnowledge.setStatus(status);
						baseKnowledge.setMotive(status.getReason());
						baseKnowledge.setOperator(getUserLogin().getFullName());
						baseKnowledgeService.update(baseKnowledge);
					} else {
						res.setStatus("fail");
						res.setException("No tiene permisos sobre el error.");
					}
				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el error no esta en estado de Borrador.");
				}
			} else if (profileActive().equals("postgres")) {
				PBaseKnowledge baseKnowledge = pbaseKnowledgeService.findById(id);
				if (baseKnowledge.getStatus().getName().equals("Borrador")) {
					if (baseKnowledge.getUser().getUsername().equals(getUserLogin().getUsername())) {
						PStatusKnowlege status = pstatusService.findByKey("name", "Obsoleto");
						baseKnowledge.setStatus(status);
						baseKnowledge.setMotive(status.getReason());
						baseKnowledge.setOperator(getUserLogin().getFullName());
						pbaseKnowledgeService.update(baseKnowledge);
					} else {
						res.setStatus("fail");
						res.setException("No tiene permisos sobre el error.");
					}
				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el error no esta en estado de Borrador.");
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	public void loadCountsRelease(HttpServletRequest request, Integer id, boolean isRm) {
		// PUser userLogin = getUserLogin();
		// List<PSystem> systems = systemService.listProjects(userLogin.getId());

		if (profileActive().equals("oracle")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			if (!isRm) {

				userC.put("draft", baseKnowledgeService.countByType(id, "Borrador", 1, null));
				userC.put("requested", baseKnowledgeService.countByType(id, "Obsoleto", 1, null));
				userC.put("completed", baseKnowledgeService.countByType(id, "Vigente", 1, null));
				userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			} else {
				userC.put("draft", baseKnowledgeService.countByType(id, "Borrador", 2, null));
				userC.put("requested", baseKnowledgeService.countByType(id, "Obsoleto", 2, null));
				userC.put("completed", baseKnowledgeService.countByType(id, "Vigente", 2, null));
				userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			}
			request.setAttribute("userC", userC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			if (!isRm) {

				userC.put("draft", pbaseKnowledgeService.countByType(id, "Borrador", 1, null));
				userC.put("requested", pbaseKnowledgeService.countByType(id, "Obsoleto", 1, null));
				userC.put("completed", pbaseKnowledgeService.countByType(id, "Vigente", 1, null));
				userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			} else {
				userC.put("draft", pbaseKnowledgeService.countByType(id, "Borrador", 2, null));
				userC.put("requested", pbaseKnowledgeService.countByType(id, "Obsoleto", 2, null));
				userC.put("completed", pbaseKnowledgeService.countByType(id, "Vigente", 2, null));
				userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			}
			request.setAttribute("userC", userC);
		}

	}

	@RequestMapping(value = { "/getComponent/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getPriority(@PathVariable Integer id, Locale locale, Model model) {

		try {

			if (profileActive().equals("oracle")) {
				List<Component> listSystemStatus = null;
				listSystemStatus = componentService.findBySystem(id);
				return listSystemStatus;
			} else if (profileActive().equals("postgres")) {
				List<PComponent> listSystemStatus = null;
				listSystemStatus = pcomponentService.findBySystem(id);
				return listSystemStatus;
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return null;
	}

	public List<String> getCC(String ccs) {

		List<String> getCC = new ArrayList<String>();
		if (ccs != null) {
			ccs.split(",");
			for (String cc : ccs.split(",")) {
				getCC.add(cc);
			}
		}
		return getCC;
	}

}
