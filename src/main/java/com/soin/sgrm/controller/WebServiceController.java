package com.soin.sgrm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseSummaryFile;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.ReleaseWS;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.NodeRFC;
import com.soin.sgrm.model.pos.PConfigurationItem;
import com.soin.sgrm.model.pos.PDependency;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PModule;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PRisk;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PTypeObject;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.DependencyService;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ModuleService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseObjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.pos.PConfigurationItemService;
import com.soin.sgrm.service.pos.PDependencyService;
import com.soin.sgrm.service.pos.PModuleService;
import com.soin.sgrm.service.pos.PRFCService;
import com.soin.sgrm.service.pos.PReleaseObjectService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PRequestService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PTypeObjectService;
import com.soin.sgrm.service.pos.PUserInfoService;
import com.soin.sgrm.utils.BulkLoad;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.UpdateJiraIssueStatus;

@RestController
@RequestMapping("/ws")
public class WebServiceController extends BaseController {

	@Autowired
	RFCService rfcService;
	@Autowired
	EmailReadService emailReadService;
	@Autowired
	ReleaseService releaseService;
	@Autowired
	UserInfoService loginService;
	@Autowired
	StatusService statusService;
	@Autowired
	ModuleService moduleService;
	@Autowired
	TypeObjectService typeObjectService;
	@Autowired
	ConfigurationItemService configurationItemService;
	@Autowired
	ReleaseObjectService releaseObjectService;
	@Autowired
	DependencyService dependencyService;
	@Autowired
	PRFCService prfcService;
	@Autowired
	EmailTemplateService emailService;
	@Autowired
	ParameterService parameterService;
	@Autowired
	StatusRFCService statusRFCService;
	@Autowired
	RequestService requestService;
	@Autowired
	SigesService sigesService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	ParameterService paramService;
	@Autowired
	SigesService sigeService;
	@Autowired
	com.soin.sgrm.service.UserService userService ;
  @Autowired  
	private Environment env;
	@Autowired
	private ProjectService projectService;

	@Autowired
	PReleaseService preleaseService;
	@Autowired
	PUserInfoService ploginService;
	@Autowired
	PStatusService pstatusService;
	@Autowired
	PModuleService pmoduleService;
	@Autowired
	PTypeObjectService ptypeObjectService;
	@Autowired
	PConfigurationItemService pconfigurationItemService;
	@Autowired
	PReleaseObjectService preleaseObjectService;
	@Autowired
	PDependencyService pdependencyService;
	private final Environment environment;
	@Autowired
	PRequestService prequestService;
	@Autowired
	public WebServiceController(Environment environment) {
		this.environment = environment;
	}
	
	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = "/release-generate", method = RequestMethod.POST)
	public @ResponseBody String createRelease(@RequestParam("file") MultipartFile file)
			throws SQLException, IOException {
		// Se genera la estructura base del release para su posterior creacion completa.
		JsonResponse res = new JsonResponse();
		String number_release = "";
		if (profileActive().equals("oracle")) {
			Release release = new Release();
			Module module = new Module();
			InputStream fileData = file.getInputStream();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(fileData, "UTF-8"));
			ReleaseWS releaseWs = new ReleaseWS();
			releaseWs.setDesc(jsonObject.get("desc").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setObservations(jsonObject.get("observacion").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setSystem(jsonObject.get("system").toString().replace("\"", ""));
			releaseWs.setRequirementName(jsonObject.get("requirementName").toString().replace("\"", ""));
			if (jsonObject.get("versionNumber") == null) {
				releaseWs.setVersionNumber("Sin version");
			} else {
				releaseWs.setVersionNumber(jsonObject.get("versionNumber").toString().replace("\"", ""));
			}

			releaseWs.setRequirement(jsonObject.get("requirement").toString().replace("\"", ""));
			releaseWs.setUserId(jsonObject.get("userId").toString().replace("\"", ""));
			String objects = jsonObject.get("objects").toString();
			objects = objects.replace("Base Datos", "Base_Datos");
			objects = objects.replace(" ", "\n");
			objects = objects.replace("\"", "");
			objects = objects.replace("Base_Datos", "Base Datos");
			releaseWs.setObjects(objects);
			releaseWs.setAuto(jsonObject.get("auto").toString().replace("\"", ""));
			releaseWs.setSolTecnic(jsonObject.get("solTecnic").toString().replace("\\r\\n", "\n").replace("\"", "")
					.replace("--From", "\n"));
			releaseWs.setSoluFunc(jsonObject.get("soluFunc").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setConsecNoInstala(
					jsonObject.get("consecNoInstala").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setRiesgo(Integer.parseInt((jsonObject.get("riesgo").toString().replace("\"", ""))));
			releaseWs.setImpacto(Integer.parseInt((jsonObject.get("impacto").toString().replace("\"", ""))));
			releaseWs.setPrioridad(Integer.parseInt((jsonObject.get("prioridad").toString().replace("\"", ""))));
			UserInfo userInfo = loginService.getUserByUsername(releaseWs.getUserId());
			User user = new User();
			if (userInfo == null) {
				userInfo = loginService.getUserByGitUsername(releaseWs.getUserId());
			}
			if (userInfo == null) {
				userInfo = loginService.getUserByUsername("admin");
			}
			user.setId(userInfo.getId());

			try {
				res.setStatus("success");

				if (!releaseWs.getRequirement().equals("TPO/BT")) {
					number_release = releaseService.generateReleaseNumber(releaseWs.getRequirement(),
							releaseWs.getRequirementName().toUpperCase(), releaseWs.getSystem());
				} else {
					number_release = releaseService.generateTPO_BT_ReleaseNumber(releaseWs.getSystem(),
							releaseWs.getRequirementName().toUpperCase());
				}
				Status status = statusService.findByName("Borrador");
				String systemModule = releaseWs.getSystem();
				module = moduleService.findBySystemId(systemModule.trim());
				if (module == null) {
					res.setStatus("fail");
					res.setException("El módulo del sistema no se encuentra configurado");
					return "fallo";
				}
				release.setSystem(module.getSystem());
				// release.setDescription(releaseWs.getDescription());
				// release.setObservations(releaseWs.getObservations());
				release.setReleaseNumber(number_release);
				release.setUser(user);
				// Status status = statusService.findByName("Borrador");
				release.setStatus(status);
				release.setModule(module);

				release.setCreateDate(CommonUtils.getSystemTimestamp());
				Risk risk = new Risk();
				risk.setId(releaseWs.getRiesgo());
				release.setRisk(risk);
				Impact impact = new Impact();
				impact.setId(releaseWs.getImpacto());
				release.setImpact(impact);
				Priority priority = new Priority();
				priority.setId(releaseWs.getImpacto());
				release.setPriority(priority);
				release.setTechnicalSolution(releaseWs.getSolTecnic());
				release.setFunctionalSolution(releaseWs.getSoluFunc());
				release.setNotInstalling(releaseWs.getConsecNoInstala());
				release.setVersionNumber(releaseWs.getVersionNumber());
				release.setReportHaveArt(false);
				release.setReportfixedTelephony(false);
				release.setReportHistoryTables(false);
				release.setReportNotHaveArt(false);
				release.setReportMobileTelephony(false);
				release.setReportTemporaryTables(false);
				release.setDescription(releaseWs.getDesc());
				release.setObservations(releaseWs.getObservations());
				release.setBilledCalls(false);
				release.setNotBilledCalls(false);

				release.setMotive("Inicio de release");
				release.setOperator(userInfo.getFullName());

				if (releaseWs.getRequirement().equals("IN"))
					release.setIncident(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("IN"))
									? "IN" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());

				if (releaseWs.getRequirement().equals("PR"))
					release.setProblem(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("PR"))
									? "PR" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());


				if (releaseWs.getRequirement().equals("SS"))
					release.setService_requests(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("SS"))
									? "SS" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());

				if (releaseWs.getRequirement().equals("SO-ICE"))
					release.setOperative_support("SO-ICE" + releaseWs.getRequirementName());

				if (!releaseWs.getRequirement().equals("TPO/BT")) {
					releaseService.save(release, "-1");
				} else {
					releaseService.save(release, releaseWs.getRequirementName());
				}
				res.setData(release.getReleaseNumber() + "");


				return release.getReleaseNumber();
			} catch (SQLException ex) {
				Sentry.capture(ex, "release");
				res.setStatus("exception");
				res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
			} catch (Exception e) {
				Sentry.capture(e, "release");
				res.setStatus("exception");
				res.setException(e.getMessage());
				// logger.log(MyLevel.RELEASE_ERROR, e.toString());
			}
			return release.getReleaseNumber();
		} else if (profileActive().equals("postgres")) {
			PRelease release = new PRelease();
			PModule module = new PModule();
			InputStream fileData = file.getInputStream();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(fileData, "UTF-8"));
			ReleaseWS releaseWs = new ReleaseWS();
			releaseWs.setDesc(jsonObject.get("desc").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setObservations(jsonObject.get("observacion").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setSystem(jsonObject.get("system").toString().replace("\"", ""));
			releaseWs.setRequirementName(jsonObject.get("requirementName").toString().replace("\"", ""));
			if (jsonObject.get("versionNumber") == null) {
				releaseWs.setVersionNumber("Sin version");
			} else {
				releaseWs.setVersionNumber(jsonObject.get("versionNumber").toString().replace("\"", ""));
			}
			releaseWs.setRequirement(jsonObject.get("requirement").toString().replace("\"", ""));
			releaseWs.setUserId(jsonObject.get("userId").toString().replace("\"", ""));
			String objects = jsonObject.get("objects").toString();
			objects = objects.replace("Base Datos", "Base_Datos");
			objects = objects.replace(" ", "\n");
			objects = objects.replace("\"", "");
			objects = objects.replace("Base_Datos", "Base Datos");
			releaseWs.setObjects(objects);
			releaseWs.setAuto(jsonObject.get("auto").toString().replace("\"", ""));
			releaseWs.setSolTecnic(jsonObject.get("solTecnic").toString().replace("\\r\\n", "\n").replace("\"", "")
					.replace("--From", "\n"));
			releaseWs.setSoluFunc(jsonObject.get("soluFunc").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setConsecNoInstala(
					jsonObject.get("consecNoInstala").toString().replace("\\r\\n", "\n").replace("\"", ""));
			releaseWs.setRiesgo(Integer.parseInt((jsonObject.get("riesgo").toString().replace("\"", ""))));
			releaseWs.setImpacto(Integer.parseInt((jsonObject.get("impacto").toString().replace("\"", ""))));
			releaseWs.setPrioridad(Integer.parseInt((jsonObject.get("prioridad").toString().replace("\"", ""))));
			PUserInfo userInfo = ploginService.getUserByUsername(releaseWs.getUserId());
			PUser user = new PUser();
			if (userInfo == null) {
				userInfo = ploginService.getUserByGitUsername(releaseWs.getUserId());
			}
			if (userInfo == null) {
				userInfo = ploginService.getUserByUsername("admin");
			}
			user.setId(userInfo.getId());
			Integer idRequeriment=0;
			try {
				res.setStatus("success");

				if (!releaseWs.getRequirement().equals("TPO/BT")) {
					number_release = preleaseService.generateReleaseNumber(releaseWs.getRequirement(),
							releaseWs.getRequirementName().toUpperCase(), releaseWs.getSystem());
				} else {
					String [] codes= releaseWs.getRequirementName().split("_");
					String codeSoin=codes[0].trim();
					String codeIce=codes[1].trim();
					 idRequeriment =prequestService.findIDRequeriment(codeSoin,codeIce);
 					number_release = preleaseService.generateTPO_BT_ReleaseNumber(releaseWs.getSystem(),
 							idRequeriment.toString());
				}
				PStatus status = pstatusService.findByName("Borrador");
				String systemModule = releaseWs.getSystem();
				module = pmoduleService.findBySystemId(systemModule.trim());
				if (module == null) {
					res.setStatus("fail");
					res.setException("El módulo del sistema no se encuentra configurado");
					return "fallo";
				}
				release.setSystem(module.getSystem());
				// release.setDescription(releaseWs.getDescription());
				// release.setObservations(releaseWs.getObservations());
				release.setReleaseNumber(number_release);
				release.setUser(user);
				// Status status = statusService.findByName("Borrador");
				release.setStatus(status);
				release.setModule(module);

				release.setCreateDate(CommonUtils.getSystemTimestamp());
				PRisk risk = new PRisk();
				risk.setId(releaseWs.getRiesgo());
				release.setRisk(risk);
				PImpact impact = new PImpact();
				impact.setId(releaseWs.getImpacto());
				release.setImpact(impact);
				PPriority priority = new PPriority();
				priority.setId(releaseWs.getImpacto());
				release.setPriority(priority);
				release.setTechnicalSolution(releaseWs.getSolTecnic());
				release.setFunctionalSolution(releaseWs.getSoluFunc());
				release.setNotInstalling(releaseWs.getConsecNoInstala());
				release.setVersionNumber(releaseWs.getVersionNumber());
				release.setReportHaveArt(false);
				release.setReportfixedTelephony(false);
				release.setReportHistoryTables(false);
				release.setReportNotHaveArt(false);
				release.setReportMobileTelephony(false);
				release.setReportTemporaryTables(false);
				release.setDescription(releaseWs.getDesc());
				release.setObservations(releaseWs.getObservations());
				release.setBilledCalls(false);
				release.setNotBilledCalls(false);

				release.setMotive("Inicio de release");
				release.setOperator(userInfo.getFullName());

				if (releaseWs.getRequirement().equals("IN"))
					release.setIncident(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("IN"))
									? "IN" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());

				if (releaseWs.getRequirement().equals("PR"))
					release.setProblem(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("PR"))
									? "PR" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());

				if (releaseWs.getRequirement().equals("SS"))
					release.setService_requests(
							(!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("SS"))
									? "SS" + releaseWs.getRequirementName()
									: releaseWs.getRequirementName());

				if (releaseWs.getRequirement().equals("SO-ICE"))
					release.setOperative_support("SO-ICE" + releaseWs.getRequirementName());

				if (!releaseWs.getRequirement().equals("TPO/BT")) {
					preleaseService.save(release, "-1");
				} else {
					
					preleaseService.save(release, idRequeriment.toString());
				}
				res.setData(release.getReleaseNumber() + "");


				return release.getReleaseNumber();
			} catch (SQLException ex) {
				Sentry.capture(ex, "release");
				res.setStatus("exception");
				res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
			} catch (Exception e) {
				Sentry.capture(e, "release");
				res.setStatus("exception");
				res.setException(e.getMessage());
				// logger.log(MyLevel.RELEASE_ERROR, e.toString());
			}
			return release.getReleaseNumber();
		}
		return number_release;
		
	}

	public boolean addObjects(String csvLines, Integer id) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				ReleaseEdit release = releaseService.findEditById(id);
				List<ConfigurationItem> configurationItemList = configurationItemService
						.listBySystem(release.getSystem().getId());
				List<TypeObject> typeObjectList = typeObjectService.listBySystem(release.getSystem().getId());
				ArrayList<MyError> errors = new ArrayList<MyError>();
				boolean isEmpty = false;
				String[] lines = null;
				String[] line = null;
				String infoErrors = "";
				BulkLoad bl = new BulkLoad();

				res.setData(csvLines);
				lines = csvLines.split("\n");
				Map<String, Integer> duplicates = new HashMap<String, Integer>();
				for (int i = 0; i < lines.length; i++) {
					if (duplicates.containsKey(lines[i].split(",")[0])) {
						duplicates.put(lines[i].split(",")[0], duplicates.get(lines[i].split(",")[0]) + 1);
					} else {
						duplicates.put(lines[i].split(",")[0], 1);
					}
				}
				for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
					if (entry.getValue() > 1)
						errors.add(new MyError("Elemento " + entry.getKey(), " repetido"));
				}
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].split(",").length != 6) {
						errors.add(new MyError("Linea# " + (i + 1), "No cumple formato"));
					} else {
						line = lines[i].split(",");
						isEmpty = false;
						for (String field : line) {
							if (field.trim().equals(""))
								isEmpty = true;
						}
						if (isEmpty)
							errors.add(new MyError("Linea# " + (i + 1), "Tiene campos en blanco"));
						else
							validLine(errors, line, i, release, configurationItemList, typeObjectList);
					}
				}
				if (errors.size() != 0) {
					res.setStatus("fail");
					for (MyError error : errors) {
						infoErrors += error.getKey() + " " + error.getMessage() + "<br>";
					}
					res.setException(infoErrors);
					res.setErrors(errors);
					return false;
				}

				ArrayList<ReleaseObjectEdit> objects = createObjects(csvLines, release, configurationItemList,
						typeObjectList);
				List<Object[]> list = releaseObjectService.findReleaseToAddByObjectList(objects, release);

				ReleaseUser releaseFrom = releaseService.findReleaseUserById(id);
				ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
				Dependency dependency = null;
				ReleaseUser releaseTo = null;
				if (list != null) {
					for (Object[] obj : list) {
						if (!release.existDependency((Integer) obj[0])) {
							releaseTo = new ReleaseUser();
							releaseTo.setId((Integer) obj[0]);
							releaseTo.setReleaseNumber((String) obj[1]);

							dependency = new Dependency();
							dependency.setRelease(releaseFrom);
							dependency.setTo_release(releaseTo);
							dependency.setMandatory(true);
							dependency.setIsFunctional(false);
							dependency.setId(0);
							dependencies.add(dependency);
						}
					}
				}

				if (dependencies.size() != 0) {
					dependencies = dependencyService.save(release, dependencies);
					bl.setDependencies(dependencies);
				}

				objects = releaseService.saveReleaseObjects(release.getId(), objects);
				bl.setDependencies(dependencies);
				bl.setObjects(objects);
				res.setObj(bl);
			} else if (profileActive().equals("postgres")) {
				PReleaseEdit release = preleaseService.findEditById(id);
				List<PConfigurationItem> configurationItemList = pconfigurationItemService
						.listBySystem(release.getSystem().getId());
				List<PTypeObject> typeObjectList = ptypeObjectService.listBySystem(release.getSystem().getId());
				ArrayList<MyError> errors = new ArrayList<MyError>();
				boolean isEmpty = false;
				String[] lines = null;
				String[] line = null;
				String infoErrors = "";
				BulkLoad bl = new BulkLoad();

				res.setData(csvLines);
				lines = csvLines.split("\n");
				Map<String, Integer> duplicates = new HashMap<String, Integer>();
				for (int i = 0; i < lines.length; i++) {
					if (duplicates.containsKey(lines[i].split(",")[0])) {
						duplicates.put(lines[i].split(",")[0], duplicates.get(lines[i].split(",")[0]) + 1);
					} else {
						duplicates.put(lines[i].split(",")[0], 1);
					}
				}
				for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
					if (entry.getValue() > 1)
						errors.add(new MyError("Elemento " + entry.getKey(), " repetido"));
				}
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].split(",").length != 6) {
						errors.add(new MyError("Linea# " + (i + 1), "No cumple formato"));
					} else {
						line = lines[i].split(",");
						isEmpty = false;
						for (String field : line) {
							if (field.trim().equals(""))
								isEmpty = true;
						}
						if (isEmpty)
							errors.add(new MyError("Linea# " + (i + 1), "Tiene campos en blanco"));
						else
							validLine(errors, line, i, release, configurationItemList, typeObjectList);
					}
				}
				if (errors.size() != 0) {
					res.setStatus("fail");
					for (MyError error : errors) {
						infoErrors += error.getKey() + " " + error.getMessage() + "<br>";
					}
					res.setException(infoErrors);
					res.setErrors(errors);
					return false;
				}

				ArrayList<PReleaseObjectEdit> objects = createObjects(csvLines, release, configurationItemList,
						typeObjectList);
				List<Object[]> list = preleaseObjectService.findReleaseToAddByObjectList(objects, release);

				PReleaseUser releaseFrom = preleaseService.findReleaseUserById(id);
				ArrayList<PDependency> dependencies = new ArrayList<PDependency>();
				PDependency dependency = null;
				PReleaseUser releaseTo = null;
				if (list != null) {
					for (Object[] obj : list) {
						if (!release.existDependency((Integer) obj[0])) {
							releaseTo = new PReleaseUser();
							releaseTo.setId((Integer) obj[0]);
							releaseTo.setReleaseNumber((String) obj[1]);

							dependency = new PDependency();
							dependency.setRelease(releaseFrom);
							dependency.setTo_release(releaseTo);
							dependency.setMandatory(true);
							dependency.setIsFunctional(false);
							dependency.setId(0);
							dependencies.add(dependency);
						}
					}
				}

				if (dependencies.size() != 0) {
					dependencies = pdependencyService.save(release, dependencies);
					bl.setDependencies(dependencies);
				}

				objects = preleaseService.saveReleaseObjects(release.getId(), objects);
				bl.setDependencies(dependencies);
				bl.setObjects(objects);
				res.setObj(bl);
			}
			
			
			res.setStatus("success");
		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseCSV");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseCSV");
			res.setStatus("exception");
			res.setException("Error al cargar csv: " + e);

		}
		return true;
	}

	public ArrayList<MyError> validLine(ArrayList<MyError> errors, String[] line, int i, ReleaseEdit release,
			List<ConfigurationItem> configurationItemList, List<TypeObject> typeObjectList) {
		if (CommonUtils.isSqlDate(line[3].trim()) == null) {
			errors.add(new MyError("Linea# " + (i + 1), "Fecha en formato incorrecto"));
		}
		if (release.existObject(line[0].trim())) {// nombre no exista ya para el release
			errors.add(new MyError("Linea# " + (i + 1) + " Nombre " + (line[0].trim()), "Ya existe"));
		}

		Boolean existItem = false;
		for (ConfigurationItem item : configurationItemList) {
			if (line[4].trim().equals(item.getName())) {
				existItem = true;
			}
		}
		Boolean existObject = false;
		for (TypeObject typeObject : typeObjectList) {
			if (line[5].trim().equals(typeObject.getName())) {
				existObject = true;
			}
		}
		if (!existItem) {// NombreItem exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreItemConfiguración " + (line[4].trim()), "inválido"));
		}
		if (!existObject) {// NombreTipo exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreTipo " + (line[5].trim()), "inválido"));
		}
		return errors;
	}

	public ArrayList<MyError> validLine(ArrayList<MyError> errors, String[] line, int i, PReleaseEdit release,
			List<PConfigurationItem> configurationItemList, List<PTypeObject> typeObjectList) {
		if (CommonUtils.isSqlDate(line[3].trim()) == null) {
			errors.add(new MyError("Linea# " + (i + 1), "Fecha en formato incorrecto"));
		}
		if (release.existObject(line[0].trim())) {// nombre no exista ya para el release
			errors.add(new MyError("Linea# " + (i + 1) + " Nombre " + (line[0].trim()), "Ya existe"));
		}

		Boolean existItem = false;
		for (PConfigurationItem item : configurationItemList) {
			if (line[4].trim().equals(item.getName())) {
				existItem = true;
			}
		}
		Boolean existObject = false;
		for (PTypeObject typeObject : typeObjectList) {
			if (line[5].trim().equals(typeObject.getName())) {
				existObject = true;
			}
		}
		if (!existItem) {// NombreItem exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreItemConfiguración " + (line[4].trim()), "inválido"));
		}
		if (!existObject) {// NombreTipo exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreTipo " + (line[5].trim()), "inválido"));
		}
		return errors;
	}
	public ArrayList<ReleaseObjectEdit> createObjects(String csv, ReleaseEdit release,
			List<ConfigurationItem> configurationItemList, List<TypeObject> typeObjectList) throws Exception {

		ArrayList<ReleaseObjectEdit> objects = new ArrayList<ReleaseObjectEdit>();
		String[] lines = csv.split("\n");
		ReleaseObjectEdit object = null;
		for (int i = 0; i < lines.length; i++) { // line
			object = new ReleaseObjectEdit();
			object.setName(lines[i].split(",")[0]); // name
			object.setDescription(lines[i].split(",")[1]); // description
			object.setRevision_SVN(lines[i].split(",")[2]); // Revision_SVN
			object.setRevision_Date(CommonUtils.getSqlDate(lines[i].split(",")[3])); // Timestamp

			for (ConfigurationItem confItem : configurationItemList) {
				if ((lines[i].split(",")[4]).equals(confItem.getName())) {
					object.setItemConfiguration(confItem.getId());
				}
			}
			for (TypeObject typeObject : typeObjectList) {
				if ((lines[i].split(",")[5]).equals(typeObject.getName())) {
					object.setTypeObject(typeObject.getId());
				}
			}
			object.setIsSql((lines[i].split(",")[4].equals("Base Datos")) ? 1 : 0);
			object.setModuleId(release.getModule_id());
			objects.add(object);
		}
		return objects;
	}

	@RequestMapping(value = "/statusChangeRelease", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse changeStatusRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "numRequest", required = true) String numRequest,
			@RequestParam(value = "idStatus", required = true) Integer idStatus,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "senders", required = false) String senders,
			@RequestParam(value = "link", required = false) String link) {
		JsonResponse res = new JsonResponse();
		try {
			if (type == 0) {
				ReleaseEdit release = releaseService.findEditByName(numRequest);
				Status status = statusService.findById(idStatus);
				release.setStatus(status);
				release.setOperator("Automatico");
				release.setDateChange("");
				release.setMotive(motive);
				releaseService.updateStatusRelease(release);
				res.setStatus("success");

				Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
				EmailTemplate emailNotify = emailService.findById(idTemplate);
				EmailTemplate email = new EmailTemplate();
				if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
					email = release.getSystem().getEmailTemplate().iterator().next();
				}
				String subject = getSubject(email, release);

				String statusName = status.getName();

				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailNotifyChangeStatusWebService(release.getReleaseNumber(), " del Release",
								statusName, release.getOperator(),
								CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
								release.getUser(), senders, emailNotify, subject, release.getMotive(), link,
								"RM-P2-R5|Registro evidencia de instalación");
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			} else if (type == 1) {
				RFC rfc = rfcService.findByKey("numRequest",numRequest.trim());
				StatusRFC status = statusRFCService.findById((long) idStatus);
				rfc.setStatus(status);
				rfc.setOperator("Automatico");
				Date currentDate = new Date();
				  Timestamp timestamp = new Timestamp(currentDate.getTime());
					rfc.setRequestDate(timestamp);
					rfc.setMotive(motive);
					rfcService.update(rfc);
					res.setStatus("success");

					Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName = status.getName();
					UserInfo user = userInfoService.findUserInfoById(rfc.getUser().getId());
					String subject = getSubject(rfc.getSiges().getEmailTemplate(), rfc);
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatusWebService(rfc.getNumRequest(), " del RFC", statusName,
									rfc.getOperator(), rfc.getRequestDate(), user, senders, emailNotify, subject,
									rfc.getMotive(), link, "RM-P2-R5|Registro evidencia de instalación");

						} catch (Exception e) {
							Sentry.capture(e, "rfc");
						}

					});
					newThread.start();
				}

			} catch (SQLException ex) {
				Sentry.capture(ex, "releaseManagement");
				res.setStatus("exception");
				res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
			} catch (Exception e) {
				Sentry.capture(e, "releaseManagement");
				res.setStatus("exception");
				res.setException("Error al cambiar estado del release: " + e.getMessage());

			}
			return res;
		}

	


	public ArrayList<PReleaseObjectEdit> createObjects(String csv, PReleaseEdit release,
			List<PConfigurationItem> configurationItemList, List<PTypeObject> typeObjectList) throws Exception {

		ArrayList<PReleaseObjectEdit> objects = new ArrayList<PReleaseObjectEdit>();
		String[] lines = csv.split("\n");
		PReleaseObjectEdit object = null;
		for (int i = 0; i < lines.length; i++) { // line
			object = new PReleaseObjectEdit();
			object.setName(lines[i].split(",")[0]); // name
			object.setDescription(lines[i].split(",")[1]); // description
			object.setRevision_SVN(lines[i].split(",")[2]); // Revision_SVN
			object.setRevision_Date(CommonUtils.getSqlDate(lines[i].split(",")[3])); // Timestamp

			for (PConfigurationItem confItem : configurationItemList) {
				if ((lines[i].split(",")[4]).equals(confItem.getName())) {
					object.setItemConfiguration(confItem.getId());
				}
			}
			for (PTypeObject typeObject : typeObjectList) {
				if ((lines[i].split(",")[5]).equals(typeObject.getName())) {
					object.setTypeObject(typeObject.getId());
				}
			}
			object.setIsSql((lines[i].split(",")[4].equals("Base Datos")) ? 1 : 0);
			object.setModuleId(release.getModule_id());
			objects.add(object);
		}
		return objects;
	}

	@RequestMapping(value = "/statusNow", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getStatus(HttpServletRequest request, Model model,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "type", required = true) Integer type) {
		JsonResponse res = new JsonResponse();

		if (type == 0) {
			ReleaseEdit release = releaseService.findEditByName(name);
			res.setStatus(release.getStatus().getName());
		} else if (type == 1) {
			RFC rfc = rfcService.findByKey("numRequest", name.trim());
			res.setStatus(rfc.getStatus().getName());
		}
		return res;
	}

	@RequestMapping(value = "/sendMail", method = RequestMethod.GET)
	public @ResponseBody JsonResponse sendMail(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "idUser", required = false) Integer idUser
			) {
		JsonResponse res = new JsonResponse();
		Release release=null;
		if(type==1) {
			
		
		try {
			release = releaseService.findReleaseById(Integer.parseInt(id));
		} catch (NumberFormatException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Status status = statusService.findByName("Solicitado");

		release.setStatus(status);
		release.setMotive(status.getMotive());
		User user=userService.findUserById(idUser);
		release.setOperator(user.getFullName());
		release.setUser(user);
		if (Boolean.valueOf(paramService.findByCode(1).getParamValue())) {
			if (release.getSystem().getEmailTemplate().iterator().hasNext()) {
				EmailTemplate email = release.getSystem().getEmailTemplate().iterator().next();
				Release releaseEmail = release;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMail(releaseEmail, email);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			}
		}
		}else {
			RFC rfc = null;

			if (CommonUtils.isNumeric(id)) {
				rfc = rfcService.findById((long) Integer.parseInt(id));
			}
			
			StatusRFC status = statusRFCService.findByKey("name", "Solicitado");

//			if (node != null)

//				release.setNode(node);

			rfc.setStatus(status);
			rfc.setMotive(status.getReason());
			rfc.setRequestDate((CommonUtils.getSystemTimestamp()));
			Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
			rfc.setRequestDate(dateFormat);
			User user=userService.findUserById(idUser);
			rfc.setOperator(user.getFullName());
			rfc.setUser(user);
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
		}
		return res;
	}
	


	private String getSubject(EmailTemplate email, RFC rfc) {
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

	public String getSubject(EmailTemplate emailNotify, ReleaseEdit release) {

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

		new Request();
		if (tpo != "") {
			requestService.findByNameCode(tpo);
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

	@RequestMapping(value = { "/updateJira" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateJira() {
		String jiraUrl = "https://soinlabs.atlassian.net";
		String username = "releaseice@soin.co.cr";
		String apiToken = "ATATT3xFfGF0n6uMxvmFJF1x_RI4ZiQVIrkPMnJ3eIIk1ykAzKsbPMymn2XroLX7HmgqWzvEIN-BLj1KShYhr3NHpxvhljAyOqUHGp5jO9v5tpzl3JrtTHN4jgl4yHdph1dmqX3GzmjwfNji61hz6hiZTIFoIXHo7CSZ5GjRBMO4drT6HLObhGI=098DCD93";
		String issueKey = "NSPCCNS-541"; // Cambia esto al identificador de tu tarea
		String newStatus = "21"; // Cambia esto al nuevo estado que deseas establecer

	        HttpClient httpClient = HttpClients.createDefault();
	        HttpPut request = new HttpPut(jiraUrl + "/rest/api/latest/issue/" + issueKey + "/transitions");

	        // Configura las cabeceras de autenticación con el API token
	        String authHeader = "Bearer " + apiToken;
	        request.setHeader("Authorization", authHeader);
	        request.setHeader("Content-Type", "application/json");

	        try {
	            // Construye el JSON con el nuevo estado
	            ObjectMapper mapper = new ObjectMapper();
	            String jsonBody = "{ \"transition\": { \"id\": \"" + newStatus + "\" } }";
	            request.setEntity(new StringEntity(jsonBody));

	            HttpResponse response = httpClient.execute(request);
	            // Procesa la respuesta aquí
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		
	}

	@RequestMapping(value = { "/getStatusJira" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String getStatusJira() {
		String jiraUrl = "https://soinlabs.atlassian.net";
		String username = "releaseice@soin.co.cr";
		String apiToken = "ATATT3xFfGF0n6uMxvmFJF1x_RI4ZiQVIrkPMnJ3eIIk1ykAzKsbPMymn2XroLX7HmgqWzvEIN-BLj1KShYhr3NHpxvhljAyOqUHGp5jO9v5tpzl3JrtTHN4jgl4yHdph1dmqX3GzmjwfNji61hz6hiZTIFoIXHo7CSZ5GjRBMO4drT6HLObhGI=098DCD93";
		String issueKey = "NSPCCNS-541"; // Cambia esto al identificador de tu tarea

		HttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(jiraUrl + "/rest/api/latest/issue/" + issueKey);

		// Configura las cabeceras de autenticación
		 String authHeader = "Bearer " + apiToken;
	        request.setHeader("Authorization", authHeader);
	        request.setHeader("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				// Procesa la respuesta JSON para obtener el estado
				String responseBody = EntityUtils.toString(response.getEntity());
				// Parsea el JSON para obtener el estado
				// Por ejemplo, utilizando la biblioteca Jackson
				 ObjectMapper mapper = new ObjectMapper();
				 JsonNode jsonNode = mapper.readTree(responseBody);
				 String status = jsonNode.get("fields").get("status").get("name").asText();
				// System.out.println("Estado de la tarea: " + status);
				return status;
			} else {
				System.out.println("Error al obtener el estado de la tarea. Código de estado: "
						+ response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}
}