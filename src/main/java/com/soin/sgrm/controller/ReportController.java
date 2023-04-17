package com.soin.sgrm.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.ErrorRFCReport;
import com.soin.sgrm.model.ErrorTypeGraph;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.ImageTree;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.ReportFile;
import com.soin.sgrm.model.ReportTest;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.ReportFileService;
import com.soin.sgrm.service.ReportService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.SystemService;
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
	public String indexReport(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRFC(request, userLogin);
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			List<Priority> priorities = priorityService.list();
			List<StatusRFC> statuses = statusRFCService.findAll();
			List<Impact> impacts = impactService.list();
			model.addAttribute("priorities", priorities);
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
	
	@RequestMapping(path = "/listRFC", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getListRfc(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			int priorityId;
			int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("priorityId").equals("")) {
				priorityId = 0;
			} else {
				priorityId = Integer.parseInt(request.getParameter("priorityId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			String dateRange = request.getParameter("dateRange");

			return rfcService.findAllReportRFC(name, sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,
					priorityId, systemId);
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
	
	@RequestMapping(value = { "/downloadreportrelease" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRFC(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			
			int statusId;
			int systemId;
			int projectId;
			
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
			String dateRange = request.getParameter("dateRange");
			ClassPathResource resource = new ClassPathResource(
					"reports" + File.separator + "ReleaseReportGeneralNew" + ".jrxml");
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

			int valueError = 0;
			int valueRequest = 0;


			report.setReleaseDataSource(releases);
			List<ReportTest> listReport = new ArrayList<>();
			
			listReport.add(report);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listReport);
			Map<String, Object> parameters = new HashMap<>();


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
	public void changeProject(HttpServletResponse response,@PathVariable Integer id, Locale locale, Model model) {
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
	
}
