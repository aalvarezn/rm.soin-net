package com.soin.sgrm.controller;

import java.io.File;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseEditWithOutObjects;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseSummaryFile;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.ReleaseSummaryMin;
import com.soin.sgrm.model.ReleaseTinySummary;
import com.soin.sgrm.model.ReleaseTracking;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.pos.PAmbient;
import com.soin.sgrm.model.pos.PBaseKnowledge;
import com.soin.sgrm.model.pos.PDependency;
import com.soin.sgrm.model.pos.PDocTemplate;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PModifiedComponent;
import com.soin.sgrm.model.pos.PModule;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseEditWithOutObjects;
import com.soin.sgrm.model.pos.PReleaseObject;
import com.soin.sgrm.model.pos.PReleaseReport;
import com.soin.sgrm.model.pos.PReleaseSummary;
import com.soin.sgrm.model.pos.PReleaseSummaryFile;
import com.soin.sgrm.model.pos.PReleaseSummaryMin;
import com.soin.sgrm.model.pos.PReleaseTinySummary;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PRelease_Objects;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PSystemConfiguration;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.pos.wf.PWFRelease;
import com.soin.sgrm.model.pos.wf.PWorkFlow;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WorkFlow;
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
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseObjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.EnvironmentService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.UserService;
import com.soin.sgrm.service.pos.PActionEnvironmentService;
import com.soin.sgrm.service.pos.PAmbientService;
import com.soin.sgrm.service.pos.PConfigurationItemService;
import com.soin.sgrm.service.pos.PDocTemplateService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PEnvironmentService;
import com.soin.sgrm.service.pos.PErrorReleaseService;
import com.soin.sgrm.service.pos.PImpactService;
import com.soin.sgrm.service.pos.PModifiedComponentService;
import com.soin.sgrm.service.pos.PModuleService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PReleaseObjectService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PRequestService;
import com.soin.sgrm.service.pos.PRiskService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemConfigurationService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypeDetailService;
import com.soin.sgrm.service.pos.PTypeObjectService;
import com.soin.sgrm.service.pos.PUserInfoService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.service.pos.wf.PNodeService;
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
	@Autowired
	private RequestService requestService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private Environment env;

	private PUserInfoService ploginService;
	@Autowired
	private PSystemConfigurationService psystemConfigurationService;
	@Autowired
	private PImpactService pimpactService;
	@Autowired
	private PPriorityService ppriorityService;
	@Autowired
	private PStatusService pstatusService;
	@Autowired
	private PReleaseService preleaseService;
	@Autowired
	private PRiskService priskService;
	@Autowired
	private PModuleService pmoduleService;
	@Autowired
	private PAmbientService pambientService;
	@Autowired
	private PModifiedComponentService pmodifiedComponentService;
	@Autowired
	private PSystemService psystemService;
	@Autowired
	private PDocTemplateService pdocsTemplateService;
	@Autowired
	private PTypeObjectService ptypeObjectService;
	@Autowired
	private PConfigurationItemService pconfigurationItemService;
	@Autowired
	private PActionEnvironmentService pactionService;
	@Autowired
	private PEmailTemplateService pemailService;
	@Autowired
	private PNodeService pnodeService;
	@Autowired
	private PReleaseObjectService preleaseObjectService;
	@Autowired
	private PErrorReleaseService perrorService;

	@Autowired
	PProjectService pprojectService;
	@Autowired
	private PEnvironmentService penvironmentService;
	
	@Autowired
	private PUserService puserService;
	
	@Autowired
	private PRequestService prequestService;
	
	private final Environment environment;

	@Autowired
	public ReleaseController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			loadCountsRelease(request, name);
			if (profileActive().equals("oracle")) {
				model.addAttribute("system", new SystemUser());
				model.addAttribute("systems", systemService.listSystemByUser(getUserLogin().getUsername()));
				model.addAttribute("status", new Status());
				model.addAttribute("statuses", statusService.list());
				model.addAttribute("list", "userRelease");
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("system", new PSystemUser());
				model.addAttribute("systems", psystemService.listSystemByUser(getUserLogin().getUsername()));
				model.addAttribute("status", new PStatus());
				model.addAttribute("statuses", pstatusService.list());
				model.addAttribute("list", "userRelease");
			}

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

				if (profileActive().equals("oracle")) {
					model.addAttribute("system", new SystemUser());
					model.addAttribute("systems", systemService.listSystemUser());
					model.addAttribute("status", new Status());
					model.addAttribute("statuses", statusService.list());
					model.addAttribute("list", "userRelease");
				} else if (profileActive().equals("postgres")) {
					model.addAttribute("system", new PSystemUser());
					model.addAttribute("systems", psystemService.listSystemUser());
					model.addAttribute("status", new PStatus());
					model.addAttribute("statuses", pstatusService.list());
					model.addAttribute("list", "userRelease");
				}
			
			
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
			
				if (profileActive().equals("oracle")) {
					return releaseService.listByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
							statusId);
				} else if (profileActive().equals("postgres")) {
					JsonSheet<?> releases = new JsonSheet<>();		
					String dateRange2 = request.getParameter("dateRange");
					releases= preleaseService.findAll1(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange2,
							systemId, statusId);
					return releases;
					
				}
			
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
			
			if (profileActive().equals("oracle")) {
				return releaseService.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						systemService.myTeams(name), dateRange, systemId, statusId);
			} else if (profileActive().equals("postgres")) {
				JsonSheet<?> releases = new JsonSheet<>();		
				String dateRange2 = request.getParameter("dateRange");
				releases= preleaseService.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch,systemService.myTeams(name), dateRange2,
						systemId, statusId);
				return releases;
				
				
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;

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
			if (profileActive().equals("oracle")) {
				return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
						dateRange, systemId, statusId);
			} else if (profileActive().equals("postgres")) {
				JsonSheet<?> releases = new JsonSheet<>();		
				String dateRange2 = request.getParameter("dateRange");
				releases= preleaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength,Constant.FILTRED, sSearch, dateRange2,
						systemId, statusId);
				return releases;
				
				
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
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
			if (profileActive().equals("oracle")) {
				return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
						dateRange, systemId, statusId);
			} else if (profileActive().equals("postgres")) {
				
				JsonSheet<?> releases = new JsonSheet<>();		
				String dateRange2 = request.getParameter("dateRange");
				releases= preleaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength,Constant.FILTRED, sSearch, dateRange2,
						systemId, statusId);
				return releases;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
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
			if (profileActive().equals("oracle")) {
				return releaseService.listByAllSystemQA(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						Constant.FILTRED, dateRange, systemId, statusId);
			} else if (profileActive().equals("postgres")) {
				
				JsonSheet<?> releases = new JsonSheet<>();		
				String dateRange2 = request.getParameter("dateRange");
				releases= preleaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength,Constant.FILTRED, sSearch, dateRange2,
						systemId, statusId);
				return releases;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(value = "/summary-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			if (profileActive().equals("oracle")) {
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
				String textChanged = "No aplica";
				if (release.getObservations() != null) {
					textChanged = release.getObservations().replaceAll("(https?://\\S+)",
							"<a href=\"$1\" target=\"_blank\">$1</a>");
				}

				model.addAttribute("textChanged", textChanged);
				Set<EmailTemplate> emailTemplate = release.getSystem().getEmailTemplate();
				if (!emailTemplate.isEmpty()) {
					model.addAttribute("cc", release.getSystem().getEmailTemplate().iterator().next().getCc());
				} else {
					model.addAttribute("cc", "");
				}
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("parameter", status);
				PReleaseSummaryMin release = null;
				if (CommonUtils.isNumeric(status)) {
					release = preleaseService.findByIdMin(Integer.parseInt(status));
				}

				if (release == null) {
					return "redirect:/";
				}
				
				PSystemConfiguration systemConfiguration = psystemConfigurationService
						.findBySystemId(release.getSystem().getId());
				List<PDocTemplate> docs = pdocsTemplateService.findBySystem(release.getSystem().getId());
				
				model.addAttribute("dependency", new PRelease());
				model.addAttribute("doc", new PDocTemplate());
				model.addAttribute("docs", docs);
				model.addAttribute("release", release);
				model.addAttribute("systemConfiguration", systemConfiguration);
				model.addAttribute("status", new PStatus());
				model.addAttribute("statuses", pstatusService.list());
				model.addAttribute("errors", perrorService.findAll());

				String textChanged = "No aplica";
				if (release.getObservations() != null) {
					textChanged = release.getObservations().replaceAll("(https?://\\S+)",
							"<a href=\"$1\" target=\"_blank\">$1</a>");
				}

				model.addAttribute("textChanged", textChanged);
				Set<PEmailTemplate> emailTemplate = release.getSystem().getEmailTemplate();
				if (!emailTemplate.isEmpty()) {
					model.addAttribute("cc", release.getSystem().getEmailTemplate().iterator().next().getCc());
				} else {
					model.addAttribute("cc", "");
				}
				
			}
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			throw ex;
		} catch (Exception e) {
			Sentry.capture(e, "release");
			System.out.println(e.getMessage());
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
			if (profileActive().equals("oracle")) {
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
				String textChanged = "No aplica";
				if (release.getObservations() != null) {
					textChanged = release.getObservations().replaceAll("(https?://\\S+)",
							"<a href=\"$1\" target=\"_blank\">$1</a>");
				}
				model.addAttribute("textChanged", textChanged);
				model.addAttribute("dependency", new Release());
				model.addAttribute("doc", new DocTemplate());
				model.addAttribute("docs", docs);
				model.addAttribute("release", release);
				model.addAttribute("systemConfiguration", systemConfiguration);
				model.addAttribute("status", new Status());
				model.addAttribute("statuses", statusService.list());
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("parameter", status);
				PReleaseSummaryMin release = null;
				if (CommonUtils.isNumeric(status)) {
					release = preleaseService.findByIdMin(Integer.parseInt(status));
				}

				if (release == null) {
					return "redirect:/";
				}
				PSystemConfiguration systemConfiguration = psystemConfigurationService
						.findBySystemId(release.getSystem().getId());
				List<PDocTemplate> docs = pdocsTemplateService.findBySystem(release.getSystem().getId());
				model.addAttribute("dependency", new PRelease());
				model.addAttribute("doc", new PDocTemplate());
				model.addAttribute("docs", docs);
				model.addAttribute("release", release);
				model.addAttribute("systemConfiguration", systemConfiguration);
				model.addAttribute("status", new PStatus());
				model.addAttribute("statuses", pstatusService.list());
			}
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
			
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("parameter", status);
				PReleaseTinySummary release = null;
				if (CommonUtils.isNumeric(status)) {
					release = preleaseService.findByIdTiny(Integer.parseInt(status));
				}

				if (release == null) {
					return "redirect:/";
				}
				PSystemConfiguration systemConfiguration = psystemConfigurationService
						.findBySystemId(release.getSystem().getId());
				List<PDocTemplate> docs = pdocsTemplateService.findBySystem(release.getSystem().getId());
				model.addAttribute("dependency", new PRelease());
				model.addAttribute("object", new PReleaseObject());
				model.addAttribute("doc", new PDocTemplate());
				model.addAttribute("docs", docs);
				model.addAttribute("release", release);
				model.addAttribute("systemConfiguration", systemConfiguration);
			}
			

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
		
		try {

			
			if (profileActive().equals("oracle")) {
				ReleaseEditWithOutObjects release = new ReleaseEditWithOutObjects();
				SystemConfiguration systemConfiguration = new SystemConfiguration();
				UserLogin user = getUserLogin();

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

				List<Release_Objects> listObjects = releaseObjectService.listObjectsSql(idRelease);

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
				model.addAttribute("releaseObject", listObjects);
				if (release.getSystem().getEmailTemplate() != null) {
					if (release.getSystem().getEmailTemplate().size() > 1) {
						model.addAttribute("ccs", getCC(release.getSystem().getEmailTemplate().iterator().next().getCc()));
					} else {
						for (EmailTemplate emailTemplate : release.getSystem().getEmailTemplate()) {
							model.addAttribute("ccs", getCC(emailTemplate.getCc()));
						}
					}

				} else {
					model.addAttribute("ccs", "");

				}
				
			} else if (profileActive().equals("postgres")) {
				PReleaseEditWithOutObjects release = new PReleaseEditWithOutObjects();
				PSystemConfiguration systemConfiguration = new PSystemConfiguration();
				UserLogin user = getUserLogin();
				if (id == null) {
					return "redirect:/";
				}

				Integer idRelease = Integer.parseInt(id);
				release = preleaseService.findEditByIdWithOutObjects(idRelease);

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

				systemConfiguration = psystemConfigurationService.findBySystemId(release.getSystem().getId());
				List<PDocTemplate> docs = pdocsTemplateService.findBySystem(release.getSystem().getId());

				if (release.getSystem().getImportObjects()) {
					model.addAttribute("typeDetailList", typeDetail.list());
				}

				List<PRelease_Objects> listObjects = preleaseObjectService.listObjectsSql(idRelease);

				model.addAttribute("systems", psystemService.listSystemByUser(getUserLogin().getUsername()));
				model.addAttribute("impacts", pimpactService.list());
				model.addAttribute("risks", priskService.list());
				model.addAttribute("priorities", ppriorityService.list());
				model.addAttribute("environments", penvironmentService.listBySystem(release.getSystem().getId()));
				model.addAttribute("systemConfiguration", systemConfiguration);
				model.addAttribute("ambients", pambientService.list(release.getSystem().getId()));
				model.addAttribute("typeObjects", ptypeObjectService.listBySystem(release.getSystem().getId()));
				model.addAttribute("actionEnvironments", pactionService.listBySystem(release.getSystem().getId()));
				model.addAttribute("configurationItems",
						pconfigurationItemService.listBySystem(release.getSystem().getId()));
				model.addAttribute("doc", new PDocTemplate());
				model.addAttribute("docs", docs);
				model.addAttribute("release", release);
				model.addAttribute("senders", release.getSenders());
				model.addAttribute("message", release.getMessage());
				model.addAttribute("releaseObject", listObjects);
				if (release.getSystem().getEmailTemplate() != null) {
					if (release.getSystem().getEmailTemplate().size() > 1) {
						model.addAttribute("ccs", getCC(release.getSystem().getEmailTemplate().iterator().next().getCc()));
					} else {
						for (PEmailTemplate emailTemplate : release.getSystem().getEmailTemplate()) {
							model.addAttribute("ccs", getCC(emailTemplate.getCc()));
						}
					}

				} else {
					model.addAttribute("ccs", "");
				}
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
			if (profileActive().equals("oracle")) {
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
				releaseCopy.setMotive("Inicio de release");
				releaseCopy.setRetries(0);
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
			} else if (profileActive().equals("postgres")) {
				PUserInfo user = ploginService.findUserInfoById(getUserLogin().getId());
				String number_release = "";
				PReleaseEdit release = preleaseService.findEditById(Integer.parseInt(idRelease));

				PReleaseEdit releaseCopy = (PReleaseEdit) release.clone();
				if (!requeriment.equals("TPO/BT")) {
					number_release = preleaseService.generateReleaseNumber(requeriment, requirement_name,
							releaseCopy.getSystem().getName());
				} else {
					number_release = preleaseService.generateTPO_BT_ReleaseNumber((releaseCopy.getSystem().getName()),
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
				releaseCopy.setStatus(pstatusService.findByName("Borrador"));
				releaseCopy.setMotive("Inicio de release");
				releaseCopy.setRetries(0);
				if (requeriment.equals("IN"))
					releaseCopy.setIncidents(requirement_name);

				if (requeriment.equals("PR"))
					releaseCopy.setProblems(requirement_name);

				if (requeriment.equals("SS"))
					releaseCopy.setServiceRequests(requirement_name);

				if (requeriment.equals("SO-ICE"))
					releaseCopy.setOperativeSupport(requirement_name);

				if (!requeriment.equals("TPO/BT")) {
					preleaseService.copy(releaseCopy, "-1");
				} else {
					preleaseService.copy(releaseCopy, requirement_name);
				}
				res.setData(releaseCopy.getId() + "");
			}
			
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
		try {
			if (profileActive().equals("oracle")) {
				String number_release = "";
				Release release = new Release();
				Module module = new Module();
				User user = loginService.findUserById(getUserLogin().getId());
				
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
				createPathRelease(release.getId());
			} else if (profileActive().equals("postgres")) {
				String number_release = "";
				PRelease release = new PRelease();
				PModule module = new PModule();
				PUser user = ploginService.findUserById(getUserLogin().getId());
				
				if (!requeriment.equals("TPO/BT")) {
					number_release = preleaseService.generateReleaseNumber(requeriment, requirement_name, system_id);
				} else {
					number_release = preleaseService.generateTPO_BT_ReleaseNumber(system_id, requirement_name);
				}
				module = pmoduleService.findBySystemId(system_id);
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
				PStatus status = pstatusService.findByName("Borrador");
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
					preleaseService.save(release, "-1");
				} else {
					preleaseService.save(release, requirement_name);
				}
				createPathPRelease(release.getId());
				res.setData(release.getId() + "");
			}
			
			
			res.setStatus("success");
			
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
			
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PReleaseEdit release = preleaseService.findEditById(id);
				if (release.getStatus().getName().equals("Borrador")) {
					if (release.getUser().getUsername().equals(getUserLogin().getUsername())) {
						PStatus status = pstatusService.findByName("Anulado");
						release.setStatus(status);
						release.setMotive(status.getMotive());
						release.setOperator(getUserLogin().getFullName());
						preleaseService.updateStatusRelease(release);
					} else {
						res.setStatus("fail");
						res.setException("No tiene permisos sobre el release.");
					}
				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
				}
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
			if (profileActive().equals("oracle")) {
				ReleaseEdit release = releaseService.findEditById(Integer.parseInt(idRelease));
				UserInfo user = loginService.findUserInfoById(Integer.parseInt(idUser));

				if (release == null || user == null) {
					res.setStatus("fail");
					res.setException("El Release o Usuario indicado no existen.");
					return res;
				}
				releaseService.assignRelease(release, user);
			} else if (profileActive().equals("postgres")) {
				PReleaseEdit release = preleaseService.findEditById(Integer.parseInt(idRelease));
				PUserInfo user = ploginService.findUserInfoById(Integer.parseInt(idUser));

				if (release == null || user == null) {
					res.setStatus("fail");
					res.setException("El Release o Usuario indicado no existen.");
					return res;
				}
				preleaseService.assignRelease(release, user);
			}
			
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
		
		ArrayList<MyError> errors = new ArrayList<MyError>(); // contiene los errores del release.
		// Validacion del Release con la configuracion por secciones del sistema.
		try {
			if (profileActive().equals("oracle")) {
				List<Ambient> ambients = new ArrayList<Ambient>();
				List<ModifiedComponent> modifiedComponents = new ArrayList<ModifiedComponent>();
				Set<Dependency> dependencies = new HashSet<Dependency>();
				Dependency dependency = null;
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
				if (rc.getSenders() != null) {
					if (rc.getSenders().length() < 256) {
						rc.setSenders(rc.getSenders());
					} else {
						rc.setSenders(release.getSenders());
					}
				}

				release.checkModifiedComponents(modifiedComponents);
				release.checkAmbientsExists(ambients);
				release.checkDependenciesExists(dependencies);
				if (rc.getSenders() != null) {
					if (rc.getSenders().length() < 256) {
						rc.setSenders(rc.getSenders());
					} else {
						rc.setSenders(release.getSenders());
					}
				}
				if (rc.getMessage() != null) {
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
			} else if (profileActive().equals("postgres")) {
				List<PAmbient> ambients = new ArrayList<PAmbient>();
				List<PModifiedComponent> modifiedComponents = new ArrayList<PModifiedComponent>();
				Set<PDependency> dependencies = new HashSet<PDependency>();
				PDependency dependency = null;
				PRelease release = preleaseService.findReleaseById(Integer.parseInt(rc.getRelease_id()));
				errors = validSections(release, errors, rc);
				// Si no tiene errores se guarda

				for (Integer idAmbient : rc.getAmbient()) {
					ambients.add(pambientService.findById(idAmbient, rc.getSystemCode()));
				}

				for (Integer idComponent : rc.getModifiedComponent()) {
					modifiedComponents.add(pmodifiedComponentService.findById(idComponent));
				}

				PReleaseUser releaseUser = preleaseService.findReleaseUserById(Integer.parseInt(rc.getRelease_id()));
				for (Integer to_id : rc.getDependenciesFunctionals()) {
					dependency = new PDependency();
					dependency.setId(0);
					dependency.setRelease(releaseUser);
					dependency.setTo_release(preleaseService.findReleaseUserById(to_id));
					dependency.setMandatory(release.isMandatory(dependency));
					dependency.setIsFunctional(true);
					dependencies.add(dependency);
				}
				for (Integer to_id : rc.getDependenciesTechnical()) {
					dependency = new PDependency();
					dependency.setId(0);
					dependency.setRelease(releaseUser);
					dependency.setTo_release(preleaseService.findReleaseUserById(to_id));
					dependency.setMandatory(release.isMandatory(dependency));
					dependency.setIsFunctional(false);
					dependencies.add(dependency);
				}

				release.checkModifiedComponents(modifiedComponents);
				release.checkAmbientsExists(ambients);
				release.checkDependenciesExists(dependencies);
				if (rc.getSenders() != null) {
					if (rc.getSenders().length() < 256) {
						rc.setSenders(rc.getSenders());
					} else {
						rc.setSenders(release.getSenders());
					}
				}

				if (rc.getMessage() != null) {
					if (rc.getMessage().length() < 256) {
						rc.setMessage(rc.getMessage());
					} else {
						rc.setMessage(release.getMessage());
					}
				}
				preleaseService.saveRelease(release, rc);

				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
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
			if (profileActive().equals("oracle")) {
				ReleaseEditWithOutObjects release = null;
				Release releaseComplete = new Release();
				if (CommonUtils.isNumeric(releaseId)) {
					release = releaseService.findEditByIdWithOutObjects(Integer.parseInt(releaseId));
					releaseComplete.setId(release.getId());
					releaseComplete.setNode(release.getNode());
					releaseComplete.setSystem(release.getSystem());
					releaseComplete.setCreateDate(release.getCreateDate());
					releaseComplete.setReleaseNumber(release.getReleaseNumber());

					releaseComplete.setStatus(release.getStatus());
					User user = userService.findUserById(release.getUser().getId());
					releaseComplete.setUser(user);
				}
				// Si el release no existe se regresa al inicio.
				if (release == null) {
					return "redirect:/";
				}
				// Verificar si existe un flujo para el sistema

				String[] listNumRelease = release.getReleaseNumber().split("\\.");
				String tpo = listNumRelease[1];
				Request requestVer=new Request();
				if (tpo.contains("TPO")) {
					tpo = tpo.replace("TPO", "TPO-");
					 requestVer = requestService.findByNameCode(tpo);
				}else if (tpo.contains("BT")) {
					tpo = tpo.replace("BT", "BT-");
					 requestVer = requestService.findByNameCode(tpo);
				}else{
					 requestVer = null;
				}
				
				

				// aca empezaria la verificacion

				if (requestVer == null) {

					Node node = nodeService.existWorkFlow(releaseComplete);
					Status status = statusService.findByName("Solicitado");

					release.setStatus(status);
					release.setMotive(status.getMotive());
					release.setOperator(getUserLogin().getFullName());

					if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
							ReleaseEditWithOutObjects releaseEmail = release;
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
						int nodeId1 = node.getId();
						release.setNode(node);
						node = checkNode(node, releaseComplete, requestVer);
						release.setNode(node);
						int nodeId2 = node.getId();
						node = release.getNode();
						release.setStatus(node.getStatus());
						if(!release.getStatus().getName().equals("Solicitado")) {
							release.setMotive("Automatico");
							release.setOperator("Automatico");
						}else {
							releaseService.requestRelease(release);
						}
						releaseComplete.setNode(node);
						releaseComplete.setStatus(node.getStatus());
						// si tiene un nodo y ademas tiene actor se notifica por correo
						if (node != null && node.getActors().size() > 0) {
							Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
							EmailTemplate emailActor = emailService.findById(idTemplate);
							WFRelease releaseEmail = new WFRelease();
							releaseEmail.convertReleaseToWFRelease(releaseComplete);
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
							releaseEmail.convertReleaseToWFRelease(releaseComplete);
							String user = getUserLogin().getFullName();
							Thread newThread = new Thread(() -> {
								try {

									emailService.sendMailNotify(releaseEmail, emailNotify, user);
								} catch (Exception e) {
									Sentry.capture(e, "release");
								}

							});
							newThread.start();
						}
						if (nodeId1 != nodeId2) {
							String statusName=releaseService.getLastStatusHistory(release.getId());
							if(!statusName.equals(release.getStatus().getName())) {
								releaseService.requestRelease(release);
							}
							
						}
					}else {
						releaseService.requestRelease(release);
					}

				} else {
					// aca tomariamos el TPO se revisa si vienen un automatico si esta automatico
					// procedemos a pasar al estado necesario
					// seleccionado por el gestor se busca el nombre del estado ahi seria en el
					// estado que nos vamos a ubicar
					// pasaria a la siguiente verificacion si no trae nada vamos por prioridad
					// revisamos el primero del nodo y pasamos a ese estado
					// si no hay el primero se verifica el segundo y pasamos al segundo y si no
					// repetimos y pasamos al tercer hay que verifiricar si el siguiente nodo
					// tiene salto

					if (requestVer.getAuto() == 1) {

						Node node = nodeService.existWorkFlow(releaseComplete);
						Status status = statusService.findByName("Solicitado");

						release.setStatus(status);
						release.setMotive(status.getMotive());
						release.setOperator(getUserLogin().getFullName());
						release.setNode(node);
						if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
							if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
								EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
								ReleaseEditWithOutObjects releaseEmail = release;
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
							WorkFlow workflow = node.getWorkFlow();
							node = nodeService.findByIdAndWorkFlow(requestVer.getNodeName(), workflow.getId());
							if(node!=null) {
							int nodeId1 = node.getId();
							node = checkNode(node, releaseComplete, requestVer);
							release.setNode(node);
							release.setStatus(node.getStatus());
							if(!release.getStatus().getName().equals("Solicitado")) {
								release.setMotive("Automatico");
								release.setOperator("Automatico");
							}else {
								releaseService.requestRelease(release);
							}
							releaseComplete.setNode(node);
							releaseComplete.setStatus(node.getStatus());
							int nodeId2 = node.getId();
							

							if (nodeId1 == nodeId2) {
								// si tiene un nodo y ademas tiene actor se notifica por correo
								if (node != null && node.getActors().size() > 0) {
									Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
									EmailTemplate emailActor = emailService.findById(idTemplate);
									WFRelease releaseEmail = new WFRelease();
									releaseEmail.convertReleaseToWFRelease(releaseComplete);
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
									releaseEmail.convertReleaseToWFRelease(releaseComplete);
									String user = getUserLogin().getFullName();
									Thread newThread = new Thread(() -> {
										try {

											emailService.sendMailNotify(releaseEmail, emailNotify, user);
										} catch (Exception e) {
											Sentry.capture(e, "release");
										}

									});
									newThread.start();
								}
							}
							if (nodeId1 != nodeId2) {
								String statusName=releaseService.getLastStatusHistory(release.getId());
								if(!statusName.equals(release.getStatus().getName())) {
									releaseService.requestRelease(release);
								}
							}
						}
						}else {
							releaseService.requestRelease(release);
						}

					} else {
						Node node = nodeService.existWorkFlow(releaseComplete);
						Status status = statusService.findByName("Solicitado");

						release.setStatus(status);
						release.setMotive(status.getMotive());
						release.setOperator(getUserLogin().getFullName());

						if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
							if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
								EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
								ReleaseEditWithOutObjects releaseEmail = release;
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
							int nodeId1 = node.getId();
							node = checkNode(node, releaseComplete, requestVer);
							release.setNode(node);
							release.setStatus(node.getStatus());
							if(!release.getStatus().getName().equals("Solicitado")) {
								release.setMotive("Automatico");
								release.setOperator("Automatico");
							}else {
								releaseService.requestRelease(release);
							}
							releaseComplete.setNode(node);
							releaseComplete.setStatus(node.getStatus());
							int nodeId2 = node.getId();

							// si tiene un nodo y ademas tiene actor se notifica por correo
							if (node != null && node.getActors().size() > 0) {
								Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
								EmailTemplate emailActor = emailService.findById(idTemplate);
								WFRelease releaseEmail = new WFRelease();
								releaseEmail.convertReleaseToWFRelease(releaseComplete);
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
								releaseEmail.convertReleaseToWFRelease(releaseComplete);
								String user = getUserLogin().getFullName();
								Thread newThread = new Thread(() -> {
									try {

										emailService.sendMailNotify(releaseEmail, emailNotify, user);
									} catch (Exception e) {
										Sentry.capture(e, "release");
									}

								});
								newThread.start();
							}

							if (nodeId1 != nodeId2) {
								String statusName=releaseService.getLastStatusHistory(release.getId());
								if(!statusName.equals(release.getStatus().getName())) {
									releaseService.requestRelease(release);
								}
							}

						}else {
							releaseService.requestRelease(release);
						}

					}

				}

				return "redirect:/release/summary-" + release.getId();
			} else if (profileActive().equals("postgres")) {
				PReleaseEditWithOutObjects release = null;
				PRelease releaseComplete = new PRelease();
				if (CommonUtils.isNumeric(releaseId)) {
					release = preleaseService.findEditByIdWithOutObjects(Integer.parseInt(releaseId));
					releaseComplete.setId(release.getId());
					releaseComplete.setNode(release.getNode());
					releaseComplete.setSystem(release.getSystem());
					releaseComplete.setCreateDate(release.getCreateDate());
					releaseComplete.setReleaseNumber(release.getReleaseNumber());

					releaseComplete.setStatus(release.getStatus());
					PUser user = puserService.findUserById(release.getUser().getId());
					releaseComplete.setUser(user);
				}
				// Si el release no existe se regresa al inicio.
				if (release == null) {
					return "redirect:/";
				}
				// Verificar si existe un flujo para el sistema

				String[] listNumRelease = release.getReleaseNumber().split("\\.");
				String tpo = listNumRelease[1];
				PRequest requestVer=new PRequest();
				if (tpo.contains("TPO")) {
					tpo = tpo.replace("TPO", "TPO-");
					 requestVer = prequestService.findByNameCode(tpo);
				}else if (tpo.contains("BT")) {
					tpo = tpo.replace("BT", "BT-");
					 requestVer = prequestService.findByNameCode(tpo);
				}else{
					 requestVer = null;
				}
				
				

				// aca empezaria la verificacion

				if (requestVer == null) {

					PNode node = pnodeService.existWorkFlow(releaseComplete);
					PStatus status = pstatusService.findByName("Solicitado");

					release.setStatus(status);
					release.setMotive(status.getMotive());
					release.setOperator(getUserLogin().getFullName());

					if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							PEmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
							PReleaseEditWithOutObjects releaseEmail = release;
							Thread newThread = new Thread(() -> {
								try {
									pemailService.sendMail(releaseEmail, email);
								} catch (Exception e) {
									Sentry.capture(e, "release");
								}

							});
							newThread.start();
						}
					}

					if (node != null) {
						int nodeId1 = node.getId();
						release.setNode(node);
						node = checkNode(node, releaseComplete, requestVer);
						release.setNode(node);
						int nodeId2 = node.getId();
						node = release.getNode();
						release.setStatus(node.getStatus());
						if(!release.getStatus().getName().equals("Solicitado")) {
							release.setMotive("Automatico");
							release.setOperator("Automatico");
						}else {
							preleaseService.requestRelease(release);
						}
						releaseComplete.setNode(node);
						releaseComplete.setStatus(node.getStatus());
						// si tiene un nodo y ademas tiene actor se notifica por correo
						if (node != null && node.getActors().size() > 0) {
							Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
							PEmailTemplate emailActor = pemailService.findById(idTemplate);
							PWFRelease releaseEmail = new PWFRelease();
							releaseEmail.convertReleaseToWFRelease(releaseComplete);
							Thread newThread = new Thread(() -> {
								try {
									pemailService.sendMailActor(releaseEmail, emailActor);
								} catch (Exception e) {
									Sentry.capture(e, "release");
								}

							});
							newThread.start();
						}

						// si tiene un nodo y ademas tiene actor se notifica por correo
						if (node != null && node.getUsers().size() > 0) {
							Integer idTemplate = Integer.parseInt(paramService.findByCode(23).getParamValue());

							PEmailTemplate emailNotify = pemailService.findById(idTemplate);
							PWFRelease releaseEmail = new PWFRelease();
							releaseEmail.convertReleaseToWFRelease(releaseComplete);
							String user = getUserLogin().getFullName();
							Thread newThread = new Thread(() -> {
								try {

									pemailService.sendMailNotify(releaseEmail, emailNotify, user);
								} catch (Exception e) {
									Sentry.capture(e, "release");
								}

							});
							newThread.start();
						}
						if (nodeId1 != nodeId2) {
							String statusName=releaseService.getLastStatusHistory(release.getId());
							if(!statusName.equals(release.getStatus().getName())) {
								preleaseService.requestRelease(release);
							}
							
						}
					}else {
						preleaseService.requestRelease(release);
					}

				} else {
					// aca tomariamos el TPO se revisa si vienen un automatico si esta automatico
					// procedemos a pasar al estado necesario
					// seleccionado por el gestor se busca el nombre del estado ahi seria en el
					// estado que nos vamos a ubicar
					// pasaria a la siguiente verificacion si no trae nada vamos por prioridad
					// revisamos el primero del nodo y pasamos a ese estado
					// si no hay el primero se verifica el segundo y pasamos al segundo y si no
					// repetimos y pasamos al tercer hay que verifiricar si el siguiente nodo
					// tiene salto

					if (requestVer.getAuto() == 1) {

						PNode node = pnodeService.existWorkFlow(releaseComplete);
						PStatus status = pstatusService.findByName("Solicitado");

						release.setStatus(status);
						release.setMotive(status.getMotive());
						release.setOperator(getUserLogin().getFullName());
						release.setNode(node);
						if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
							if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
								PEmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
								PReleaseEditWithOutObjects releaseEmail = release;
								Thread newThread = new Thread(() -> {
									try {
										pemailService.sendMail(releaseEmail, email);
									} catch (Exception e) {
										Sentry.capture(e, "release");
									}

								});
								newThread.start();
							}
						}
						
						if (node != null) {
							PWorkFlow workflow = node.getWorkFlow();
							node = pnodeService.findByIdAndWorkFlow(requestVer.getNodeName(), workflow.getId());
							if(node!=null) {
							int nodeId1 = node.getId();
							node = checkNode(node, releaseComplete, requestVer);
							release.setNode(node);
							release.setStatus(node.getStatus());
							if(!release.getStatus().getName().equals("Solicitado")) {
								release.setMotive("Automatico");
								release.setOperator("Automatico");
							}else {
								preleaseService.requestRelease(release);
							}
							releaseComplete.setNode(node);
							releaseComplete.setStatus(node.getStatus());
							int nodeId2 = node.getId();
							

							if (nodeId1 == nodeId2) {
								// si tiene un nodo y ademas tiene actor se notifica por correo
								if (node != null && node.getActors().size() > 0) {
									Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
									PEmailTemplate emailActor = pemailService.findById(idTemplate);
									PWFRelease releaseEmail = new PWFRelease();
									releaseEmail.convertReleaseToWFRelease(releaseComplete);
									Thread newThread = new Thread(() -> {
										try {
											pemailService.sendMailActor(releaseEmail, emailActor);
										} catch (Exception e) {
											Sentry.capture(e, "release");
										}

									});
									newThread.start();
								}

								// si tiene un nodo y ademas tiene actor se notifica por correo
								if (node != null && node.getUsers().size() > 0) {
									Integer idTemplate = Integer.parseInt(paramService.findByCode(23).getParamValue());

									PEmailTemplate emailNotify = pemailService.findById(idTemplate);
									PWFRelease releaseEmail = new PWFRelease();
									releaseEmail.convertReleaseToWFRelease(releaseComplete);
									String user = getUserLogin().getFullName();
									Thread newThread = new Thread(() -> {
										try {

											pemailService.sendMailNotify(releaseEmail, emailNotify, user);
										} catch (Exception e) {
											Sentry.capture(e, "release");
										}

									});
									newThread.start();
								}
							}
							if (nodeId1 != nodeId2) {
								String statusName=preleaseService.getLastStatusHistory(release.getId());
								if(!statusName.equals(release.getStatus().getName())) {
									preleaseService.requestRelease(release);
								}
							}
						}
						}else {
							preleaseService.requestRelease(release);
						}

					} else {
						PNode node = pnodeService.existWorkFlow(releaseComplete);
						PStatus status = pstatusService.findByName("Solicitado");

						release.setStatus(status);
						release.setMotive(status.getMotive());
						release.setOperator(getUserLogin().getFullName());

						if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
							if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
								PEmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
								PReleaseEditWithOutObjects releaseEmail = release;
								Thread newThread = new Thread(() -> {
									try {
										pemailService.sendMail(releaseEmail, email);
									} catch (Exception e) {
										Sentry.capture(e, "release");
									}

								});
								newThread.start();
							}
						}

						if (node != null) {
							release.setNode(node);
							int nodeId1 = node.getId();
							node = checkNode(node, releaseComplete, requestVer);
							release.setNode(node);
							release.setStatus(node.getStatus());
							if(!release.getStatus().getName().equals("Solicitado")) {
								release.setMotive("Automatico");
								release.setOperator("Automatico");
							}else {
								preleaseService.requestRelease(release);
							}
							releaseComplete.setNode(node);
							releaseComplete.setStatus(node.getStatus());
							int nodeId2 = node.getId();

							// si tiene un nodo y ademas tiene actor se notifica por correo
							if (node != null && node.getActors().size() > 0) {
								Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
								PEmailTemplate emailActor = pemailService.findById(idTemplate);
								PWFRelease releaseEmail = new PWFRelease();
								releaseEmail.convertReleaseToWFRelease(releaseComplete);
								Thread newThread = new Thread(() -> {
									try {
										pemailService.sendMailActor(releaseEmail, emailActor);
									} catch (Exception e) {
										Sentry.capture(e, "release");
									}

								});
								newThread.start();
							}

							// si tiene un nodo y ademas tiene actor se notifica por correo
							if (node != null && node.getUsers().size() > 0) {
								Integer idTemplate = Integer.parseInt(paramService.findByCode(23).getParamValue());

								PEmailTemplate emailNotify = pemailService.findById(idTemplate);
								PWFRelease releaseEmail = new PWFRelease();
								releaseEmail.convertReleaseToWFRelease(releaseComplete);
								String user = getUserLogin().getFullName();
								Thread newThread = new Thread(() -> {
									try {

										pemailService.sendMailNotify(releaseEmail, emailNotify, user);
									} catch (Exception e) {
										Sentry.capture(e, "release");
									}

								});
								newThread.start();
							}

							if (nodeId1 != nodeId2) {
								String statusName=releaseService.getLastStatusHistory(release.getId());
								if(!statusName.equals(release.getStatus().getName())) {
									preleaseService.requestRelease(release);
								}
							}

						}else {
							preleaseService.requestRelease(release);
						}

					}

				}

				return "redirect:/release/summary-" + release.getId();
			}


		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	private Node checkNode(Node node, Release release, Request requestVer) throws SQLException {
		if (node != null) {
			if (requestVer != null) {
				release.setStatus(node.getStatus());
				release.setMotive(requestVer.getMotive());
				release.setOperator("Automatico");
				updateRelease(node, release, requestVer, release.getMotive());
				release.setNodeFinish(node);
				node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipId());
				}
				if (node == null) {
					node = release.getNodeFinish();
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else if (node.getSkipByRequest()) {
				updateRelease(node, release, requestVer, node.getMotiveSkipR());
				node = nodeService.findById(node.getSkipByRequestId());
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipId());
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else if (node.getSkipReapprove()) {
				ReleaseTrackingShow tracking = releaseService.findReleaseTracking(release.getId());
				Set<ReleaseTracking> trackingList = tracking.getTracking();
				boolean verification = false;
				for (ReleaseTracking track : trackingList) {
					if (track.getStatus().equals("En Aprobacion")) {
						verification = true;// se verifica si ya paso por aprobacion si no se hace lo normal
					}
				}
				if (verification) {
					updateRelease(node, release, requestVer, node.getMotiveSkipRA());
					node = nodeService.findById(node.getSkipReapproveId());

					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipReapproveId());
					}
					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
					}
					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipId());
					}
					requestVer = null;
					return checkNode(node, release, requestVer);
				} else {
					node = nodeService.findById(node.getSkipReapproveId());

					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipReapproveId());
					}
					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
					}
					if (node == null) {
						node = nodeService.findById(release.getNodeFinish().getSkipId());
					}
					return node;
				}

			} else if (node.getSkipNode()) {
				updateRelease(node, release, requestVer, node.getMotiveSkip());
				node = nodeService.findById(node.getSkipId());
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = nodeService.findById(release.getNodeFinish().getSkipId());
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else {
				return node;
			}
		}
		return null;

	}
	
	private PNode checkNode(PNode node, PRelease release, PRequest requestVer) throws SQLException {
		if (node != null) {
			if (requestVer != null) {
				release.setStatus(node.getStatus());
				release.setMotive(requestVer.getMotive());
				release.setOperator("Automatico");
				updateRelease(node, release, requestVer, release.getMotive());
				release.setNodeFinish(node);
				node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipId());
				}
				if (node == null) {
					node = release.getNodeFinish();
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else if (node.getSkipByRequest()) {
				updateRelease(node, release, requestVer, node.getMotiveSkipR());
				node = pnodeService.findById(node.getSkipByRequestId());
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipId());
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else if (node.getSkipReapprove()) {
				ReleaseTrackingShow tracking = releaseService.findReleaseTracking(release.getId());
				Set<ReleaseTracking> trackingList = tracking.getTracking();
				boolean verification = false;
				for (ReleaseTracking track : trackingList) {
					if (track.getStatus().equals("En Aprobacion")) {
						verification = true;// se verifica si ya paso por aprobacion si no se hace lo normal
					}
				}
				if (verification) {
					updateRelease(node, release, requestVer, node.getMotiveSkipRA());
					node = pnodeService.findById(node.getSkipReapproveId());

					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipReapproveId());
					}
					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
					}
					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipId());
					}
					requestVer = null;
					return checkNode(node, release, requestVer);
				} else {
					node = pnodeService.findById(node.getSkipReapproveId());

					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipReapproveId());
					}
					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
					}
					if (node == null) {
						node = pnodeService.findById(release.getNodeFinish().getSkipId());
					}
					return node;
				}

			} else if (node.getSkipNode()) {
				updateRelease(node, release, requestVer, node.getMotiveSkip());
				node = pnodeService.findById(node.getSkipId());
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipReapproveId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipByRequestId());
				}
				if (node == null) {
					node = pnodeService.findById(release.getNodeFinish().getSkipId());
				}
				requestVer = null;
				return checkNode(node, release, requestVer);
			} else {
				return node;
			}
		}
		return null;

	}

	public void updateRelease(Node node, Release release, Request requestVer, String motive) {

		try {
			release.setNode(node);
			release.setStatus(node.getStatus());
			if (requestVer == null) {
				release.setMotive(motive);
			} else {
				release.setMotive(motive);
			}
			release.setOperator("Automatico");
			releaseService.requestRelease(release);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateRelease(PNode node, PRelease release, PRequest requestVer, String motive) {

		try {
			release.setNode(node);
			release.setStatus(node.getStatus());
			if (requestVer == null) {
				release.setMotive(motive);
			} else {
				release.setMotive(motive);
			}
			release.setOperator("Automatico");
			preleaseService.requestRelease(release);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public ArrayList<MyError> validSections(Release release, ArrayList<MyError> errors, ReleaseCreate rc) {
		// Se verifican las secciones que se deben validar por release.
		try {

			errors = rc.validEmailInformation(rc, errors);
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
	
	public ArrayList<MyError> validSections(PRelease release, ArrayList<MyError> errors, ReleaseCreate rc) {
		// Se verifican las secciones que se deben validar por release.
		try {

			errors = rc.validEmailInformation(rc, errors);
			PSystemConfiguration systemConfiguration = psystemConfigurationService
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
		
		

			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				List<PSystemUser> systems = psystemService.listSystemByUser(name);
				Map<String, Integer> userC = new HashMap<String, Integer>();
				userC.put("draft", preleaseService.countByType(name, "Borrador", 1, null));
				userC.put("requested", preleaseService.countByType(name, "Solicitado", 1, null));
				userC.put("completed", preleaseService.countByType(name, "Completado", 1, null));
				userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
				request.setAttribute("userC", userC);

				Object[] ids = psystemService.myTeams(name);
				Map<String, Integer> teamC = new HashMap<String, Integer>();

				if (systems.size() == 0) {
					teamC.put("draft", 0);
					teamC.put("requested", 0);
					teamC.put("certification", 0);
					teamC.put("completed", 0);
					request.setAttribute("userC", userC);
				} else {
					teamC.put("draft", preleaseService.countByType(name, "Borrador", 2, ids));
					teamC.put("requested", preleaseService.countByType(name, "Solicitado", 2, ids));
					teamC.put("completed", preleaseService.countByType(name, "Completado", 2, ids));
					teamC.put("all", (teamC.get("draft") + teamC.get("requested") + teamC.get("completed")));
				}
				request.setAttribute("teamC", teamC);

				Map<String, Integer> systemC = new HashMap<String, Integer>();
				systemC.put("draft", preleaseService.countByType(name, "Borrador", 3, null));
				systemC.put("requested", preleaseService.countByType(name, "Solicitado", 3, null));
				systemC.put("completed", preleaseService.countByType(name, "Completado", 3, null));
				systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
				request.setAttribute("systemC", systemC);
			}
	

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
			Integer releaseId = Integer.parseInt(request.getParameter("releaseId"));
			String sqlS = request.getParameter("sql");
			Integer sql = 0;
			if (sqlS != null) {
				sql = Integer.parseInt(request.getParameter("sql"));
			}
			if (profileActive().equals("oracle")) {
				releaseObjects = releaseObjectService.listObjectsByReleases(sEcho, iDisplayStart, iDisplayLength, sSearch,
						releaseId, sql);
			} else if (profileActive().equals("postgres")) {
				releaseObjects = preleaseObjectService.listObjectsByReleases(sEcho, iDisplayStart, iDisplayLength, sSearch,
						releaseId, sql);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return releaseObjects;
	}

	@RequestMapping(value = { "/countObjects/{releaseId}" }, method = RequestMethod.GET)
	public @ResponseBody Integer changeProject(@PathVariable Integer releaseId, Locale locale, Model model) {
		Integer releaseObjects = 0;
		try {
			if (profileActive().equals("oracle")) {
				releaseObjects = releaseObjectService.listCountByReleases(releaseId);
			} else if (profileActive().equals("postgres")) {
				releaseObjects = preleaseObjectService.listCountByReleases(releaseId);
			}
		} catch (Exception e) {
			Sentry.capture(e, "countObjects");

			e.printStackTrace();
		}

		return releaseObjects;
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
	@RequestMapping(value = "/getRelease-{id}", method = RequestMethod.GET)
	public @ResponseBody Object getRelease(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			if (profileActive().equals("oracle")) {
				ReleaseReport release = new ReleaseReport();
				release = releaseService.findByIdReleaseReport(id);
				return release;
			} else if (profileActive().equals("postgres")) {
				PReleaseReport release = new PReleaseReport();
				release = preleaseService.findByIdReleaseReport(id);
				return release;
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return null;
	}


	@RequestMapping(value = "/tracking/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tracking(@PathVariable int id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseTrackingShow tracking = releaseService.findReleaseTracking(id);
			res.setStatus("success");
			res.setObj(tracking);
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	/**
	 * @description: Se crea la direccion donde se guardan los archivos del release.
	 * @author: Anthony Alvarez N.
	 * @return: Base path del release.
	 * @throws SQLException
	 **/
	public String createPathRelease(Integer idRelease) throws SQLException {
			ReleaseSummary release=releaseService.findById(idRelease);
			String basePath = environment.getProperty("fileStore.path");
			Project project = projectService.findById(release.getSystem().getProyectId());
			String path = project.getCode() + "/" + release.getSystem().getName() + "/";
			if (release.getRequestList().size() != 0) {
				Request request = release.getRequestList().iterator().next();
				if (request.getCode_ice() != null) {
					path += request.getCode_soin().replace(" ","") + "_" + request.getCode_ice().replace(" ","") + "/";
				} else {
					path += request.getCode_soin().replace(" ","") + "/";
				}
			}
			path += release.getReleaseNumber() + "/";
			path=path.trim();
			new File(basePath + path).mkdirs();
			return path;
		

	}
	
	/**
	 * @description: Se crea la direccion donde se guardan los archivos del release.
	 * @author: Anthony Alvarez N.
	 * @return: Base path del release.
	 * @throws SQLException
	 **/
	public String createPathPRelease(Integer idRelease) throws SQLException {
			PReleaseSummary release=preleaseService.findById(idRelease);
			Project project = projectService.findById(release.getSystem().getProyectId());
			String basePath = environment.getProperty("fileStore.path");
			String path = project.getCode() + "/" + release.getSystem().getName() + "/";
			if (release.getRequestList().size() != 0) {
				PRequest request = release.getRequestList().iterator().next();
				if (request.getCode_ice() != null) {
					path += request.getCode_soin().replace(" ","") + "_" + request.getCode_ice().replace(" ","") + "/";
				} else {
					path += request.getCode_soin().replace(" ","") + "/";
				}
			}
			path += release.getReleaseNumber() + "/";
			path=path.trim();
			new File(basePath + path).mkdirs();
			return path;
		

	}


}
