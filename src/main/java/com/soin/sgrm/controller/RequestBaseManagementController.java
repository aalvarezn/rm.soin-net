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
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseReference;
import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.RequestRM_P1_R2;
import com.soin.sgrm.model.RequestRM_P1_R3;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;
import com.soin.sgrm.model.Request_Estimate;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorRequestService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.RequestBaseR1Service;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestErrorService;
import com.soin.sgrm.service.RequestRM_P1_R4Service;
import com.soin.sgrm.service.Request_EstimateService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/request")
public class RequestBaseManagementController extends BaseController {

	public static final Logger logger = Logger.getLogger(RequestBaseManagementController.class);
	@Autowired
	SystemService systemService;

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
	com.soin.sgrm.service.UserService userService;

	@Autowired
	RequestRM_P1_R4Service requestServiceRm4;

	@Autowired
	AmbientService ambientService;

	@Autowired
	EmailTemplateService emailService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	ErrorRequestService errorService;

	@Autowired
	RequestErrorService requestErrorService;
	
	@Autowired
	Request_EstimateService requestEstimateService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRequest(request, userLogin);
			List<System> systems = systemService.list();
			List<StatusRequest> statuses = statusService.findAll();
			List<TypePetition> typePetitions = typePetitionService.findAll();
			List<Errors_Requests> errors = errorService.findAll();
			model.addAttribute("statuses", statuses);
			model.addAttribute("typePetitions", typePetitions);
			model.addAttribute("errors", errors);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "request");
			e.printStackTrace();
		}
		return "/request/requestManagement";
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

			requests = requestBaseR1Service.findAllRequest(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId,
					dateRange, systemId, typePetitionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requests;
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/statusRequest", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRequest", required = true) Long idRequest,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError,
			@RequestParam(value = "sendEmail", required = true) boolean sendEmail,
			@RequestParam(value = "senders", required = false) String senders,
			@RequestParam(value = "requestDateEstimate", required = false) String requestDateEstimate,
			@RequestParam(value = "note", required = false) String note) {
		JsonResponse res = new JsonResponse();
		try {
			RequestBaseR1 requestBase = requestBaseService.findByR1(idRequest);
			StatusRequest status = statusService.findById(idStatus);
			String user = getUserLogin().getFullName();
			Errors_Requests error = new Errors_Requests();
			Boolean errorVer = false;
			UserLogin userLogin = getUserLogin();
			requestBase.setStatus(status);
			requestBase.setOperator(getUserLogin().getFullName());
			Timestamp dateFormatNow = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
			requestBase.setRequestDate(dateFormatNow);

			requestBase.setMotive(motive);
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
				requestBaseNew.setSiges(requestBaseService.findById(idRequest).getSiges());
			}

			if (status != null && status.getName().equals("Error")) {
				error = errorService.findById(idError);
				RequestError requestError = new RequestError();
				requestError.setSystem(requestBaseNew.getSystemInfo());
				requestError.setTypePetition(requestBaseNew.getTypePetition());
				requestError.setError(error);
				requestError.setRequest(requestBaseNew);
				requestError.setObservations(motive);
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				requestError.setErrorDate(dateFormat);
				requestErrorService.save(requestError);
				requestBaseNew.setStatus(status);
				requestBaseNew.setOperator(getUserLogin().getFullName());
				requestBaseNew.setMotive(motive);
				requestBaseNew.setRequestDate(dateFormat);
				requestBaseService.update(requestBaseNew);

				StatusRequest statusChange = statusService.findByKey("name", "Borrador");
				requestBaseNew.setStatus(statusChange);

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
				Timestamp dateFormat2 = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				requestBaseNew.setStatus(status);
				requestBaseNew.setOperator(getUserLogin().getFullName());
				requestBaseNew.setMotive(motive);
				requestBaseNew.setRequestDate(dateFormat2);
			}
			
			if(status != null && status.getName().equals("En proceso")) {
				Request_Estimate requestEstimate=requestEstimateService.findByIdRequest(idRequest);
				Timestamp dateFormatRequestDate = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				Timestamp dateFormatRequestDateEstimate = CommonUtils.convertStringToTimestamp(requestDateEstimate, "dd/MM/yyyy hh:mm a");
				if(requestEstimate!=null) {
					
				
					requestEstimate.setRequestDate(dateFormatRequestDate);
					requestEstimate.setRequestDateEstimate(dateFormatRequestDateEstimate);
					requestEstimate.setRequestDateFinal(null);
					requestEstimateService.update(requestEstimate);
				}else {
					RequestBaseReference requestBaseReference=new RequestBaseReference();
				    requestEstimate=new Request_Estimate();
					requestBaseReference.setId(idRequest);
					requestEstimate.setRequestBase(requestBaseReference);
					requestEstimate.setRequestDate(dateFormatRequestDate);
					requestEstimate.setRequestDateEstimate(dateFormatRequestDateEstimate);
					requestEstimate.setRequestDateFinal(null);
					requestEstimateService.save(requestEstimate);
				}
				
			}

			requestBaseService.update(requestBaseNew);
			res.setStatus("success");

			if (sendEmail) {

				if (!errorVer) {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName = status.getName();
					EmailTemplate email = new EmailTemplate();
					TypePetition typePettion = requestBase.getTypePetition();

					if (typePettion.getEmailTemplate() != null) {
						email = typePettion.getEmailTemplate();

					}

					String subject = getSubject(email, requestBaseNew);
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatus(requestBaseNew.getNumRequest(),
									" de la Solicitud " + requestBaseNew.getTypePetition().getCode(), statusName,
									requestBaseNew.getOperator(), requestBaseNew.getRequestDate(), userLogin, senders,
									emailNotify, subject, requestBaseNew.getMotive(), note,
									"RM-P2-R5|Registro evidencia de instalación");
						} catch (Exception e) {
							Sentry.capture(e, "request");
						}

					});
					newThread.start();
				} else {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(31).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName = status.getName();
					String typeError = error.getName();
					EmailTemplate email = new EmailTemplate();
					TypePetition typePettion = requestBase.getTypePetition();

					if (typePettion.getEmailTemplate() != null) {
						email = typePettion.getEmailTemplate();

					}

					String subject = getSubject(email, requestBaseNew);
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatusError(typeError, requestBaseNew.getNumRequest(),
									" de la Solicitud " + requestBaseNew.getTypePetition().getCode(), statusName,
									requestBaseNew.getOperator(), requestBaseNew.getRequestDate(), userLogin, senders,
									emailNotify, subject, requestBaseNew.getMotive(), note,
									"RM-P2-R5|Registro evidencia de instalación");
						} catch (Exception e) {
							Sentry.capture(e, "release");
						}

					});
					newThread.start();
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "requestManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado de la solictud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteRequest/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRequest(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
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
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/cancelRequest", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRequest", required = true) Long idRequest) {
		JsonResponse res = new JsonResponse();
		try {
			RequestBase requestBase = requestBaseService.findById(idRequest);
			StatusRequest status = statusService.findByKey("name", "Anulado");
			requestBase.setStatus(status);
			requestBase.setOperator(getUserLogin().getFullName());
			requestBase.setMotive(status.getReason());
			requestBaseService.update(requestBase);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "requestManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	public void loadCountsRequest(HttpServletRequest request, Integer id) {
		Map<String, Integer> rfcC = new HashMap<String, Integer>();
		rfcC.put("draft", requestBaseR1Service.countByType(id, "Borrador", 2, null));
		rfcC.put("requested", requestBaseR1Service.countByType(id, "Solicitado", 2, null));
		rfcC.put("completed", requestBaseR1Service.countByType(id, "Completado", 2, null));
		rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
		request.setAttribute("rfcC", rfcC);

	}

	public String getSubject(EmailTemplate email, RequestBase request) {
		String temp = "";
		if (request.getTypePetition().getCode().equals("RM-P1-R4")) {

			/* ------ Subject ------ */
			if (email.getSubject().contains("{{requestNumber}}")) {
				email.setSubject(email.getSubject().replace("{{requestNumber}}",
						(request.getNumRequest() != null ? request.getNumRequest() : "")));
			}
			if (email.getSubject().contains("{{projectCode}}")) {
				String projectCode = request.getSystemInfo().getName() != null ? request.getSystemInfo().getName() : "";
				projectCode = projectCode.replace("\n", "<br>");
				email.setSubject(email.getSubject().replace("{{projectCode}}", projectCode));
			}

			if (email.getSubject().contains("{{systemMain}}")) {
				temp = "";
				Siges codeSiges = sigeService.findByKey("codeSiges", request.getCodeProyect());

				temp += codeSiges.getSystem().getName();

				email.setSubject(
						email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
			}

		} else if (request.getTypePetition().getCode().equals("RM-P1-R5")) {

			/* ------ Subject ------ */
			if (email.getSubject().contains("{{requestNumber}}")) {
				email.setSubject(email.getSubject().replace("{{requestNumber}}",
						(request.getNumRequest() != null ? request.getNumRequest() : "")));
			}

			if (email.getSubject().contains("{{projectCode}}")) {
				String projectCode = request.getSystemInfo().getName() != null ? request.getSystemInfo().getName() : "";
				projectCode = projectCode.replace("\n", "<br>");
				email.setSubject(email.getSubject().replace("{{projectCode}}", projectCode));
			}

			if (email.getSubject().contains("{{systemMain}}")) {
				temp = "";
				Siges codeSiges = sigeService.findByKey("codeSiges", request.getCodeProyect());

				temp += codeSiges.getSystem().getName();

				email.setSubject(
						email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
			}
		} else if (request.getTypePetition().getCode().equals("RM-P1-R2")) {

			/* ------ Subject ------ */
			if (email.getSubject().contains("{{requestNumber}}")) {
				email.setSubject(email.getSubject().replace("{{requestNumber}}",
						(request.getNumRequest() != null ? request.getNumRequest() : "")));
			}

			if (email.getSubject().contains("{{projectCode}}")) {
				String projectCode = request.getSystemInfo().getName() != null ? request.getSystemInfo().getName() : "";
				projectCode = projectCode.replace("\n", "<br>");
				email.setSubject(email.getSubject().replace("{{projectCode}}", projectCode));
			}

			if (email.getSubject().contains("{{systemMain}}")) {
				temp = "";
				Siges codeSiges = sigeService.findByKey("codeSiges", request.getCodeProyect());

				temp += codeSiges.getSystem().getName();

				email.setSubject(
						email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
			}
		} else if (request.getTypePetition().getCode().equals("RM-P1-R3")) {

			/* ------ Subject ------ */
			if (email.getSubject().contains("{{requestNumber}}")) {
				email.setSubject(email.getSubject().replace("{{requestNumber}}",
						(request.getNumRequest() != null ? request.getNumRequest() : "")));
			}

			if (email.getSubject().contains("{{projectCode}}")) {
				String projectCode = request.getSystemInfo().getName() != null ? request.getSystemInfo().getName() : "";
				projectCode = projectCode.replace("\n", "<br>");
				email.setSubject(email.getSubject().replace("{{projectCode}}", projectCode));
			}
			if (email.getSubject().contains("{{systemMain}}")) {
				temp = "";
				Siges codeSiges = sigeService.findByKey("codeSiges", request.getCodeProyect());

				temp += codeSiges.getSystem().getName();

				email.setSubject(
						email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
			}
		} else if (request.getTypePetition().getCode().equals("RM-P1-R1")) {
			/* ------ Subject ------ */
			if (email.getSubject().contains("{{codeOpor}}")) {
				email.setSubject(email.getSubject().replace("{{codeOpor}}",
						(request.getNumRequest() != null ? request.getNumRequest() : "")));
			}

			if (email.getSubject().contains("{{projectCode}}")) {
				String projectCode = request.getSystemInfo().getName() != null ? request.getSystemInfo().getName() : "";
				projectCode = projectCode.replace("\n", "<br>");
				email.setSubject(email.getSubject().replace("{{projectCode}}", projectCode));
			}
		}

		return email.getSubject();
	}

}