package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import com.soin.sgrm.model.DocsTemplate;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.Parameter;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.DocsTemplateService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ModifiedComponentService;
import com.soin.sgrm.service.ModuleService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.SystemEnvironmentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.ReleaseCreate;

@Controller
@RequestMapping("/release")
public class ReleaseController extends BaseController {

	@Autowired
	private UserInfoService loginService;
	@Autowired
	private ProjectService projectService;
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
	private DocsTemplateService docsTemplateService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private TypeObjectService typeObjectService;
	@Autowired
	private SystemEnvironmentService environmentService;
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

	public void loadInfoData(Model model, String name, int query, Object[] ids) {
		try {
			model.addAttribute("certification", releaseService.countByType(name, "Certificacion", query, ids));
			model.addAttribute("draft", releaseService.countByType(name, "Borrador", query, ids));
			model.addAttribute("review", releaseService.countByType(name, "En Revision", query, ids));
			model.addAttribute("completed", releaseService.countByType(name, "Completado", query, ids));
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemByUser(name));
			model.addAttribute("project", new Project());
			model.addAttribute("projects", projectService.listAll());

		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error en conteo de releases por pagina." + getErrorFormat(e));
		}

	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			UserInfo user = (UserInfo) request.getAttribute("userInfo");
			if (user.getIsReleaseManager() == 1) {
				return "redirect:/release/release-management";
			}

			String name = getUserName();
			loadInfoData(model, name, 1, null);
			model.addAttribute("list", "userRelease");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial." + " ERROR: " + e.getMessage());
			logs("RELEASE_ERROR", "Error en la carga de la pagina inicial." + getErrorFormat(e));
			return "redirect:/";
		}
		return "/release/release";

	}

	@RequestMapping(value = "/release-management", method = RequestMethod.GET)
	public String progress(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			UserInfo user = (UserInfo) request.getAttribute("userInfo");
			if (user.getIsReleaseManager() == 0) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos de release Manager.");
				return "redirect:/release/";
			}
			model.addAttribute("list", "systemRelease");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logs("RELEASE_ERROR", "Error en la carga de la pagina inicial/systemas." + getErrorFormat(e));
			return "redirect:/";
		}
		return "/release/releaseManager";
	}

	@RequestMapping(path = "/userRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getUserRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String name = getUserName(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch);
		} catch (SQLException ex) {
			logs("SYSTEM_ERROR", "Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error durante el proceso de paginacion de releases de usuario." + getErrorFormat(e));
		}
		return null;

	}

	@RequestMapping(path = "/teamRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getAllEmployees(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String name = getUserName(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch,
					systemService.myTeams(name));
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error durante el proceso de paginacion de releases de equipo." + getErrorFormat(e));
			return null;
		}

	}

	@RequestMapping(path = "/systemRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String name = getUserName(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch);
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error durante el proceso de paginacion de releases de sistema." + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(value = "/summary-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			ReleaseSummary release = null;
			if (isNumeric(status)) {
				release = releaseService.findById(Integer.parseInt(status));
			}

			if (release == null) {
				return "redirect:/";
			}
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());
			List<DocsTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			model.addAttribute("dependency", new Release());
			model.addAttribute("object", new ReleaseObject());
			model.addAttribute("doc", new DocsTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("systemConfiguration", systemConfiguration);
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logs("RELEASE_ERROR", "Error en la carga de la pagina resumen release." + getErrorFormat(e));
			return "redirect:/";
		}
		return "/release/summaryRelease";
	}

	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		try {
			model.addAttribute("parameter", status);
			ReleaseSummary release = null;
			if (isNumeric(status)) {
				release = releaseService.findById(Integer.parseInt(status));
			}

			if (release == null) {
				return "redirect:/";
			}
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());
			List<DocsTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			model.addAttribute("dependency", new Release());
			model.addAttribute("object", new ReleaseObject());
			model.addAttribute("doc", new DocsTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			model.addAttribute("systemConfiguration", systemConfiguration);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen release." + " ERROR: " + e.getMessage());
			logs("RELEASE_ERROR", "Error en la carga de la pagina resumen release." + getErrorFormat(e));
			return "redirect:/";
		}
		return "/release/tinySummaryRelease";
	}

	@RequestMapping(value = "/editRelease-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable String id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		ReleaseEdit release = new ReleaseEdit();
		SystemConfiguration systemConfiguration = new SystemConfiguration();

		try {
			if (id == null) {
				return "redirect:/";
			}

			Integer idRelease = Integer.parseInt(id);
			release = releaseService.findEditById(idRelease);

			if (release == null) {
				return "/plantilla/404";
			}

			if (!release.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Release no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			if (release.getUser().getId() != getUserId()) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el release.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			systemConfiguration = systemConfigurationService.findBySystemId(release.getSystem().getId());
			List<DocsTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());

			if (release.getSystem().getImportObjects()) {
				model.addAttribute("typeDetailList", typeDetail.list());
			}

			model.addAttribute("systems", systemService.listSystemByUser(getUserName()));
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
			model.addAttribute("doc", new DocsTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);

			return "/release/editRelease";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("data", getErrorFormat(e));
			logs("RELEASE_ERROR", "Error en la carga de la pagina editRelease." + getErrorFormat(e));
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
			String number_release = "";
			ReleaseEdit release = releaseService.findEditById(Integer.parseInt(idRelease));

			ReleaseEdit releaseCopy = (ReleaseEdit) release.clone();
			if (!requeriment.equals("TPO/BT")) {
				number_release = generateReleaseNumber(requeriment, requirement_name,
						releaseCopy.getSystem().getName());
			} else {
				number_release = generateTPO_BT_ReleaseNumber((releaseCopy.getSystem().getName()), requirement_name);
			}

			releaseCopy.setId(0);
			releaseCopy.setDescription(description);
			releaseCopy.setObservations(observations);
			releaseCopy.setReleaseNumber(number_release);
			releaseCopy.clearObjectsCopy();
			releaseCopy.copyObjects(releaseCopy, release);
			releaseCopy.setCreateDate(getSystemTimestamp());
			releaseCopy.setUser(getUseInfo());
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
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus("exception");
			res.setException(e.getMessage());
			logs("RELEASE_ERROR", "Error en la copia del release-copy." + getErrorFormat(e));
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
		try {
			res.setStatus("success");
			if (!requeriment.equals("TPO/BT")) {
				number_release = generateReleaseNumber(requeriment, requirement_name, system_id);
			} else {
				number_release = generateTPO_BT_ReleaseNumber(system_id, requirement_name);
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
			release.setUser_id(getUserId());
			release.setStatus(statusService.findByName("Borrador"));
			release.setModule(module);
			release.setCreateDate(getSystemTimestamp());

			release.setReportHaveArt(false);
			release.setReportfixedTelephony(false);
			release.setReportHistoryTables(false);
			release.setReportNotHaveArt(false);
			release.setReportMobileTelephony(false);
			release.setReportTemporaryTables(false);

			release.setBilledCalls(false);
			release.setNotBilledCalls(false);

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
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException(e.getMessage());
			logs("RELEASE_ERROR", "Error en la generacion del release-generate." + getErrorFormat(e));
		}
		return res;
	}

	public String generateReleaseNumber(String requeriment, String requirement_name, String system_id) {
		String number_release = "";
		String partCode = "";
		try {

			switch (requeriment) {
			case "IN":
				// Si no comienza con IN, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("IN")) {
					partCode = system_id + "." + "IN" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "PR":
				// Si no comienza con PR, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("PR")) {
					partCode = system_id + "." + "PR" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "SS":
				// Si no comienza con SS, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("SS")) {
					partCode = system_id + "." + "SS" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "SO-ICE":
				partCode = system_id + "." + "SO-ICE" + requirement_name;
				number_release = verifySecuence(partCode);
				break;
			default:
				number_release = "Sin Asignar";
				break;
			}
		} catch (Exception e) {
			number_release = "Sin Asignar";
			logs("RELEASE_ERROR", "Error en la generacion del numero de release." + getErrorFormat(e));
		}
		return number_release;
	}

	public String verifySecuence(String partCode) {
		try {
			int amount = releaseService.existNumRelease(partCode);

			if (amount == 0) {
				return partCode + "." + getSystemDate("yyyyMMdd");
			} else {
				return partCode + "_" + (amount + 1) + "." + getSystemDate("yyyyMMdd");
			}

		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error en la generacion del numero de release VerifySecuence." + getErrorFormat(e));
		}
		return "Sin Asignar";

	}

	public String generateTPO_BT_ReleaseNumber(String system_id, String requirement_name) {

		try {
			String ids = requirement_name;
			String[] words = ids.split(",");
			String partCode = "";
			int id = Integer.parseInt(words[0]);
			String number_release = "";
			Request request = requestService.findById(id);
			String[] code = request.getCode_soin().split("-");
			String codeSOIN = "";
			String codeICE = request.getCode_ice();
			for (int i = 0; i < code.length; i++) {
				if (i <= 1) {
					codeSOIN += code[i];
				} else {
					codeSOIN += "-" + code[i];
				}
			}
			// Esto es para los releases de ATV
			if (request.getCode_soin().substring(0, 2).toString().equals("R-")) {
				partCode = system_id + "." + codeSOIN;
			} else {
				// en caso de las BT el codeICE es null por lo que no es requerido.
				if (codeICE == null) {
					partCode = system_id + "." + codeSOIN;
				} else {
					partCode = system_id + "." + codeSOIN + "." + codeICE;
				}
			}
			partCode = partCode.replaceAll("\\s", ""); // Se eliminan los espacios en blanco
			number_release = verifySecuence(partCode);
			return number_release;
		} catch (Exception e) {
			logs("RELEASE_ERROR",
					"Error en la generacion del numero de release generateTPO_BT_ReleaseNumber." + getErrorFormat(e));
		}
		return "Sin Asignar";
	}

	@RequestMapping(value = "/deleteRelease/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRelease(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			ReleaseEdit release = releaseService.findEditById(id);
			if (release.getStatus().getName().equals("Borrador")) {
				if (release.getUser().getId() != getUserId()) {
					res.setStatus("fail");
					res.setException("No tiene permisos sobre el release.");
				} else {
					Status status = statusService.findByName("Anulado");
					release.setStatus(status);
					releaseService.cancelRelease(release);
				}
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
			}
		} catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logs("RELEASE_ERROR", "Error al eliminar el release: " + id.toString() + ". " + getErrorFormat(e));
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
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logs("RELEASE_ERROR", "Error al eliminar el release: " + idRelease + ". " + getErrorFormat(e));
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

			for (Integer to_id : rc.getDependenciesFunctionals()) {
				dependency = new Dependency();
				dependency.setId(0);
				dependency.setRelease(releaseService.findReleaseUserById(Integer.parseInt(rc.getRelease_id())));
				dependency.setTo_release(releaseService.findReleaseById(to_id));
				dependency.setMandatory(release.isMandatory(dependency));
				dependency.setIsFunctional(true);
				dependencies.add(dependency);
			}
			for (Integer to_id : rc.getDependenciesTechnical()) {
				dependency = new Dependency();
				dependency.setId(0);
				dependency.setRelease(releaseService.findReleaseUserById(Integer.parseInt(rc.getRelease_id())));
				dependency.setTo_release(releaseService.findReleaseById(to_id));
				dependency.setMandatory(release.isMandatory(dependency));
				dependency.setIsFunctional(false);
				dependencies.add(dependency);
			}

			release.checkModifiedComponents(modifiedComponents);
			release.checkAmbientsExists(ambients);
			release.checkDependenciesExists(dependencies);
			releaseService.saveRelease(release, rc);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}
		} catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al guardar el release: " + rc.getReleaseNumber() + "." + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/updateRelease/{releaseId}", method = RequestMethod.GET)
	public String updateRelease(@PathVariable String releaseId, HttpServletRequest request, Locale locale,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Release release = null;

			if (isNumeric(releaseId)) {
				release = releaseService.findReleaseById(Integer.parseInt(releaseId));
			}
			// Si el release no existe se regresa al inicio.
			if (release == null) {
				return "redirect:/";
			}
			// Se cambia el estado a Solicitado
			Status status = statusService.findByName("Solicitado");
			release.setStatus(status);
			if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
				if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
					EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
					Release releaseEmail = release;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMail(releaseEmail, email);
						} catch (Exception e) {
							// log para error
							e.printStackTrace();
						}

					});
					newThread.start();
				}
			}
//			releaseService.requestRelease(release);
			return "redirect:/release/summary-" + release.getId();
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error en la carga de la pagina resumen release." + getErrorFormat(e));
		}

		return "redirect:/";
	}

	public ArrayList<MyError> validSections(Release release, ArrayList<MyError> errors, ReleaseCreate rc) {
		// Se verifican las secciones que se deben validar por release.
		try {
			SystemConfiguration systemConfiguration = systemConfigurationService
					.findBySystemId(release.getSystem().getId());

			if (systemConfiguration.getGeneralInfo())
				errors = rc.validGeneralInformation(rc, errors);

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
				if (rc.getObjectItemConfiguration().size() == 0) {
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
			logs("RELEASE_ERROR", "Error en la validacion de secciones. " + getErrorFormat(e));
		}
		return errors;

	}

}
