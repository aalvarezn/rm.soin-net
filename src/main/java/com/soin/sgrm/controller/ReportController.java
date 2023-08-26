package com.soin.sgrm.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.springframework.core.env.Environment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.metamodel.relational.Exportable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.CountReport;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.ImageTree;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectClean;
import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.ReportBlank;
import com.soin.sgrm.model.ReportFile;
import com.soin.sgrm.model.ReportGhap;
import com.soin.sgrm.model.ReportTest;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingToError;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.RequestRM_P1_R2;
import com.soin.sgrm.model.RequestRM_P1_R3;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PDocTemplate;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCReport;
import com.soin.sgrm.model.pos.PRFCReportComplete;
import com.soin.sgrm.model.pos.PRFCTrackingToError;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseObject;
import com.soin.sgrm.model.pos.PReleaseObjectClean;
import com.soin.sgrm.model.pos.PReleaseReport;
import com.soin.sgrm.model.pos.PReleaseReportFast;
import com.soin.sgrm.model.pos.PReleaseSummary;
import com.soin.sgrm.model.pos.PReleaseTrackingToError;
import com.soin.sgrm.model.pos.PRelease_RFCFast;
import com.soin.sgrm.model.pos.PReportFile;
import com.soin.sgrm.model.pos.PReportTest;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestRM_P1_R1;
import com.soin.sgrm.model.pos.PRequestRM_P1_R2;
import com.soin.sgrm.model.pos.PRequestRM_P1_R3;
import com.soin.sgrm.model.pos.PRequestRM_P1_R4;
import com.soin.sgrm.model.pos.PRequestRM_P1_R5;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemConfiguration;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.model.pos.PTypePetition;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.ReportFileService;
import com.soin.sgrm.service.RequestBaseR1Service;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestRM_P1_R1Service;
import com.soin.sgrm.service.RequestRM_P1_R2Service;
import com.soin.sgrm.service.RequestRM_P1_R3Service;
import com.soin.sgrm.service.RequestRM_P1_R4Service;
import com.soin.sgrm.service.RequestRM_P1_R5Service;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.service.pos.PAmbientService;
import com.soin.sgrm.service.pos.PDocTemplateService;
import com.soin.sgrm.service.pos.PImpactService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PRFCService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PReportFileService;
import com.soin.sgrm.service.pos.PRequestBaseR1Service;
import com.soin.sgrm.service.pos.PRequestBaseService;
import com.soin.sgrm.service.pos.PRequestRM_P1_R1Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R2Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R3Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R4Service;
import com.soin.sgrm.service.pos.PRequestRM_P1_R5Service;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusRequestService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemConfigurationService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypeChangeService;
import com.soin.sgrm.service.pos.PTypePetitionService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	StatusService statusService;
	@Autowired

	StatusRFCService statusRFCService;
	@Autowired
	PriorityService priorityService;
	@Autowired
	ImpactService impactService;
	@Autowired
	ReleaseService releaseService;
	@Autowired
	SystemService systemService;
	@Autowired
	SystemConfigurationService systemConfigurationService;
	@Autowired
	DocTemplateService docsTemplateService;
	@Autowired
	ReportFileService reportFileService;
	@Autowired
	ProjectService projectService;
	@Autowired
	Environment env;
	@Autowired
	RFCService rfcService;
	@Autowired
	RequestBaseR1Service requestBaseR1Service;
	@Autowired
	TypePetitionService typePetitionService;
	@Autowired
	SigesService sigesService;
	@Autowired
	TypeChangeService typeChangeService;
	@Autowired
	RequestBaseService requestBaseService;
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
	StatusRequestService statusRequestService;
	@Autowired
	AmbientService ambientService;
	@Autowired
	RequestBaseService requestService;

	@Autowired
	PStatusService pstatusService;
	@Autowired
	PStatusRFCService pstatusRFCService;
	@Autowired
	PPriorityService ppriorityService;
	@Autowired
	PImpactService pimpactService;
	@Autowired
	PReleaseService preleaseService;
	@Autowired
	PSystemService psystemService;
	@Autowired
	PSystemConfigurationService psystemConfigurationService;
	@Autowired
	PDocTemplateService pdocsTemplateService;
	@Autowired
	PReportFileService preportFileService;
	@Autowired
	PProjectService pprojectService;
	@Autowired
	PRFCService prfcService;
	@Autowired
	PRequestBaseR1Service prequestBaseR1Service;
	@Autowired
	PTypePetitionService ptypePetitionService;
	@Autowired
	PSigesService psigesService;
	@Autowired
	PTypeChangeService ptypeChangeService;
	@Autowired
	PRequestBaseService prequestBaseService;
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
	PStatusRequestService pstatusRequestService;
	@Autowired
	PAmbientService pambientService;
	@Autowired
	PRequestBaseService prequestService;

	private final Environment environment;

	@Autowired
	public ReportController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = "/releases", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			loadCountsRelease(request, name);
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<Project> projects = projectService.listAll();
				model.addAttribute("system", new SystemUser());
				model.addAttribute("systems", systems);
				model.addAttribute("status", new Status());
				model.addAttribute("projects", projects);
				model.addAttribute("statuses", statusService.list());
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PProject> projects = pprojectService.listAll();
				model.addAttribute("system", new PSystemUser());
				model.addAttribute("systems", systems);
				model.addAttribute("status", new PStatus());
				model.addAttribute("projects", projects);
				model.addAttribute("statuses", pstatusService.list());
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/report/releases";
	}

	@RequestMapping(value = "/rfc", method = RequestMethod.GET)
	public String indexRFC(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRFC(request, userLogin);
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<Project> projects = projectService.listAll();
				List<StatusRFC> statuses = statusRFCService.findAll();
				List<Impact> impacts = impactService.list();
				model.addAttribute("projects", projects);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PProject> projects = pprojectService.listAll();
				List<PStatusRFC> statuses = pstatusRFCService.findAll();
				List<PImpact> impacts = pimpactService.list();
				model.addAttribute("projects", projects);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
			}
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/report/rfc";
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String indexRequest(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRequest(request, userLogin);
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<TypePetition> typePetitions = typePetitionService.findAll();
				List<Project> projects = projectService.listAll();
				model.addAttribute("typePetitions", typePetitions);
				model.addAttribute("projects", projects);
				model.addAttribute("systems", systems);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PTypePetition> typePetitions = ptypePetitionService.findAll();
				List<PProject> projects = pprojectService.listAll();
				model.addAttribute("typePetitions", typePetitions);
				model.addAttribute("projects", projects);
				model.addAttribute("systems", systems);
			}
		} catch (Exception e) {
			Sentry.capture(e, "requestManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/report/request";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/listRequest" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRequest(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RequestBaseR1> requests = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Integer systemId;
			Long typePetitionId;
			int projectId;

			// int priorityId;
			// int systemId;

			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
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
				if (systemId == 0) {

					if (projectId == 0) {
						return requestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
								dateRange, systemId, typePetitionId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer> systemsId = new ArrayList<Integer>();
						for (System system : systems) {
							systemsId.add(system.getId());
						}

						return requestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
								dateRange, systemsId, typePetitionId);
					}

				} else {

					return requestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
							dateRange, systemId, typePetitionId);
				}
			} else if (profileActive().equals("postgres")) {
				if (systemId == 0) {

					if (projectId == 0) {
						return prequestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
								dateRange, systemId, typePetitionId);
					} else {
						List<PSystem> systems = new ArrayList<>();

						systems = psystemService.getSystemByProject(projectId);
						List<Integer> systemsId = new ArrayList<Integer>();
						for (PSystem system : systems) {
							systemsId.add(system.getId());
						}

						return prequestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
								dateRange, systemsId, typePetitionId);
					}

				} else {

					return prequestBaseR1Service.findAllReportRequest(sEcho, iDisplayStart, iDisplayLength, sSearch,
							dateRange, systemId, typePetitionId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requests;
	}

	@RequestMapping(path = "/listRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));
			Integer projectId = Integer.parseInt(request.getParameter("projectId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));

			if (profileActive().equals("oracle")) {
				return releaseService.listByAllWithObjects(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						Constant.FILTRED, dateRange, systemId, statusId, projectId);
			} else if (profileActive().equals("postgres")) {
				return preleaseService.listByAllWithObjects(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
						Constant.FILTRED, dateRange, systemId, statusId, projectId);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	public void loadCountsRelease(HttpServletRequest request, String name) {
		if (profileActive().equals("oracle")) {
			Map<String, Integer> systemC = new HashMap<String, Integer>();
			systemC.put("draft", releaseService.countByType(name, "Borrador", 3, null));
			systemC.put("requested", releaseService.countByType(name, "Solicitado", 3, null));
			systemC.put("completed", releaseService.countByType(name, "Completado", 3, null));
			systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
			request.setAttribute("systemC", systemC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> systemC = new HashMap<String, Integer>();
			systemC.put("draft", preleaseService.countByType(name, "Borrador", 3, null));
			systemC.put("requested", preleaseService.countByType(name, "Solicitado", 3, null));
			systemC.put("completed", preleaseService.countByType(name, "Completado", 3, null));
			systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
			request.setAttribute("systemC", systemC);
		}
		
	}

	public void loadCountsRequest(HttpServletRequest request, Integer id) {
		if (profileActive().equals("oracle")) {
			Map<String, Integer> rfcC = new HashMap<String, Integer>();
			rfcC.put("draft", requestBaseR1Service.countByType(id, "Borrador", 2, null));
			rfcC.put("requested", requestBaseR1Service.countByType(id, "Solicitado", 2, null));
			rfcC.put("completed", requestBaseR1Service.countByType(id, "Completado", 2, null));
			rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
			request.setAttribute("rfcC", rfcC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> rfcC = new HashMap<String, Integer>();
			rfcC.put("draft", prequestBaseR1Service.countByType(id, "Borrador", 2, null));
			rfcC.put("requested", prequestBaseR1Service.countByType(id, "Solicitado", 2, null));
			rfcC.put("completed", prequestBaseR1Service.countByType(id, "Completado", 2, null));
			rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
			request.setAttribute("rfcC", rfcC);
		}
	
	}

	@RequestMapping(path = "/listRFC", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getListRfc(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {

		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");

			Long sigesId;
			int systemId;
			int projectId;

			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			if (request.getParameter("sigesId").equals("")) {
				sigesId = null;
			} else {
				sigesId = (long) Integer.parseInt(request.getParameter("sigesId"));
			}
			String dateRange = request.getParameter("dateRange");
			if (profileActive().equals("oracle")) {
				if (systemId == 0) {

					if (projectId == 0) {
						return rfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
								systemId, sigesId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer> systemsId = new ArrayList<Integer>();
						for (System system : systems) {
							systemsId.add(system.getId());
						}

						return rfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
								systemsId, sigesId);
					}

				} else {

					return rfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
							sigesId);
				}
			} else if (profileActive().equals("postgres")) {
				if (systemId == 0) {

					if (projectId == 0) {
						return prfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
								systemId, sigesId);
					} else {
						List<PSystem> systems = new ArrayList<>();

						systems = psystemService.getSystemByProject(projectId);
						List<Integer> systemsId = new ArrayList<Integer>();
						for (PSystem system : systems) {
							systemsId.add(system.getId());
						}

						return prfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
								systemsId, sigesId);
					}

				} else {

					return prfcService.findAllReportRFC(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
							sigesId);
				}
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
 
		return null;
	}

	public void loadCountsRFC(HttpServletRequest request, Integer id) {

		if (profileActive().equals("oracle")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			userC.put("draft", rfcService.countByType(id, "Borrador", 1, null));
			userC.put("requested", rfcService.countByType(id, "Solicitado", 1, null));
			userC.put("completed", rfcService.countByType(id, "Completado", 1, null));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> userC = new HashMap<String, Integer>();
			userC.put("draft", prfcService.countByType(id, "Borrador", 1, null));
			userC.put("requested", prfcService.countByType(id, "Solicitado", 1, null));
			userC.put("completed", prfcService.countByType(id, "Completado", 1, null));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		}

	}
	@RequestMapping(value = "/summaryReportRelease-{status}", method = RequestMethod.GET)
	public String summaryReportRelease(@PathVariable String status, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);

			if (profileActive().equals("oracle")) {
				ReleaseReport release = null;
				if (CommonUtils.isNumeric(status)) {
					release = releaseService.findByIdReleaseReport(Integer.parseInt(status));
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
				model.addAttribute("status", new Status());
				model.addAttribute("statuses", statusService.list());
			} else if (profileActive().equals("postgres")) {
				PReleaseReport release = null;
				if (CommonUtils.isNumeric(status)) {
					release = preleaseService.findByIdReleaseReport(Integer.parseInt(status));
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
				model.addAttribute("status", new PStatus());
				model.addAttribute("statuses", pstatusService.list());
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/report/summaryReportRelease";
	}

	@RequestMapping(value = "/summaryReportRFC-{status}", method = RequestMethod.GET)
	public String summaryReportRFC(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		try {
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				RFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = rfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				Siges codeSiges = sigesService.findByKey("codeSiges", rfc.getCodeProyect());

				List<String> systemsImplicated = new ArrayList<String>();

				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Integer totalObjects = 0;
				List<ReleaseObjectClean> listObjects = new ArrayList<ReleaseObjectClean>();
				Set<Release_RFCFast> releases = rfc.getReleases();
				if (releases != null) {
					if (releases.size() != 0) {
						for (Release_RFCFast release : releases) {
							nameSystem = release.getSystem().getName();
							if (release.getObjects() != null) {
								totalObjects += release.getObjects().size();
								Set<ReleaseObjectClean> objects = release.getObjects();
								for (ReleaseObjectClean object : objects) {
									object.setNumRelease(release.getReleaseNumber());
									listObjects.add(object);
								}
							}

							for (String system : systemsImplicated) {
								if (system.equals(nameSystem)) {
									validate = false;
								}
							}
							if (validate) {
								systemsImplicated.add(nameSystem);
							}
							validate = true;
						}
					}

				}

				model.addAttribute("statuses", statusRFCService.findAll());
				model.addAttribute("systems", systems);
				model.addAttribute("impacts", impactService.list());
				model.addAttribute("typeChange", typeChangeService.findAll());
				model.addAttribute("priorities", priorityService.list());
				model.addAttribute("codeSiges", codeSiges);
				model.addAttribute("systemsImplicated", systemsImplicated);
				model.addAttribute("rfc", rfc);
				model.addAttribute("totalObjects", totalObjects);
				model.addAttribute("listObjects", listObjects);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				PRFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = prfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				PSiges codeSiges = psigesService.findByKey("codeSiges", rfc.getCodeProyect());

				List<String> systemsImplicated = new ArrayList<String>();

				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Integer totalObjects = 0;
				List<PReleaseObjectClean> listObjects = new ArrayList<PReleaseObjectClean>();
				Set<PRelease_RFCFast> releases = rfc.getReleases();
				if (releases != null) {
					if (releases.size() != 0) {
						for (PRelease_RFCFast release : releases) {
							nameSystem = release.getSystem().getName();
							if (release.getObjects() != null) {
								totalObjects += release.getObjects().size();
								Set<PReleaseObjectClean> objects = release.getObjects();
								for (PReleaseObjectClean object : objects) {
									object.setNumRelease(release.getReleaseNumber());
									listObjects.add(object);
								}
							}

							for (String system : systemsImplicated) {
								if (system.equals(nameSystem)) {
									validate = false;
								}
							}
							if (validate) {
								systemsImplicated.add(nameSystem);
							}
							validate = true;
						}
					}

				}

				model.addAttribute("statuses", pstatusRFCService.findAll());
				model.addAttribute("systems", systems);
				model.addAttribute("impacts", pimpactService.list());
				model.addAttribute("typeChange", ptypeChangeService.findAll());
				model.addAttribute("priorities", ppriorityService.list());
				model.addAttribute("codeSiges", codeSiges);
				model.addAttribute("systemsImplicated", systemsImplicated);
				model.addAttribute("rfc", rfc);
				model.addAttribute("totalObjects", totalObjects);
				model.addAttribute("listObjects", listObjects);
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen rfc." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/report/summaryReportRFC";

	}

	@RequestMapping(value = "/summaryReportRequest-{status}", method = RequestMethod.GET)
	public String summaryReportRequest(@PathVariable String status, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

	
		
		try {
			
			if (profileActive().equals("oracle")) {
				RequestBaseR1 requestEdit = new RequestBaseR1();
				if (status == null) {
					return "redirect:/";
				}

				requestEdit = requestBaseService.findByR1(Long.parseLong(status));

				if (requestEdit == null) {
					return "/plantilla/404";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
					model.addAttribute("request", requestEdit);
					RequestRM_P1_R1 requestR1 = requestServiceRm1.requestRm1(requestEdit.getId());
					model.addAttribute("requestR1", requestR1);
					model.addAttribute("statuses", statusRequestService.findAll());
					return "/report/request/sectionsEditR1/summaryRequest";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
					model.addAttribute("request", requestEdit);
					RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(requestEdit.getId());
					model.addAttribute("requestR2", requestR2);
					model.addAttribute("statuses", statusRequestService.findAll());
					model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR2/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
					model.addAttribute("request", requestEdit);
					RequestRM_P1_R3 requestR3 = requestServiceRm3.requestRm3(requestEdit.getId());
					model.addAttribute("requestR3", requestR3);
					model.addAttribute("statuses", statusRequestService.findAll());
					return "/report/request/sectionsEditR3/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
					model.addAttribute("request", requestEdit);
					List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(requestEdit.getId());
					model.addAttribute("listUsers", listUser);
					model.addAttribute("statuses", statusRequestService.findAll());
					model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR4/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
					model.addAttribute("request", requestEdit);
					RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(requestEdit.getId());
					model.addAttribute("requestR5", requestR5);
					model.addAttribute("statuses", statusRequestService.findAll());
					model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR5/summaryRequest";
				}
			} else if (profileActive().equals("postgres")) {
				PRequestBaseR1 requestEdit = new PRequestBaseR1();
				if (status == null) {
					return "redirect:/";
				}

				requestEdit = prequestBaseService.findByR1(Long.parseLong(status));

				if (requestEdit == null) {
					return "/plantilla/404";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R1")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R1 requestR1 = prequestServiceRm1.requestRm1(requestEdit.getId());
					model.addAttribute("requestR1", requestR1);
					model.addAttribute("statuses", pstatusRequestService.findAll());
					return "/report/request/sectionsEditR1/summaryRequest";
				}

				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R2")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R2 requestR2 = prequestServiceRm2.requestRm2(requestEdit.getId());
					model.addAttribute("requestR2", requestR2);
					model.addAttribute("statuses", pstatusRequestService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR2/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R3")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R3 requestR3 = prequestServiceRm3.requestRm3(requestEdit.getId());
					model.addAttribute("requestR3", requestR3);
					model.addAttribute("statuses", pstatusRequestService.findAll());
					return "/report/request/sectionsEditR3/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
					model.addAttribute("request", requestEdit);
					List<PRequestRM_P1_R4> listUser = prequestServiceRm4.listRequestRm4(requestEdit.getId());
					model.addAttribute("listUsers", listUser);
					model.addAttribute("statuses", pstatusRequestService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR4/summaryRequest";
				}
				if (requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
					model.addAttribute("request", requestEdit);
					PRequestRM_P1_R5 requestR5 = prequestServiceRm5.requestRm5(requestEdit.getId());
					model.addAttribute("requestR5", requestR5);
					model.addAttribute("statuses", pstatusRequestService.findAll());
					model.addAttribute("ambients", pambientService.list("", requestEdit.getSystemInfo().getCode()));
					return "/report/request/sectionsEditR5/summaryRequest";
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


	@RequestMapping(value = "/summaryRelease-{status}", method = RequestMethod.GET)
	public String summaryRelease(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			if (profileActive().equals("oracle")) {
				ReleaseSummary release = null;
				if (CommonUtils.isNumeric(status)) {
					release = releaseService.findById(Integer.parseInt(status));
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
				model.addAttribute("status", new Status());
				model.addAttribute("statuses", statusService.list());
			} else if (profileActive().equals("postgres")) {
				PReleaseSummary release = null;
				if (CommonUtils.isNumeric(status)) {
					release = preleaseService.findById(Integer.parseInt(status));
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
		return "/report/summaryRelease";
	}

	@RequestMapping(value = { "/downloadreportrequest" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRequest(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			Long typePetitionId;
			int systemId;
			int projectId;
			int typeDocument;

			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			if (request.getParameter("typePetitionId").equals("")) {
				typePetitionId = null;
			} else {
				typePetitionId = (long) Integer.parseInt(request.getParameter("typePetitionId"));

			}
			if (request.getParameter("typeDocument").equals("")) {
				typeDocument = 0;
			} else {
				typeDocument = Integer.parseInt(request.getParameter("typeDocument"));
			}


			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = null;
			if (typeDocument == 2) {
				resource = new ClassPathResource("reports" + File.separator + "RequestReportGeneral" + ".jrxml");
			} else {
				resource = new ClassPathResource("reports" + File.separator + "RequestReportGeneralExcel1" + ".jrxml");
			}
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			
			if (profileActive().equals("oracle")) {
				List<RequestReport> requests = requestBaseR1Service.listRequestReportFilter(projectId, systemId,
						typePetitionId, dateRange);
				List<RequestBaseTrackingToError> requestTotals = requestService.listByAllSystemError(dateRange, systemId);
				System system = systemService.findSystemById(systemId);
				Project project = projectService.findById(projectId);
				ReportTest report = new ReportTest();
				statusRequestService.findWithFilter();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);

				systemService.list();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;
				new ArrayList<ReportGhap>();
				List<ReportGhap> countReportRequest = new ArrayList<>();

				new CountReport();
				for (RequestReport requestOnly : requests) {
					if (requestOnly.getStatus().getName().equals("Completado")) {

						valueComplete++;

					}
					if (requestOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (requestOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!requestOnly.getStatus().getName().equals("Borrador")
							&& !requestOnly.getStatus().getName().equals("Solicitado")
							&& !requestOnly.getStatus().getName().equals("Produccion")
							&& !requestOnly.getStatus().getName().equals("Anulado")
							&& !requestOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("Solicitud completadas");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("Solicitud pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("Solicitud sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("Solicitud en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRequest.add(statusCountReportComplete);
				countReportRequest.add(statusCountReportPendent);
				countReportRequest.add(statusCountReportDraft);
				countReportRequest.add(statusCountReportProcess);
				report.setCountDataSource(countReportRequest);
				report.setListCountDataSource(countReportRequest);
				report.setRequestDataSource(requests);
				List<ReportTest> listReport = new ArrayList<>();

				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", requestTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = env.getProperty("fileStore.path");
				if (typeDocument == 2) {
					reportName = "SolicitudGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "SolicitudGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);

					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				Map<String, String> dataNew = new HashMap<String, String>();

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			} else if (profileActive().equals("postgres")) {
				List<PRequestReport> requests = prequestBaseR1Service.listRequestReportFilter(projectId, systemId,
						typePetitionId, dateRange);
				List<PRequestBaseTrackingToError> requestTotals = prequestService.listByAllSystemError(dateRange, systemId);
				PSystem system = psystemService.findSystemById(systemId);
				PProject project = pprojectService.findById(projectId);
				PReportTest report = new PReportTest();
				pstatusRequestService.findWithFilter();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);

				psystemService.list();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;
				new ArrayList<ReportGhap>();
				List<ReportGhap> countReportRequest = new ArrayList<>();

				new CountReport();
				for (PRequestReport requestOnly : requests) {
					if (requestOnly.getStatus().getName().equals("Completado")) {

						valueComplete++;

					}
					if (requestOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (requestOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!requestOnly.getStatus().getName().equals("Borrador")
							&& !requestOnly.getStatus().getName().equals("Solicitado")
							&& !requestOnly.getStatus().getName().equals("Produccion")
							&& !requestOnly.getStatus().getName().equals("Anulado")
							&& !requestOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("Solicitud completadas");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("Solicitud pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("Solicitud sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("Solicitud en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRequest.add(statusCountReportComplete);
				countReportRequest.add(statusCountReportPendent);
				countReportRequest.add(statusCountReportDraft);
				countReportRequest.add(statusCountReportProcess);
				report.setCountDataSource(countReportRequest);
				report.setListCountDataSource(countReportRequest);
				report.setRequestDataSource(requests);
				List<PReportTest> listReport = new ArrayList<>();

				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", requestTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = env.getProperty("fileStore.path");
				if (typeDocument == 2) {
					reportName = "SolicitudGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "SolicitudGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);

					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				Map<String, String> dataNew = new HashMap<String, String>();

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			}
			
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}

	@RequestMapping(value = { "/downloadreportrfc" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRFC(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			Long sigesId;
			int systemId;
			int projectId;
			int typeDocument;

			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			if (request.getParameter("sigesId").equals("")) {
				sigesId = null;
			} else {
				sigesId = (long) Integer.parseInt(request.getParameter("sigesId"));
			}

			if (request.getParameter("typeDocument").equals("")) {
				typeDocument = 0;
			} else {
				typeDocument = Integer.parseInt(request.getParameter("typeDocument"));
			}

			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = null;
			if (typeDocument == 2) {
				resource = new ClassPathResource("reports" + File.separator + "RFCReportGeneral" + ".jrxml");

			} else {
				resource = new ClassPathResource("reports" + File.separator + "RFCReportGeneralExcel1" + ".jrxml");
			}

			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				List<RFCReport> rfcs = rfcService.listRFCReportFilter(projectId, systemId, sigesId, dateRange);
				List<RFCTrackingToError> rfcTotals = rfcService.listByAllSystemError(dateRange, systemId);
				System system = systemService.findSystemById(systemId);
				Project project = projectService.findById(projectId);
				ReportTest report = new ReportTest();
				statusRFCService.findWithFilter();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);

				systemService.list();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;
				new ArrayList<ReportGhap>();
				List<ReportGhap> countReportRFC = new ArrayList<>();

				new CountReport();
				for (RFCReport rfcOnly : rfcs) {
					if (rfcOnly.getStatus().getName().equals("Completado")) {

						valueComplete++;

					}
					if (rfcOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (rfcOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!rfcOnly.getStatus().getName().equals("Borrador")
							&& !rfcOnly.getStatus().getName().equals("Solicitado")
							&& !rfcOnly.getStatus().getName().equals("Produccion")
							&& !rfcOnly.getStatus().getName().equals("Anulado")
							&& !rfcOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("RFC completados");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("RFC pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("RFC sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("RFC en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRFC.add(statusCountReportComplete);
				countReportRFC.add(statusCountReportPendent);
				countReportRFC.add(statusCountReportDraft);
				countReportRFC.add(statusCountReportProcess);
				report.setListCountDataSource(countReportRFC);
				report.setCountDataSource(countReportRFC);
				report.setRfcDataSource(rfcs);
				List<ReportTest> listReport = new ArrayList<>();

				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", rfcTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String reportName = "";
				String basePath = env.getProperty("fileStore.path");

				if (typeDocument == 2) {
					reportName = "RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);
					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				Map<String, String> dataNew = new HashMap<String, String>();

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			} else if (profileActive().equals("postgres")) {
				List<PRFCReport> rfcs = prfcService.listRFCReportFilter(projectId, systemId, sigesId, dateRange);
				List<PRFCTrackingToError> rfcTotals = prfcService.listByAllSystemError(dateRange, systemId);
				PSystem system = psystemService.findSystemById(systemId);
				PProject project = pprojectService.findById(projectId);
				PReportTest report = new PReportTest();
				pstatusRFCService.findWithFilter();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);

				psystemService.list();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;
				new ArrayList<ReportGhap>();
				List<ReportGhap> countReportRFC = new ArrayList<>();

				new CountReport();
				for (PRFCReport rfcOnly : rfcs) {
					if (rfcOnly.getStatus().getName().equals("Completado")) {

						valueComplete++;

					}
					if (rfcOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (rfcOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!rfcOnly.getStatus().getName().equals("Borrador")
							&& !rfcOnly.getStatus().getName().equals("Solicitado")
							&& !rfcOnly.getStatus().getName().equals("Produccion")
							&& !rfcOnly.getStatus().getName().equals("Anulado")
							&& !rfcOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("RFC completados");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("RFC pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("RFC sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("RFC en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRFC.add(statusCountReportComplete);
				countReportRFC.add(statusCountReportPendent);
				countReportRFC.add(statusCountReportDraft);
				countReportRFC.add(statusCountReportProcess);
				report.setListCountDataSource(countReportRFC);
				report.setCountDataSource(countReportRFC);
				report.setRfcDataSource(rfcs);
				List<PReportTest> listReport = new ArrayList<>();

				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", rfcTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String reportName = "";
				String basePath = env.getProperty("fileStore.path");

				if (typeDocument == 2) {
					reportName = "RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);
					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				Map<String, String> dataNew = new HashMap<String, String>();

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			}
			
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}


	@RequestMapping(value = { "/downloadreportrelease" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadreportGeneral(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			int systemId;
			int projectId;
			int typeDocument;
			if (!request.getParameter("systemId").equals("") && request.getParameter("systemId") != null) {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			} else {
				systemId = 0;
			}

			if (!request.getParameter("projectId").equals("") && request.getParameter("projectId") != null) {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			} else {
				projectId = 0;
			}
			if (request.getParameter("typeDocument").equals("")) {
				typeDocument = 0;
			} else {
				typeDocument = Integer.parseInt(request.getParameter("typeDocument"));
			}
			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = null;
			if (typeDocument == 2) {
				resource = new ClassPathResource("reports" + File.separator + "ReleaseReportGeneralNew" + ".jrxml");

			} else {
				resource = new ClassPathResource("reports" + File.separator + "ReleaseReportGeneralExcel" + ".jrxml");
			}

			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				List<ReleaseReportFast> releases = releaseService.listReleaseReportFilter(systemId, projectId, dateRange);
				List<ReleaseTrackingToError> releasesTotals = releaseService.listByAllSystemError(dateRange, systemId);
				new ArrayList<ReportGhap>();

				System system = systemService.findSystemById(systemId);
				Project project = projectService.findById(projectId);
				ReportTest report = new ReportTest();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);
				List<System> systems = new ArrayList<System>();
				if (system != null) {
					systems.add(system);
				} else {
					systems = systemService.list();
				}

				statusService.listWithOutAnul();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;

				report.setReleaseDataSource(releases);
				List<ReportTest> listReport = new ArrayList<>();
				List<ReportGhap> countReportRelease = new ArrayList<>();

				new CountReport();
				for (ReleaseReportFast releaseOnly : releases) {
					if (releaseOnly.getStatus().getName().equals("Produccion")) {

						valueComplete++;

					}
					if (releaseOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (releaseOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!releaseOnly.getStatus().getName().equals("Borrador")
							&& !releaseOnly.getStatus().getName().equals("Solicitado")
							&& !releaseOnly.getStatus().getName().equals("Produccion")
							&& !releaseOnly.getStatus().getName().equals("Anulado")
							&& !releaseOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("Releases completados");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("Releases pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("Releases sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("Releases en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRelease.add(statusCountReportComplete);
				countReportRelease.add(statusCountReportPendent);
				countReportRelease.add(statusCountReportDraft);
				countReportRelease.add(statusCountReportProcess);

				report.setCountDataSource(countReportRelease);
				report.setListCountDataSource(countReportRelease);
				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", releasesTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = env.getProperty("fileStore.path");

				if (typeDocument == 2) {
					reportName = "ReleaseGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "ReleaseGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);
					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("reporte.xlsx"));
				exporter.exportReport();
				Map<String, String> dataNew = new HashMap<String, String>();
				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			} else if (profileActive().equals("postgres")) {
				List<PReleaseReportFast> releases = preleaseService.listReleaseReportFilter(systemId, projectId, dateRange);
				List<PReleaseTrackingToError> releasesTotals = preleaseService.listByAllSystemError(dateRange, systemId);
				new ArrayList<ReportGhap>();

				PSystem system = psystemService.findSystemById(systemId);
				PProject project = pprojectService.findById(projectId);
				PReportTest report = new PReportTest();
				report.setSystem(system);
				report.setProject(project);
				report.setDateNew(dateRange);
				List<PSystem> systems = new ArrayList<PSystem>();
				if (system != null) {
					systems.add(system);
				} else {
					systems = psystemService.list();
				}

				statusService.listWithOutAnul();

				int valueComplete = 0;
				int valuePendent = 0;
				int valueDraft = 0;
				int valueProcess = 0;

				report.setReleaseDataSource(releases);
				List<PReportTest> listReport = new ArrayList<>();
				List<ReportGhap> countReportRelease = new ArrayList<>();

				new CountReport();
				for (PReleaseReportFast releaseOnly : releases) {
					if (releaseOnly.getStatus().getName().equals("Produccion")) {

						valueComplete++;

					}
					if (releaseOnly.getStatus().getName().equals("Solicitado")) {

						valuePendent++;

					}

					if (releaseOnly.getStatus().getName().equals("Borrador")) {

						valueDraft++;

					}
					if (!releaseOnly.getStatus().getName().equals("Borrador")
							&& !releaseOnly.getStatus().getName().equals("Solicitado")
							&& !releaseOnly.getStatus().getName().equals("Produccion")
							&& !releaseOnly.getStatus().getName().equals("Anulado")
							&& !releaseOnly.getStatus().getName().equals("Completado")) {

						valueProcess++;

					}

				}

				ReportGhap statusCountReportComplete = new ReportGhap();
				statusCountReportComplete.setLabel("Releases completados");
				statusCountReportComplete.setValue(valueComplete);

				ReportGhap statusCountReportPendent = new ReportGhap();
				statusCountReportPendent.setLabel("Releases pendientes");
				statusCountReportPendent.setValue(valuePendent);

				ReportGhap statusCountReportDraft = new ReportGhap();
				statusCountReportDraft.setLabel("Releases sin iniciar");
				statusCountReportDraft.setValue(valueDraft);

				ReportGhap statusCountReportProcess = new ReportGhap();
				statusCountReportProcess.setLabel("Releases en proceso");
				statusCountReportProcess.setValue(valueProcess);
				countReportRelease.add(statusCountReportComplete);
				countReportRelease.add(statusCountReportPendent);
				countReportRelease.add(statusCountReportDraft);
				countReportRelease.add(statusCountReportProcess);

				report.setCountDataSource(countReportRelease);
				report.setListCountDataSource(countReportRelease);
				listReport.add(report);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());
				parameters.put("totalRequest", releasesTotals.size());
				parameters.put("pendents", valuePendent);
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = env.getProperty("fileStore.path");

				if (typeDocument == 2) {
					reportName = "ReleaseGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);

				} else {
					reportName = "ReleaseGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setDetectCellType(true);

					configuration.setCollapseRowSpan(true);
					configuration.setIgnoreCellBorder(true);
					configuration.setWhitePageBackground(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					configuration.setOnePagePerSheet(true);
					configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
					exporter.setConfiguration(configuration);
					exporter.setExporterOutput(
							new SimpleOutputStreamExporterOutput(new FileOutputStream(basePath + reportName)));
					exporter.exportReport();
				}

				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("reporte.xlsx"));
				exporter.exportReport();
				Map<String, String> dataNew = new HashMap<String, String>();
				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			}
			
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}

	@RequestMapping(value = { "/downloadReportGeneral" }, method = RequestMethod.GET)
	public void downloadGeneralReport(HttpServletResponse response, Locale locale, Model model) {
		try {

			ClassPathResource resource = new ClassPathResource(
					"reports" + File.separator + "ReleaseReportGeneral" + ".jrxml");
			InputStream inputStream = resource.getInputStream();
			// Compile the Jasper report from .jrxml to .japser
			// InputStream jasperReport = reportManager.export(fileName);
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				List<ReleaseReport> releases = releaseService.listReleaseReport();
				// releases.add(release);
				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-General";
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName);

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else if (profileActive().equals("postgres")) {
				List<PReleaseReport> releases = preleaseService.listReleaseReport();
				// releases.add(release);
				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-General";
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName);

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
		}

	}

	
	public void downloadReportRelease(HttpServletResponse response, @PathVariable Integer id, Locale locale,
			Model model) {

		try {
			if (profileActive().equals("oracle")) {
				ReleaseReport release = null;
				release = releaseService.findByIdReleaseReport(id);
				ReportFile reportFile = reportFileService.findReportFileById(id);
				File file = new File(reportFile.getPath());
				ImageTree image = new ImageTree();
				image.setUploadFile(file);

				String imageEncode = Base64.getEncoder().encodeToString((FileUtils.readFileToByteArray(file)));
				new String(imageEncode);
				byte[] decodedBytes = DatatypeConverter
						.parseBase64Binary(imageEncode.replaceAll("data:image/.+;base64,", ""));
				InputStream targetStream = new ByteArrayInputStream(decodedBytes);
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "ReleaseReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<ReleaseReport> releases = new ArrayList<>();

				releases.add(release);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				parameters.put("treeImage", targetStream);
				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + release.getReleaseNumber() + ".pdf";
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName);

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else if (profileActive().equals("postgres")) {
				PReleaseReport release = null;
				release = preleaseService.findByIdReleaseReport(id);
				PReportFile reportFile = preportFileService.findReportFileById(id);
				File file = new File(reportFile.getPath());
				ImageTree image = new ImageTree();
				image.setUploadFile(file);

				String imageEncode = Base64.getEncoder().encodeToString((FileUtils.readFileToByteArray(file)));
				new String(imageEncode);
				byte[] decodedBytes = DatatypeConverter
						.parseBase64Binary(imageEncode.replaceAll("data:image/.+;base64,", ""));
				InputStream targetStream = new ByteArrayInputStream(decodedBytes);
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "ReleaseReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<PReleaseReport> releases = new ArrayList<>();

				releases.add(release);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				parameters.put("treeImage", targetStream);
				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + release.getReleaseNumber() + ".pdf";
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName);

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}


		} catch (Exception e) {
			Sentry.capture(e, "report");
			e.printStackTrace();
		
		}
	}
	
	
	@RequestMapping(value = { "/downloadRFC/{id}" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadReportRFC(HttpServletResponse response, @PathVariable Long id,
			Locale locale, Model model) {
		
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				RFCReportComplete rfc = null;
				rfc = rfcService.findByIdRFCReport(id);

				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "RFCReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				
				List<RFCReportComplete> rfcs = new ArrayList<>();
				List<ReleaseObjectClean> listObjects = new ArrayList<ReleaseObjectClean>();
				Set<Release_RFCFast> releases = rfc.getReleases();
				List<String> systemsImplicated = new ArrayList<String>();
				Siges codeSiges = sigesService.findByKey("codeSiges", rfc.getCodeProyect());
				rfc.setSigesName(codeSiges.getCodeSiges());
				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Integer totalObjects = 0;
				List<ReportBlank> listReleases = new ArrayList<ReportBlank>();
				List<ReportBlank> listObject = new ArrayList<ReportBlank>();
				if (releases != null) {
					if (releases.size() != 0) {
						for (Release_RFCFast release : releases) {
							nameSystem = release.getSystem().getName();
							if (release.getObjects() != null) {
								totalObjects += release.getObjects().size();
								ReportBlank releaseBlank = new ReportBlank();
								releaseBlank.setField1(release.getReleaseNumber());
								releaseBlank.setField2(release.getDescription());
								releaseBlank.setField3("" + release.getObjects().size());
								listReleases.add(releaseBlank);
								Set<ReleaseObjectClean> objects = release.getObjects();
								for (ReleaseObjectClean object : objects) {
									object.setNumRelease(release.getReleaseNumber());
									ReportBlank objectBlank = new ReportBlank();
									objectBlank.setField1(release.getReleaseNumber());
									objectBlank.setField2(object.getName());
									objectBlank.setField3(object.getDescription());

									listObject.add(objectBlank);

									listObjects.add(object);
								}
							}

							for (String system : systemsImplicated) {
								if (system.equals(nameSystem)) {
									validate = false;
								}
							}
							if (validate) {
								systemsImplicated.add(nameSystem);
							}
							validate = true;
						}
					}

				}

				rfc.setReleasesDataSource(listReleases);
				rfc.setObjectsDataSource(listObject);
				rfcs.add(rfc);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(rfcs);

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + rfc.getRfcNumber() + ".pdf";

				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String basePath = env.getProperty("fileStore.path");
				JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			} else if (profileActive().equals("postgres")) {
				PRFCReportComplete rfc = null;
				rfc = prfcService.findByIdRFCReport(id);

				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "RFCReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				
				List<PRFCReportComplete> rfcs = new ArrayList<>();
				List<PReleaseObjectClean> listObjects = new ArrayList<PReleaseObjectClean>();
				Set<PRelease_RFCFast> releases = rfc.getReleases();
				List<String> systemsImplicated = new ArrayList<String>();
				Siges codeSiges = sigesService.findByKey("codeSiges", rfc.getCodeProyect());
				rfc.setSigesName(codeSiges.getCodeSiges());
				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Integer totalObjects = 0;
				List<ReportBlank> listReleases = new ArrayList<ReportBlank>();
				List<ReportBlank> listObject = new ArrayList<ReportBlank>();
				if (releases != null) {
					if (releases.size() != 0) {
						for (PRelease_RFCFast release : releases) {
							nameSystem = release.getSystem().getName();
							if (release.getObjects() != null) {
								totalObjects += release.getObjects().size();
								ReportBlank releaseBlank = new ReportBlank();
								releaseBlank.setField1(release.getReleaseNumber());
								releaseBlank.setField2(release.getDescription());
								releaseBlank.setField3("" + release.getObjects().size());
								listReleases.add(releaseBlank);
								Set<PReleaseObjectClean> objects = release.getObjects();
								for (PReleaseObjectClean object : objects) {
									object.setNumRelease(release.getReleaseNumber());
									ReportBlank objectBlank = new ReportBlank();
									objectBlank.setField1(release.getReleaseNumber());
									objectBlank.setField2(object.getName());
									objectBlank.setField3(object.getDescription());

									listObject.add(objectBlank);

									listObjects.add(object);
								}
							}

							for (String system : systemsImplicated) {
								if (system.equals(nameSystem)) {
									validate = false;
								}
							}
							if (validate) {
								systemsImplicated.add(nameSystem);
							}
							validate = true;
						}
					}

				}

				rfc.setReleasesDataSource(listReleases);
				rfc.setObjectsDataSource(listObject);
				rfcs.add(rfc);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(rfcs);

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + rfc.getRfcNumber() + ".pdf";

				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String basePath = env.getProperty("fileStore.path");
				JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "report");
			Map<String, String> dataNew = new HashMap<String, String>();

			dataNew.put("error", e.getMessage());
			res.setObj(dataNew);
			e.printStackTrace();
		}
		return res;

	}

	@RequestMapping(value = { "/downloadRequest/{id}" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadReportRequest(HttpServletResponse response, @PathVariable Long id,
			Locale locale, Model model) {
	
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				RequestReport request = null;
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "RequestReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<RequestReport> requests = new ArrayList<>();

				request = requestBaseService.findByReport(id);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				if (request.getTypePetition().getCode().equals("RM-P1-R2")) {

					RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(request.getId());
					request.setTypePetitionNum(2);
					parameters.put("ambient", requestR2.getAmbient());
					parameters.put("typeChange", "");
					parameters.put("changeService", "");
					parameters.put("justify", "");
					parameters.put("typeService", requestR2.getTypeService());
					parameters.put("hierarchy", requestR2.getHierarchy());
					parameters.put("requeriments", requestR2.getRequeriments());

				}

				if (request.getTypePetition().getCode().equals("RM-P1-R4")) {

					List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(request.getId());
					request.setTypePetitionNum(4);
					request.setR4DataSource(listUser);
					parameters.put("ambient", "");
					parameters.put("typeChange", "");
					parameters.put("changeService", "");
					parameters.put("justify", "");
					parameters.put("typeService", "");
					parameters.put("hierarchy", "");
					parameters.put("requeriments", "");
				}
				if (request.getTypePetition().getCode().equals("RM-P1-R5")) {

					RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(request.getId());
					request.setTypePetitionNum(5);
					parameters.put("ambient", "");
					parameters.put("typeChange", requestR5.getTypeChange());
					parameters.put("changeService", requestR5.getChangeService());
					parameters.put("justify", requestR5.getJustify());
					parameters.put("typeService", "");
					parameters.put("hierarchy", "");
					parameters.put("requeriments", "");
				}

				requests.add(request);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(requests);

				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + request.getNumRequest() + ".pdf";

				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String basePath = env.getProperty("fileStore.path");
				JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			} else if (profileActive().equals("postgres")) {
				PRequestReport request = null;
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "RequestReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<PRequestReport> requests = new ArrayList<>();

				request = prequestBaseService.findByReport(id);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();

				if (request.getTypePetition().getCode().equals("RM-P1-R2")) {

					PRequestRM_P1_R2 requestR2 = prequestServiceRm2.requestRm2(request.getId());
					request.setTypePetitionNum(2);
					parameters.put("ambient", requestR2.getAmbient());
					parameters.put("typeChange", "");
					parameters.put("changeService", "");
					parameters.put("justify", "");
					parameters.put("typeService", requestR2.getTypeService());
					parameters.put("hierarchy", requestR2.getHierarchy());
					parameters.put("requeriments", requestR2.getRequeriments());

				}

				if (request.getTypePetition().getCode().equals("RM-P1-R4")) {

					List<PRequestRM_P1_R4> listUser = prequestServiceRm4.listRequestRm4(request.getId());
					request.setTypePetitionNum(4);
					request.setR4DataSource(listUser);
					parameters.put("ambient", "");
					parameters.put("typeChange", "");
					parameters.put("changeService", "");
					parameters.put("justify", "");
					parameters.put("typeService", "");
					parameters.put("hierarchy", "");
					parameters.put("requeriments", "");
				}
				if (request.getTypePetition().getCode().equals("RM-P1-R5")) {

					PRequestRM_P1_R5 requestR5 = prequestServiceRm5.requestRm5(request.getId());
					request.setTypePetitionNum(5);
					parameters.put("ambient", "");
					parameters.put("typeChange", requestR5.getTypeChange());
					parameters.put("changeService", requestR5.getChangeService());
					parameters.put("justify", requestR5.getJustify());
					parameters.put("typeService", "");
					parameters.put("hierarchy", "");
					parameters.put("requeriments", "");
				}

				requests.add(request);

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(requests);

				// String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				// JasperExportManager.exportReportToPdfFile(jasperPrint, basePath +
				// "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName = "REPORTE-" + request.getNumRequest() + ".pdf";

				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource("images" + File.separator + "logo" + ".png");
				parameters.put("logo", images.getInputStream());

				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

				String basePath = env.getProperty("fileStore.path");
				JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
				File file = new File(basePath + reportName);
				byte[] encoded = org.apache.commons.net.util.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				dataNew.put("file", new String(encoded, StandardCharsets.US_ASCII));
				dataNew.put("ContentType", mimeType);
				dataNew.put("name", reportName);
				res.setObj(dataNew);
			}
		} catch (Exception e) {
			Sentry.capture(e, "report");
			Map<String, String> dataNew = new HashMap<String, String>();

			dataNew.put("error", e.getMessage());
			res.setObj(dataNew);
			e.printStackTrace();
		}
		return res;

	}

}
