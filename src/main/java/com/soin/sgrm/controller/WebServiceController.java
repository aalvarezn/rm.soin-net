package com.soin.sgrm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.ReleaseWS;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.DependencyService;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.EnvironmentService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ModifiedComponentService;
import com.soin.sgrm.service.ModuleService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseObjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.utils.BulkLoad;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;

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
	private UserInfoService loginService;
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	@Autowired
	private ImpactService impactService;
	@Autowired
	private PriorityService priorityService;
	@Autowired
	private StatusService statusService;
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
	private DocTemplateService docsTemplateService;
	@Autowired
	private TypeObjectService typeObjectService;
	@Autowired
	private EnvironmentService environmentService;
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
	@Autowired
	private NodeService nodeService;
	@Autowired
	ReleaseObjectService releaseObjectService;

	
	@Autowired
	private DependencyService dependencyService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<RFC> getRFCS() {
		return rfcService.findAll();
	}

	@RequestMapping(value = { "/readEmail" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String readEmails() {
		try {
			emailReadService.emailRead();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "se leyo correctamente";

	}

	@RequestMapping(value = "/release-generate", method = RequestMethod.POST)
	public @ResponseBody String createRelease( @RequestParam("file") MultipartFile file) throws SQLException, IOException {
		// Se genera la estructura base del release para su posterior creacion completa.
		JsonResponse res = new JsonResponse();
		String number_release = "";
		Release release = new Release();
		Module module = new Module();
		InputStream fileData= file.getInputStream();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject)jsonParser.parse(
		      new InputStreamReader(fileData, "UTF-8"));
		System.out.print(jsonObject);
		ReleaseWS releaseWs=new ReleaseWS();
		releaseWs.setDesc(jsonObject.get("desc").toString().replace("\\r\\n","\n").replace("\"", ""));
		releaseWs.setSystem(jsonObject.get("system").toString().replace("\"", ""));
		releaseWs.setRequirementName(jsonObject.get("requirementName").toString().replace("\"", ""));
		releaseWs.setRequirement(jsonObject.get("requirement").toString().replace("\"", ""));
		releaseWs.setUserId(jsonObject.get("userId").toString().replace("\"", ""));
		releaseWs.setObjects(jsonObject.get("objects").toString().replace("\"", "").replace(" ","\n"));
		releaseWs.setAuto(jsonObject.get("auto").toString().replace("\"", ""));
		releaseWs.setSolTecnic(jsonObject.get("solTecnic").toString().replace("\\r\\n","\n").replace("\"", ""));
		releaseWs.setSoluFunc(jsonObject.get("soluFunc").toString().replace("\\r\\n","\n").replace("\"", ""));
		releaseWs.setConsecNoInstala(jsonObject.get("consecNoInstala").toString().replace("\\r\\n","\n").replace("\"", ""));
		releaseWs.setRiesgo(Integer.parseInt((jsonObject.get("riesgo").toString().replace("\"", ""))));
		releaseWs.setImpacto(Integer.parseInt((jsonObject.get("impacto").toString().replace("\"", ""))));
		releaseWs.setPrioridad(Integer.parseInt((jsonObject.get("prioridad").toString().replace("\"", ""))));
		UserInfo userInfo = loginService.getUserByUsername(releaseWs.getUserId());
		User user=new User();
	
		user.setId(userInfo.getId());
		try {
			res.setStatus("success");
			
			if (!releaseWs.getRequirement().equals("TPO/BT")) {
				number_release = releaseService.generateReleaseNumber(releaseWs.getRequirement(), releaseWs.getRequirementName().toUpperCase(), releaseWs.getSystem());
			} else {
				number_release = releaseService.generateTPO_BT_ReleaseNumber(releaseWs.getSystem(),  releaseWs.getRequirementName().toUpperCase());
			}
			Status status = statusService.findByName("Borrador");
			module = moduleService.findBySystemId(releaseWs.getSystem());
			if (module == null) {
				res.setStatus("fail");
				res.setException("El módulo del sistema no se encuentra configurado");
				return "fallo";
			}
			release.setSystem(module.getSystem());
			//release.setDescription(releaseWs.getDescription());
			//release.setObservations(releaseWs.getObservations());
			release.setReleaseNumber(number_release);
			release.setUser(user);
			//Status status = statusService.findByName("Borrador");
			release.setStatus(status);
			release.setModule(module);
			release.setCreateDate(CommonUtils.getSystemTimestamp());
			Risk risk=new Risk();
			risk.setId(releaseWs.getRiesgo());
			release.setRisk(risk);
			Impact impact=new Impact();
			impact.setId(releaseWs.getImpacto());
			release.setImpact(impact);
			Priority priority=new Priority();
			priority.setId(releaseWs.getImpacto());
			release.setPriority(priority);	
			release.setTechnicalSolution(releaseWs.getSolTecnic());
			release.setFunctionalSolution(releaseWs.getSoluFunc());
			release.setNotInstalling(releaseWs.getConsecNoInstala());
			release.setReportHaveArt(false);
			release.setReportfixedTelephony(false);
			release.setReportHistoryTables(false);
			release.setReportNotHaveArt(false);
			release.setReportMobileTelephony(false);
			release.setReportTemporaryTables(false);
			release.setDescription(releaseWs.getDesc());
			release.setBilledCalls(false);
			release.setNotBilledCalls(false);
			
			release.setMotive("Inicio de release");
			release.setOperator(userInfo.getFullName());

			if (releaseWs.getRequirement().equals("IN"))
				release.setIncident((!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("IN"))
						? "IN" + releaseWs.getRequirementName()
						: releaseWs.getRequirementName());

			if (releaseWs.getRequirement().equals("PR"))
				release.setProblem((!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("PR"))
						? "PR" + releaseWs.getRequirementName()
						: releaseWs.getRequirementName());

			if (releaseWs.getRequirement().equals("SS"))
				release.setService_requests((!releaseWs.getRequirementName().substring(0, 2).toString().toUpperCase().equals("SS"))
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
			
			if(addObjects( releaseWs.getObjects(),  release.getId())) {
			System.out.print("si");
			}else {
				System.out.print("no");
			}
			return release.getReleaseNumber();
		} catch (SQLException ex) {
			Sentry.capture(ex, "release");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException(e.getMessage());
			//logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return release.getReleaseNumber();
	}
	
	
	
	public boolean addObjects(String csvLines, Integer id) {
		JsonResponse res = new JsonResponse();
		try {
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
	
	
}