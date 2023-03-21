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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseEditWithOutObjects;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseSummaryMin;
import com.soin.sgrm.model.ReleaseTinySummary;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ModifiedComponentService;
import com.soin.sgrm.service.ModuleService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ReleaseObjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.EnvironmentService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.ReleaseCreate;

import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/release")
public class ReleaseController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReleaseController.class);

	@Autowired
	private UserInfoService loginService;
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	@Autowired
	private ImpactService impactService;
	@Autowired
	private PriorityService priorityService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private RiskService riskService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private AmbientService ambientService;
	@Autowired
	private ModifiedComponentService modifiedComponentService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private DocTemplateService docsTemplateService;
	@Autowired
	private TypeObjectService typeObjectService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private ConfigurationItemService configurationItemService;
	@Autowired
	private ActionEnvironmentService actionService;
	@Autowired
	private TypeDetailService typeDetail;
	@Autowired
	private EmailTemplateService emailService;
	@Autowired
	private ParameterService paramService;
	@Autowired
	private NodeService nodeService;
	@Autowired 
	ReleaseObjectService releaseObjectService;
	@Autowired
	private ErrorReleaseService errorService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			loadCountsRelease(request, name);
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemByUser(getUserLogin().getUsername()));
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("list", "userRelease");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/release/release";

	}
	@RequestMapping(value = "/qa", method = RequestMethod.GET)
	public String releaseQA(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			loadCountsRelease(request, name);
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("list", "userRelease");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/release/releaseQA";

	}

	@RequestMapping(path = "/userRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getUserRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;

			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
					statusId);
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			logger.log(MyLevel.RELEASE_ERROR, ex.toString());
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;

	}

	@RequestMapping(path = "/teamRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getAllEmployees(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
					systemService.myTeams(name), dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
					dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/systemRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
					dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	@RequestMapping(path = "/systemReleaseQA", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemReleaseQA(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByAllSystemQA(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
					dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(value = "/summary-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			ReleaseSummaryMin release = null;
			if (CommonUtils.isNumeric(status)) {
				release = releaseService.findByIdMin(Integer.parseInt(status));
			}

			if (release == null) {
				return "redirect:/";
			}
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());
			List<DocTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			model.addAttribute("dependency", new Release());
			model.addAttribute("doc", new DocTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("systemConfiguration", systemConfiguration);
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("errors", errorService.findAll());
			
			model.addAttribute("cc", release.getSystem().getEmailTemplate().iterator().next().getCc());
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			throw ex;
		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/release/summaryRelease";
	}

	@RequestMapping(value = "/summaryQA-{status}", method = RequestMethod.GET)
	public String summmaryQA(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			ReleaseSummaryMin release = null;
			if (CommonUtils.isNumeric(status)) {
				release = releaseService.findByIdMin(Integer.parseInt(status));
			}

			if (release == null) {
				return "redirect:/";
			}
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());
			List<DocTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			model.addAttribute("dependency", new Release());
			model.addAttribute("doc", new DocTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("systemConfiguration", systemConfiguration);
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			throw ex;
		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/release/summaryReleaseQA";
	}
	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		try {
			model.addAttribute("parameter", status);
			ReleaseTinySummary release = null;
			if (CommonUtils.isNumeric(status)) {
				release = releaseService.findByIdTiny(Integer.parseInt(status));
			}

			if (release == null) {
				return "redirect:/";
			}
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());
			List<DocTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			model.addAttribute("dependency", new Release());
			model.addAttribute("object", new ReleaseObject());
			model.addAttribute("doc", new DocTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("systemConfiguration", systemConfiguration);

		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/release/tinySummaryRelease";
	}

	@RequestMapping(value = "/editRelease-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable String id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		ReleaseEditWithOutObjects release = new ReleaseEditWithOutObjects();
		SystemConfiguration systemConfiguration = new SystemConfiguration();
		UserLogin user = getUserLogin();
		try {
			if (id == null) {
				return "redirect:/";
			}

			Integer idRelease = Integer.parseInt(id);
			release = releaseService.findEditByIdWithOutObjects(idRelease);

			if (release == null) {
				return "/plantilla/404";
			}

			if (!release.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Release no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			if (!(release.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el release.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			systemConfiguration = systemConfigurationService.findBySystemId(release.getSystem().getId());
			List<DocTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());

			if (release.getSystem().getImportObjects()) {
				model.addAttribute("typeDetailList", typeDetail.list());
			}
			
			List<Release_Objects> listObjects=	releaseObjectService.listObjectsSql(idRelease); 
			
			model.addAttribute("systems", systemService.listSystemByUser(getUserLogin().getUsername()));
			model.addAttribute("impacts", impactService.list());
			model.addAttribute("risks", riskService.list());
			model.addAttribute("priorities", priorityService.list());
			model.addAttribute("environments", environmentService.listBySystem(release.getSystem().getId()));
			model.addAttribute("systemConfiguration", systemConfiguration);
			model.addAttribute("ambients", ambientService.list(release.getSystem().getId()));
			model.addAttribute("typeObjects", typeObjectService.listBySystem(release.getSystem().getId()));
			model.addAttribute("actionEnvironments", actionService.listBySystem(release.getSystem().getId()));
			model.addAttribute("configurationItems",
					configurationItemService.listBySystem(release.getSystem().getId()));
			model.addAttribute("doc", new DocTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("senders", release.getSenders());
			model.addAttribute("message", release.getMessage());
			model.addAttribute("releaseObject",listObjects);
			if(release.getSystem().getEmailTemplate()!=null) {
				if(release.getSystem().getEmailTemplate().size()>1) {
					model.addAttribute("ccs", getCC(release.getSystem().getEmailTemplate().iterator().next().getCc()));
				}else {
					for(EmailTemplate emailTemplate:release.getSystem().getEmailTemplate()) {
						model.addAttribute("ccs", getCC(emailTemplate.getCc()));
					}
				}
			
			}else {
				model.addAttribute("ccs", "");
			}
			return "/release/editRelease";

		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/release-copy", method = RequestMethod.POST)
	public @ResponseBody JsonResponse copyRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) String idRelease,
			@RequestParam(value = "description", required = true) String description,
			@RequestParam(value = "observations", required = true) String observations,
			@RequestParam(value = "requeriment", required = true) String requeriment,
			@RequestParam(value = "requirement_name", required = true) String requirement_name,
			@RequestParam(value = "addObject", required = true) Boolean addObject) {
		JsonResponse res = new JsonResponse();
		try {
			UserInfo user = loginService.findUserInfoById(getUserLogin().getId());
			String number_release = "";
			ReleaseEdit release = releaseService.findEditById(Integer.parseInt(idRelease));

			ReleaseEdit releaseCopy = (ReleaseEdit) release.clone();
			if (!requeriment.equals("TPO/BT")) {
				number_release = releaseService.generateReleaseNumber(requeriment, requirement_name,
						releaseCopy.getSystem().getName());
			} else {
				number_release = releaseService.generateTPO_BT_ReleaseNumber((releaseCopy.getSystem().getName()),
						requirement_name);
			}

			releaseCopy.setId(0);
			releaseCopy.setDescription(description);
			releaseCopy.setObservations(observations);
			releaseCopy.setReleaseNumber(number_release);
			releaseCopy.clearObjectsCopy();
			if (addObject) {
				releaseCopy.copyObjects(releaseCopy, release);
			}
			releaseCopy.setCreateDate(CommonUtils.getSystemTimestamp());
			releaseCopy.setUser(user);
			releaseCopy.setStatus(statusService.findByName("Borrador"));

			if (requeriment.equals("IN"))
				releaseCopy.setIncidents(requirement_name);

			if (requeriment.equals("PR"))
				releaseCopy.setProblems(requirement_name);

			if (requeriment.equals("SS"))
				releaseCopy.setServiceRequests(requirement_name);

			if (requeriment.equals("SO-ICE"))
				releaseCopy.setOperativeSupport(requirement_name);

			if (!requeriment.equals("TPO/BT")) {
				releaseService.copy(releaseCopy, "-1");
			} else {
				releaseService.copy(releaseCopy, requirement_name);
			}
			res.setData(releaseCopy.getId() + "");
			res.setStatus("success");
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/release-generate", method = RequestMethod.POST)
	public @ResponseBody JsonResponse createRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "system_id", required = true) String system_id,
			@RequestParam(value = "description", required = true) String description,
			@RequestParam(value = "observations", required = true) String observations,
			@RequestParam(value = "requeriment", required = true) String requeriment,
			@RequestParam(value = "requirement_name", required = true) String requirement_name) {
		// Se genera la estructura base del release para su posterior creacion completa.
		JsonResponse res = new JsonResponse();
		String number_release = "";
		Release release = new Release();
		Module module = new Module();
		User user = loginService.findUserById(getUserLogin().getId());
		try {
			res.setStatus("success");
			if (!requeriment.equals("TPO/BT")) {
				number_release = releaseService.generateReleaseNumber(requeriment, requirement_name, system_id);
			} else {
				number_release = releaseService.generateTPO_BT_ReleaseNumber(system_id, requirement_name);
			}
			module = moduleService.findBySystemId(system_id);
			if (module == null) {
				res.setStatus("fail");
				res.setException("El módulo del sistema no se encuentra configurado");
				return res;
			}
			release.setSystem(module.getSystem());
			release.setDescription(description);
			release.setObservations(observations);
			release.setReleaseNumber(number_release);
			release.setUser(user);
			Status status = statusService.findByName("Borrador");
			release.setStatus(status);
			release.setModule(module);
			release.setCreateDate(CommonUtils.getSystemTimestamp());

			release.setReportHaveArt(false);
			release.setReportfixedTelephony(false);
			release.setReportHistoryTables(false);
			release.setReportNotHaveArt(false);
			release.setReportMobileTelephony(false);
			release.setReportTemporaryTables(false);

			release.setBilledCalls(false);
			release.setNotBilledCalls(false);

			release.setMotive("Inicio de release");
			release.setOperator(getUserLogin().getFullName());

			if (requeriment.equals("IN"))
				release.setIncident((!requirement_name.substring(0, 2).toString().toUpperCase().equals("IN"))
						? "IN" + requirement_name
						: requirement_name);

			if (requeriment.equals("PR"))
				release.setProblem((!requirement_name.substring(0, 2).toString().toUpperCase().equals("PR"))
						? "PR" + requirement_name
						: requirement_name);

			if (requeriment.equals("SS"))
				release.setService_requests((!requirement_name.substring(0, 2).toString().toUpperCase().equals("SS"))
						? "SS" + requirement_name
						: requirement_name);

			if (requeriment.equals("SO-ICE"))
				release.setOperative_support("SO-ICE" + requirement_name);

			if (!requeriment.equals("TPO/BT")) {
				releaseService.save(release, "-1");
			} else {
				releaseService.save(release, requirement_name);
			}
			res.setData(release.getId() + "");
			return res;
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteRelease/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRelease(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			ReleaseEdit release = releaseService.findEditById(id);
			if (release.getStatus().getName().equals("Borrador")) {
				if (release.getUser().getUsername().equals(getUserLogin().getUsername())) {
					Status status = statusService.findByName("Anulado");
					release.setStatus(status);
					release.setMotive(status.getMotive());
					release.setOperator(getUserLogin().getFullName());
					releaseService.updateStatusRelease(release);
				} else {
					res.setStatus("fail");
					res.setException("No tiene permisos sobre el release.");
				}
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
			}
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/assignRelease", method = RequestMethod.POST)
	public @ResponseBody JsonResponse assignRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) String idRelease,
			@RequestParam(value = "idUser", required = true) String idUser) {
		JsonResponse res = new JsonResponse();
		try {
			// Validar que sea lider del sistema al que pertenece el release

			ReleaseEdit release = releaseService.findEditById(Integer.parseInt(idRelease));
			UserInfo user = loginService.findUserInfoById(Integer.parseInt(idUser));

			if (release == null || user == null) {
				res.setStatus("fail");
				res.setException("El Release o Usuario indicado no existen.");
				return res;
			}
			releaseService.assignRelease(release, user);
			res.setStatus("success");
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRelease", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request,
			@ModelAttribute("ReleaseCreate") ReleaseCreate rc, ModelMap model, Locale locale, HttpSession session) {

		JsonResponse res = new JsonResponse();
		List<Ambient> ambients = new ArrayList<Ambient>();
		List<ModifiedComponent> modifiedComponents = new ArrayList<ModifiedComponent>();
		Set<Dependency> dependencies = new HashSet<Dependency>();
		Dependency dependency = null;
		ArrayList<MyError> errors = new ArrayList<MyError>(); // contiene los errores del release.
		// Validacion del Release con la configuracion por secciones del sistema.
		try {

			Release release = releaseService.findReleaseById(Integer.parseInt(rc.getRelease_id()));
			errors = validSections(release, errors, rc);
			// Si no tiene errores se guarda

			for (Integer idAmbient : rc.getAmbient()) {
				ambients.add(ambientService.findById(idAmbient, rc.getSystemCode()));
			}

			for (Integer idComponent : rc.getModifiedComponent()) {
				modifiedComponents.add(modifiedComponentService.findById(idComponent));
			}

			ReleaseUser releaseUser = releaseService.findReleaseUserById(Integer.parseInt(rc.getRelease_id()));
			for (Integer to_id : rc.getDependenciesFunctionals()) {
				dependency = new Dependency();
				dependency.setId(0);
				dependency.setRelease(releaseUser);
				dependency.setTo_release(releaseService.findReleaseUserById(to_id));
				dependency.setMandatory(release.isMandatory(dependency));
				dependency.setIsFunctional(true);
				dependencies.add(dependency);
			}
			for (Integer to_id : rc.getDependenciesTechnical()) {
				dependency = new Dependency();
				dependency.setId(0);
				dependency.setRelease(releaseUser);
				dependency.setTo_release(releaseService.findReleaseUserById(to_id));
				dependency.setMandatory(release.isMandatory(dependency));
				dependency.setIsFunctional(false);
				dependencies.add(dependency);
			}

			release.checkModifiedComponents(modifiedComponents);
			release.checkAmbientsExists(ambients);
			release.checkDependenciesExists(dependencies);
			if(rc.getSenders()!=null) {
			if (rc.getSenders().length() < 256) {
				rc.setSenders(rc.getSenders());
			} else {
				rc.setSenders(release.getSenders());
			}
			}
			
			if(rc.getMessage()!=null) {
			if (rc.getMessage().length() < 256) {
				rc.setMessage(rc.getMessage());
			} else {
				rc.setMessage(release.getMessage());
			}
			}
			releaseService.saveRelease(release, rc);
			
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateRelease/{releaseId}", method = RequestMethod.GET)
	public String updateRelease(@PathVariable String releaseId, HttpServletRequest request, Locale locale,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Release release = null;

			if (CommonUtils.isNumeric(releaseId)) {
				release = releaseService.findReleaseById(Integer.parseInt(releaseId));
			}
			// Si el release no existe se regresa al inicio.
			if (release == null) {
				return "redirect:/";
			}
			// Verificar si existe un flujo para el sistema
			Node node = nodeService.existWorkFlow(release);
			Status status = statusService.findByName("Solicitado");

			release.setStatus(status);
			release.setMotive(status.getMotive());
			release.setOperator(getUserLogin().getFullName());
      
			if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
				if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
					EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
					Release releaseEmail = release;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMail(releaseEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "release");
						}

					});
					newThread.start();
				}
			}
			
			if (node != null) {
			release.setNode(node);

			// si tiene un nodo y ademas tiene actor se notifica por correo
			if (node != null && node.getActors().size() > 0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
				EmailTemplate emailActor = emailService.findById(idTemplate);
				WFRelease releaseEmail = new WFRelease();
				releaseEmail.convertReleaseToWFRelease(release);
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailActor(releaseEmail, emailActor);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			}
			
			// si tiene un nodo y ademas tiene actor se notifica por correo
			if (node != null && node.getUsers().size() > 0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(23).getParamValue());
				
				EmailTemplate emailNotify = emailService.findById(idTemplate);
				WFRelease releaseEmail = new WFRelease();
				releaseEmail.convertReleaseToWFRelease(release);
				String user=getUserLogin().getFullName();
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailNotify(releaseEmail, emailNotify,user);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			}
			}
			releaseService.requestRelease(release);

			return "redirect:/release/summary-" + release.getId();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	public ArrayList<MyError> validSections(Release release, ArrayList<MyError> errors, ReleaseCreate rc) {
		// Se verifican las secciones que se deben validar por release.
		try {
			
			errors =rc.validEmailInformation(rc,errors);
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());

			if (systemConfiguration.getGeneralInfo())
				errors = rc.validGeneralInformation(rc, errors);

			if (systemConfiguration.getObservations())
				errors = rc.validObservations(rc, errors);

			if (systemConfiguration.getSolutionInfo())
				errors = rc.validInformationSolution(rc, errors);

			if (systemConfiguration.getDefinitionEnvironment())
				errors = rc.validAmbientDefinition(rc, errors);

			if (release.getSystem().getIsAIA())
				errors = rc.validModifiedComponentDefinition(rc, errors);

			if (systemConfiguration.getDependencies()) {
				for (Integer functionalID : rc.getDependenciesFunctionals()) {
					for (Integer technicalID : rc.getDependenciesTechnical()) {
						if (functionalID.equals(technicalID)) {
							errors.add(new MyError("dependency", "Dependencia no puede ser funcional y técnica."));
							break;
						}
					}
				}
			}

			if (systemConfiguration.getInstalationData())
				errors = rc.validInstalationData(rc, errors);

			if (systemConfiguration.getDataBaseInstructions())
				errors = rc.validDataBaseInstalation(rc, errors);

			if (systemConfiguration.getSuggestedTests())
				errors = rc.validMinimalEvidence(rc, errors);

			if (systemConfiguration.getConfigurationItems()) {
				if (release.getObjects().size() == 0) {
					errors.add(new MyError("configurationItemsTable", "Ingrese un objeto."));
				}
				errors = rc.validSqlObject(rc, errors);
			}

			if (systemConfiguration.getDownEnvironment()) {
				errors = rc.validActions(rc, errors);
			}

			if (systemConfiguration.getEnvironmentObservations()) {
				errors = rc.validEnvironmentObservations(rc, errors);
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return errors;

	}

	public void loadCountsRelease(HttpServletRequest request, String name) {
		List<SystemUser> systems = systemService.listSystemByUser(name);
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", releaseService.countByType(name, "Borrador", 1, null));
		userC.put("requested", releaseService.countByType(name, "Solicitado", 1, null));
		userC.put("completed", releaseService.countByType(name, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);

		Object[] ids = systemService.myTeams(name);
		Map<String, Integer> teamC = new HashMap<String, Integer>();

		if (systems.size() == 0) {
			teamC.put("draft", 0);
			teamC.put("requested", 0);
			teamC.put("certification", 0);
			teamC.put("completed", 0);
			request.setAttribute("userC", userC);
		} else {
			teamC.put("draft", releaseService.countByType(name, "Borrador", 2, ids));
			teamC.put("requested", releaseService.countByType(name, "Solicitado", 2, ids));
			teamC.put("completed", releaseService.countByType(name, "Completado", 2, ids));
			teamC.put("all", (teamC.get("draft") + teamC.get("requested") + teamC.get("completed")));
		}
		request.setAttribute("teamC", teamC);

		Map<String, Integer> systemC = new HashMap<String, Integer>();
		systemC.put("draft", releaseService.countByType(name, "Borrador", 3, null));
		systemC.put("requested", releaseService.countByType(name, "Solicitado", 3, null));
		systemC.put("completed", releaseService.countByType(name, "Completado", 3, null));
		systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
		request.setAttribute("systemC", systemC);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/listObjects" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listObjects(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<?> releaseObjects = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Integer releaseId =  Integer.parseInt(request.getParameter("releaseId"));
			String sqlS=request.getParameter("sql");
			Integer sql=0;
			if(sqlS!=null) {
				sql=Integer.parseInt(request.getParameter("sql"));
			}

			releaseObjects = releaseObjectService.listObjectsByReleases(sEcho, iDisplayStart, iDisplayLength, sSearch, releaseId,sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return releaseObjects;
	}
	
	@RequestMapping(value = { "/countObjects/{releaseId}" }, method = RequestMethod.GET)
	public @ResponseBody Integer changeProject(@PathVariable Integer releaseId, Locale locale, Model model) {
		Integer releaseObjects = 0;
		try {
			releaseObjects = releaseObjectService.listCountByReleases( releaseId);
		} catch (Exception e) {
			Sentry.capture(e, "countObjects");

			e.printStackTrace();
		}

		return releaseObjects;
	}


	
	public List<String> getCC(String ccs) {
		
		List<String> getCC = new ArrayList<String>();
		if(ccs!=null) {
			ccs.split(",");
			for (String cc : ccs.split(",")) {
				getCC.add(cc);
				}
		}
		return getCC;
	}
}
