package com.soin.sgrm.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ErrorRFCReport;
import com.soin.sgrm.model.ErrorReleaseReport;
import com.soin.sgrm.model.ErrorRequestReport;
import com.soin.sgrm.model.ErrorTypeGraph;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.RequestBaseTrackingToError;
import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PErrorRFCReport;
import com.soin.sgrm.model.pos.PErrorReleaseReport;
import com.soin.sgrm.model.pos.PErrorRequestReport;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PErrors_Release;
import com.soin.sgrm.model.pos.PErrors_Requests;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PRFCError;
import com.soin.sgrm.model.pos.PRFCTrackingToError;
import com.soin.sgrm.model.pos.PReleaseError;
import com.soin.sgrm.model.pos.PReleaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestError;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PTypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ErrorRequestService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCErrorService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestErrorService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.service.pos.PErrorRFCService;
import com.soin.sgrm.service.pos.PErrorReleaseService;
import com.soin.sgrm.service.pos.PErrorRequestService;
import com.soin.sgrm.service.pos.PImpactService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PRFCErrorService;
import com.soin.sgrm.service.pos.PRFCService;
import com.soin.sgrm.service.pos.PReleaseErrorService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PRequestBaseService;
import com.soin.sgrm.service.pos.PRequestErrorService;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypePetitionService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Controller
@RequestMapping("/management/error")
public class ErrorsManagementController extends BaseController {
	public static final Logger logger = Logger.getLogger(ErrorsManagementController.class);
	@Autowired
	RFCService rfcService;
	@Autowired
	StatusRFCService statusService;

	@Autowired
	StatusService statusReleaseService;

	@Autowired
	ReleaseService releaseService;

	@Autowired
	RequestBaseService requestService;

	@Autowired
	SystemService systemService;

	@Autowired
	PriorityService priorityService;

	@Autowired
	ProjectService projectService;

	@Autowired
	ImpactService impactService;

	@Autowired
	ErrorReleaseService errorReleaseService;

	@Autowired
	ErrorRFCService errorRFCService;

	@Autowired
	ErrorRequestService errorRequestService;

	@Autowired
	ReleaseErrorService releaseErrorService;

	@Autowired
	RFCErrorService rfcErrorService;

	@Autowired
	RequestErrorService requestErrorService;

	@Autowired
	SigesService sigesService;

	@Autowired
	TypePetitionService typePetitionService;

	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@Autowired
	PRFCService prfcService;
	@Autowired
	PStatusRFCService pstatusService;

	@Autowired
	PStatusService pstatusReleaseService;

	@Autowired
	PReleaseService preleaseService;

	@Autowired
	PRequestBaseService prequestService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PPriorityService ppriorityService;

	@Autowired
	PProjectService pprojectService;

	@Autowired
	PImpactService pimpactService;

	@Autowired
	PErrorReleaseService perrorReleaseService;

	@Autowired
	PErrorRFCService perrorRFCService;

	@Autowired
	PErrorRequestService perrorRequestService;

	@Autowired
	PReleaseErrorService preleaseErrorService;

	@Autowired
	PRFCErrorService prfcErrorService;

	@Autowired
	PRequestErrorService prequestErrorService;

	@Autowired
	PSigesService psigesService;

	@Autowired
	PTypePetitionService ptypePetitionService;

	@Autowired
	PUserService puserService;

	@Autowired
	private Environment environment;

	
	@Autowired
	public ErrorsManagementController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@RequestMapping(value = "/release", method = RequestMethod.GET)
	public String indexReleaseError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<Project> projects = projectService.listAll();
				List<Errors_Release> errors = errorReleaseService.findAll();
				model.addAttribute("projects", projects);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PProject> projects = pprojectService.listAll();
				List<PErrors_Release> errors = perrorReleaseService.findAll();
				model.addAttribute("projects", projects);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsReleaseManagement";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/release/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listReleaseError(HttpServletRequest request, Locale locale, Model model) {
		
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Long errorId;
			int projectId;
			int systemId;
			if (request.getParameter("errorId").equals("")) {
				errorId = null;
			} else {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}
			String dateRange = request.getParameter("dateRange");
			if (profileActive().equals("oracle")) {
				JsonSheet<ReleaseError> releaseError = new JsonSheet<>();
				releaseError = releaseErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
						dateRange, projectId, systemId);
				return releaseError;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PReleaseError> releaseError = new JsonSheet<>();
				releaseError = preleaseErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
						dateRange, projectId, systemId);
				return releaseError;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/rfc", method = RequestMethod.GET)
	public String indexRFCError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<Siges> siges = sigesService.findAll();
				List<Errors_RFC> errors = errorRFCService.findAll();
				List<Project> projects = projectService.listAll();
				model.addAttribute("siges", siges);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
				model.addAttribute("projects", projects);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PSiges> siges = psigesService.findAll();
				List<PErrors_RFC> errors = perrorRFCService.findAll();
				List<PProject> projects = pprojectService.listAll();
				model.addAttribute("siges", siges);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
				model.addAttribute("projects", projects);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsRFCManagement";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/rfc/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRFCError(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Long errorId;
			Long sigesId;
			int systemId;
			int projectId;
			if (request.getParameter("errorId").equals("")) {
				errorId = null;
			} else {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			}

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
				sigesId = (long) 0;
			} else {
				sigesId = (long) Integer.parseInt(request.getParameter("sigesId"));
			}
			String dateRange = request.getParameter("dateRange");
			if (profileActive().equals("oracle")) {
				JsonSheet<RFCError> rfcError = new JsonSheet<>();
				if (systemId == 0) {

					if (projectId == 0) {
						
						rfcError = rfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, sigesId, systemId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer>systemsId=new ArrayList<Integer>();
						for(System system:systems) {
							systemsId.add(system.getId());
						}
						rfcError = rfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, sigesId, systemsId);

					}

				} else {
					rfcError = rfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId, dateRange,
							sigesId, systemId);
				}
				return rfcError;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PRFCError> rfcError = new JsonSheet<>();
				if (systemId == 0) {

					if (projectId == 0) {
						
						rfcError = prfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, sigesId, systemId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer>systemsId=new ArrayList<Integer>();
						for(System system:systems) {
							systemsId.add(system.getId());
						}
						rfcError = prfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, sigesId, systemsId);

					}

				} else {
					rfcError = prfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId, dateRange,
							sigesId, systemId);
				}
				return rfcError;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String indexRequestError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<TypePetition> typePetitions = typePetitionService.findAll();
				List<Errors_Requests> errors = errorRequestService.findAll();
				List<Project> projects = projectService.listAll();
				model.addAttribute("projects", projects);
				model.addAttribute("typePetitions", typePetitions);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PTypePetition> typePetitions = ptypePetitionService.findAll();
				List<PErrors_Requests> errors = perrorRequestService.findAll();
				List<PProject> projects = pprojectService.listAll();
				model.addAttribute("projects", projects);
				model.addAttribute("typePetitions", typePetitions);
				model.addAttribute("errors", errors);
				model.addAttribute("systems", systems);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsRequestManagement";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/request/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRequestError(HttpServletRequest request, Locale locale, Model model) {
		
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Long errorId;
			Long typePetitionId;
			int systemId;
			int projectId;
			if (request.getParameter("errorId").equals("")) {
				errorId = null;
			} else {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			
			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}
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
			String dateRange = request.getParameter("dateRange");
			if (profileActive().equals("oracle")) {
				JsonSheet<RequestError> requestError = new JsonSheet<>();
				if (systemId == 0) {

					if (projectId == 0) {
						requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, typePetitionId, systemId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer>systemsId=new ArrayList<Integer>();
						for(System system:systems) {
							systemsId.add(system.getId());
						}
						requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, typePetitionId, systemsId);


					}

				} else {
					requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
							dateRange, typePetitionId, systemId);
				}
				return requestError;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<RequestError> requestError = new JsonSheet<>();
				if (systemId == 0) {

					if (projectId == 0) {
						requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, typePetitionId, systemId);
					} else {
						List<System> systems = new ArrayList<>();

						systems = systemService.getSystemByProject(projectId);
						List<Integer>systemsId=new ArrayList<Integer>();
						for(System system:systems) {
							systemsId.add(system.getId());
						}
						requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
								dateRange, typePetitionId, systemsId);


					}

				} else {
					requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId,
							dateRange, typePetitionId, systemId);
				}
				return requestError;
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = { "/getSystem/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getSystem(@PathVariable Integer id, Locale locale, Model model) {
		
		try {
			if (profileActive().equals("oracle")) {
				List<System> systems = new ArrayList<>();
				systems = systemService.getSystemByProject(id);
				return systems;
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = new ArrayList<>();
				systems = psystemService.getSystemByProject(id);
				return systems;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "system");

			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = { "/getSiges/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getSiges(@PathVariable Integer id, Locale locale, Model model) {
		
		try {
			if (profileActive().equals("oracle")) {
				List<Siges> siges = new ArrayList<>();
				siges = sigesService.listCodeSiges(id);
				return siges;
			} else if (profileActive().equals("postgres")) {
				List<PSiges> siges = new ArrayList<>();
				siges = psigesService.listCodeSiges(id);
				return siges;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "system");

			e.printStackTrace();

		}

		return null;
	}

	@RequestMapping(value = { "/downloaderrorrelease" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRelease(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();

		try {

			Long errorId;
			int projectId;
			int systemId;
			int typeDocument;
			if (!request.getParameter("errorId").equals("") && request.getParameter("errorId") != null) {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			} else {
				errorId = null;
			}

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
							"reports" + File.separator + "ErrorReleaseGeneral" + ".jrxml");
			
			}else {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ErrorReleaseGeneralExcel" + ".jrxml");
			}
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				System system = systemService.findSystemById(systemId);
				Project project = projectService.findById(projectId);
				Errors_Release typeError = errorReleaseService.findById(errorId);
				ErrorReleaseReport errorReleaseReport = new ErrorReleaseReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorProjectGraphList = new ArrayList<ErrorTypeGraph>();

				errorReleaseReport.setSystem(system);
				errorReleaseReport.setProject(project);
				errorReleaseReport.setTypeError(typeError);
				errorReleaseReport.setDateNew(dateRange);
				List<ReleaseError> releases = releaseErrorService.findAllList(errorId, dateRange, projectId, systemId);
				List<ReleaseTrackingToError> releasesTotals = releaseService.listByAllSystemError(dateRange, systemId);
				Integer totalReleases = 0;
				Integer totalReleasesError = releases.size();
				List<System> systems = new ArrayList<>();
				if (projectId == 0) {
					systems = systemService.list();
				} else {
					systems = systemService.getSystemByProject(projectId);
				}
				projectService.listAll();
				List<Errors_Release> errors = errorReleaseService.findAll();
				int valueError = 0;
				int valueRequest = 0;
				for (Errors_Release error : errors) {
					valueError = 0;
					for (ReleaseError releaseError : releases) {
						if (releaseError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}

				for (System systemOnly : systems) {
					valueError = 0;
					for (ReleaseError releaseError : releases) {
						if (releaseError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (System systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (ReleaseTrackingToError releaseErrorTracking : releasesTotals) {
						if (releaseErrorTracking.getRelease().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (ReleaseError releaseError : releases) {
						if (releaseError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalReleases += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorProjectGraphList.add(errorTypeGraph);
					}
				}

				errorReleaseReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorReleaseReport.setSystemGraphSource(errorSystemGraphList);
				errorReleaseReport.setProjectGraphSource(errorProjectGraphList);
				errorReleaseReport.setProjectTableGraphSource(errorProjectGraphList);
				errorReleaseReport.setErrordataSource(releases);
				List<ErrorReleaseReport> listError = new ArrayList<>();
				listError.add(errorReleaseReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalReleases - (double) totalReleasesError) / (double) totalReleases);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalReleases", totalReleases.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalReleasesError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesReleases-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesReleases-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
			       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			} else if (profileActive().equals("postgres")) {
				PSystem system = psystemService.findSystemById(systemId);
				PProject project = pprojectService.findById(projectId);
				PErrors_Release typeError = perrorReleaseService.findById(errorId);
				PErrorReleaseReport errorReleaseReport = new PErrorReleaseReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorProjectGraphList = new ArrayList<ErrorTypeGraph>();

				errorReleaseReport.setSystem(system);
				errorReleaseReport.setProject(project);
				errorReleaseReport.setTypeError(typeError);
				errorReleaseReport.setDateNew(dateRange);
				List<PReleaseError> releases = preleaseErrorService.findAllList(errorId, dateRange, projectId, systemId);
				List<PReleaseTrackingToError> releasesTotals = preleaseService.listByAllSystemError(dateRange, systemId);
				Integer totalReleases = 0;
				Integer totalReleasesError = releases.size();
				List<PSystem> systems = new ArrayList<>();
				if (projectId == 0) {
					systems = psystemService.list();
				} else {
					systems = psystemService.getSystemByProject(projectId);
				}

				pprojectService.listAll();
				List<PErrors_Release> errors = perrorReleaseService.findAll();
				int valueError = 0;
				int valueRequest = 0;
				for (PErrors_Release error : errors) {
					valueError = 0;
					for (PReleaseError releaseError : releases) {
						if (releaseError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}

				for (PSystem systemOnly : systems) {
					valueError = 0;
					for (PReleaseError releaseError : releases) {
						if (releaseError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (PSystem systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (PReleaseTrackingToError releaseErrorTracking : releasesTotals) {
						if (releaseErrorTracking.getRelease().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (PReleaseError releaseError : releases) {
						if (releaseError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalReleases += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorProjectGraphList.add(errorTypeGraph);
					}
				}

				errorReleaseReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorReleaseReport.setSystemGraphSource(errorSystemGraphList);
				errorReleaseReport.setProjectGraphSource(errorProjectGraphList);
				errorReleaseReport.setProjectTableGraphSource(errorProjectGraphList);
				errorReleaseReport.setErrordataSource(releases);
				List<PErrorReleaseReport> listError = new ArrayList<>();
				listError.add(errorReleaseReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalReleases - (double) totalReleasesError) / (double) totalReleases);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalReleases", totalReleases.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalReleasesError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesReleases-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesReleases-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
			       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			}
			
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");
			res.setException(e.getMessage());
			e.printStackTrace();
			return res;
		}

	}

	@RequestMapping(value = { "/downloaderrorrfc" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRFC(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			Long errorId;
			Long sigesId;
			int systemId;
			int projectId;
			int typeDocument;
			if (!request.getParameter("errorId").equals("") && request.getParameter("errorId") != null) {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			} else {
				errorId = null;
			}

			if (!request.getParameter("systemId").equals("") && request.getParameter("systemId") != null) {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			} else {
				systemId = 0;
			}

			if (!request.getParameter("sigesId").equals("") && request.getParameter("sigesId") != null) {
				sigesId = (long) Integer.parseInt(request.getParameter("sigesId"));
			} else {
				sigesId = (long) 0;
			}
			if (!request.getParameter("projectId").equals("") && request.getParameter("projectId") != null) {
				Integer.parseInt(request.getParameter("projectId"));
			} else {
			}
			if (request.getParameter("typeDocument").equals("")) {
				typeDocument = 0;
			} else {
				typeDocument =  Integer.parseInt(request.getParameter("typeDocument"));
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
							"reports" + File.separator + "ErrorRFCGeneral" + ".jrxml");
			
			}else {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ErrorRFCGeneralExcel" + ".jrxml");
			}

			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				System system = systemService.findSystemById(systemId);
				Siges siges = sigesService.findById(sigesId);
				Errors_RFC typeError = errorRFCService.findById(errorId);
				ErrorRFCReport errorRFCReport = new ErrorRFCReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemXErrorGraphList = new ArrayList<ErrorTypeGraph>();
				List<RFCTrackingToError> rfcTotals = rfcService.listByAllSystemError(dateRange, systemId);
				errorRFCReport.setSystem(system);
				errorRFCReport.setSiges(siges);
				errorRFCReport.setTypeError(typeError);
				errorRFCReport.setDateNew(dateRange);
				List<RFCError> rfcs = rfcErrorService.findAllList(errorId, dateRange, sigesId, systemId);
				Integer totalRFC = 0;
				Integer totalRFCError = rfcs.size();
				List<System> systems = systemService.list();
				sigesService.findAll();
				List<Errors_RFC> errors = errorRFCService.findAll();
				int valueError = 0;
				int valueRequest = 0;

				for (Errors_RFC error : errors) {
					valueError = 0;
					for (RFCError rfcError : rfcs) {
						if (rfcError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}

				for (System systemOnly : systems) {
					valueError = 0;
					for (RFCError rfcError : rfcs) {
						if (rfcError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (System systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (RFCTrackingToError rfcErrorTracking : rfcTotals) {
						if (rfcErrorTracking.getRfc().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (RFCError rfcError : rfcs) {
						if (rfcError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalRFC += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemXErrorGraphList.add(errorTypeGraph);
					}
				}
				errorRFCReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorRFCReport.setSystemGraphSource(errorSystemGraphList);
				errorRFCReport.setSystemXGraphSource(errorSystemXErrorGraphList);
				errorRFCReport.setSystemTableXGraphSource(errorSystemXErrorGraphList);
				errorRFCReport.setErrordataSource(rfcs);
				List<ErrorRFCReport> listError = new ArrayList<>();
				listError.add(errorRFCReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalRFC - (double) totalRFCError) / (double) totalRFC);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalRFC", totalRFC.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalRFCError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesRFC-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesRFC-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
				       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			     
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			} else if (profileActive().equals("postgres")) {
				PSystem system = psystemService.findSystemById(systemId);
				PSiges siges = psigesService.findById(sigesId);
				PErrors_RFC typeError = perrorRFCService.findById(errorId);
				PErrorRFCReport errorRFCReport = new PErrorRFCReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemXErrorGraphList = new ArrayList<ErrorTypeGraph>();
				List<PRFCTrackingToError> rfcTotals = prfcService.listByAllSystemError(dateRange, systemId);
				errorRFCReport.setSystem(system);
				errorRFCReport.setSiges(siges);
				errorRFCReport.setTypeError(typeError);
				errorRFCReport.setDateNew(dateRange);
				List<PRFCError> rfcs = prfcErrorService.findAllList(errorId, dateRange, sigesId, systemId);
				Integer totalRFC = 0;
				Integer totalRFCError = rfcs.size();
				List<PSystem> systems = psystemService.list();
				psigesService.findAll();
				List<PErrors_RFC> errors = perrorRFCService.findAll();
				int valueError = 0;
				int valueRequest = 0;

				for (PErrors_RFC error : errors) {
					valueError = 0;
					for (PRFCError rfcError : rfcs) {
						if (rfcError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}
				for (PSystem systemOnly : systems) {
					valueError = 0;
					for (PRFCError rfcError : rfcs) {
						if (rfcError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (PSystem systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (PRFCTrackingToError rfcErrorTracking : rfcTotals) {
						if (rfcErrorTracking.getRfc().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (PRFCError rfcError : rfcs) {
						if (rfcError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalRFC += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemXErrorGraphList.add(errorTypeGraph);
					}
				}
				errorRFCReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorRFCReport.setSystemGraphSource(errorSystemGraphList);
				errorRFCReport.setSystemXGraphSource(errorSystemXErrorGraphList);
				errorRFCReport.setSystemTableXGraphSource(errorSystemXErrorGraphList);
				errorRFCReport.setErrordataSource(rfcs);
				List<PErrorRFCReport> listError = new ArrayList<>();
				listError.add(errorRFCReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalRFC - (double) totalRFCError) / (double) totalRFC);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalRFC", totalRFC.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalRFCError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesRFC-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesRFC-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
				       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			     
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			}
			

			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}

	@RequestMapping(value = { "/downloaderrorrequest" }, method = RequestMethod.GET)
	public @ResponseBody JsonResponse downloadErrorRequest(HttpServletRequest request, Locale locale, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			Long errorId;
			Long typePetitionId;
			int systemId;
			int projectId;
			int typeDocument;
			if (!request.getParameter("errorId").equals("") && request.getParameter("errorId") != null) {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			} else {
				errorId = null;
			}

			if (!request.getParameter("systemId").equals("") && request.getParameter("systemId") != null) {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			} else {
				systemId = 0;
			}

			if (!request.getParameter("typePetitionId").equals("") && request.getParameter("typePetitionId") != null) {
				typePetitionId = (long) Integer.parseInt(request.getParameter("typePetitionId"));
			} else {
				typePetitionId = null;
			}
			if (request.getParameter("typeDocument").equals("")) {
				typeDocument = 0;
			} else {
				typeDocument =  Integer.parseInt(request.getParameter("typeDocument"));
			}
			ClassPathResource resource = null;
			if(typeDocument==2) {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ErrorRequestGeneral" + ".jrxml");
			
			}else {
				 resource = new ClassPathResource(
							"reports" + File.separator + "ErrorRequestGeneralExcel" + ".jrxml");
			}

			String dateRange = request.getParameter("dateRange");
	
			InputStream inputStream = resource.getInputStream();
			JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
			if (profileActive().equals("oracle")) {
				System system = systemService.findSystemById(systemId);
				TypePetition typePetition = typePetitionService.findById(typePetitionId);
				Errors_Requests typeError = errorRequestService.findById(errorId);
				ErrorRequestReport errorRequestReport = new ErrorRequestReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorTypePetitionGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemXErrorGraphList = new ArrayList<ErrorTypeGraph>();
				List<RequestBaseTrackingToError> requestTotals = requestService.listByAllSystemError(dateRange, systemId);
				errorRequestReport.setSystem(system);
				errorRequestReport.setTypePetition(typePetition);
				errorRequestReport.setTypeError(typeError);
				errorRequestReport.setDateNew(dateRange);
				List<RequestError> requests = requestErrorService.findAllList(errorId, dateRange, typePetitionId, systemId);
				List<System> systems = systemService.list();
				List<TypePetition> typePetitionList = typePetitionService.findAll();
				List<Errors_Requests> errors = errorRequestService.findAll();
				Integer totalRequest = 0;
				Integer totalRequestError = requests.size();
				int valueError = 0;
				int valueRequest = 0;
				for (Errors_Requests error : errors) {
					valueError = 0;
					for (RequestError requestError : requests) {
						if (requestError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}
				for (System systemOnly : systems) {
					valueError = 0;
					for (RequestError requestError : requests) {
						if (requestError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (TypePetition typePettionOnly : typePetitionList) {
					valueError = 0;
					for (RequestError requestError : requests) {
						if (requestError.getTypePetition().getId() == typePettionOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(typePettionOnly.getCode());
						errorTypePetitionGraphList.add(errorTypeGraph);
					}
				}
				for (System systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (RequestBaseTrackingToError requestErrorTracking : requestTotals) {
						if (requestErrorTracking.getRequest().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (RequestError requestError : requests) {
						if (requestError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalRequest += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemXErrorGraphList.add(errorTypeGraph);
					}
				}
				errorRequestReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorRequestReport.setSystemGraphSource(errorSystemGraphList);
				errorRequestReport.setTypePetitionGraphSource(errorTypePetitionGraphList);
				errorRequestReport.setSystemXGraphSource(errorSystemXErrorGraphList);
				errorRequestReport.setSystemTableXGraphSource(errorSystemXErrorGraphList);
				
				errorRequestReport.setErrordataSource(requests);
				List<ErrorRequestReport> listError = new ArrayList<>();
				listError.add(errorRequestReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalRequest - (double) totalRequestError) / (double) totalRequest);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalRequest", totalRequest.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalRequestError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesSolicitudes-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesSolicitudes-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
				       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			     
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			} else if (profileActive().equals("postgres")) {
				PSystem system = psystemService.findSystemById(systemId);
				PTypePetition typePetition = ptypePetitionService.findById(typePetitionId);
				PErrors_Requests typeError = perrorRequestService.findById(errorId);
				PErrorRequestReport errorRequestReport = new PErrorRequestReport();
				List<ErrorTypeGraph> errorTypeGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorTypePetitionGraphList = new ArrayList<ErrorTypeGraph>();
				List<ErrorTypeGraph> errorSystemXErrorGraphList = new ArrayList<ErrorTypeGraph>();
				List<PRequestBaseTrackingToError> requestTotals = prequestService.listByAllSystemError(dateRange, systemId);
				errorRequestReport.setSystem(system);
				errorRequestReport.setTypePetition(typePetition);
				errorRequestReport.setTypeError(typeError);
				errorRequestReport.setDateNew(dateRange);
				List<PRequestError> requests = prequestErrorService.findAllList(errorId, dateRange, typePetitionId, systemId);
				List<PSystem> systems = psystemService.list();
				List<PTypePetition> typePetitionList = ptypePetitionService.findAll();
				List<PErrors_Requests> errors = perrorRequestService.findAll();
				Integer totalRequest = 0;
				Integer totalRequestError = requests.size();
				int valueError = 0;
				int valueRequest = 0;
				for (PErrors_Requests error : errors) {
					valueError = 0;
					for (PRequestError requestError : requests) {
						if (requestError.getError().getId() == error.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(error.getName());
						errorTypeGraphList.add(errorTypeGraph);
					}
				}

				for (PSystem systemOnly : systems) {
					valueError = 0;
					for (PRequestError requestError : requests) {
						if (requestError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemGraphList.add(errorTypeGraph);
					}
				}

				for (PTypePetition typePettionOnly : typePetitionList) {
					valueError = 0;
					for (PRequestError requestError : requests) {
						if (requestError.getTypePetition().getId() == typePettionOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0) {
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setLabel(typePettionOnly.getCode());
						errorTypePetitionGraphList.add(errorTypeGraph);
					}
				}
				for (PSystem systemOnly : systems) {
					valueError = 0;
					valueRequest = 0;
					for (PRequestBaseTrackingToError requestErrorTracking : requestTotals) {
						if (requestErrorTracking.getRequest().getSystem().getId() == systemOnly.getId()) {
							valueRequest++;
						}
					}
					for (PRequestError requestError : requests) {
						if (requestError.getSystem().getId() == systemOnly.getId()) {
							valueError++;
						}
					}
					if (valueError != 0 || valueRequest != 0) {
						totalRequest += valueRequest;
						ErrorTypeGraph errorTypeGraph = new ErrorTypeGraph();
						Double percentageErrors = (((double) valueRequest - (double) valueError) / (double) valueRequest);
						percentageErrors = percentageErrors * 100;
						percentageErrors = 100 - percentageErrors;
						percentageErrors = Math.round(percentageErrors * 100d) / 100d;
						errorTypeGraph.setLabel2(percentageErrors.toString()+"%");
						errorTypeGraph.setValue(valueError);
						errorTypeGraph.setValueRequest(valueRequest);
						errorTypeGraph.setLabel(systemOnly.getName());
						errorSystemXErrorGraphList.add(errorTypeGraph);
					}
				}
				errorRequestReport.setErrorTypeGraphSource(errorTypeGraphList);
				errorRequestReport.setSystemGraphSource(errorSystemGraphList);
				errorRequestReport.setTypePetitionGraphSource(errorTypePetitionGraphList);
				errorRequestReport.setSystemXGraphSource(errorSystemXErrorGraphList);
				errorRequestReport.setSystemTableXGraphSource(errorSystemXErrorGraphList);
				
				errorRequestReport.setErrordataSource(requests);
				List<PErrorRequestReport> listError = new ArrayList<>();
				listError.add(errorRequestReport);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listError);
				Map<String, Object> parameters = new HashMap<>();
				ClassPathResource images = new ClassPathResource(
						"images" + File.separator + "logo" + ".png");
				parameters.put("logo",images.getInputStream());
				Double percentageErrors = (((double) totalRequest - (double) totalRequestError) / (double) totalRequest);
				percentageErrors = percentageErrors * 100;
				percentageErrors = 100 - percentageErrors;
				percentageErrors = Math.round(percentageErrors * 100d) / 100d;
				parameters.put("totalRequest", totalRequest.toString());
				parameters.put("percentageErrors", percentageErrors.toString()+"%");
				parameters.put("totalErrors", totalRequestError.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
				String reportName = "";
				String basePath = environment.getProperty("fileStore.path");
				if(typeDocument==2) {
					reportName= "SalidasNoConformesSolicitudes-" + CommonUtils.getSystemDate("yyyyMMdd") + ".pdf";
					JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + reportName);
					
				}else {
					reportName="SalidasNoConformesSolicitudes-" + CommonUtils.getSystemDate("yyyyMMdd") + ".xlsx";
					JRXlsxExporter exporter = new JRXlsxExporter();
			        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			        SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			        configuration.setDetectCellType(true);
				       
			        configuration.setCollapseRowSpan(true);
			        configuration.setIgnoreCellBorder(true);
			        configuration.setWhitePageBackground(true);
			        configuration.setRemoveEmptySpaceBetweenColumns(true);
			     
			        configuration.setOnePagePerSheet(true);
			        configuration.setSheetNames(new String[] { "Hoja1", "Hoja2", "Hoja3" });
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
			}
			return res;
		} catch (Exception e) {
			Sentry.capture(e, "report");

			e.printStackTrace();
			return res;
		}

	}

	public ByteArrayOutputStream exportFile(String type, JasperPrint jasperPrint) throws JRException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if (type.equalsIgnoreCase("Excel")) {
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} else {
			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		}
		return stream;
	}
}