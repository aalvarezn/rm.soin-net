package com.soin.sgrm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectClean;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.Tree;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.RFCWithoutReleaseService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCTrackingShow;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.model.pos.PReleaseObjectClean;
import com.soin.sgrm.model.pos.PReleaseTrackingShow;
import com.soin.sgrm.model.pos.PRelease_RFCFast;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.model.pos.PTypeChange;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.wf.PNodeRFC;
import com.soin.sgrm.model.pos.wf.PWFRFC;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeRFC;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PErrorRFCService;
import com.soin.sgrm.service.pos.PImpactService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.service.pos.PRFCService;
import com.soin.sgrm.service.pos.PRFCWithoutReleaseService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTreeService;
import com.soin.sgrm.service.pos.PTypeChangeService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.service.pos.wf.PNodeService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {
	@Autowired
	PriorityService priorityService;

	@Autowired
	RFCService rfcService;

	@Autowired
	RFCWithoutReleaseService rfcWRService;

	@Autowired
	StatusRFCService statusService;

	@Autowired
	StatusService statusReleaseService;

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

	@Autowired
	TreeService treeService;

	@Autowired
	ErrorRFCService errorService;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	@Autowired
	EmailReadService emailReadService;

	@Autowired
	private NodeService nodeService;
	@Autowired
	private ParameterService paramService;

	@Autowired
	PPriorityService ppriorityService;

	@Autowired
	PRFCService prfcService;

	@Autowired
	PRFCWithoutReleaseService prfcWRService;

	@Autowired
	PStatusRFCService pstatusService;

	@Autowired
	PStatusService pstatusReleaseService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PSigesService psigeService;

	@Autowired
	PImpactService pimpactService;

	@Autowired
	PTypeChangeService ptypeChangeService;

	@Autowired
	PReleaseService preleaseService;

	@Autowired
	PParameterService pparameterService;

	@Autowired
	PEmailTemplateService pemailService;

	@Autowired
	PTreeService ptreeService;

	@Autowired
	PErrorRFCService perrorService;

	@Autowired
	PUserService puserService;

	@Autowired
	private PNodeService pnodeService;


	public static final Logger logger = Logger.getLogger(RFCController.class);

	private final Environment environment;

	@Autowired
	public RFCController(Environment environment) {
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
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);

			if (profileActive().equals("oracle")) {
				List<System> systems = systemService.listProjects(getUserLogin().getId());
				List<Priority> priorities = priorityService.list();
				List<StatusRFC> statuses = statusService.findAll();
				List<Impact> impacts = impactService.list();
				model.addAttribute("priorities", priorities);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
			} else if (profileActive().equals("postgres")) {
				List<PSystem> systems = psystemService.listProjects(getUserLogin().getId());
				List<PPriority> priorities = ppriorityService.list();
				List<PStatusRFC> statuses = pstatusService.findAll();
				List<PImpact> impacts = pimpactService.list();
				model.addAttribute("priorities", priorities);
				model.addAttribute("impacts", impacts);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systems);
			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

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
			if (profileActive().equals("oracle")) {
				JsonSheet<RFC_WithoutRelease> rfcs = new JsonSheet<>();
				rfcs = rfcWRService.findAll2(name, sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,
						priorityId, systemId);
				return rfcs;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PRFC_WithoutRelease> rfcs = new JsonSheet<>();
				rfcs = prfcWRService.findAll2(name, sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,
						priorityId, systemId);
				return rfcs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = { "/changeProject/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> changeProject(@PathVariable Integer id, Locale locale, Model model) {
		if (profileActive().equals("oracle")) {
			try {
				List<Siges> codeSiges = null;
				codeSiges = sigeService.listCodeSiges(id);
				return codeSiges;
			} catch (Exception e) {
				Sentry.capture(e, "siges");

				e.printStackTrace();
			}
		} else if (profileActive().equals("postgres")) {
			try {
				List<PSiges> codeSiges = null;
				codeSiges = psigeService.listCodeSiges(id);
				return codeSiges;
			} catch (Exception e) {
				Sentry.capture(e, "siges");

				e.printStackTrace();
			}
		}

		return null;

	}

	@RequestMapping(value = { "/changeRelease" }, method = RequestMethod.GET)
	public @ResponseBody com.soin.sgrm.utils.JsonSheet<?> changeRelease(HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {

		try {
			Integer systemId;
			String sSearch = request.getParameter("sSearch");
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			if (profileActive().equals("oracle")) {
				return releaseService.listReleasesBySystem(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
			} else if (profileActive().equals("postgres")) {
				return preleaseService.listReleasesBySystem(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
			}

		} catch (Exception e) {
			Sentry.capture(e, "siges");
			return null;

		}
		return null;

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody RFC addRFC) {
		JsonResponse res = new JsonResponse();
		try {

			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				StatusRFC status = statusService.findByKey("code", "draft");
				if (status != null) {
					addRFC.setStatus(status);
					addRFC.setUser(user);
					addRFC.setRequiredBD(false);
					addRFC.setRequestDate(CommonUtils.getSystemTimestamp());
					res.setStatus("success");
					addRFC.setMotive("Inicio de RFC");
					addRFC.setOperator(user.getFullName());
					Siges codeSiges = sigeService.findById(addRFC.getCodeSigesId());
					addRFC.setSiges(codeSiges);
					addRFC.setCodeProyect(codeSiges.getCodeSiges());
					SystemInfo systemInfo = systemService.findById(addRFC.getSystemId());
					addRFC.setSystemInfo(systemInfo);
					addRFC.setNumRequest(rfcService.generateRFCNumber(addRFC.getCodeProyect(), systemInfo.getCode()));
					rfcService.save(addRFC);
					res.setData(addRFC.getId().toString());
					res.setMessage("Se creo correctamente el RFC!");
				} else {
					res.setStatus("exception");
					res.setMessage("Error al crear RFC comunicarse con los administradores!");
				}
			} else if (profileActive().equals("postgres")) {
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				PStatusRFC status = pstatusService.findByKey("code", "draft");
				if (status != null) {
					PRFC paddRFC = new PRFC();
					paddRFC.setStatus(status);
					paddRFC.setUser(user);
					paddRFC.setRequiredBD(false);
					paddRFC.setRequestDate(CommonUtils.getSystemTimestamp());
					res.setStatus("success");
					paddRFC.setMotive("Inicio de RFC");
					paddRFC.setOperator(user.getFullName());
					PSiges codeSiges = psigeService.findById(addRFC.getCodeSigesId());
					paddRFC.setSiges(codeSiges);
					paddRFC.setCodeProyect(codeSiges.getCodeSiges());
					PSystemInfo systemInfo = psystemService.findById(addRFC.getSystemId());
					paddRFC.setSystemInfo(systemInfo);
					paddRFC.setNumRequest(prfcService.generateRFCNumber(paddRFC.getCodeProyect(), systemInfo.getCode()));
					prfcService.save(paddRFC);
					res.setData(paddRFC.getId().toString());
					res.setMessage("Se creo correctamente el RFC!");
				} else {
					res.setStatus("exception");
					res.setMessage("Error al crear RFC comunicarse con los administradores!");
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setMessage("Error al crear RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveRFC", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody RFC addRFC) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				Priority priority = null;
				Impact impact = null;
				TypeChange typeChange = null;
				ArrayList<MyError> errors = new ArrayList<MyError>();
				List<Release_RFCFast> listRelease = new ArrayList<Release_RFCFast>();
				errors = validSections(addRFC, errors);
				RFC rfcMod = rfcService.findById(addRFC.getId());
				addRFC.setUser(rfcMod.getUser());
				addRFC.setNumRequest(rfcMod.getNumRequest());
				addRFC.setCodeProyect(rfcMod.getCodeProyect());
				addRFC.setStatus(rfcMod.getStatus());
				addRFC.setMotive(rfcMod.getMotive());
				addRFC.setSiges(rfcMod.getSiges());
				addRFC.setSystemInfo(rfcMod.getSystemInfo());
				addRFC.setOperator(rfcMod.getUser().getFullName());
				addRFC.setRequestDate(rfcMod.getRequestDate());
				addRFC.setFiles(rfcMod.getFiles());
				if (addRFC.getImpactId() != 0) {
					impact = impactService.findById(addRFC.getImpactId());
					addRFC.setImpact(impact);
				}

				if (addRFC.getPriorityId() != 0) {
					priority = priorityService.findById(addRFC.getPriorityId());
					addRFC.setPriority(priority);
				}
				if (addRFC.getTypeChangeId() != null) {
					typeChange = typeChangeService.findById(addRFC.getTypeChangeId());
					addRFC.setTypeChange(typeChange);

				}
				if (addRFC.getSenders().length() < 256) {
					addRFC.setSenders(addRFC.getSenders());
				} else {
					addRFC.setSenders(rfcMod.getSenders());
				}
				if (addRFC.getMessage().length() < 256) {
					addRFC.setMessage(addRFC.getMessage());
				} else {
					addRFC.setMessage(rfcMod.getMessage());
				}
				if (addRFC.getReleasesList() != null) {
					JSONArray jsonArray = new JSONArray(addRFC.getReleasesList());
					Set<Release_RFCFast> listReleasesOld = rfcMod.getReleases();
					Boolean read = false;
					String dbScheme = addRFC.getSchemaDB();
					if (listReleasesOld.size() < jsonArray.length()) {
						if (jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);

								Release_RFCFast release = releaseService.findRelease_RFCByIdFast(object.getInt(("id")));
								Set<ReleaseObjectClean> releaseObjects = release.getObjects();
								for (ReleaseObjectClean releaseObject : releaseObjects) {

									if (releaseObject.getIsSql() == 1) {
										String scheme = releaseObject.getDbScheme();
										addRFC.setRequiredBD(true);
										if (dbScheme.trim().equals("")) {
											dbScheme = scheme;
										} else {
											String[] split = dbScheme.split(",");
											boolean verify = ArrayUtils.contains(split, scheme);
											if (!verify) {
												dbScheme = dbScheme + "," + scheme;
											}
										}
									}
								}
								listRelease.add(release);

							}
							addRFC.setSchemaDB(dbScheme);
							addRFC.setReleases(Sets.newHashSet(listRelease));
						}
					} else if (listReleasesOld.size() > jsonArray.length()) {
						if (jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);

								Release_RFCFast release = releaseService.findRelease_RFCByIdFast(object.getInt(("id")));
								Set<ReleaseObjectClean> releaseObjects = release.getObjects();
								for (ReleaseObjectClean releaseObject : releaseObjects) {

									if (releaseObject.getIsSql() == 1) {
										String scheme = releaseObject.getDbScheme();
										addRFC.setRequiredBD(true);
										if (dbScheme.trim().equals("")) {
											dbScheme = scheme;
										} else {
											String[] split = dbScheme.split(",");
											boolean verify = ArrayUtils.contains(split, scheme);
											if (!verify) {
												dbScheme = dbScheme + "," + scheme;
											}
										}
									}
								}
								listRelease.add(release);

							}
							addRFC.setSchemaDB(dbScheme);
							addRFC.setReleases(Sets.newHashSet(listRelease));
						}

					} else if (listReleasesOld.size() == jsonArray.length()) {
						List<Integer> listReleaseOld = new ArrayList<Integer>();
						List<Integer> listReleaseNew = new ArrayList<Integer>();
						for (Release_RFCFast releaseOld : listReleasesOld) {
							listReleaseOld.add(releaseOld.getId());
						}
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							listReleaseNew.add(object.getInt(("id")));
						}
						Collections.sort(listReleaseNew);
						Collections.sort(listReleaseOld);
						if (listReleaseNew.equals(listReleaseOld)) {
							java.lang.System.out.println("Son iguales");
						} else {
							read = true;
						}
						if (read) {
							if (jsonArray.length() != 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray.getJSONObject(i);

									Release_RFCFast release = releaseService
											.findRelease_RFCByIdFast(object.getInt(("id")));
									Set<ReleaseObjectClean> releaseObjects = release.getObjects();
									for (ReleaseObjectClean releaseObject : releaseObjects) {

										if (releaseObject.getIsSql() == 1) {
											String scheme = releaseObject.getDbScheme();
											addRFC.setRequiredBD(true);
											if (dbScheme.trim().equals("")) {
												dbScheme = scheme;
											} else {
												String[] split = dbScheme.split(",");
												boolean verify = ArrayUtils.contains(split, scheme);
												if (!verify) {
													dbScheme = dbScheme + "," + scheme;
												}
											}
										}
									}
									listRelease.add(release);

								}
								addRFC.setSchemaDB(dbScheme);
								addRFC.setReleases(Sets.newHashSet(listRelease));
							}
						} else {
							addRFC.setSchemaDB(dbScheme);
							addRFC.setReleases(Sets.newHashSet(listReleasesOld));
						}

					}

				}

				rfcService.update(addRFC);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
			} else if (profileActive().equals("postgres")) {
				PPriority priority = null;
				PImpact impact = null;
				PTypeChange typeChange = null;
				ArrayList<MyError> errors = new ArrayList<MyError>();
				List<PRelease_RFCFast> listRelease = new ArrayList<PRelease_RFCFast>();
				errors = validSections(addRFC, errors);
				PRFC rfcMod = prfcService.findById(addRFC.getId());
				PRFC paddRFC = new PRFC();
				paddRFC.setId(addRFC.getId());
				paddRFC.setUser(rfcMod.getUser());
				paddRFC.setNumRequest(rfcMod.getNumRequest());
				paddRFC.setCodeProyect(rfcMod.getCodeProyect());
				paddRFC.setStatus(rfcMod.getStatus());
				paddRFC.setMotive(rfcMod.getMotive());
				paddRFC.setSiges(rfcMod.getSiges());
				paddRFC.setSystemInfo(rfcMod.getSystemInfo());
				paddRFC.setOperator(rfcMod.getUser().getFullName());
				paddRFC.setRequestDate(rfcMod.getRequestDate());
				paddRFC.setFiles(rfcMod.getFiles());
				paddRFC.setDetail(addRFC.getDetail());
				paddRFC.setEvidence(addRFC.getEvidence());
				paddRFC.setReturnPlan(addRFC.getReturnPlan());
				paddRFC.setRequestEsp(addRFC.getRequestEsp());
				paddRFC.setReasonChange(addRFC.getReasonChange());
				paddRFC.setEffect(addRFC.getEffect());
				paddRFC.setRequestDateBegin(addRFC.getRequestDateBegin());
				paddRFC.setRequestDateFinish(addRFC.getRequestDateFinish());
				paddRFC.setRequiredBD(addRFC.getRequiredBD());
				paddRFC.setSchemaDB(addRFC.getSchemaDB());
				if (addRFC.getImpactId() != 0) {
					impact = pimpactService.findById(addRFC.getImpactId());
					paddRFC.setImpact(impact);
				}

				if (addRFC.getPriorityId() != 0) {
					priority = ppriorityService.findById(addRFC.getPriorityId());
					paddRFC.setPriority(priority);
				}
				if (addRFC.getTypeChangeId() != null) {
					typeChange = ptypeChangeService.findById(addRFC.getTypeChangeId());
					paddRFC.setTypeChange(typeChange);

				}
				if (addRFC.getSenders().length() < 256) {
					paddRFC.setSenders(addRFC.getSenders());
				} else {
					paddRFC.setSenders(rfcMod.getSenders());
				}
				if (addRFC.getMessage().length() < 256) {
					paddRFC.setMessage(addRFC.getMessage());
				} else {
					paddRFC.setMessage(rfcMod.getMessage());
				}
				if (addRFC.getReleasesList() != null) {
					JSONArray jsonArray = new JSONArray(addRFC.getReleasesList());
					Set<PRelease_RFCFast> listReleasesOld = rfcMod.getReleases();
					Boolean read = false;
					String dbScheme = addRFC.getSchemaDB();
					if (listReleasesOld.size() < jsonArray.length()) {
						if (jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);

								PRelease_RFCFast release = preleaseService.findRelease_RFCByIdFast(object.getInt(("id")));
								Set<PReleaseObjectClean> releaseObjects = release.getObjects();
								for (PReleaseObjectClean releaseObject : releaseObjects) {

									if (releaseObject.getIsSql() == 1) {
										String scheme = releaseObject.getDbScheme();
										paddRFC.setRequiredBD(true);
										if (dbScheme.trim().equals("")) {
											dbScheme = scheme;
										} else {
											String[] split = dbScheme.split(",");
											boolean verify = ArrayUtils.contains(split, scheme);
											if (!verify) {
												dbScheme = dbScheme + "," + scheme;
											}
										}
									}
								}
								listRelease.add(release);

							}
							paddRFC.setSchemaDB(dbScheme);
							paddRFC.setReleases(Sets.newHashSet(listRelease));
						}
					} else if (listReleasesOld.size() > jsonArray.length()) {
						if (jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);

								PRelease_RFCFast release = preleaseService.findRelease_RFCByIdFast(object.getInt(("id")));
								Set<PReleaseObjectClean> releaseObjects = release.getObjects();
								for (PReleaseObjectClean releaseObject : releaseObjects) {

									if (releaseObject.getIsSql() == 1) {
										String scheme = releaseObject.getDbScheme();
										paddRFC.setRequiredBD(true);
										if (dbScheme.trim().equals("")) {
											dbScheme = scheme;
										} else {
											String[] split = dbScheme.split(",");
											boolean verify = ArrayUtils.contains(split, scheme);
											if (!verify) {
												dbScheme = dbScheme + "," + scheme;
											}
										}
									}
								}
								listRelease.add(release);

							}
							paddRFC.setSchemaDB(dbScheme);
							paddRFC.setReleases(Sets.newHashSet(listRelease));
						}

					} else if (listReleasesOld.size() == jsonArray.length()) {
						List<Integer> listReleaseOld = new ArrayList<Integer>();
						List<Integer> listReleaseNew = new ArrayList<Integer>();
						for (PRelease_RFCFast releaseOld : listReleasesOld) {
							listReleaseOld.add(releaseOld.getId());
						}
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							listReleaseNew.add(object.getInt(("id")));
						}
						Collections.sort(listReleaseNew);
						Collections.sort(listReleaseOld);
						if (listReleaseNew.equals(listReleaseOld)) {
							java.lang.System.out.println("Son iguales");
						} else {
							read = true;
						}
						if (read) {
							if (jsonArray.length() != 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray.getJSONObject(i);

									PRelease_RFCFast release = preleaseService
											.findRelease_RFCByIdFast(object.getInt(("id")));
									Set<PReleaseObjectClean> releaseObjects = release.getObjects();
									for (PReleaseObjectClean releaseObject : releaseObjects) {

										if (releaseObject.getIsSql() == 1) {
											String scheme = releaseObject.getDbScheme();
											paddRFC.setRequiredBD(true);
											if (dbScheme.trim().equals("")) {
												dbScheme = scheme;
											} else {
												String[] split = dbScheme.split(",");
												boolean verify = ArrayUtils.contains(split, scheme);
												if (!verify) {
													dbScheme = dbScheme + "," + scheme;
												}
											}
										}
									}
									listRelease.add(release);

								}
								paddRFC.setSchemaDB(dbScheme);
								paddRFC.setReleases(Sets.newHashSet(listRelease));
							}
						} else {
							paddRFC.setSchemaDB(dbScheme);
							paddRFC.setReleases(Sets.newHashSet(listReleasesOld));
						}

					}

				}

				prfcService.update(paddRFC);
				res.setStatus("success");
				if (errors.size() > 0) {
					// Se adjunta lista de errores
					res.setStatus("fail");
					res.setErrors(errors);
				}
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
	
		try {
			if (profileActive().equals("oracle")) {
				RFC rfcEdit = new RFC();
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				List<System> systems = systemService.listProjects(user.getId());
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
				Integer idManager = getUserLogin().getId();
				Integer countByManager = rfcService.countByManager(idManager, rfcEdit.getId());
				if (countByManager == 0) {
					redirectAttributes.addFlashAttribute("data",
							"No tiene permisos sobre el rfc ya que no formas parte de este equipo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				/*
				 * if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
				 * .equals((user.getUsername().toLowerCase().trim()))) {
				 * redirectAttributes.addFlashAttribute("data",
				 * "No tiene permisos sobre el rfc."); String referer =
				 * request.getHeader("Referer"); return "redirect:" + referer; }
				 */
				Set<Release_RFCFast> releases = rfcEdit.getReleases();
				for (Release_RFCFast release : releases) {
					release.setHaveDependecy(releaseService.getDependency(release.getId()));
				}

				model.addAttribute("systems", systems);
				model.addAttribute("impacts", impactService.list());
				model.addAttribute("typeChange", typeChangeService.findAll());
				model.addAttribute("priorities", priorityService.list());
				model.addAttribute("rfc", rfcEdit);
				model.addAttribute("senders", rfcEdit.getSenders());
				model.addAttribute("message", rfcEdit.getMessage());

				if (rfcEdit.getSiges().getEmailTemplate() != null) {
					model.addAttribute("ccs", getCC(rfcEdit.getSiges().getEmailTemplate().getCc()));
				} else {
					model.addAttribute("ccs", getCC(""));
				}
			} else if (profileActive().equals("postgres")) {
				PRFC rfcEdit = new PRFC();
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				List<PSystem> systems = psystemService.listProjects(user.getId());
				if (id == null) {
					return "redirect:/";
				}

				rfcEdit = prfcService.findById(id);

				if (rfcEdit == null) {
					return "/plantilla/404";
				}

				if (!rfcEdit.getStatus().getName().equals("Borrador")) {
					redirectAttributes.addFlashAttribute("data", "RFC no disponible para editar.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				Integer idManager = getUserLogin().getId();
				Integer countByManager = rfcService.countByManager(idManager, rfcEdit.getId());
				if (countByManager == 0) {
					redirectAttributes.addFlashAttribute("data",
							"No tiene permisos sobre el rfc ya que no formas parte de este equipo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				/*
				 * if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
				 * .equals((user.getUsername().toLowerCase().trim()))) {
				 * redirectAttributes.addFlashAttribute("data",
				 * "No tiene permisos sobre el rfc."); String referer =
				 * request.getHeader("Referer"); return "redirect:" + referer; }
				 */
				Set<PRelease_RFCFast> releases = rfcEdit.getReleases();
				for (PRelease_RFCFast release : releases) {
					release.setHaveDependecy(preleaseService.getDependency(release.getId()));
				}

				model.addAttribute("systems", systems);
				model.addAttribute("impacts", pimpactService.list());
				model.addAttribute("typeChange", ptypeChangeService.findAll());
				model.addAttribute("priorities", ppriorityService.list());
				model.addAttribute("rfc", rfcEdit);
				model.addAttribute("senders", rfcEdit.getSenders());
				model.addAttribute("message", rfcEdit.getMessage());

				if (rfcEdit.getSiges().getEmailTemplate() != null) {
					model.addAttribute("ccs", getCC(rfcEdit.getSiges().getEmailTemplate().getCc()));
				} else {
					model.addAttribute("ccs", getCC(""));
				}
			}
			

			return "/rfc/editRFC";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/getRFC-{id}", method = RequestMethod.GET)
	public @ResponseBody Object getRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		

		try {
			if (profileActive().equals("oracle")) {
				RFC rfcEdit = new RFC();
				rfcEdit = rfcService.findById(id);
				Set<Release_RFCFast> releases = rfcEdit.getReleases();
				for (Release_RFCFast release : releases) {
					release.setHaveDependecy(releaseService.getDependency(release.getId()));
				}
				return rfcEdit;
			} else if (profileActive().equals("postgres")) {
				PRFC rfcEdit = new PRFC();
				rfcEdit = prfcService.findById(id);
				Set<PRelease_RFCFast> releases = rfcEdit.getReleases();
				for (PRelease_RFCFast release : releases) {
					release.setHaveDependecy(releaseService.getDependency(release.getId()));
				}
				return rfcEdit;
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return null;
	}

	@RequestMapping(value = "/summaryRFC-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		
		try {
			
			model.addAttribute("parameter", status);
			
			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				List<System> systems = systemService.listProjects(user.getId());
				RFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = rfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				Siges codeSiges = rfc.getSiges();

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
				List<Errors_RFC> errors = errorService.findAll();
				model.addAttribute("errors", errors);
				model.addAttribute("statuses", statusService.findAll());
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
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				List<PSystem> systems = psystemService.listProjects(user.getId());
				PRFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = prfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				PSiges codeSiges = rfc.getSiges();

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
				List<PErrors_RFC> errors = perrorService.findAll();
				model.addAttribute("errors", errors);
				model.addAttribute("statuses", pstatusService.findAll());
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

		return "/rfc/summaryRFC";
	}

	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		
		try {
			model.addAttribute("parameter", status);
			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				List<System> systems = systemService.listProjects(user.getId());
				RFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = rfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				Siges codeSiges = rfc.getSiges();

				List<String> systemsImplicated = new ArrayList<String>();

				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Set<Release_RFCFast> releases = rfc.getReleases();
				List<ReleaseObjectClean> listObjects = new ArrayList<ReleaseObjectClean>();
				Integer totalObjects = 0;
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
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				List<PSystem> systems = psystemService.listProjects(user.getId());
				PRFC rfc = null;
				if (CommonUtils.isNumeric(status)) {
					rfc = prfcService.findById(Long.parseLong(status));
				}

				if (rfc == null) {
					return "redirect:/";
				}
				PSiges codeSiges = rfc.getSiges();

				List<String> systemsImplicated = new ArrayList<String>();

				systemsImplicated.add(codeSiges.getSystem().getName());
				String nameSystem = "";
				boolean validate = true;
				Set<PRelease_RFCFast> releases = rfc.getReleases();
				List<PReleaseObjectClean> listObjects = new ArrayList<PReleaseObjectClean>();
				Integer totalObjects = 0;
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
		return "/rfc/tinySummaryRFC";
	}

	@RequestMapping(value = "/updateRFC/{rfcId}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable String rfcId, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			
			if (profileActive().equals("oracle")) {
				RFC rfc = null;

				if (CommonUtils.isNumeric(rfcId)) {
					rfc = rfcService.findById((long) Integer.parseInt(rfcId));
				}
				// Si el release no existe se regresa al inicio.
				if (rfc == null) {
					return "redirect:/homeRFC";
				}
				// Verificar si existe un flujo para el sistema
				NodeRFC node = nodeService.existWorkFlowNodeRFC(rfc);
				StatusRFC status = statusService.findByKey("name", "Solicitado");

//				if (node != null)

//					release.setNode(node);

				rfc.setStatus(status);
				rfc.setMotive(status.getReason());
				rfc.setRequestDate((CommonUtils.getSystemTimestamp()));

				rfc.setOperator(getUserLogin().getFullName());
				Siges siges = sigeService.findById(rfc.getSiges().getId());
				if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
					if (siges.getEmailTemplate() != null) {
						EmailTemplate email = siges.getEmailTemplate();
						RFC rfcEmail = rfc;
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
				if (node != null) {
					rfc.setNode(node);

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getActors().size() > 0) {
						Integer idTemplate = Integer.parseInt(paramService.findByCode(27).getParamValue());
						EmailTemplate emailActor = emailService.findById(idTemplate);
						WFRFC rfcEmail = new WFRFC();
						rfcEmail.convertRFCToWFRFC(rfc);
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailActorRFC(rfcEmail, emailActor);
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getUsers().size() > 0) {
						Integer idTemplate = Integer.parseInt(paramService.findByCode(29).getParamValue());

						EmailTemplate emailNotify = emailService.findById(idTemplate);
						WFRFC rfcEmail = new WFRFC();
						rfcEmail.convertRFCToWFRFC(rfc);
						String user = getUserLogin().getFullName();
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailNotifyRFC(rfcEmail, emailNotify, user);
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}
				}

				rfcService.update(rfc);
				Set<Release_RFCFast> releases = rfc.getReleases();
				String user = getUserLogin().getFullName();
				for (Release_RFCFast release : releases) {
					release.setStatusBefore(release.getStatus());
					Status statusRelease = statusReleaseService.findByName("RFC");
					release.setStatus(statusRelease);
					release.setMotive(statusRelease.getMotive());
					releaseService.updateStatusReleaseRFC(release, user);
				}

				return "redirect:/rfc/summaryRFC-" + rfc.getId();
			} else if (profileActive().equals("postgres")) {
				PRFC rfc = null;

				if (CommonUtils.isNumeric(rfcId)) {
					rfc = prfcService.findById((long) Integer.parseInt(rfcId));
				}
				// Si el release no existe se regresa al inicio.
				if (rfc == null) {
					return "redirect:/homeRFC";
				}
				// Verificar si existe un flujo para el sistema
				PNodeRFC node = pnodeService.existWorkFlowNodeRFC(rfc);
				PStatusRFC status = pstatusService.findByKey("name", "Solicitado");

//				if (node != null)

//					release.setNode(node);

				rfc.setStatus(status);
				rfc.setMotive(status.getReason());
				rfc.setRequestDate((CommonUtils.getSystemTimestamp()));

				rfc.setOperator(getUserLogin().getFullName());
				PSiges siges = psigeService.findById(rfc.getSiges().getId());
				if (Boolean.valueOf(pparameterService.getParameterByCode(1).getParamValue())) {
					if (siges.getEmailTemplate() != null) {
						PEmailTemplate email = siges.getEmailTemplate();
						PRFC rfcEmail = rfc;
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailRFC(rfcEmail, email);
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}
				}
				if (node != null) {
					rfc.setNode(node);

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getActors().size() > 0) {
						Integer idTemplate = Integer.parseInt(pparameterService.findByCode(27).getParamValue());
						PEmailTemplate emailActor = pemailService.findById(idTemplate);
						PWFRFC rfcEmail = new PWFRFC();
						rfcEmail.convertRFCToWFRFC(rfc);
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailActorRFC(rfcEmail, emailActor);
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getUsers().size() > 0) {
						Integer idTemplate = Integer.parseInt(pparameterService.findByCode(29).getParamValue());

						PEmailTemplate emailNotify = pemailService.findById(idTemplate);
						PWFRFC rfcEmail = new PWFRFC();
						rfcEmail.convertRFCToWFRFC(rfc);
						String user = getUserLogin().getFullName();
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailNotifyRFC(rfcEmail, emailNotify, user);
							} catch (Exception e) {
								Sentry.capture(e, "rfc");
							}

						});
						newThread.start();
					}
				}

				prfcService.update(rfc);
				Set<PRelease_RFCFast> releases = rfc.getReleases();
				String user = getUserLogin().getFullName();
				for (PRelease_RFCFast release : releases) {
					release.setStatusBefore(release.getStatus());
					PStatus statusRelease = pstatusReleaseService.findByName("RFC");
					release.setStatus(statusRelease);
					release.setMotive(statusRelease.getMotive());
					preleaseService.updateStatusReleaseRFC(release, user);
				}

				return "redirect:/rfc/summaryRFC-" + rfc.getId();
			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRFC";
	}

	public ArrayList<MyError> validSections(RFC rfc, ArrayList<MyError> errors) {

		if (rfc.getImpactId() == 0)
			errors.add(new MyError("impactId", "Valor requerido."));
		if (rfc.getTypeChangeId() == null) {
			errors.add(new MyError("typeChangeId", "Valor requerido."));
		} else {
			if (rfc.getTypeChangeId() == 0)
				errors.add(new MyError("typeChangeId", "Valor requerido."));
		}

		if (rfc.getPriorityId() == 0)
			errors.add(new MyError("priorityId", "Valor requerido."));

		if (rfc.getRequestDateBegin().trim().equals(""))
			errors.add(new MyError("dateBegin", "Valor requerido."));
			
		if (rfc.getRequestDateBegin().trim().equals(""))
			errors.add(new MyError("dateFinish", "Valor requerido."));

		if (rfc.getReasonChange().trim().equals(""))
			errors.add(new MyError("rfcReason", "Valor requerido."));

		if (rfc.getEffect().trim().equals(""))
			errors.add(new MyError("rfcEffect", "Valor requerido."));

		if (rfc.getDetail().trim().equals(""))
			errors.add(new MyError("detailRFC", "Valor requerido."));

		if (rfc.getReturnPlan().trim().equals(""))
			errors.add(new MyError("returnPlanRFC", "Valor requerido."));

		if (rfc.getEvidence().trim().equals(""))
			errors.add(new MyError("evidenceRFC", "Valor requerido."));

		if (rfc.getRequestEsp().equals(""))
			errors.add(new MyError("requestEspRFC", "Valor requerido."));

		if (rfc.getSenders() != null) {
			if (rfc.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(rfc.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (rfc.getMessage() != null) {
			if (rfc.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}
		
		if(rfc.getIsRequest()==1) {
			TypeChange typeChange=typeChangeService.findById(rfc.getTypeChangeId());
			if(!typeChange.getName().equals("Emergencia")) {
			if(!rfc.getRequestDateBegin().trim().equals("")) {
				Timestamp requestDate=CommonUtils.getSystemTimestamp();
				Timestamp requestDateBegin = CommonUtils.convertStringToTimestamp(rfc.getRequestDateBegin(), "dd/MM/yyyy hh:mm a");
				int comparation = requestDate.compareTo(requestDateBegin);
				if(comparation>0) {
					errors.add(new MyError("dateBegin", "La fecha inicio tiene que ser mayor a la fecha que se realiza la solicitud."));
				}
				
			}
			
			if(!rfc.getRequestDateFinish().trim().equals("")) {
				Timestamp requestDate=CommonUtils.getSystemTimestamp();
				Timestamp requestDateFinish = CommonUtils.convertStringToTimestamp(rfc.getRequestDateFinish(), "dd/MM/yyyy hh:mm a");
				int comparation = requestDate.compareTo(requestDateFinish);
				if(comparation>0) {
					errors.add(new MyError("dateFinish", "La fecha fin tiene que ser mayor a la fecha que se realiza la solicitud."));
				}
				
			}
			}
		}

		return errors;
	}



	public MyError getErrorSenders(String senders) {

		String[] listSenders = senders.split(",");
		String to_invalid = "";
		for (int i = 0; i < listSenders.length; i++) {
			if (!CommonUtils.isValidEmailAddress(listSenders[i])) {
				if (to_invalid.equals("")) {
					to_invalid += listSenders[i];
				} else {
					to_invalid += "," + listSenders[i];
				}

			}
		}
		if (!to_invalid.equals("")) {
			return new MyError("senders", "dirección(es) inválida(s) " + to_invalid);
		}
		return null;
	}

	@RequestMapping(value = "/deleteRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				RFC rfc = rfcService.findById(id);
				if (rfc.getStatus().getName().equals("Borrador")) {
					if (rfc.getUser().getUsername().equals(getUserLogin().getUsername())) {
						StatusRFC status = statusService.findByKey("name", "Anulado");
						rfc.setStatus(status);
						rfc.setMotive(status.getReason());
						rfc.setOperator(getUserLogin().getFullName());
						rfcService.update(rfc);
					} else {
						res.setStatus("fail");
						res.setException("No tiene permisos sobre el rfc.");
					}
				} else {
					res.setStatus("fail");
					res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
				}
			} else if (profileActive().equals("postgres")) {
				PRFC rfc = prfcService.findById(id);
				if (rfc.getStatus().getName().equals("Borrador")) {
					if (rfc.getUser().getUsername().equals(getUserLogin().getUsername())) {
						PStatusRFC status = pstatusService.findByKey("name", "Anulado");
						rfc.setStatus(status);
						rfc.setMotive(status.getReason());
						rfc.setOperator(getUserLogin().getFullName());
						prfcService.update(rfc);
					} else {
						res.setStatus("fail");
						res.setException("No tiene permisos sobre el rfc.");
					}
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

	public void loadCountsRelease(HttpServletRequest request, Integer id) {
		// PUser userLogin = getUserLogin();
		// List<PSystem> systems = systemService.listProjects(userLogin.getId());

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

	@RequestMapping(value = "/tree/{releaseNumber}/{depth}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tree(@PathVariable String releaseNumber, @PathVariable Integer depth,
			HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				List<Tree> treeList = treeService.findTree(releaseNumber, depth);
				res.setStatus("success");
				res.setObj(treeList);
			} else if (profileActive().equals("postgres")) {
				List<Tree> treeList = ptreeService.findTree(releaseNumber, depth);
				res.setStatus("success");
				res.setObj(treeList);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/tracking/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tracking(@PathVariable int id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				ReleaseTrackingShow tracking = releaseService.findReleaseTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			} else if (profileActive().equals("postgres")) {
				PReleaseTrackingShow tracking = preleaseService.findReleaseTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			}
		
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/trackingRFC/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse trackingRFC(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				RFCTrackingShow tracking = rfcService.findRFCTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			} else if (profileActive().equals("postgres")) {
				PRFCTrackingShow tracking = prfcService.findRFCTracking(id);
				res.setStatus("success");
				res.setObj(tracking);
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
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

	
}