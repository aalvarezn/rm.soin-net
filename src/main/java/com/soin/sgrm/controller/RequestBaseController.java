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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingShow;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.RequestRM_P1_R2;
import com.soin.sgrm.model.RequestRM_P1_R3;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PErrors_Requests;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PRequestBase;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTracking;
import com.soin.sgrm.model.pos.PRequestBaseTrackingShow;
import com.soin.sgrm.model.pos.PRequestRM_P1_R1;
import com.soin.sgrm.model.pos.PRequestRM_P1_R2;
import com.soin.sgrm.model.pos.PRequestRM_P1_R3;
import com.soin.sgrm.model.pos.PRequestRM_P1_R4;
import com.soin.sgrm.model.pos.PRequestRM_P1_R5;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PStatusRequest;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PTypePetition;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;
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
import com.soin.sgrm.service.pos.PAmbientService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PErrorRequestService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PRequestBaseR1FastService;
import com.soin.sgrm.service.pos.PRequestBaseR1Service;
import com.soin.sgrm.service.pos.PRequestBaseService;
import com.soin.sgrm.service.pos.PRequestRM_P1_R1Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R2Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R3Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R4Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R5Service;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PStatusRequestService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypePetitionR4Service;
import com.soin.sgrm.service.pos.PTypePetitionService;
import com.soin.sgrm.service.pos.PUserInfoService;
import com.soin.sgrm.service.pos.PUserService;
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

	@Autowired
	PSystemService psystemService;

	@Autowired
	PProjectService pprojectService;

	@Autowired
	PSigesService psigeService;

	@Autowired
	PStatusRequestService pstatusService;

	@Autowired
	PRequestBaseService prequestBaseService;

	@Autowired
	PRequestBaseR1Service prequestBaseR1Service;
	
	@Autowired
	PRequestBaseR1FastService prequestBaseR1FastService;

	@Autowired
	PTypePetitionService ptypePetitionService;

	@Autowired
	PTypePetitionR4Service ptypePetitionR4Service;

	@Autowired
	PUserService puserService;

	@Autowired
	PRequestRM_P1_R1Service prequestServiceRm1;

	@Autowired
	PRequestRM_P1_R2Service prequestServiceRm2;

	@Autowired
	PRequestRM_P1_R3Service prequestServiceRm3;

	@Autowired
	PRequestRM_P1_R4Service prequestServiceRm4;

	@Autowired
	PRequestRM_P1_R5Service prequestServiceRm5;

	@Autowired
	PAmbientService pambientService;

	@Autowired
	PEmailTemplateService pemailService;

	@Autowired
	PParameterService pparameterService;

	@Autowired
	PErrorRequestService perrorService;

	@Autowired
	PUserInfoService puserInfoService;


	private final Environment environment;

	@Autowired
	public RequestBaseController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	public static final Logger logger = Logger.getLogger(RFCController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.listProjects(getUserLogin().getId());
				List<PStatusRequest> statuses = pstatusService.findAll();
				List<PTypePetition> typePetitionsFilter = ptypePetitionService.listTypePetition();
				List<PTypePetition> typePetitions = ptypePetitionService.findAll();
				List<PProject> proyects = pprojectService.listAll();
				model.addAttribute("users", puserInfoService.list());
				model.addAttribute("user", new PUserInfo());
				model.addAttribute("statuses", statuses);
				model.addAttribute("typePetitionsFilter", typePetitionsFilter);
				model.addAttribute("typePetitions", typePetitions);
				model.addAttribute("systems", systems);
				model.addAttribute("proyects", proyects);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			e.printStackTrace();
		}
		return "/request/request";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

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
			if (profileActive().equals("oracle")) {
				JsonSheet<RequestBaseR1> requests = new JsonSheet<>();
				requests = requestBaseR1Service.findAllRequest(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, systemId, typePetitionId);
				return requests;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<?> requests = new JsonSheet<>();
				requests = prequestBaseR1FastService.findAllRequest(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, systemId, typePetitionId);
				return requests;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		JsonResponse res = new JsonResponse();
		try {

			if (profileActive().equals("oracle")) {
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
							Siges codeSiges = sigeService.findById(addRequest.getCodeSigesId());
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
						Siges codeSiges = sigeService.findById(addRequest.getCodeSigesId());
						addRequest.setSiges(codeSiges);
						addRequest.setSystemInfo(systemService.findById(addRequest.getSystemId()));
						addRequest.setTypePetition(typePetitionService.findById(addRequest.getTypePetitionId()));
						addRequest.setNumRequest(requestBaseService.generateRequestNumber(addRequest.getCodeProyect(),
								addRequest.getTypePetition().getCode(), addRequest.getSystemInfo().getCode()));

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
			} else if (profileActive().equals("postgres")) {
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				PStatusRequest status = pstatusService.findByKey("code", "draft");
				PRequestBase paddRequest = new PRequestBase();
				paddRequest.setTypePetition(ptypePetitionService.findById(addRequest.getTypePetitionId()));
				paddRequest.setId(addRequest.getId());
				if (status != null) {

					if (paddRequest.getTypePetition().getCode().equals("RM-P1-R1")) {
						RequestBase verifyRequest = requestBaseService.findByKey("numRequest",
								addRequest.getCodeOpportunity());
						if (verifyRequest == null) {
							paddRequest.setStatus(status);
							paddRequest.setUser(user);
							paddRequest.setRequestDate(CommonUtils.getSystemTimestamp());
							res.setStatus("success");
							paddRequest.setMotive("Inicio de Solicitud");
							paddRequest.setOperator(user.getFullName());
							PSiges codeSiges = psigeService.findById(addRequest.getCodeSigesId());
							paddRequest.setSiges(codeSiges);

							paddRequest.setNumRequest(addRequest.getCodeOpportunity());
							paddRequest.setCodeProyect((codeSiges.getCodeSiges()));
							paddRequest.setSystemInfo(psystemService.findById(addRequest.getSystemId()));
							prequestBaseService.save(paddRequest);
							PRequestRM_P1_R1 requestR1 = new PRequestRM_P1_R1();
							requestR1.setRequestBase(paddRequest);
							prequestServiceRm1.save(requestR1);

							res.setData(addRequest.getId().toString());
							res.setMessage("Se creo correctamente la solicitud!");
						} else {
							res.setStatus("exception");
							res.setMessage("Error al crear el codigo con los administradores!");
						}
					} else {
						paddRequest.setStatus(status);
						paddRequest.setUser(user);
						paddRequest.setRequestDate(CommonUtils.getSystemTimestamp());
						res.setStatus("success");
						paddRequest.setMotive("Inicio de Solicitud");
						paddRequest.setOperator(user.getFullName());
						PSiges codeSiges = psigeService.findById(addRequest.getCodeSigesId());
						if(codeSiges==null) {
							codeSiges=psigeService.findByKey("codeSiges", addRequest.getCodeProyect());
						}
						
						paddRequest.setSiges(codeSiges);
						paddRequest.setCodeProyect(codeSiges.getCodeSiges());
						paddRequest.setSystemInfo(psystemService.findById(addRequest.getSystemId()));
						paddRequest.setTypePetition(ptypePetitionService.findById(addRequest.getTypePetitionId()));
						paddRequest.setNumRequest(prequestBaseService.generateRequestNumber(paddRequest.getCodeProyect(),
								paddRequest.getTypePetition().getCode(), paddRequest.getSystemInfo().getCode()));

						prequestBaseService.save(paddRequest);
						if (paddRequest.getTypePetition().getCode().equals("RM-P1-R5")) {
							PRequestRM_P1_R5 requestR5 = new PRequestRM_P1_R5();
							requestR5.setRequestBase(paddRequest);
							prequestServiceRm5.save(requestR5);
						}
						if (paddRequest.getTypePetition().getCode().equals("RM-P1-R3")) {
							PRequestRM_P1_R3 requestR3 = new PRequestRM_P1_R3();
							requestR3.setRequestBase(paddRequest);
							prequestServiceRm3.save(requestR3);
						}
						if (paddRequest.getTypePetition().getCode().equals("RM-P1-R2")) {
							PRequestRM_P1_R2 requestR2 = new PRequestRM_P1_R2();
							requestR2.setRequestBase(paddRequest);
							prequestServiceRm2.save(requestR2);
						}
						res.setData(paddRequest.getId().toString());
						res.setMessage("Se creo correctamente la solicitud!");
					}

				} else {

					res.setStatus("exception");
					res.setMessage("Error al crear la solicitud comunicarse con los administradores!");
				}
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
			if (profileActive().equals("oracle")) {

				Project proyect = projectService.findById(addSystem.getProyectId());
				if (!systemService.checkUniqueCode(addSystem.getCode(), addSystem.getProyectId(), 1)) {
					res.setStatus("error");

					res.setMessage("Error al crear sistema codigo ya utilizado para un mismo proyecto!");
				} else if (!systemService.checkUniqueCode(addSystem.getName(), addSystem.getProyectId(), 0)) {
					res.setStatus("error");
					res.setMessage("Error al crear sistema nombre ya utilizado para un mismo proyecto!");
				} else if (!proyect.getAllowRepeat() && !sigeService.checkUniqueCode(addSystem.getSigesCode())) {
					res.setStatus("error");
					res.setMessage(
							"Error al crear sistema,codigo proyecto ya utilizado para un mismo proyecto,este proyecto no permite codigo repetido!");
				} else {
					res.setStatus("success");
					addSystem.setProyect(projectService.findById(addSystem.getProyectId()));
					TypePetition typePetition = typePetitionService.findByKey("code", "RM-P1-R2");

					User leader = new User();
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
					addSystem.setName(addSystem.getCode());
					Set<User> managersNews = new HashSet<>();
					User manager = new User();
					manager.setId(getUserLogin().getId());
					managersNews.add(manager);

					addSystem.checkManagersExists(managersNews);
					systemService.saveAndSiges(addSystem);
					res.setObj(addSystem);
				}

			} else if (profileActive().equals("postgres")) {
				PSystem paddSystem = new PSystem();
				paddSystem.setCode(addSystem.getCode());
				paddSystem.setProyectId(addSystem.getProyectId());
				paddSystem.setName(addSystem.getName());
				paddSystem.setSigesCode(addSystem.getSigesCode());

				PProject proyect = pprojectService.findById(paddSystem.getProyectId());
				if (!psystemService.checkUniqueCode(paddSystem.getCode(), paddSystem.getProyectId(), 1)) {
					res.setStatus("error");
					res.setMessage("Error al crear sistema codigo ya utilizado para un mismo proyecto!");
				} else if (!psystemService.checkUniqueCode(paddSystem.getName(), paddSystem.getProyectId(), 0)) {
					res.setStatus("error");
					res.setMessage("Error al crear sistema nombre ya utilizado para un mismo proyecto!");
				} else if (!proyect.getAllowRepeat() && !psigeService.checkUniqueCode(paddSystem.getSigesCode())) {
					res.setStatus("error");
					res.setMessage(
							"Error al crear sistema,codigo proyecto ya utilizado para un mismo proyecto,este proyecto no permite codigo repetido!");
				} else {
					res.setStatus("success");
					paddSystem.setProyect(pprojectService.findById(addSystem.getProyectId()));
					PTypePetition typePetition = ptypePetitionService.findByKey("code", "RM-P1-R2");
					paddSystem.setSigesCode(addSystem.getSigesCode());
					paddSystem.setName(addSystem.getName());
					PUser leader = new PUser();
					leader= puserInfoService.findUserById(addSystem.getLeaderId());
			
					paddSystem.setLeader(leader);
					paddSystem.setTypePetitionId(typePetition.getId());
					paddSystem.setAdditionalObservations(false);
					paddSystem.changeEmail(null);
					paddSystem.setIsAIA(false);
					paddSystem.setIsBO(false);
					paddSystem.setImportObjects(false);
					paddSystem.setNomenclature(false);
					paddSystem.setCustomCommands(false);
					paddSystem.setImportObjects(false);
					paddSystem.setInstallationInstructions(false);
					paddSystem.setName(addSystem.getCode());
					Set<PUser> managersNews = new HashSet<>();
					PUser manager = new PUser();
					manager=puserInfoService.findUserById(getUserLogin().getId());
					managersNews.add(manager);

					paddSystem.checkManagersExists(managersNews);
					psystemService.saveAndSiges(paddSystem);
					res.setObj(paddSystem);
				}

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
			if (profileActive().equals("oracle")) {
				return systemService.checkUniqueCode(sCode, proyectId, typeCheck);
			} else if (profileActive().equals("postgres")) {
				return psystemService.checkUniqueCode(sCode, proyectId, typeCheck);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return false;
	}

	@RequestMapping(value = "/editRequest-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		try {

			if (profileActive().equals("oracle")) {
				RequestBaseR1 requestEdit = new RequestBaseR1();
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

			} else if (profileActive().equals("postgres")) {
				PRequestBaseR1 requestEdit = new PRequestBaseR1();

				if (id == null) {
					return "redirect:/";
				}

				requestEdit = prequestBaseService.findByR1(id);

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
					List<PUser> usersRM = puserService.getUsersRM();
					PRequestRM_P1_R3 requestR3 = prequestServiceRm3.requestRm3(requestEdit.getId());
					model.addAttribute("requestR3", requestR3);
					model.addAttribute("usersRM", usersRM);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/editRequestR3";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
					PProject project = pprojectService.findById(requestEdit.getSystemInfo().getProyectId());
					boolean verifySos = false;
					if (project.getCode().equals("Sostenibilidad")) {
						verifySos = true;
					} else {
						verifySos = false;
					}
					model.addAttribute("typesPetition", ptypePetitionR4Service.listTypePetition());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					model.addAttribute("SGRMList", pambientService.list("", "SGRM"));
					model.addAttribute("verifySos", verifySos);
					return "/request/editRequestR4";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
					PRequestRM_P1_R5 requestR5 = prequestServiceRm5.requestRm5(requestEdit.getId());
					model.addAttribute("requestR5", requestR5);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/editRequestR5";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
					PRequestRM_P1_R2 requestR2 = prequestServiceRm2.requestRm2(requestEdit.getId());
					model.addAttribute("requestR2", requestR2);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/editRequestR2";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
					PRequestRM_P1_R1 requestR1 = prequestServiceRm1.requestRm1(requestEdit.getId());
					model.addAttribute("requestR1", requestR1);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/editRequestR1";
				}

				return "redirect:/homeRequest";

			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRequest";
	}

	@RequestMapping(value = { "/listUser/{id}" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> changeProject(@PathVariable Long id, Locale locale, Model model) {

		try {
			if (profileActive().equals("oracle")) {
				JsonSheet<RequestRM_P1_R4> requestsRM = new JsonSheet<>();
				requestsRM.setData(requestServiceRm4.listRequestRm4(id));
				return requestsRM;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PRequestRM_P1_R4> requestsRM = new JsonSheet<>();
				requestsRM.setData(prequestServiceRm4.listRequestRm4(id));
				return requestsRM;
			}

		} catch (Exception e) {
			Sentry.capture(e, "requestUser");

			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "/addUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveUser(HttpServletRequest request,
			@RequestBody RequestRM_P1_R4 userRequestAdd) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				userRequestAdd.setAmbient(ambientService.findById(userRequestAdd.getAmbientId()));
				userRequestAdd.setType(typePetitionR4Service.findById(userRequestAdd.getTypeId()));
				userRequestAdd.setRequestBase(requestBaseService.findById(userRequestAdd.getRequestBaseId()));
				requestServiceRm4.save(userRequestAdd);
				res.setStatus("success");
				res.setMessage("Se guardo correctamente el usuario!");
			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R4 puserRequestAdd = new PRequestRM_P1_R4();
				puserRequestAdd.setEspec(userRequestAdd.getEspec());
				puserRequestAdd.setName(userRequestAdd.getName());
				puserRequestAdd.setPermissions(userRequestAdd.getPermissions());
				puserRequestAdd.setEmail(userRequestAdd.getEmail());
				puserRequestAdd.setUserGit(userRequestAdd.getUserGit());
				puserRequestAdd.setAmbient(pambientService.findById(userRequestAdd.getAmbientId()));
				puserRequestAdd.setType(ptypePetitionR4Service.findById(userRequestAdd.getTypeId()));
				puserRequestAdd.setRequestBase(prequestBaseService.findById(userRequestAdd.getRequestBaseId()));
				prequestServiceRm4.save(puserRequestAdd);
				res.setStatus("success");
				res.setMessage("Se guardo correctamente el usuario!");
			}

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
			if (profileActive().equals("oracle")) {
				userRequestAdd.setAmbient(ambientService.findById(userRequestAdd.getAmbientId()));
				userRequestAdd.setType(typePetitionR4Service.findById(userRequestAdd.getTypeId()));
				userRequestAdd.setRequestBase(requestBaseService.findById(userRequestAdd.getRequestBaseId()));
				requestServiceRm4.update(userRequestAdd);
			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R4 puserRequestAdd = new PRequestRM_P1_R4();
				puserRequestAdd.setId(userRequestAdd.getId());
				puserRequestAdd.setEspec(userRequestAdd.getEspec());
				puserRequestAdd.setName(userRequestAdd.getName());
				puserRequestAdd.setPermissions(userRequestAdd.getPermissions());
				puserRequestAdd.setEmail(userRequestAdd.getEmail());
				puserRequestAdd.setUserGit(userRequestAdd.getUserGit());
				puserRequestAdd.setAmbient(pambientService.findById(userRequestAdd.getAmbientId()));
				puserRequestAdd.setType(ptypePetitionR4Service.findById(userRequestAdd.getTypeId()));
				puserRequestAdd.setRequestBase(prequestBaseService.findById(userRequestAdd.getRequestBaseId()));
				prequestServiceRm4.update(puserRequestAdd);
			}

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
			if (profileActive().equals("oracle")) {
				requestServiceRm4.delete(id);
			} else if (profileActive().equals("postgres")) {
				prequestServiceRm4.delete(id);
			}

			res.setStatus("success");
			res.setMessage("Usuario eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "rm4");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteRequest/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRequest(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				RequestBase requestBase = requestBaseService.findById(id);
				if (requestBase.getStatus().getName().equals("Borrador")) {

					StatusRequest status = statusService.findByKey("name", "Anulado");
					requestBase.setStatus(status);
					requestBase.setMotive(status.getReason());
					requestBase.setOperator(getUserLogin().getFullName());
					requestBaseService.update(requestBase);

				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, la solicitud no esta en estado de Borrador.");
				}
			} else if (profileActive().equals("postgres")) {
				PRequestBase requestBase = prequestBaseService.findById(id);
				if (requestBase.getStatus().getName().equals("Borrador")) {
					PStatusRequest status = pstatusService.findByKey("name", "Anulado");
					requestBase.setStatus(status);
					requestBase.setMotive(status.getReason());
					requestBase.setOperator(getUserLogin().getFullName());
					prequestBaseService.update(requestBase);

				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, la solicitud no esta en estado de Borrador.");
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


	@RequestMapping(value = "/tiny/{id}", method = RequestMethod.GET)
	public String indexSumm(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		try {

			if (profileActive().equals("oracle")) {
				RequestBaseR1 requestEdit = new RequestBaseR1();
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
			} else if (profileActive().equals("postgres")) {
				PRequestBaseR1 requestEdit = new PRequestBaseR1();
				if (id == null) {
					return "redirect:/";
				}

				requestEdit = prequestBaseService.findByR1(id);

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
					PRequestRM_P1_R1 requestR1 = prequestServiceRm1.requestRm1(requestEdit.getId());
					model.addAttribute("requestR1", requestR1);
					return "/request/sectionsEditR1/tinySummaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R2 requestR2 = prequestServiceRm2.requestRm2(requestEdit.getId());
					model.addAttribute("requestR2", requestR2);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/sectionsEditR2/tinySummaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R3 requestR3 = prequestServiceRm3.requestRm3(requestEdit.getId());
					model.addAttribute("requestR3", requestR3);
					return "/request/sectionsEditR3/tinySummaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
					model.addAttribute("request", requestEdit);
					List<PRequestRM_P1_R4> listUser = prequestServiceRm4.listRequestRm4(requestEdit.getId());
					model.addAttribute("listUsers", listUser);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/sectionsEditR4/tinySummaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R5 requestR5 = prequestServiceRm5.requestRm5(requestEdit.getId());
					model.addAttribute("requestR5", requestR5);
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/sectionsEditR5/tinySummaryRequest";
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "requestSummary");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "redirect:/";

	}

	@RequestMapping(value = "/saveRequest", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		JsonResponse res = new JsonResponse();
		try {

			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				ArrayList<MyError> errors = new ArrayList<MyError>();
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
			} else if (profileActive().equals("postgres")) {
				PRequestBase paddRequest = new PRequestBase();
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				ArrayList<MyError> errors = new ArrayList<MyError>();
				PRequestBase requestMod = prequestBaseService.findById(addRequest.getId());
				paddRequest.setTypePetition(requestMod.getTypePetition());
				TypePetition typePetition=new TypePetition();
				typePetition.setCode(paddRequest.getTypePetition().getCode());
				addRequest.setTypePetition(typePetition);
				errors = validSections(addRequest, errors);
				paddRequest.setId(addRequest.getId());
				paddRequest.setCodeOpportunity(requestMod.getCodeOpportunity());
				paddRequest.setUser(requestMod.getUser());
				paddRequest.setNumRequest(requestMod.getNumRequest());
				paddRequest.setCodeProyect(requestMod.getCodeProyect());
				paddRequest.setSiges(requestMod.getSiges());
				paddRequest.setOperator(user.getFullName());
				paddRequest.setStatus(requestMod.getStatus());
				paddRequest.setUser(requestMod.getUser());
				paddRequest.setRequestDate(requestMod.getRequestDate());
				if (addRequest.getSenders().length() < 256) {
					paddRequest.setSenders(addRequest.getSenders());
				} else {
					paddRequest.setSenders(requestMod.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					paddRequest.setMessage(addRequest.getMessage());
				} else {
					paddRequest.setMessage(requestMod.getMessage());
				}
				paddRequest.setSystemInfo(requestMod.getSystemInfo());
				prequestBaseService.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
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
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R5 paddRequest=new PRequestRM_P1_R5();
				PRequestRM_P1_R5 requestMod = prequestServiceRm5.findById(addRequest.getId());

				errors = validSections(addRequest, errors);
				paddRequest.setId(addRequest.getId());
				paddRequest.setRequestBase(requestMod.getRequestBase());
				PRequestBase requestBase = prequestBaseService.findById(paddRequest.getRequestBase().getId());
				if (addRequest.getSenders().length() < 256) {
					paddRequest.setSenders(addRequest.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					paddRequest.setMessage(addRequest.getMessage());
				}
				
				paddRequest.setChangeService(addRequest.getChangeService());
				paddRequest.setJustify(addRequest.getJustify());
				paddRequest.setAmbient(addRequest.getAmbient());
				paddRequest.setTypeChange(addRequest.getTypeChange());
				prequestBaseService.update(requestBase);
				prequestServiceRm5.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
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

		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			
			if (profileActive().equals("oracle")) {
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

			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R1 requestMod = prequestServiceRm1.findById(addRequest.getId());
				PRequestRM_P1_R1 paddRequest=new PRequestRM_P1_R1();
				errors = validSections(addRequest, errors);
				paddRequest.setId(addRequest.getId());
				paddRequest.setTimeAnswer(addRequest.getTimeAnswer());
				paddRequest.setInitialRequeriments(addRequest.getInitialRequeriments());
				paddRequest.setObservations(addRequest.getObservations());
				paddRequest.setSenders(addRequest.getSenders());
				paddRequest.setMessage(addRequest.getMessage());
				paddRequest.setRequestBase(requestMod.getRequestBase());
				PRequestBaseR1 requestBaseR1 = prequestBaseService.findByR1(paddRequest.getRequestBase().getId());
				if (addRequest.getSenders().length() < 256) {
					requestBaseR1.setSenders(addRequest.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					requestBaseR1.setMessage(addRequest.getMessage());
				}
				PRequestBase prequestBase = new PRequestBase();
				prequestBase.setCodeProyect(requestBaseR1.getCodeProyect());
				prequestBase.setFiles(requestBaseR1.getFiles());
				prequestBase.setId(requestBaseR1.getId());
				prequestBase.setTypePetition(requestBaseR1.getTypePetition());
				prequestBase.setMessage(requestBaseR1.getMessage());
				prequestBase.setSenders(requestBaseR1.getSenders());
				prequestBase.setStatus(requestBaseR1.getStatus());
				prequestBase.setSystemInfo(requestBaseR1.getSystemInfo());
				prequestBase.setNumRequest(requestBaseR1.getNumRequest());
				prequestBase.setMotive(requestBaseR1.getMotive());
				prequestBase.setOperator(requestBaseR1.getOperator());
				prequestBase.setUser(requestBaseR1.getUser());
				prequestBase.setTracking(requestBaseR1.getTracking());
				prequestBase.setRequestDate(requestBaseR1.getRequestDate());
				prequestBaseService.update(prequestBase);
				prequestServiceRm1.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}

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
		
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R2 requestMod = prequestServiceRm2.findById(addRequest.getId());
				PRequestRM_P1_R2 paddRequest=new PRequestRM_P1_R2();
				paddRequest.setId(addRequest.getId());
				errors = validSections(addRequest, errors);

				paddRequest.setRequestBase(requestMod.getRequestBase());
				PRequestBase requestBase = prequestBaseService.findById(paddRequest.getRequestBase().getId());
				if (addRequest.getSenders().length() < 256) {
					requestBase.setSenders(addRequest.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					requestBase.setMessage(addRequest.getMessage());
				}
				if (addRequest.getHierarchy().length() < 256) {
					paddRequest.setHierarchy(addRequest.getHierarchy());
				} else {
					paddRequest.setHierarchy(requestMod.getHierarchy());
				}
				paddRequest.setRequeriments(addRequest.getRequeriments());
				paddRequest.setAmbient(addRequest.getAmbient());
				paddRequest.setTypeService(addRequest.getTypeService());
				prequestBaseService.update(requestBase);
				prequestServiceRm2.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
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

		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			if (profileActive().equals("oracle")) {
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

			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R3 requestMod = prequestServiceRm3.findById(addRequest.getId());
				PRequestRM_P1_R3 paddRequest = new PRequestRM_P1_R3();
				paddRequest.setId(addRequest.getId());
				paddRequest.setRequestBase(requestMod.getRequestBase());
				PUser temp = null;
				Set<PUser> authsUser = new HashSet<>();
				for (Integer index : addRequest.getUsersRMId()) {
					temp = puserService.findUserById(index);
					if (temp != null) {
						authsUser.add(temp);
					}
				}
				paddRequest.checkUserRmExists(authsUser);
				errors = validSections(addRequest, errors);
				PRequestBase requestBase = prequestBaseService.findById(paddRequest.getRequestBase().getId());
				if (addRequest.getSenders().length() < 256) {
					requestBase.setSenders(addRequest.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					requestBase.setMessage(addRequest.getMessage());
				}
				paddRequest.setConnectionMethod(addRequest.getConnectionMethod());
				prequestBaseService.update(requestBase);
				prequestServiceRm3.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}

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
	public @ResponseBody Object getRequestR3(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
	

		try {
			
			if (profileActive().equals("oracle")) {
				RequestRM_P1_R3 requestRM_P1_R3 = new RequestRM_P1_R3();
				requestRM_P1_R3 = requestServiceRm3.findById(id);
				return requestRM_P1_R3;
			} else if (profileActive().equals("postgres")) {
				PRequestRM_P1_R3 requestRM_P1_R3 = new PRequestRM_P1_R3();
				requestRM_P1_R3 = prequestServiceRm3.findById(id);
				return requestRM_P1_R3;
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "requestR3");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return null;
	}

	@RequestMapping(value = "/updateRequest/{id}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PRequestBaseR1 requestBase = new PRequestBaseR1();
				requestBase = prequestBaseService.findByR1(id);
				// Si la solicitud no existe se regresa al inicio.
				if (request == null) {
					return "redirect:/homeRequest";
				}
				// Verificar si existe un flujo para el sistema

				PStatusRequest status = pstatusService.findByKey("name", "Solicitado");

				requestBase.setStatus(status);
				requestBase.setMotive(status.getReason());
				requestBase.setRequestDate((CommonUtils.getSystemTimestamp()));

				requestBase.setOperator(getUserLogin().getFullName());
				PTypePetition typePettion = requestBase.getTypePetition();
				if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
					if (typePettion.getEmailTemplate() != null) {
						PEmailTemplate email = typePettion.getEmailTemplate();
						PRequestBaseR1 requestEmail = requestBase;
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailRequestR4(requestEmail, email);
							} catch (Exception e) {
								Sentry.capture(e, "request");
							}

						});
						newThread.start();
					}
				}
				PRequestBase requestBaseNew = new PRequestBase();
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
					requestBaseNew.setSiges(prequestBaseService.findById(id).getSiges());
				}
				prequestBaseService.update(requestBaseNew);

				return "redirect:/request/summaryRequest-" + requestBase.getId();
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRequest";
	}

	@RequestMapping(value = "/summaryRequest-{id}", method = RequestMethod.GET)
	public String summmary(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

	
		try {
			
			if (profileActive().equals("oracle")) {
				RequestBaseR1 requestEdit = new RequestBaseR1();
				if (id == null) {
					return "redirect:/";
				}

				requestEdit = requestBaseService.findByR1(id);

				if (requestEdit == null) {
					return "/plantilla/404";
				}

				List<Errors_Requests> errors = errorService.findAll();
				model.addAttribute("errors", errors);
				String ccs=CommonUtils.combinedEmails(requestEdit.getTypePetition().getEmailTemplate().getCc(),requestEdit.getSenders());
				model.addAttribute("ccs",ccs );
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
			} else if (profileActive().equals("postgres")) {
				PRequestBaseR1 requestEdit = new PRequestBaseR1();
				if (id == null) {
					return "redirect:/";
				}

				requestEdit = prequestBaseService.findByR1(id);

				if (requestEdit == null) {
					return "/plantilla/404";
				}

				List<PErrors_Requests> errors = perrorService.findAll();
				model.addAttribute("errors", errors);
				String ccs=CommonUtils.combinedEmails(requestEdit.getTypePetition().getEmailTemplate().getCc(),requestEdit.getSenders());
				model.addAttribute("ccs",ccs );
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R1 requestR1 = prequestServiceRm1.requestRm1(requestEdit.getId());
					model.addAttribute("requestR1", requestR1);
					model.addAttribute("statuses", pstatusService.findAll());
					return "/request/sectionsEditR1/summaryRequest";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R2 requestR2 = prequestServiceRm2.requestRm2(requestEdit.getId());
					model.addAttribute("requestR2", requestR2);
					model.addAttribute("statuses", pstatusService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/sectionsEditR2/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R3 requestR3 = prequestServiceRm3.requestRm3(requestEdit.getId());
					model.addAttribute("requestR3", requestR3);
					model.addAttribute("statuses", pstatusService.findAll());
					return "/request/sectionsEditR3/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
					model.addAttribute("request", requestEdit);
					PProject project = pprojectService.findById(requestEdit.getSystemInfo().getProyectId());
					boolean verifySos = false;
					if (project.getCode().equals("Sostenibilidad")) {
						verifySos = true;
					} else {
						verifySos = false;
					}

					List<PRequestRM_P1_R4> listUser = prequestServiceRm4.listRequestRm4(requestEdit.getId());
					model.addAttribute("listUsers", listUser);
					model.addAttribute("statuses", pstatusService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					model.addAttribute("verifySos", verifySos);
					return "/request/sectionsEditR4/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R5 requestR5 = prequestServiceRm5.requestRm5(requestEdit.getId());
					model.addAttribute("requestR5", requestR5);
					model.addAttribute("statuses", pstatusService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/request/sectionsEditR5/summaryRequest";
				}
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
			if (profileActive().equals("oracle")) {
				List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(request.getId());
				if (listUser.size() == 0) {
					errors.add(new MyError("requiredUser", "Se requiere al menos un usuario"));
				}
			} else if (profileActive().equals("postgres")) {
				List<PRequestRM_P1_R4> listUser = prequestServiceRm4.listRequestRm4(request.getId());
				if (listUser.size() == 0) {
					errors.add(new MyError("requiredUser", "Se requiere al menos un usuario"));
				}
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
		if (profileActive().equals("oracle")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			userC.put("draft", requestBaseR1Service.countByType(id, "Borrador", 1, null));
			userC.put("requested", requestBaseR1Service.countByType(id, "Solicitado", 1, null));
			userC.put("completed", requestBaseR1Service.countByType(id, "Completado", 1, null));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			userC.put("draft", prequestBaseR1Service.countByType(id, "Borrador", 1, null));
			userC.put("requested", prequestBaseR1Service.countByType(id, "Solicitado", 1, null));
			userC.put("completed", prequestBaseR1Service.countByType(id, "Completado", 1, null));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		}
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
	
	@RequestMapping(value = "/tracking/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tracking(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				RequestBaseTrackingShow tracking = requestBaseR1Service.findRequestTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			} else if (profileActive().equals("postgres")) {
				PRequestBaseTrackingShow tracking = prequestBaseR1Service.findRequestTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	/*
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idSystem", required = true) Long idSystem,
			@RequestParam(value = "fullName", required = true) Long fullName,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "userGit", required = false) Long userGit) {
		JsonResponse res = new JsonResponse();
		try {

			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				ArrayList<MyError> errors = new ArrayList<MyError>();
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
			} else if (profileActive().equals("postgres")) {
				
				PRequestBase paddRequest = new PRequestBase();
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				ArrayList<MyError> errors = new ArrayList<MyError>();
				PRequestBase requestMod = prequestBaseService.findById(addRequest.getId());
				paddRequest.setTypePetition(requestMod.getTypePetition());
				TypePetition typePetition=new TypePetition();
				typePetition.setCode(paddRequest.getTypePetition().getCode());
				addRequest.setTypePetition(typePetition);
				errors = validSections(addRequest, errors);
				paddRequest.setId(addRequest.getId());
				paddRequest.setCodeOpportunity(requestMod.getCodeOpportunity());
				paddRequest.setUser(requestMod.getUser());
				paddRequest.setNumRequest(requestMod.getNumRequest());
				paddRequest.setCodeProyect(requestMod.getCodeProyect());
				paddRequest.setSiges(requestMod.getSiges());
				paddRequest.setOperator(user.getFullName());
				paddRequest.setStatus(requestMod.getStatus());
				paddRequest.setUser(requestMod.getUser());
				paddRequest.setRequestDate(requestMod.getRequestDate());
				if (addRequest.getSenders().length() < 256) {
					paddRequest.setSenders(addRequest.getSenders());
				} else {
					paddRequest.setSenders(requestMod.getSenders());
				}
				if (addRequest.getMessage().length() < 256) {
					paddRequest.setMessage(addRequest.getMessage());
				} else {
					paddRequest.setMessage(requestMod.getMessage());
				}
				paddRequest.setSystemInfo(requestMod.getSystemInfo());
				prequestBaseService.update(paddRequest);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	*/
}