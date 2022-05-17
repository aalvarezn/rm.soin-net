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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PParameter;
import com.soin.sgrm.model.pos.PPriority;

import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.EmailTemplateService;
import com.soin.sgrm.service.pos.ImpactService;
import com.soin.sgrm.service.pos.ParameterService;
import com.soin.sgrm.service.pos.PriorityService;
import com.soin.sgrm.service.pos.RFCService;
import com.soin.sgrm.service.pos.ReleaseService;
import com.soin.sgrm.service.pos.SigesService;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PTypeChange;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.service.pos.StatusService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.service.pos.TypeChangeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.ReleaseCreate;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {

	@Autowired
	PriorityService priorityService;

	@Autowired
	RFCService rfcService;

	@Autowired
	StatusService statusService;

	@Autowired
	SystemService systemService;

	@Autowired
	SigesService sigeService;

	@Autowired
	ImpactService impactService;

	@Autowired
	TypeChangeService typeChangeService;

	@Autowired
	ReleaseService releaseService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	EmailTemplateService emailService;

	public static final Logger logger = Logger.getLogger(RFCController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			PUser userLogin = getUserLogin();
			loadCountsRelease(request, userLogin.getName());
			List<PSystem> systems = systemService.listProjects(userLogin.getId());
			List<PPriority> priorities = priorityService.findAll();
			List<PStatus> statuses = statusService.findAll();
			List<PImpact> impacts = impactService.findAll();
			model.addAttribute("priorities", priorities);
			model.addAttribute("impacts", impacts);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PRFC> rfcs = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String name = getUserLogin().getUsername();
			String sSearch = request.getParameter("sSearch");
			 Long statusId;
			 Long priorityId;
			 Long impactId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("priorityId").equals("")) {
				priorityId = null;
			} else {
				priorityId = (long) Integer.parseInt(request.getParameter("priorityId"));
			}
			
			if (request.getParameter("impactId").equals("")) {
				impactId = null;
			} else {
				impactId = (long) Integer.parseInt(request.getParameter("impactId"));
			}
			String dateRange = request.getParameter("dateRange");

			rfcs = rfcService.findAll(name,sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,priorityId, impactId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(value = { "/changeProject/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<PSiges> changeProject(@PathVariable Long id, Locale locale, Model model) {
		List<PSiges> codeSiges = null;
		try {
			codeSiges = sigeService.listCodeSiges(id);
		} catch (Exception e) {
			Sentry.capture(e, "siges");

			e.printStackTrace();
		}

		return codeSiges;
	}

	@RequestMapping(value = { "/changeRelease" }, method = RequestMethod.GET)
	public @ResponseBody com.soin.sgrm.utils.JsonSheet<?> changeRelease(HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {

		try {
			Long systemId;
			String sSearch = request.getParameter("sSearch");
			if (request.getParameter("systemId").equals("")) {
				systemId = (long) 0;
			} else {
				systemId = (long) Integer.parseInt(request.getParameter("systemId"));
			}

			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listReleasesBySystem(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
		} catch (Exception e) {
			Sentry.capture(e, "siges");

			return null;
		}

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PRFC addRFC) {
		JsonResponse res = new JsonResponse();
		try {
			PUser userLogin = getUserLogin();
			PStatus status = statusService.findByKey("code", "draft");
			addRFC.setStatus(status);
			addRFC.setUser(userLogin);
			addRFC.setRequiredBD(false);
			addRFC.setRequestDate(CommonUtils.getSystemTimestamp());
			res.setStatus("success");
			addRFC.setMotive("Inicio de RFC");	
			addRFC.setOperator(userLogin.getName());
			addRFC.setNumRequest(rfcService.generateRFCNumber(addRFC.getCodeProyect()));

			rfcService.save(addRFC);
			res.setData(addRFC.getId().toString());
			res.setMessage("Se creo correctamente el RFC!");
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setMessage("Error al crear RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/saveRFC", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody PRFC addRFC) {

		JsonResponse res = new JsonResponse();
		PPriority priority = null;
		PImpact impact = null;
		PTypeChange typeChange = null;
		ArrayList<MyError> errors = new ArrayList<MyError>();
		List<PRelease> listRelease = new ArrayList<PRelease>();

		try {
			errors = validSections(addRFC, errors);
			PRFC rfcMod = rfcService.findById(addRFC.getId());
			addRFC.setUser(rfcMod.getUser());
			addRFC.setNumRequest(rfcMod.getNumRequest());
			addRFC.setCodeProyect(rfcMod.getCodeProyect());
			addRFC.setStatus(rfcMod.getStatus());
			addRFC.setMotive(rfcMod.getMotive());
			addRFC.setOperator(rfcMod.getUser().getName());
			addRFC.setRequestDate(rfcMod.getRequestDate());
			addRFC.setFiles(rfcMod.getFiles());
			if (addRFC.getImpactId() != null) {
				impact = impactService.findById(addRFC.getImpactId());
				addRFC.setImpact(impact);
			}

			if (addRFC.getPriorityId() != null) {
				priority = priorityService.findById(addRFC.getPriorityId());
				addRFC.setPriority(priority);
			}
			if (addRFC.getTypeChangeId() != null) {
				typeChange = typeChangeService.findById(addRFC.getTypeChangeId());
				addRFC.setTypeChange(typeChange);

			}
			if (addRFC.getReleasesList() != null) {
				JSONArray jsonArray = new JSONArray(addRFC.getReleasesList());

				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						System.out.println(object.getInt(("id")));
						PRelease release = releaseService.findById(Long.valueOf(object.getInt(("id"))));
						listRelease.add(release);

					}
					addRFC.setReleases(Sets.newHashSet(listRelease));
				}
			}

			rfcService.update(addRFC);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setException("Error al guardar el rfc: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/editRFC-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		PRFC rfcEdit = new PRFC();
		PUser user = getUserLogin();
		List<PSystem> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			rfcEdit = rfcService.findById(id);

			if (rfcEdit == null) {
				return "/plantilla/404";
			}

			if (!rfcEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "RFC no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.findAll());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.findAll());
			model.addAttribute("rfc", rfcEdit);

			return "/rfc/editRFC";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/getRFC-{id}", method = RequestMethod.GET)
	public @ResponseBody PRFC getRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		PRFC rfcEdit = new PRFC();

		try {

			rfcEdit = rfcService.findById(id);
			return rfcEdit;

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return rfcEdit;
	}

	@RequestMapping(value = "/summaryRFC-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		PUser user = getUserLogin();
		List<PSystem> systems = systemService.listProjects(user.getId());
		try {
			model.addAttribute("parameter", status);
			PRFC rfc = null;
			if (CommonUtils.isNumeric(status)) {
				rfc = rfcService.findById(Long.parseLong(status));
			}

			if (rfc == null) {
				return "redirect:/";
			}
			PSiges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsImplicated = new ArrayList<String>();

			systemsImplicated.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<PRelease> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (PRelease release : releases) {
						nameSystem = release.getSystem().getName();
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
			model.addAttribute("statuses", statusService.findAll());
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.findAll());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.findAll());
			model.addAttribute("codeSiges", codeSiges);
			model.addAttribute("systemsImplicated", systemsImplicated);
			model.addAttribute("rfc", rfc);

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen rfc." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/rfc/summaryRFC";
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		PUser user = getUserLogin();
		List<PSystem> systems = systemService.listProjects(user.getId());
		try {
			model.addAttribute("parameter", status);
			PRFC rfc = null;
			if (CommonUtils.isNumeric(status)) {
				rfc = rfcService.findById(Long.parseLong(status));
			}

			if (rfc == null) {
				return "redirect:/";
			}
			PSiges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsImplicated = new ArrayList<String>();

			systemsImplicated.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<PRelease> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (PRelease release : releases) {
						nameSystem = release.getSystem().getName();
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
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.findAll());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.findAll());
			model.addAttribute("codeSiges", codeSiges);
			model.addAttribute("systemsImplicated", systemsImplicated);
			model.addAttribute("rfc", rfc);

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen rfc." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/rfc/tinySummaryRFC";
	}

	@RequestMapping(value = "/updateRFC/{rfcId}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable String rfcId, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			PRFC rfc = null;

			if (CommonUtils.isNumeric(rfcId)) {
				rfc = rfcService.findById((long) Integer.parseInt(rfcId));
			}
			// Si el release no existe se regresa al inicio.
			if (rfc == null) {
				return "redirect:/";
			}
			// Verificar si existe un flujo para el sistema

			PStatus status = statusService.findByKey("name", "Solicitado");

//			if (node != null)
//				release.setNode(node);

			rfc.setStatus(status);
			rfc.setMotive(status.getReason());
		
			
			 rfc.setOperator(getUserLogin().getName());

			if (Boolean.valueOf(parameterService.getParameterByCode((long) 1).getParamValue())) {
				if (emailService.findByKey("name", "RFC Solicitado") != null) {
					PEmailTemplate email = emailService.findByKey("name", "RFC Solicitado");
					PRFC rfcEmail = rfc;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailRFC(rfcEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "rfc");
						}

					});
					newThread.start();
				}
			}
			/*
			 * // si tiene un nodo y ademas tiene actor se notifica por correo if (node !=
			 * null && node.getActors().size() > 0) { Integer idTemplate =
			 * Integer.parseInt(paramService.findByCode(22).getParamValue()); EmailTemplate
			 * emailActor = emailService.findById(idTemplate); WFRelease releaseEmail = new
			 * WFRelease(); releaseEmail.convertReleaseToWFRelease(release); Thread
			 * newThread = new Thread(() -> { try { emailService.sendMailActor(releaseEmail,
			 * emailActor); } catch (Exception e) { Sentry.capture(e, "release"); }
			 * 
			 * });
			 * 
			 * newThread.start(); }
			 */

			rfcService.update(rfc);

			return "redirect:/rfc/summaryRFC-" + rfc.getId();
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	public ArrayList<MyError> validSections(PRFC rfc, ArrayList<MyError> errors) {

		if (rfc.getImpactId() == null)
			errors.add(new MyError("impactId", "Valor requerido."));

		if (rfc.getTypeChangeId() == null)
			errors.add(new MyError("typeChangeId", "Valor requerido."));

		if (rfc.getPriorityId() == null)
			errors.add(new MyError("priorityId", "Valor requerido."));

		if (rfc.getRequestDateBegin().equals(""))
			errors.add(new MyError("dateBegin", "Valor requerido."));

		if (rfc.getRequestDateBegin().equals(""))
			errors.add(new MyError("dateFinish", "Valor requerido."));

		if (rfc.getReasonChange().equals(""))
			errors.add(new MyError("rfcReason", "Valor requerido."));

		if (rfc.getEffect().equals(""))
			errors.add(new MyError("rfcEffect", "Valor requerido."));

		if (rfc.getDetail().trim().equals(""))
			errors.add(new MyError("detailRFC", "Valor requerido."));

		if (rfc.getReturnPlan().trim().equals(""))
			errors.add(new MyError("returnPlanRFC", "Valor requerido."));

		if (rfc.getEvidence().trim().equals(""))
			errors.add(new MyError("evidenceRFC", "Valor requerido."));

		if (rfc.getRequestEsp().equals(""))
			errors.add(new MyError("requestEspRFC", "Valor requerido."));
		return errors;
	}

	@RequestMapping(value = "/deleteRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PRFC rfc = rfcService.findById(id);
			if (rfc.getStatus().getName().equals("Borrador")) {
				if (rfc.getUser().getUsername().equals(getUserLogin().getUsername())) {
					PStatus status = statusService.findByKey("name", "Anulado");
					rfc.setStatus(status);
					rfc.setMotive(status.getReason());
					rfc.setOperator(getUserLogin().getName());
					rfcService.update(rfc);
				} else {
					res.setStatus("fail");
					res.setException("No tiene permisos sobre el rfc.");
				}
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	public void loadCountsRelease(HttpServletRequest request, String name) {
		//PUser userLogin = getUserLogin();
		//List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", rfcService.countByType(name, "Borrador", 1, null));
		userC.put("requested", rfcService.countByType(name, "Solicitado", 1, null));
		userC.put("completed", rfcService.countByType(name, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);
		
	}

}