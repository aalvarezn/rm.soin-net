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
import javax.annotation.Resource;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.CountReport;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.ErrorRFCReport;
import com.soin.sgrm.model.ErrorTypeGraph;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.ImageTree;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectClean;
import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.ReportBlank;
import com.soin.sgrm.model.ReportFile;
import com.soin.sgrm.model.ReportTest;
import com.soin.sgrm.model.RequestBaseR1;
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
import com.soin.sgrm.model.User;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.ReportFileService;
import com.soin.sgrm.service.ReportService;
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
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.XlsxReportConfiguration;

@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	private StatusService statusService;
	@Autowired
	private StatusRFCService statusRFCService;
	@Autowired
	private PriorityService priorityService;
	@Autowired
	private ImpactService impactService;
 	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	@Autowired
	private DocTemplateService docsTemplateService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportFileService reportFileService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private Environment env;
	@Autowired
	private RFCService rfcService;
	@Autowired
	private RequestBaseR1Service requestBaseR1Service;
	@Autowired
	private TypePetitionService typePetitionService;
	@Autowired
	private SigesService sigesService;
	@Autowired
	private TypeChangeService typeChangeService;
	@Autowired
	private RequestBaseService requestBaseService;
	@Autowired
	private RequestRM_P1_R1Service requestServiceRm1;
	@Autowired
	private RequestRM_P1_R2Service requestServiceRm2;
	@Autowired
	private RequestRM_P1_R3Service requestServiceRm3;
	@Autowired
	private RequestRM_P1_R4Service requestServiceRm4;
	@Autowired
	private RequestRM_P1_R5Service requestServiceRm5;
	@Autowired
	private StatusRequestService statusRequestService;
	@Autowired
	private AmbientService ambientService;
	
	
	@RequestMapping(value = "/releases", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			List<System> systems = systemService.list();
			List<Project> projects = projectService.listAll();
			loadCountsRelease(request, name);
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systems);
			model.addAttribute("status", new Status());
			model.addAttribute("projects", projects);
			model.addAttribute("statuses", statusService.list());
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
			List<System> systems = systemService.list();
			List<Project> projects = projectService.listAll();
			List<StatusRFC> statuses = statusRFCService.findAll();
			List<Impact> impacts = impactService.list();
			model.addAttribute("projects", projects);
			model.addAttribute("impacts", impacts);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
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
			List<System> systems = systemService.list();
			List<TypePetition> typePetitions = typePetitionService.findAll();
			List<Project> projects = projectService.listAll();
			model.addAttribute("typePetitions", typePetitions);
			model.addAttribute("projects", projects);
			model.addAttribute("systems", systems);
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
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
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

			if (systemId == 0) {

				if (projectId == 0) {
					return requestBaseR1Service.findAllReportRequest( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,typePetitionId);
				} else {
					List<System> systems = new ArrayList<>();

					systems = systemService.getSystemByProject(projectId);
					List<Integer>systemsId=new ArrayList<Integer>();
					for(System system:systems) {
						systemsId.add(system.getId());
					}
			
					return requestBaseR1Service.findAllReportRequest( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemsId,typePetitionId);
				}

			} else {
	
				return requestBaseR1Service.findAllReportRequest( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,typePetitionId);
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
			return releaseService.listByAllWithObjects(name, sEcho, iDisplayStart, iDisplayLength, sSearch, Constant.FILTRED,
					dateRange, systemId, statusId,projectId);
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	
	public void loadCountsRelease(HttpServletRequest request, String name) {
		Map<String, Integer> systemC = new HashMap<String, Integer>();
		systemC.put("draft", releaseService.countByType(name, "Borrador", 3, null));
		systemC.put("requested", releaseService.countByType(name, "Solicitado", 3, null));
		systemC.put("completed", releaseService.countByType(name, "Completado", 3, null));
		systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
		request.setAttribute("systemC", systemC);
	}
	
	public void loadCountsRequest(HttpServletRequest request, Integer id) {
		Map<String, Integer> rfcC = new HashMap<String, Integer>();
		rfcC.put("draft", requestBaseR1Service.countByType(id, "Borrador", 2, null));
		rfcC.put("requested", requestBaseR1Service.countByType(id, "Solicitado", 2, null));
		rfcC.put("completed", requestBaseR1Service.countByType(id, "Completado", 2, null));
		rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
		request.setAttribute("rfcC", rfcC);

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

			if (systemId == 0) {

				if (projectId == 0) {
					return rfcService.findAllReportRFC( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,sigesId);
				} else {
					List<System> systems = new ArrayList<>();

					systems = systemService.getSystemByProject(projectId);
					List<Integer>systemsId=new ArrayList<Integer>();
					for(System system:systems) {
						systemsId.add(system.getId());
					}
			
					return rfcService.findAllReportRFC( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemsId,sigesId);
				}

			} else {
	
				return rfcService.findAllReportRFC( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,sigesId);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	
	public void loadCountsRFC(HttpServletRequest request, Integer id) {
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", rfcService.countByType(id, "Borrador", 1, null));
		userC.put("requested", rfcService.countByType(id, "Solicitado", 1, null));
		userC.put("completed", rfcService.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);

	}
	
	@RequestMapping(value = "/summaryReportRelease-{status}", method = RequestMethod.GET)
	public String summaryReportRelease(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
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


		List<System> systems = systemService.list();
		try {
		
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
	public String summaryReportRequest(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		
		List<System> systems = systemService.list();  
		RequestBaseR1 requestEdit = new RequestBaseR1();
		try {
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
		
			
			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = new ClassPathResource(
					"reports" + File.separator + "RequestReportGeneral" + ".jrxml");
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			List<RequestReport> requests = requestBaseR1Service.listRequestReportFilter(projectId,systemId,typePetitionId,dateRange);
			
			RequestReport requestNew= requests.get(1);
			System system = systemService.findSystemById(systemId);
			Project project = projectService.findById(projectId);
			ReportTest report=new ReportTest();
			report.setSystem(system);
			report.setProject(project);
			report.setDateNew(dateRange);

			Integer totalRFC = 0;
			List<System> systems = systemService.list();

			int valueError = 0;
			int valueRequest = 0;
			List<CountReport> countReportRFC=new ArrayList<>();
			for(System systemOnly: systems) {
				CountReport countReport=new CountReport();
				valueRequest=0;
				for(RequestReport requestOnly : requests) {
					if(requestOnly.getStatus().getName()=="Completado") {
						if(requestOnly.getSystem().getId()==systemOnly.getId()) {
							valueRequest++;
						}
					}
				}
				if(valueRequest>0) {
					countReport.setLabel(systemOnly.getCode());
					countReport.setValue1(valueRequest);
					countReportRFC.add(countReport);
				}
				
			}
			report.setCountDataSource(countReportRFC);

			report.setRequestDataSource(requests);
			List<ReportTest> listReport = new ArrayList<>();
			
			listReport.add(report);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
			Map<String, Object> parameters = new HashMap<>();

			ClassPathResource images = new ClassPathResource(
					"images" + File.separator + "logo" + ".png");
			parameters.put("logo",images.getInputStream());
			JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

			String reportName = "SolicitudGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
			String basePath = env.getProperty("fileStore.path");
			JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
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
				typeDocument =  Integer.parseInt(request.getParameter("typeDocument"));
			}
		
			
			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource=null;
			if(typeDocument==2) {
				 resource = new ClassPathResource(
							"reports" + File.separator + "RFCReportGeneral" + ".jrxml");
			
			}else {
				 resource = new ClassPathResource(
							"reports" + File.separator + "RFCReportGeneralExcel" + ".jrxml");
			}
			
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			List<RFCReport> rfcs = rfcService.listRFCReportFilter(projectId,systemId,sigesId,dateRange);
			
		
			System system = systemService.findSystemById(systemId);
			Project project = projectService.findById(projectId);
			ReportTest report=new ReportTest();
			report.setSystem(system);
			report.setProject(project);
			report.setDateNew(dateRange);

			Integer totalRFC = 0;
			List<System> systems = systemService.list();

			int valueError = 0;
			int valueRFC = 0;
			List<CountReport> countReportRFC=new ArrayList<>();
			for(System systemOnly: systems) {
				CountReport countReport=new CountReport();
				valueRFC=0;
				for(RFCReport rfcOnly : rfcs) {
					if(rfcOnly.getStatus().getName().equals("Completado")) {
						if(rfcOnly.getSystem().getId()==systemOnly.getId()) {
							valueRFC++;
						}
					}
				}
				if(valueRFC>0) {
					countReport.setLabel(systemOnly.getCode());
					countReport.setValue1(valueRFC);
					countReportRFC.add(countReport);
				}
				
			}
			report.setCountDataSource(countReportRFC);
			report.setRfcDataSource(rfcs);
			List<ReportTest> listReport = new ArrayList<>();
			
			listReport.add(report);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
			Map<String, Object> parameters = new HashMap<>();

			ClassPathResource images = new ClassPathResource(
					"images" + File.separator + "logo" + ".png");
			parameters.put("logo",images.getInputStream());
			JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

			String reportName = "";
			String basePath = env.getProperty("fileStore.path");
			
			if(typeDocument==2) {
				reportName= "RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
				
			}else {
				reportName="RFCGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
				JRXlsxExporter exporter = new JRXlsxExporter();
		        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

		        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
		        configuration.setDetectCellType(true);
		        configuration.setCollapseRowSpan(true);
		        configuration.setIgnoreCellBorder(true);
		        configuration.setWhitePageBackground(true);
		        configuration.setRemoveEmptySpaceBetweenColumns(true);
		        exporter.setConfiguration(configuration);
		        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream( basePath + reportName)));	
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

			
			int statusId;
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
				typeDocument =  Integer.parseInt(request.getParameter("typeDocument"));
			}
			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = null;
			if(typeDocument==2) {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ReleaseReportGeneralNew" + ".jrxml");
			
			}else {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ReleaseReportGeneralNewExcel" + ".jrxml");
			}
					
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			List<ReleaseReportFast> releases = releaseService.listReleaseReportFilter(systemId,projectId,dateRange);
			
			
			ReleaseReportFast release= releases.get(1);
			System system = systemService.findSystemById(systemId);
			Project project = projectService.findById(projectId);
			ReportTest report=new ReportTest();
			report.setSystem(system);
			report.setProject(project);
			report.setDateNew(dateRange);

			Integer totalRFC = 0;
			List<System> systems = systemService.list();

			
			int valueRelease= 0;

			
			report.setReleaseDataSource(releases);
			List<ReportTest> listReport = new ArrayList<>();
			List<CountReport> countReportRelease=new ArrayList<>();
			for(System systemOnly: systems) {
				CountReport countReport=new CountReport();
				valueRelease=0;
				for(ReleaseReportFast releaseOnly : releases) {
					if(releaseOnly.getStatus().getName().equals("Produccion")) {
						if(releaseOnly.getSystem().getId()==systemOnly.getId()) {
							valueRelease++;
						}
					}
				}
				if(valueRelease>0) {
					countReport.setLabel(systemOnly.getCode());
					countReport.setValue1(valueRelease);
					countReportRelease.add(countReport);
				}
				
			}
			report.setCountDataSource(countReportRelease);
			listReport.add(report);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
			Map<String, Object> parameters = new HashMap<>();
			ClassPathResource images = new ClassPathResource(
					"images" + File.separator + "logo" + ".png");
			parameters.put("logo",images.getInputStream());

			JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

			String reportName = "ReleaseGeneral-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
			String basePath = env.getProperty("fileStore.path");
			
			
			JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
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
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}
	
	@RequestMapping(value = { "/downloadReportGeneral" }, method = RequestMethod.GET)
	public void downloadGeneralReport(HttpServletResponse response, Locale locale, Model model) {
		ReleaseReport release = null;
		try {
			
				String fileName = "ReleaseReport";
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "ReleaseReportGeneral" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<ReleaseReport> releases = releaseService.listReleaseReport();
			//	releases.add(release);
				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				//String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				//JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName= "REPORTE-General";
				response.setHeader("Content-Disposition", "attachment;filename=" +reportName );

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				

				
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
		}

		
	}
	
	
	
	@RequestMapping(value = { "/downloadRelease/{id}" }, method = RequestMethod.GET)
	public void downloadReportRelease(HttpServletResponse response,@PathVariable Integer id, Locale locale, Model model) {
		ReleaseReport release = null;
		try {
			release= releaseService.findByIdReleaseReport(id);
			ReportFile reportFile = reportFileService.findReportFileById(id);
			File file = new File(reportFile.getPath());
			ImageTree image=new ImageTree();
			image.setUploadFile(file);
			
				String imageEncode = Base64.getEncoder().encodeToString(( FileUtils.readFileToByteArray(file)));
		        String result = new String(imageEncode);
				String fileName = "ReleaseReport";
				 byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageEncode.replaceAll("data:image/.+;base64,", ""));
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
				
			

				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				//String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				//JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName= "REPORTE-"+release.getReleaseNumber()+".pdf";
				response.setHeader("Content-Disposition", "attachment;filename=" +reportName );

				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
						
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
		}

		
	}
	
	@RequestMapping(value = { "/downloadRFC/{id}" }, method = RequestMethod.GET)
	public  @ResponseBody JsonResponse downloadReportRFC(HttpServletResponse response,@PathVariable Long id, Locale locale, Model model) {
		RFCReportComplete rfc = null;
		JsonResponse res = new JsonResponse();
		try {
			rfc= rfcService.findByIdRFCReport(id);
	
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
				List<ReportBlank> listReleases= new ArrayList<ReportBlank>();
				List<ReportBlank> listObject= new ArrayList<ReportBlank>();
				if (releases != null) {
					if (releases.size() != 0) {
						for (Release_RFCFast release : releases) {
							nameSystem = release.getSystem().getName();
							if (release.getObjects() != null) {
								totalObjects += release.getObjects().size();
								ReportBlank releaseBlank= new ReportBlank();
								releaseBlank.setField1(release.getReleaseNumber());
								releaseBlank.setField2(release.getDescription());
								releaseBlank.setField3(""+release.getObjects().size());
								listReleases.add(releaseBlank);
								Set<ReleaseObjectClean> objects = release.getObjects();
								for (ReleaseObjectClean object : objects) {
									object.setNumRelease(release.getReleaseNumber());
									ReportBlank objectBlank= new ReportBlank();
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

	
				
				//String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				//JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName= "REPORTE-"+rfc.getRfcNumber()+".pdf";
		
				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				
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
	public  @ResponseBody JsonResponse downloadReportRequest(HttpServletResponse response,@PathVariable Long id, Locale locale, Model model) {
		RequestReport request = null;
		JsonResponse res = new JsonResponse();
		try {
		
	
				ClassPathResource resource = new ClassPathResource("reports" + File.separator + "RequestReport" + ".jrxml");
				InputStream inputStream = resource.getInputStream();
				// Compile the Jasper report from .jrxml to .japser
				// InputStream jasperReport = reportManager.export(fileName);
				JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
				List<RequestReport> requests = new ArrayList<>();
				

				String nameSystem = "";
				boolean validate = true;
				Integer totalObjects = 0;
				request = requestBaseService.findByReport(id);
				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
			

				


				if (request.getTypePetition().getCode().equals("RM-P1-R2")) {
					
					RequestRM_P1_R2 requestR2 = requestServiceRm2.requestRm2(request.getId());
					request.setTypePetitionNum(2);
					parameters.put("ambient",requestR2.getAmbient());
					parameters.put("typeChange","");
					parameters.put("changeService","");
					parameters.put("justify","");
					parameters.put("typeService",requestR2.getTypeService());
					parameters.put("hierarchy",requestR2.getHierarchy());
					parameters.put("requeriments",requestR2.getRequeriments());
					
				}
				
				if (request.getTypePetition().getCode().equals("RM-P1-R4")) {
					
					List<RequestRM_P1_R4> listUser = requestServiceRm4.listRequestRm4(request.getId());
					request.setTypePetitionNum(4);
					request.setR4DataSource(listUser);
					parameters.put("ambient","");
					parameters.put("typeChange","");
					parameters.put("changeService","");
					parameters.put("justify","");
					parameters.put("typeService","");
					parameters.put("hierarchy","");
					parameters.put("requeriments","");
				}
				if (request.getTypePetition().getCode().equals("RM-P1-R5")) {
					
					RequestRM_P1_R5 requestR5 = requestServiceRm5.requestRm5(request.getId());
					request.setTypePetitionNum(5);
					parameters.put("ambient","");
					parameters.put("typeChange",requestR5.getTypeChange());
					parameters.put("changeService",requestR5.getChangeService());
					parameters.put("justify",requestR5.getJustify());
					parameters.put("typeService","");
					parameters.put("hierarchy","");
					parameters.put("requeriments","");
				}
	

				requests.add(request);
			

				// Get your data source
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(requests);
				

				

	
				
				//String basePath = env.getProperty("fileStore.path");
				// Export the report to a PDF file
				//JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "/Emp-Rpt.pdf");
				response.setContentType("application/pdf");
				String reportName= "REPORTE-"+request.getNumRequest()+".pdf";
		
				Map<String, String> dataNew = new HashMap<String, String>();

				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				
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
