package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.RequestRM_P1_R2;
import com.soin.sgrm.model.RequestRM_P1_R3;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorRequestService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RequestBaseR1Service;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestRM_P1_R1Service;
import com.soin.sgrm.service.RequestRM_P1_R2Service;
import com.soin.sgrm.service.RequestRM_P1_R3Service;
import com.soin.sgrm.service.RequestRM_P1_R4Service;
import com.soin.sgrm.service.RequestRM_P1_R5Service;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionR4Service;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/request")
public class RequestBaseController extends BaseController {

	@Autowired
	SystemService systemService;

	@Autowired
	ProjectService projectService;

	@Autowired
	SigesService sigeService;

	@Autowired
	StatusRequestService statusService;

	@Autowired
	RequestBaseService requestBaseService;

	@Autowired
	RequestBaseR1Service requestBaseR1Service;

	@Autowired
	TypePetitionService typePetitionService;

	@Autowired
	TypePetitionR4Service typePetitionR4Service;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	@Autowired
	RequestRM_P1_R1Service requestServiceRm1;

	@Autowired
	RequestRM_P1_R2Service requestServiceRm2;

	@Autowired
	RequestRM_P1_R3Service requestServiceRm3;

	@Autowired
	RequestRM_P1_R4Service requestServiceRm4;

	@Autowired
	RequestRM_P1_R5Service requestServiceRm5;

	@Autowired
	AmbientService ambientService;

	@Autowired
	EmailTemplateService emailService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	ErrorRequestService errorService;
	
	@Autowired
	UserInfoService userInfoService;

	public static final Logger logger = Logger.getLogger(RFCController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			List<StatusRequest> statuses = statusService.findAll();
			List<TypePetition> typePetitionsFilter = typePetitionService.listTypePetition();
			List<TypePetition> typePetitions = typePetitionService.findAll();
			List<Project> proyects = projectService.listAll();
			model.addAttribute("users", userInfoService.list());
			model.addAttribute("user", new UserInfo());
			model.addAttribute("statuses", statuses);
			model.addAttribute("typePetitionsFilter", typePetitionsFilter);
			model.addAttribute("typePetitions", typePetitions);
			model.addAttribute("systems", systems);
			model.addAttribute("proyects", proyects);
		} catch (Exception e) {
			Sentry.capture(e, "request");
			e.printStackTrace();
		}
		return "/request/request";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RequestBaseR1> requests = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			Integer systemId;
			Long typePetitionId;
			// int priorityId;
			// int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}

			if (request.getParameter("typePetitionId").equals("")) {
				typePetitionId = null;
			} else {
				typePetitionId = (long) Integer.parseInt(request.getParameter("typePetitionId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			String dateRange = request.getParameter("dateRange");

			requests = requestBaseR1Service.findAllRequest(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
					statusId, dateRange, systemId, typePetitionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requests;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		JsonResponse res = new JsonResponse();
		try {
			User user = userService.getUserByUsername(getUserLogin().getUsername());
			StatusRequest status = statusService.findByKey("code", "draft");
			addRequest.setTypePetition(typePetitionService.findById(addRequest.getTypePetitionId()));
			if (status != null) {

				if (addRequest.getTypePetition().getCode().equals("RM-P1-R1")) {
					RequestBase verifyRequest = requestBaseService.findByKey("numRequest",
							addRequest.getCodeOpportunity());
					if (verifyRequest == null) {
						addRequest.setStatus(status);
						addRequest.setUser(user);
						addRequest.setRequestDate(CommonUtils.getSystemTimestamp());
						res.setStatus("success");
						addRequest.setMotive("Inicio de Solicitud");
						addRequest.setOperator(user.getFullName());
						Siges codeSiges = sigeService.findById( addRequest.getCodeSigesId());
						addRequest.setSiges(codeSiges);

						addRequest.setNumRequest(addRequest.getCodeOpportunity());
						addRequest.setCodeProyect((addRequest.getCodeOpportunity()));
						addRequest.setSystemInfo(systemService.findById(addRequest.getSystemId()));
						requestBaseService.save(addRequest);
						RequestRM_P1_R1 requestR1 = new RequestRM_P1_R1();
						requestR1.setRequestBase(addRequest);
						requestServiceRm1.save(requestR1);

						res.setData(addRequest.getId().toString());
						res.setMessage("Se creo correctamente la solicitud!");
					} else {
						res.setStatus("exception");
						res.setMessage("Error al crear el codigo con los administradores!");
					}
				} else {
					addRequest.setStatus(status);
					addRequest.setUser(user);
					addRequest.setRequestDate(CommonUtils.getSystemTimestamp());
					res.setStatus("success");
					addRequest.setMotive("Inicio de Solicitud");
					addRequest.setOperator(user.getFullName());
					Siges codeSiges = sigeService.findById( addRequest.getCodeSigesId());
					addRequest.setSiges(codeSiges);
					addRequest.setTypePetition(typePetitionService.findById(addRequest.getTypePetitionId()));
					addRequest.setNumRequest(requestBaseService.generateRequestNumber(addRequest.getCodeProyect(),
							addRequest.getTypePetition().getCode()));
					addRequest.setSystemInfo(systemService.findById(addRequest.getSystemId()));
					requestBaseService.save(addRequest);
					if (addRequest.getTypePetition().getCode().equals("RM-P1-R5")) {
						RequestRM_P1_R5 requestR5 = new RequestRM_P1_R5();
						requestR5.setRequestBase(addRequest);
						requestServiceRm5.save(requestR5);
					}
					if (addRequest.getTypePetition().getCode().equals("RM-P1-R3")) {
						RequestRM_P1_R3 requestR3 = new RequestRM_P1_R3();
						requestR3.setRequestBase(addRequest);
						requestServiceRm3.save(requestR3);
					}
					if (addRequest.getTypePetition().getCode().equals("RM-P1-R2")) {
						RequestRM_P1_R2 requestR2 = new RequestRM_P1_R2();
						requestR2.setRequestBase(addRequest);
						requestServiceRm2.save(requestR2);
					}
					res.setData(addRequest.getId().toString());
					res.setMessage("Se creo correctamente la solicitud!");
				}

			} else {

				res.setStatus("exception");
				res.setMessage("Error al crear la solicitud comunicarse con los administradores!");
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setMessage("Error al crear solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/savesys", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveSystemRequest(HttpServletRequest request, @RequestBody System addSystem) {
		JsonResponse res = new JsonResponse();
		try {
			User user = userService.getUserByUsername(getUserLogin().getUsername());
			if(!systemService.checkUniqueCode(addSystem.getCode(), addSystem.getProyectId(), 1)) {
				res.setStatus("error");
				
				res.setMessage("Error al crear sistema codigo ya utilizado para un mismo proyecto!");
			}else if(!systemService.checkUniqueCode(addSystem.getName(), addSystem.getProyectId(), 0)) {
				res.setStatus("error");
				res.setMessage("Error al crear sistema nombre ya utilizado para un mismo proyecto!");
			}else {
				res.setStatus("success");
				addSystem.setProyect(projectService.findById(addSystem.getProyectId()));
				TypePetition typePetition=typePetitionService.findByKey("code", "RM-P1-R2");
				
				User leader =new User();
				leader.setId(addSystem.getLeaderId());
				addSystem.setLeader(leader);
				addSystem.setTypePetitionId(typePetition.getId());
				addSystem.setAdditionalObservations(false);
				addSystem.changeEmail(null);
				addSystem.setIsAIA(false);
				addSystem.setIsBO(false);
				addSystem.setImportObjects(false);
				addSystem.setNomenclature(false);
				addSystem.setCustomCommands(false);
				addSystem.setImportObjects(false);
				addSystem.setInstallationInstructions(false);
				addSystem.setManagersId(getUserLogin().getId().toString());
				systemService.saveAndSiges(addSystem);
				res.setObj(addSystem);
			}
		

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("error");
			res.setMessage("Error al crear solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/sysName", method = RequestMethod.POST)
	public @ResponseBody Boolean checkSysName(HttpServletRequest request, @RequestParam("sCode") String sCode,
			@RequestParam("proyectId") Integer proyectId, @RequestParam("typeCheck") Integer typeCheck) {

		try {
			return systemService.checkUniqueCode(sCode, proyectId, typeCheck);
		} catch (Exception e) {
			Sentry.capture(e, "request");

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return false;
	}

	@RequestMapping(value = "/editRequest-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		RequestBaseR1 requestEdit = new RequestBaseR1();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findByR1(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}

			if (!requestEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Solicitud no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			model.addAttribute("request", requestEdit);
			model.addAttribute("senders", requestEdit.getSenders());
			model.addAttribute("message", requestEdit.getMessage());
			model.addAttribute("ccs", getCC(requestEdit.getTypePetition().getEmailTemplate().getCc()));
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
				List<User> usersRM = userService.getUsersRM();
				RequestRM_P1_R3 requestR3 = requestServiceRm3.requestRm3(requestEdit.getId());
				model.addAttribute("requestR3", requestR3);
				model.addAttribute("usersRM", usersRM);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR3";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				Project project = projectService.findById(requestEdit.getSystemInfo().getProyectId());
				boolean verifySos = false;
				if (project.getCode().equals("Sostenibilidad")) {
					verifySos = true;
				} else {
					verifySos = false;
				}
				model.addAttribute("typesPetition", typePetitionR4Service.listTypePetition());
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				model.addAttribute("SGRMList", ambientService.list("", "SGRM"));
				model.addAttribute("verifySos", verifySos);
				return "/request/editRequestR4";
			}

			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
				RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(requestEdit.getId());
				model.addAttribute("requestR5", requestR5);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR5";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
				RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(requestEdit.getId());
				model.addAttribute("requestR2", requestR2);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR2";
			}

			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
				RequestRM_P1_R1 requestR1 = requestServiceRm1.requestRm1(requestEdit.getId());
				model.addAttribute("requestR1", requestR1);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR1";
			}

			return "redirect:/homeRequest";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRequest";
	}

	@RequestMapping(value = { "/listUser/{id}" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet<RequestRM_P1_R4> changeProject(@PathVariable Long id, Locale locale, Model model) {
		JsonSheet<RequestRM_P1_R4> requestsRM = new JsonSheet<>();
		try {
			requestsRM.setData(requestServiceRm4.listRequestRm4(id));
		} catch (Exception e) {
			Sentry.capture(e, "requestUser");

			e.printStackTrace();
		}

		return requestsRM;
	}

	@RequestMapping(path = "/addUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveUser(HttpServletRequest request,
			@RequestBody RequestRM_P1_R4 userRequestAdd) {
		JsonResponse res = new JsonResponse();
		try {

			userRequestAdd.setAmbient(ambientService.findById(userRequestAdd.getAmbientId()));
			userRequestAdd.setType(typePetitionR4Service.findById(userRequestAdd.getTypeId()));
			userRequestAdd.setRequestBase(requestBaseService.findById(userRequestAdd.getRequestBaseId()));
			requestServiceRm4.save(userRequestAdd);
			res.setStatus("success");
			res.setMessage("Se guardo correctamente el usuario!");

		} catch (Exception e) {
			Sentry.capture(e, "usuario");
			res.setStatus("exception");
			res.setMessage("Error al guardar usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/modUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateUser(HttpServletRequest request,
			@RequestBody RequestRM_P1_R4 userRequestAdd) {
		JsonResponse res = new JsonResponse();
		try {

			userRequestAdd.setAmbient(ambientService.findById(userRequestAdd.getAmbientId()));
			userRequestAdd.setType(typePetitionR4Service.findById(userRequestAdd.getTypeId()));
			userRequestAdd.setRequestBase(requestBaseService.findById(userRequestAdd.getRequestBaseId()));
			requestServiceRm4.update(userRequestAdd);
			res.setStatus("success");
			res.setMessage("Se modifico correctamente el usuario!");

		} catch (Exception e) {
			Sentry.capture(e, "usuario");
			res.setStatus("exception");
			res.setMessage("Error al guardar usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteUserRM4(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			requestServiceRm4.delete(id);
			res.setMessage("Usuario eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "rm4");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/tiny/{id}", method = RequestMethod.GET)
	public String indexSumm(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		RequestBaseR1 requestEdit = new RequestBaseR1();
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findByR1(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}

			if (!requestEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Solicitud no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R1 requestR1 = requestServiceRm1.requestRm1(requestEdit.getId());
				model.addAttribute("requestR1", requestR1);
				return "/request/sectionsEditR1/tinySummaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(requestEdit.getId());
				model.addAttribute("requestR2", requestR2);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR2/tinySummaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R3 requestR3 = requestServiceRm3.requestRm3(requestEdit.getId());
				model.addAttribute("requestR3", requestR3);
				return "/request/sectionsEditR3/tinySummaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				model.addAttribute("request", requestEdit);
				List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(requestEdit.getId());
				model.addAttribute("listUsers", listUser);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR4/tinySummaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(requestEdit.getId());
				model.addAttribute("requestR5", requestR5);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR5/tinySummaryRequest";
			}
		} catch (Exception e) {
			Sentry.capture(e, "requestSummary");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "redirect:/";

	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/saveRequest", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestBase requestMod = requestBaseService.findById(addRequest.getId());
			addRequest.setTypePetition(requestMod.getTypePetition());
			errors = validSections(addRequest, errors);

			addRequest.setUser(requestMod.getUser());
			addRequest.setNumRequest(requestMod.getNumRequest());
			addRequest.setCodeProyect(requestMod.getCodeProyect());
			addRequest.setSiges(requestMod.getSiges());
			addRequest.setOperator(user.getFullName());
			addRequest.setStatus(requestMod.getStatus());
			addRequest.setUser(requestMod.getUser());
			addRequest.setRequestDate(requestMod.getRequestDate());
			if (addRequest.getSenders().length() < 256) {
				addRequest.setSenders(addRequest.getSenders());
			} else {
				addRequest.setSenders(requestMod.getSenders());
			}
			if (addRequest.getMessage().length() < 256) {
				addRequest.setMessage(addRequest.getMessage());
			} else {
				addRequest.setMessage(requestMod.getMessage());
			}
			addRequest.setSystemInfo(requestMod.getSystemInfo());
			requestBaseService.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRequestR5", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRequestR5(HttpServletRequest request,
			@RequestBody RequestRM_P1_R5 addRequest) {
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestRM_P1_R5 requestMod = requestServiceRm5.findById(addRequest.getId());

			errors = validSections(addRequest, errors);

			addRequest.setRequestBase(requestMod.getRequestBase());
			RequestBase requestBase = requestBaseService.findById(addRequest.getRequestBase().getId());
			if (addRequest.getSenders().length() < 256) {
				requestBase.setSenders(addRequest.getSenders());
			}
			if (addRequest.getMessage().length() < 256) {
				requestBase.setMessage(addRequest.getMessage());
			}
			requestBaseService.update(requestBase);
			requestServiceRm5.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRequestR1", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRequestR1(HttpServletRequest request,
			@RequestBody RequestRM_P1_R1 addRequest) {
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestRM_P1_R1 requestMod = requestServiceRm1.findById(addRequest.getId());

			errors = validSections(addRequest, errors);

			addRequest.setRequestBase(requestMod.getRequestBase());
			RequestBaseR1 requestBaseR1 = requestBaseService.findByR1(addRequest.getRequestBase().getId());
			if (addRequest.getSenders().length() < 256) {
				requestBaseR1.setSenders(addRequest.getSenders());
			}
			if (addRequest.getMessage().length() < 256) {
				requestBaseR1.setMessage(addRequest.getMessage());
			}
			RequestBase requestBase = new RequestBase();
			requestBase.setCodeProyect(requestBaseR1.getCodeProyect());
			requestBase.setFiles(requestBaseR1.getFiles());
			requestBase.setId(requestBaseR1.getId());
			requestBase.setTypePetition(requestBaseR1.getTypePetition());
			requestBase.setMessage(requestBaseR1.getMessage());
			requestBase.setSenders(requestBaseR1.getSenders());
			requestBase.setStatus(requestBaseR1.getStatus());
			requestBase.setSystemInfo(requestBaseR1.getSystemInfo());
			requestBase.setNumRequest(requestBaseR1.getNumRequest());
			requestBase.setMotive(requestBaseR1.getMotive());
			requestBase.setOperator(requestBaseR1.getOperator());
			requestBase.setUser(requestBaseR1.getUser());
			requestBase.setTracking(requestBaseR1.getTracking());
			requestBase.setRequestDate(requestBaseR1.getRequestDate());
			requestBaseService.update(requestBase);
			requestServiceRm1.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRequestR2", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRequestR2(HttpServletRequest request,
			@RequestBody RequestRM_P1_R2 addRequest) {
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestRM_P1_R2 requestMod = requestServiceRm2.findById(addRequest.getId());

			errors = validSections(addRequest, errors);

			addRequest.setRequestBase(requestMod.getRequestBase());
			RequestBase requestBase = requestBaseService.findById(addRequest.getRequestBase().getId());
			if (addRequest.getSenders().length() < 256) {
				requestBase.setSenders(addRequest.getSenders());
			}
			if (addRequest.getMessage().length() < 256) {
				requestBase.setMessage(addRequest.getMessage());
			}
			if (addRequest.getHierarchy().length() < 256) {
				addRequest.setHierarchy(addRequest.getHierarchy());
			} else {
				addRequest.setHierarchy(requestMod.getHierarchy());
			}
			requestBaseService.update(requestBase);
			requestServiceRm2.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRequestR3", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRequestR3(HttpServletRequest request,
			@RequestBody RequestRM_P1_R3 addRequest) {
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestRM_P1_R3 requestMod = requestServiceRm3.findById(addRequest.getId());
			addRequest.setRequestBase(requestMod.getRequestBase());
			User temp = null;
			Set<User> authsUser = new HashSet<>();
			for (Integer index : addRequest.getUsersRMId()) {
				temp = userService.findUserById(index);
				if (temp != null) {
					authsUser.add(temp);
				}
			}
			addRequest.checkUserRmExists(authsUser);
			errors = validSections(addRequest, errors);
			RequestBase requestBase = requestBaseService.findById(addRequest.getRequestBase().getId());
			if (addRequest.getSenders().length() < 256) {
				requestBase.setSenders(addRequest.getSenders());
			}
			if (addRequest.getMessage().length() < 256) {
				requestBase.setMessage(addRequest.getMessage());
			}

			requestBaseService.update(requestBase);
			requestServiceRm3.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/getR3-{id}", method = RequestMethod.GET)
	public @ResponseBody RequestRM_P1_R3 getRequestR3(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		RequestRM_P1_R3 requestRM_P1_R3 = new RequestRM_P1_R3();

		try {

			requestRM_P1_R3 = requestServiceRm3.findById(id);

			return requestRM_P1_R3;

		} catch (Exception e) {
			Sentry.capture(e, "requestR3");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return requestRM_P1_R3;
	}

	@RequestMapping(value = "/updateRequest/{id}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			RequestBaseR1 requestBase = new RequestBaseR1();
			requestBase = requestBaseService.findByR1(id);
			// Si la solicitud no existe se regresa al inicio.
			if (request == null) {
				return "redirect:/homeRequest";
			}
			// Verificar si existe un flujo para el sistema

			StatusRequest status = statusService.findByKey("name", "Solicitado");

			requestBase.setStatus(status);
			requestBase.setMotive(status.getReason());
			requestBase.setRequestDate((CommonUtils.getSystemTimestamp()));

			requestBase.setOperator(getUserLogin().getFullName());
			TypePetition typePettion = requestBase.getTypePetition();
			if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
				if (typePettion.getEmailTemplate() != null) {
					EmailTemplate email = typePettion.getEmailTemplate();
					RequestBaseR1 requestEmail = requestBase;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailRequestR4(requestEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "request");
						}

					});
					newThread.start();
				}
			}
			RequestBase requestBaseNew = new RequestBase();
			requestBaseNew.setCodeProyect(requestBase.getCodeProyect());
			requestBaseNew.setFiles(requestBase.getFiles());
			requestBaseNew.setId(requestBase.getId());
			requestBaseNew.setTypePetition(requestBase.getTypePetition());
			requestBaseNew.setMessage(requestBase.getMessage());
			requestBaseNew.setSenders(requestBase.getSenders());
			requestBaseNew.setStatus(requestBase.getStatus());
			requestBaseNew.setSystemInfo(requestBase.getSystemInfo());
			requestBaseNew.setNumRequest(requestBase.getNumRequest());
			requestBaseNew.setMotive(requestBase.getMotive());
			requestBaseNew.setOperator(requestBase.getOperator());
			requestBaseNew.setUser(requestBase.getUser());
			requestBaseNew.setTracking(requestBase.getTracking());
			requestBaseNew.setRequestDate(requestBase.getRequestDate());
			if (!requestBaseNew.getTypePetition().getCode().equals("RM-P1-R1")) {
				requestBaseNew.setSiges(requestBaseService.findById(id).getSiges());
			}
			requestBaseService.update(requestBaseNew);

			return "redirect:/request/summaryRequest-" + requestBase.getId();
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRequest";
	}

	@RequestMapping(value = "/summaryRequest-{id}", method = RequestMethod.GET)
	public String summmary(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		RequestBaseR1 requestEdit = new RequestBaseR1();
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findByR1(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}

			List<Errors_Requests> errors = errorService.findAll();
			model.addAttribute("errors", errors);
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R1 requestR1 = requestServiceRm1.requestRm1(requestEdit.getId());
				model.addAttribute("requestR1", requestR1);
				model.addAttribute("statuses", statusService.findAll());
				return "/request/sectionsEditR1/summaryRequest";
			}

			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(requestEdit.getId());
				model.addAttribute("requestR2", requestR2);
				model.addAttribute("statuses", statusService.findAll());
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR2/summaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R3 requestR3 = requestServiceRm3.requestRm3(requestEdit.getId());
				model.addAttribute("requestR3", requestR3);
				model.addAttribute("statuses", statusService.findAll());
				return "/request/sectionsEditR3/summaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				model.addAttribute("request", requestEdit);
				Project project = projectService.findById(requestEdit.getSystemInfo().getProyectId());
				boolean verifySos = false;
				if (project.getCode().equals("Sostenibilidad")) {
					verifySos = true;
				} else {
					verifySos = false;
				}

				List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(requestEdit.getId());
				model.addAttribute("listUsers", listUser);
				model.addAttribute("statuses", statusService.findAll());
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				model.addAttribute("verifySos", verifySos);
				return "/request/sectionsEditR4/summaryRequest";
			}
			if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
				model.addAttribute("request", requestEdit);
				RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(requestEdit.getId());
				model.addAttribute("requestR5", requestR5);
				model.addAttribute("statuses", statusService.findAll());
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR5/summaryRequest";
			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen de la solicitud." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/rfc/summaryRFC";
	}

	public ArrayList<MyError> validSections(RequestRM_P1_R1 request, ArrayList<MyError> errors) {

		if (request.getTimeAnswer() == "" || request.getTimeAnswer() == null) {
			errors.add(new MyError("timeAns", "Valor requerido."));
		}

		if (request.getInitialRequeriments() == "" || request.getInitialRequeriments() == null) {
			errors.add(new MyError("requeriments", "Valor requerido."));
		}
		if (request.getObservations() == "" || request.getObservations() == null) {
			errors.add(new MyError("observations", "Valor requerido."));
		}

		if (request.getSenders() != null) {
			if (request.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(request.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (request.getMessage() != null) {
			if (request.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}

		return errors;
	}

	public ArrayList<MyError> validSections(RequestBase request, ArrayList<MyError> errors) {
		if (request.getTypePetition().getCode().equals("RM-P1-R4")) {
			List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(request.getId());
			if (listUser.size() == 0) {
				errors.add(new MyError("requiredUser", "Se requiere al menos un usuario"));
			}

			if (request.getSenders() != null) {
				if (request.getSenders().length() > 256) {
					errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
				} else {
					MyError error = getErrorSenders(request.getSenders());
					if (error != null) {
						errors.add(error);
					}
				}

			}
			if (request.getMessage() != null) {
				if (request.getMessage().length() > 256) {
					errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
				}
			}
		}

		return errors;
	}

	public ArrayList<MyError> validSections(RequestRM_P1_R3 request, ArrayList<MyError> errors) {

		if (request.getUserRM().size() == 0) {
			errors.add(new MyError("userRM", "Debe seleccionar al menos a un usuario del departamento de RM"));
		}

		if (request.getConnectionMethod() == "" || request.getConnectionMethod() == null) {
			errors.add(new MyError("connectionMethod", "Valor requerido."));
		}

		if (request.getSenders() != null) {
			if (request.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(request.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (request.getMessage() != null) {
			if (request.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}

		return errors;
	}

	public ArrayList<MyError> validSections(RequestRM_P1_R2 request, ArrayList<MyError> errors) {

		if (request.getHierarchy() == "" || request.getHierarchy() == null) {
			errors.add(new MyError("hierarchy", "Valor requerido."));
		}

		if (request.getAmbient() == "" || request.getAmbient() == null) {
			errors.add(new MyError("ambient", "Se requiere seleccionar uno o varios ambientes de cambio."));
		}
		if (request.getRequeriments() == "" || request.getRequeriments() == null) {
			errors.add(new MyError("requeriments", "Valor requerido."));
		}

		if (request.getSenders() != null) {
			if (request.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(request.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}

		}
		if (request.getMessage() != null) {
			if (request.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}
		if (request.getHierarchy() != null) {
			if (request.getHierarchy().length() > 256) {
				errors.add(new MyError("hierarchy", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}

		return errors;
	}

	public ArrayList<MyError> validSections(RequestRM_P1_R5 request, ArrayList<MyError> errors) {

		if (request.getJustify() == "" || request.getJustify() == null) {
			errors.add(new MyError("justify", "Valor requerido."));
		}
		if (request.getAmbient() == "" || request.getAmbient() == null) {
			errors.add(new MyError("ambient", "Se requiere seleccionar uno o varios ambientes de cambio."));
		}
		if (request.getChangeService() == "" || request.getChangeService() == null) {
			errors.add(new MyError("change", "Valor requerido."));
		}
		if (request.getTypeChange() == "" || request.getTypeChange() == null) {
			errors.add(new MyError("type", "Se requiere seleccionar un tipo de cambio de la solicitud."));
		}

		if (request.getSenders() != null) {
			if (request.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(request.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (request.getMessage() != null) {
			if (request.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}

		return errors;
	}

	public void loadCountsRelease(HttpServletRequest request, Integer id) {
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", requestBaseR1Service.countByType(id, "Borrador", 1, null));
		userC.put("requested", requestBaseR1Service.countByType(id, "Solicitado", 1, null));
		userC.put("completed", requestBaseR1Service.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);

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