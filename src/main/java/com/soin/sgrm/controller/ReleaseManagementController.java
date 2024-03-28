package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseUserFast;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PErrors_Release;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseError;
import com.soin.sgrm.model.pos.PReleaseUserFast;
import com.soin.sgrm.model.pos.PReleases_WithoutObj;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PErrorReleaseService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PReleaseErrorService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PRequestService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/management/release")
public class ReleaseManagementController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReleaseManagementController.class);

	@Autowired
	private StatusService statusService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private SystemService systemService;

	@Autowired
	private ErrorReleaseService errorService;

	@Autowired
	private ReleaseErrorService releaseErrorService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private EmailTemplateService emailService;
	
	@Autowired
	private RequestService requestService;
	
	
	@Autowired
	private PStatusService pstatusService;
	@Autowired
	private PReleaseService preleaseService;
	@Autowired
	private PSystemService psystemService;

	@Autowired
	private PErrorReleaseService perrorService;

	@Autowired
	private PReleaseErrorService preleaseErrorService;

	@Autowired
	private PProjectService pprojectService;

	@Autowired
	private PParameterService pparameterService;

	@Autowired
	private PEmailTemplateService pemailService;
	
	@Autowired
	private PRequestService prequestService;
 
	private final Environment environment;

	@Autowired
	public ReleaseManagementController(Environment environment) {
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
			loadCountsRelease(request, getUserLogin().getUsername());
			
			if (profileActive().equals("oracle")) {
				model.addAttribute("system", new SystemUser());
				model.addAttribute("systems", systemService.listSystemUser());
				model.addAttribute("status", new Status());
				model.addAttribute("statuses", statusService.list());
				model.addAttribute("errors", errorService.findAll());
			} else if (profileActive().equals("postgres")) {
				model.addAttribute("system", new PSystemUser());
				model.addAttribute("systems", psystemService.listSystemUser());
				model.addAttribute("status", new PStatus());
				model.addAttribute("statuses", pstatusService.list());
				model.addAttribute("errors", perrorService.findAll());
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/release/releaseManagement";

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
				return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null, dateRange,
						systemId, statusId);
			} else if (profileActive().equals("postgres")) {
				JsonSheet<?> releases = new JsonSheet<>();		
				String dateRange2 = request.getParameter("dateRange");
				releases= preleaseService.findAll1(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange2,
						systemId, statusId);
				return releases;
			}
				
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(value = "/cancelRelease", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease) {
		JsonResponse res = new JsonResponse();
		try {
			
			if (profileActive().equals("oracle")) {
				ReleaseEdit release = releaseService.findEditById(idRelease);
				Status status = statusService.findByName("Anulado");
				release.setStatus(status);
				release.setOperator(getUserLogin().getFullName());
				release.setMotive(status.getMotive());
				releaseService.updateStatusRelease(release);
			} else if (profileActive().equals("postgres")) {
				PReleaseEdit release = preleaseService.findEditById(idRelease);
				PStatus status = pstatusService.findByName("Anulado");
				release.setStatus(status);
				release.setOperator(getUserLogin().getFullName());
				release.setMotive(status.getMotive());
				preleaseService.updateStatusRelease(release);
			}
			
			res.setStatus("success");

		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseManagement");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar el release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/statusRelease", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease,
			@RequestParam(value = "idStatus", required = true) Integer idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError,
			@RequestParam(value = "sendEmail", required = true) boolean sendEmail,
			@RequestParam(value = "senders", required = false) String senders,
			@RequestParam(value = "note", required = false) String note) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				ReleaseUserFast release = releaseService.findByIdReleaseUserFast(idRelease);
				Status status = statusService.findById(idStatus);
				UserLogin userLogin = getUserLogin();
				Errors_Release error = new Errors_Release();
				Boolean errorVer = false;
				if (status != null && status.getName().equals("Borrador")) {
					if (release.getStatus().getId() != status.getId())
						release.setRetries(release.getRetries() + 1);
				} else if (status != null && status.getName().equals("Error")) {
					error = errorService.findById(idError);
					ReleaseError releaseError = new ReleaseError();
					releaseError.setSystem(release.getSystem());
					errorVer = true;
					releaseError.setProject(projectService.findById(release.getSystem().getProyectId()));
					releaseError.setError(error);
					Releases_WithoutObj releaseWithObj = releaseService.findReleaseWithouObj(release.getId());
					releaseError.setRelease(releaseWithObj);
					releaseError.setObservations(motive);
					Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					releaseError.setErrorDate(dateFormat);
					releaseErrorService.save(releaseError);
					release.setStatus(status);
					release.setOperator(getUserLogin().getFullName());
					release.setMotive(motive);
					release.setDateChange(dateChange);
					releaseService.updateStatusReleaseUser(release);
					release.setTimeNew(null);
					Status statusChange = statusService.findByName("Borrador");
					release.setStatus(statusChange);

					if (statusChange != null && statusChange.getName().equals("Borrador")) {
						if (release.getStatus().getId() != status.getId())
							release.setRetries(release.getRetries() + 1);
					}
					status = statusChange;
					motive = "Paso a borrador por " + error.getName();
					dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateFormat);
					calendar.add(Calendar.MINUTE, 1);
					Timestamp time1Minute = new Timestamp(calendar.getTimeInMillis());
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					java.util.Date fechaNueva = (java.util.Date) format.parse(time1Minute.toString());
					format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
					String time1MinuteFormat = format.format(fechaNueva);
					dateChange = time1MinuteFormat;
				}

				release.setStatus(status);
				release.setOperator(getUserLogin().getFullName());
				release.setDateChange(dateChange);
				release.setMotive(motive);
				releaseService.updateStatusReleaseUser(release);
				res.setStatus("success");
				if (sendEmail) {

					if (!errorVer) {
						 
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
						EmailTemplate emailNotify = emailService.findById(idTemplate);
						EmailTemplate email=new EmailTemplate();
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							 email = release.getSystem().getEmailTemplate().iterator().next();
							 
						}
						if(email.getId()==0) {
							
							 email.setSubject("[ INSTALACION - "+release.getSystem().getName()+" ]:{{releaseNumber}}");
						 }
						String subject =getSubject(email,release);
						
						String statusName = status.getName();
						
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailNotifyChangeStatus(release.getReleaseNumber(), " del Release",
										statusName, release.getOperator(),
										CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
										userLogin, senders, emailNotify,subject, release.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					} else {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(31).getParamValue());
						EmailTemplate emailNotify = emailService.findById(idTemplate);
						String statusName = status.getName();
						EmailTemplate email=new EmailTemplate();
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							 email = release.getSystem().getEmailTemplate().iterator().next();
						}
						if(email.getId()==0) {
							email.setSubject("[ INSTALACION - "+release.getSystem().getName()+" ]:{{releaseNumber}}");
						 }
						String subject =getSubject(email,release);
						
						String typeError = error.getName();
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailNotifyChangeStatusError(typeError, release.getReleaseNumber(),
										" del Release", statusName, release.getOperator(),
										CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
										userLogin, senders, emailNotify,subject, release.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}
				}
			} else if (profileActive().equals("postgres")) {
				PReleaseUserFast release = preleaseService.findByIdReleaseUserFast(idRelease);
				PStatus status = pstatusService.findById(idStatus);
				UserLogin userLogin = getUserLogin();
				PErrors_Release error = new PErrors_Release();
				Boolean errorVer = false;
				if (status != null && status.getName().equals("Borrador")) {
					if (release.getStatus().getId() != status.getId())
						release.setRetries(release.getRetries() + 1);
				} else if (status != null && status.getName().equals("Error")) {
					error = perrorService.findById(idError);
					PReleaseError releaseError = new PReleaseError();
					releaseError.setSystem(release.getSystem());
					errorVer = true;
					releaseError.setProject(pprojectService.findById(release.getSystem().getProyectId()));
					releaseError.setError(error);
					PReleases_WithoutObj releaseWithObj = preleaseService.findReleaseWithouObj(release.getId());
					releaseError.setRelease(releaseWithObj);
					releaseError.setObservations(motive);
					Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					releaseError.setErrorDate(dateFormat);
					preleaseErrorService.save(releaseError);
					release.setStatus(status);
					release.setOperator(getUserLogin().getFullName());
					release.setMotive(motive);
					release.setDateChange(dateChange);
					preleaseService.updateStatusRelease(release);
					release.setTimeNew(null);
					PStatus statusChange = pstatusService.findByName("Borrador");
					release.setStatus(statusChange);

					if (statusChange != null && statusChange.getName().equals("Borrador")) {
						if (release.getStatus().getId() != status.getId())
							release.setRetries(release.getRetries() + 1);
					}
					status = statusChange;
					motive = "Paso a borrador por " + error.getName();
					dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateFormat);
					calendar.add(Calendar.MINUTE, 1);
					Timestamp time1Minute = new Timestamp(calendar.getTimeInMillis());
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					java.util.Date fechaNueva = (java.util.Date) format.parse(time1Minute.toString());
					format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
					String time1MinuteFormat = format.format(fechaNueva);
					dateChange = time1MinuteFormat;
				}

				release.setStatus(status);
				release.setOperator(getUserLogin().getFullName());
				release.setDateChange(dateChange);
				release.setMotive(motive);
				preleaseService.updateStatusRelease(release);
				res.setStatus("success");
				if (sendEmail) {

					if (!errorVer) {
						 
						Integer idTemplate = Integer.parseInt(pparameterService.findByCode(30).getParamValue());
						PEmailTemplate emailNotify = pemailService.findById(idTemplate);
						PEmailTemplate email=new PEmailTemplate();
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							 email = release.getSystem().getEmailTemplate().iterator().next();
						}
						String subject =getSubject(email,release);
						
						String statusName = status.getName();
						
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailNotifyChangeStatus(release.getReleaseNumber(), " del Release",
										statusName, release.getOperator(),
										CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
										userLogin, senders, emailNotify,subject, release.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					} else {
						Integer idTemplate = Integer.parseInt(pparameterService.findByCode(31).getParamValue());
						PEmailTemplate emailNotify = pemailService.findById(idTemplate);
						String statusName = status.getName();
						PEmailTemplate email=new PEmailTemplate();
						if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
							 email = release.getSystem().getEmailTemplate().iterator().next();
						}
						String subject =getSubject(email,release);
						
						String typeError = error.getName();
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailNotifyChangeStatusError(typeError, release.getReleaseNumber(),
										" del Release", statusName, release.getOperator(),
										CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
										userLogin, senders, emailNotify,subject, release.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}
				}

			}
			
			
			
		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseManagement");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
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
	@SuppressWarnings("unused")
	public String getSubject(EmailTemplate emailNotify,ReleaseUserFast release) {
		String tpo = "";
		String releaseNumber = release.getReleaseNumber();
		String[] parts = releaseNumber.split("\\.");
		for (String part : parts) {
			if (part.contains("TPO")) {
				String[] partsTPO = part.split("TPO");
				String[] partsNumber = part.split(partsTPO[1]);
				tpo = partsNumber[0] + "-" + partsTPO[1];
			}
		}

		Request requestNew = new Request();
		if (tpo != "") {
			requestNew = requestService.findByNameCode(tpo);
		}
		/* ------ Subject ------ */
		if (emailNotify.getSubject().contains("{{tpoNumber}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{tpoNumber}}", (tpo != "" ? tpo : "")));
		}
		if (emailNotify.getSubject().contains("{{releaseNumber}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}
		if (emailNotify.getSubject().contains("{{version}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}
		return emailNotify.getSubject();
	}
	
	@SuppressWarnings("unused")
	public String getSubject(PEmailTemplate emailNotify,PReleaseUserFast release) {
		
		
		String tpo = "";
		String releaseNumber = release.getReleaseNumber();
		String[] parts = releaseNumber.split("\\.");
		for (String part : parts) {
			if (part.contains("TPO")) {
				String[] partsTPO = part.split("TPO");
				String[] partsNumber = part.split(partsTPO[1]);
				tpo = partsNumber[0] + "-" + partsTPO[1];
			}
		}

		PRequest requestNew = new PRequest();
		if (tpo != "") {
			requestNew = prequestService.findByNameCode(tpo);
		}
		/* ------ Subject ------ */
		if (emailNotify.getSubject().contains("{{tpoNumber}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{tpoNumber}}", (tpo != "" ? tpo : "")));
		}
		if (emailNotify.getSubject().contains("{{releaseNumber}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}
		if (emailNotify.getSubject().contains("{{version}}")) {
			emailNotify.setSubject(emailNotify.getSubject().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}
		return emailNotify.getSubject();
	}
	

}
