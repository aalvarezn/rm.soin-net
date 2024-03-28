package com.soin.sgrm.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCError;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.model.pos.PRelease_RFCFast;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCErrorService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.RFCWithoutReleaseService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PErrorRFCService;
import com.soin.sgrm.service.pos.PImpactService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.service.pos.PRFCErrorService;
import com.soin.sgrm.service.pos.PRFCService;
import com.soin.sgrm.service.pos.PRFCWithoutReleaseService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/rfc")
public class RFCManagementController extends BaseController {
	public static final Logger logger = Logger.getLogger(RFCManagementController.class);
	@Autowired
	RFCService rfcService;

	@Autowired
	RFCWithoutReleaseService rfcWRService;
	@Autowired
	StatusRFCService statusService;

	@Autowired
	StatusService statusReleaseService;

	@Autowired
	ReleaseService releaseService;

	@Autowired
	SystemService systemService;

	@Autowired
	PriorityService priorityService;

	@Autowired
	ImpactService impactService;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	@Autowired
	ErrorRFCService errorService;

	@Autowired
	SigesService sigesService;

	@Autowired
	RFCErrorService rfcErrorService;
	
	@Autowired
	ParameterService parameterService;

	@Autowired
	EmailTemplateService emailService;
	
	@Autowired
	PRFCService prfcService;

	@Autowired
	PRFCWithoutReleaseService  prfcWRService;
	@Autowired
	PStatusRFCService pstatusService;

	@Autowired
	PStatusService pstatusReleaseService;

	@Autowired
	PReleaseService preleaseService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PPriorityService ppriorityService;

	@Autowired
	PImpactService pimpactService;

	@Autowired
	PUserService puserService;

	@Autowired
	PErrorRFCService perrorService;

	@Autowired
	PSigesService psigesService;

	@Autowired
	PRFCErrorService prfcErrorService;
	
	@Autowired
	PParameterService pparameterService;

	@Autowired
	PEmailTemplateService pemailService;

	private final Environment environment;

	@Autowired
	public RFCManagementController(Environment environment) {
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
			loadCountsRFC(request, getUserLogin().getId());
			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.list();
				List<Priority> priorities = priorityService.list();
				List<StatusRFC> statuses = statusService.findAll();
				List<Impact> impacts = impactService.list();
				List<Errors_RFC> errors = errorService.findAll();
				model.addAttribute("priorities", priorities);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
				model.addAttribute("errors", errors);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.list();
				List<PPriority> priorities = ppriorityService.list();
				List<PStatusRFC> statuses = pstatusService.findAll();
				List<PImpact> impacts = pimpactService.list();
				List<PErrors_RFC> errors = perrorService.findAll();
				model.addAttribute("priorities", priorities);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
				model.addAttribute("errors", errors);
			}
			
			
			
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfcManagement";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			int priorityId = 0;
			int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}

			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			String dateRange = request.getParameter("dateRange");

			if (profileActive().equals("oracle")) {
				JsonSheet<RFC_WithoutRelease> rfcs = new JsonSheet<>();		
				rfcs = rfcWRService.findAll1(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange, priorityId,
						systemId);
				return rfcs;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PRFC_WithoutRelease> rfcs = new JsonSheet<>();		
				rfcs = prfcWRService.findAll1(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange, priorityId,
						systemId);
				return rfcs;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/cancelRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				RFC rfc = rfcService.findById(idRFC);
				StatusRFC status = statusService.findByKey("name", "Anulado");
				rfc.setStatus(status);
				rfc.setOperator(getUserLogin().getFullName());
				rfc.setMotive(status.getReason());
				rfcService.update(rfc);
			} else if (profileActive().equals("postgres")) {
				PRFC rfc = prfcService.findById(idRFC);
				PStatusRFC status = pstatusService.findByKey("name", "Anulado");
				rfc.setStatus(status);
				rfc.setOperator(getUserLogin().getFullName());
				rfc.setMotive(status.getReason());
				prfcService.update(rfc);
			}
			
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar el rfc: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/statusRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError,
			@RequestParam(value = "sendEmail", required = true) boolean sendEmail,
			@RequestParam(value = "senders", required = false) String senders,
			@RequestParam(value = "note", required = false) String note) {
		JsonResponse res = new JsonResponse();
		try {
			
			if (profileActive().equals("oracle")) {
				RFC rfc = rfcService.findById(idRFC);
				StatusRFC status = statusService.findById(idStatus);
				String user = getUserLogin().getFullName();
				UserLogin userLogin=getUserLogin();
				Errors_RFC error= new Errors_RFC();
				boolean errorVer=false;
				
				if (status != null && status.getName().equals("Borrador")) {
					Set<Release_RFCFast> releases = rfc.getReleases();
					for (Release_RFCFast release : releases) {
						release.setStatus(release.getStatusBefore());
						release.setMotive("Devuelto al estado " + release.getStatus().getName());
						releaseService.updateStatusReleaseRFC(release, user);
					}

					/*
					 * if (release.getStatus().getId() != status.getId())
					 * release.setRetries(release.getRetries() + 1);
					 */
				} else if (status != null && status.getName().equals("Error")) {
					 error = errorService.findById(idError);
					errorVer=true;
					
					RFCError rfcError = new RFCError();
					rfcError.setSystem(rfc.getSystemInfo());
					rfcError.setSiges(sigesService.findByKey("codeSiges", rfc.getCodeProyect()));
					rfcError.setError(error);
					RFC_WithoutRelease rfcWithRelease = rfcService.findRfcWithRelease(rfc.getId());
					rfcError.setRfc(rfcWithRelease);
					rfcError.setObservations(motive);
					Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					rfcError.setErrorDate(dateFormat);
					rfcErrorService.save(rfcError);
					rfc.setStatus(status);
					rfc.setOperator(getUserLogin().getFullName());
					rfc.setMotive(motive);
					rfc.setRequestDate(dateFormat);
					rfcService.update(rfc);

					StatusRFC statusChange = statusService.findByKey("name", "Borrador");
					rfc.setStatus(statusChange);

					if (statusChange != null && statusChange.getName().equals("Borrador")) {
						Set<Release_RFCFast> releases = rfc.getReleases();
						for (Release_RFCFast release : releases) {
							release.setStatus(release.getStatusBefore());
							release.setMotive("Devuelto al estado " + release.getStatus().getName());
							releaseService.updateStatusReleaseRFC(release, user);
						}
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
				rfc.setStatus(status);
				rfc.setOperator(getUserLogin().getFullName());
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				rfc.setRequestDate(dateFormat);
				rfc.setMotive(motive);
				rfcService.update(rfc);
				if (sendEmail) {

					if(!errorVer) {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName=status.getName();
					EmailTemplate email= rfc.getSiges().getEmailTemplate();
					if(email==null) {
						 email=new EmailTemplate();
						 email.setSubject("[RFC - "+rfc.getSystemInfo().getName()+" ]:{{rfcNumber}}");
					 }
					String subject=getSubject(email,rfc);
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatus(rfc.getNumRequest()," del RFC",statusName,rfc.getOperator(),rfc.getRequestDate(),userLogin,senders,emailNotify,subject,rfc.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
						} catch (Exception e) {
							Sentry.capture(e, "rfc");
						}

					});
					newThread.start();
					}else {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(31).getParamValue());
						EmailTemplate emailNotify = emailService.findById(idTemplate);
						String statusName=status.getName();
						String typeError=error.getName();
						EmailTemplate email= rfc.getSiges().getEmailTemplate();
						if(email==null) {
							 email=new EmailTemplate();
							 email.setSubject("[RFC - "+rfc.getSystemInfo().getName()+" ]:{{rfcNumber}}");
						 }
						String subject=getSubject(email,rfc);
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailNotifyChangeStatusError(typeError,rfc.getNumRequest()," del RFC",statusName,rfc.getOperator(),rfc.getRequestDate(),userLogin,senders,emailNotify,subject,rfc.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}
				}
				res.setStatus("success");
			} else if (profileActive().equals("postgres")) {
				PRFC rfc = prfcService.findById(idRFC);
				PStatusRFC status = pstatusService.findById(idStatus);
				String user = getUserLogin().getFullName();
				UserLogin userLogin=getUserLogin();
				PErrors_RFC error= new PErrors_RFC();
				boolean errorVer=false;
				
				if (status != null && status.getName().equals("Borrador")) {
					Set<PRelease_RFCFast> releases = rfc.getReleases();
					for (PRelease_RFCFast release : releases) {
						release.setStatus(release.getStatusBefore());
						release.setMotive("Devuelto al estado " + release.getStatus().getName());
						preleaseService.updateStatusReleaseRFC(release, user);
					}

					/*
					 * if (release.getStatus().getId() != status.getId())
					 * release.setRetries(release.getRetries() + 1);
					 */
				} else if (status != null && status.getName().equals("Error")) {
					 error = perrorService.findById(idError);
					errorVer=true;
					
					PRFCError rfcError = new PRFCError();
					rfcError.setSystem(rfc.getSystemInfo());
					rfcError.setSiges(psigesService.findByKey("codeSiges", rfc.getCodeProyect()));
					rfcError.setError(error);
					PRFC_WithoutRelease rfcWithRelease = prfcService.findRfcWithRelease(rfc.getId());
					rfcError.setRfc(rfcWithRelease);
					rfcError.setObservations(motive);
					Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
					rfcError.setErrorDate(dateFormat);
					prfcErrorService.save(rfcError);
					rfc.setStatus(status);
					rfc.setOperator(getUserLogin().getFullName());
					rfc.setMotive(motive);
					rfc.setRequestDate(dateFormat);
					prfcService.update(rfc);

					PStatusRFC statusChange = pstatusService.findByKey("name", "Borrador");
					rfc.setStatus(statusChange);

					if (statusChange != null && statusChange.getName().equals("Borrador")) {
						Set<PRelease_RFCFast> releases = rfc.getReleases();
						for (PRelease_RFCFast release : releases) {
							release.setStatus(release.getStatusBefore());
							release.setMotive("Devuelto al estado " + release.getStatus().getName());
							preleaseService.updateStatusReleaseRFC(release, user);
						}
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
				rfc.setStatus(status);
				rfc.setOperator(getUserLogin().getFullName());
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				rfc.setRequestDate(dateFormat);
				rfc.setMotive(motive);
				prfcService.update(rfc);
				if (sendEmail) {

					if(!errorVer) {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
					PEmailTemplate emailNotify = pemailService.findById(idTemplate);
					String statusName=status.getName();
					PEmailTemplate email= rfc.getSiges().getEmailTemplate();
					if(email==null) {
						 email=new PEmailTemplate();
						 email.setSubject("[RFC - "+rfc.getSystemInfo().getName()+" ]:{{rfcNumber}}");
					 }
					String subject=getSubject(email,rfc);
					Thread newThread = new Thread(() -> {
						try {
							pemailService.sendMailNotifyChangeStatus(rfc.getNumRequest()," del RFC",statusName,rfc.getOperator(),rfc.getRequestDate(),userLogin,senders,emailNotify,subject,rfc.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
						} catch (Exception e) {
							Sentry.capture(e, "rfc");
						}

					});
					newThread.start();
					}else {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(31).getParamValue());
						PEmailTemplate emailNotify = pemailService.findById(idTemplate);
						String statusName=status.getName();
						String typeError=error.getName();
						PEmailTemplate email= rfc.getSiges().getEmailTemplate();
						if(email==null) {
							 email=new PEmailTemplate();
							 email.setSubject("[RFC - "+rfc.getSystemInfo().getName()+" ]:{{rfcNumber}}");
						 }
						String subject=getSubject(email,rfc);
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailNotifyChangeStatusError(typeError,rfc.getNumRequest()," del RFC",statusName,rfc.getOperator(),rfc.getRequestDate(),userLogin,senders,emailNotify,subject,rfc.getMotive(),note,"RM-P2-R5|Registro evidencia de instalación");
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}
				}
				res.setStatus("success");
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del RFC: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			if (profileActive().equals("oracle")) {
				RFC rfc = rfcService.findById(id);
				if (rfc.getStatus().getName().equals("Borrador")) {

					StatusRFC status = statusService.findByKey("name", "Anulado");
					rfc.setStatus(status);
					rfc.setMotive(status.getReason());
					rfc.setOperator(getUserLogin().getFullName());
					rfcService.update(rfc);

				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
				}
			} else if (profileActive().equals("postgres")) {
				PRFC rfc = prfcService.findById(id);
				if (rfc.getStatus().getName().equals("Borrador")) {

					PStatusRFC status = pstatusService.findByKey("name", "Anulado");
					rfc.setStatus(status);
					rfc.setMotive(status.getReason());
					rfc.setOperator(getUserLogin().getFullName());
					prfcService.update(rfc);

				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
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

	public void loadCountsRFC(HttpServletRequest request, Integer id) {
		// PUser userLogin = getUserLogin();
		// List<PSystem> systems = systemService.listProjects(userLogin.getId());
		if (profileActive().equals("oracle")) {
			Map<String, Integer> rfcC = new HashMap<String, Integer>();
			rfcC.put("draft", rfcService.countByType(id, "Borrador", 2, null));
			rfcC.put("requested", rfcService.countByType(id, "Solicitado", 2, null));
			rfcC.put("completed", rfcService.countByType(id, "Completado", 2, null));
			rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
			request.setAttribute("rfcC", rfcC);
		} else if (profileActive().equals("postgres")) {
			Map<String, Integer> rfcC = new HashMap<String, Integer>();
			rfcC.put("draft", prfcService.countByType(id, "Borrador", 2, null));
			rfcC.put("requested", prfcService.countByType(id, "Solicitado", 2, null));
			rfcC.put("completed", prfcService.countByType(id, "Completado", 2, null));
			rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
			request.setAttribute("rfcC", rfcC);
		}
		

	}
	
	public String getSubject(EmailTemplate email,RFC rfc) {
		
		
		String temp = "";
		/* ------ Subject ------ */
		if (email.getSubject().contains("{{rfcNumber}}")) {
			email.setSubject(email.getSubject().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
		}

		if (email.getSubject().contains("{{priority}}")) {
			email.setSubject(email.getSubject().replace("{{priority}}",
					(rfc.getPriority().getName() != null ? rfc.getPriority().getName() : "")));
		}

		if (email.getSubject().contains("{{impact}}")) {
			email.setSubject(email.getSubject().replace("{{impact}}",
					(rfc.getImpact().getName() != null ? rfc.getImpact().getName() : "")));
		}

		if (email.getSubject().contains("{{typeChange}}")) {
			email.setSubject(email.getSubject().replace("{{typeChange}}",
					(rfc.getTypeChange().getName() != null ? rfc.getTypeChange().getName() : "")));
		}

		if (email.getHtml().contains("{{message}}")) {
			email.setHtml(email.getHtml().replace("{{message}}", (rfc.getMessage() != null ? rfc.getMessage() : "NA")));
		}

		if (email.getSubject().contains("{{systemMain}}")) {
			temp = "";
			Siges codeSiges = sigesService.findByKey("codeSiges", rfc.getCodeProyect());

			temp += codeSiges.getSystem().getName();

			email.setSubject(email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
		}
		return email.getSubject();
	}
	
public String getSubject(PEmailTemplate email,PRFC rfc) {
		
		
		String temp = "";
		/* ------ Subject ------ */
		if (email.getSubject().contains("{{rfcNumber}}")) {
			email.setSubject(email.getSubject().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
		}

		if (email.getSubject().contains("{{priority}}")) {
			email.setSubject(email.getSubject().replace("{{priority}}",
					(rfc.getPriority().getName() != null ? rfc.getPriority().getName() : "")));
		}

		if (email.getSubject().contains("{{impact}}")) {
			email.setSubject(email.getSubject().replace("{{impact}}",
					(rfc.getImpact().getName() != null ? rfc.getImpact().getName() : "")));
		}

		if (email.getSubject().contains("{{typeChange}}")) {
			email.setSubject(email.getSubject().replace("{{typeChange}}",
					(rfc.getTypeChange().getName() != null ? rfc.getTypeChange().getName() : "")));
		}

		if (email.getHtml().contains("{{message}}")) {
			email.setHtml(email.getHtml().replace("{{message}}", (rfc.getMessage() != null ? rfc.getMessage() : "NA")));
		}

		if (email.getSubject().contains("{{systemMain}}")) {
			temp = "";
			PSiges codeSiges = psigesService.findByKey("codeSiges", rfc.getCodeProyect());

			temp += codeSiges.getSystem().getName();

			email.setSubject(email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
		}
		return email.getSubject();
	}

}