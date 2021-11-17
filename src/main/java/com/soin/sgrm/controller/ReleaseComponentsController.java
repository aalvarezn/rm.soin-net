package com.soin.sgrm.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseActionEdit;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.corp.RMReleaseFile;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.DependencyService;
import com.soin.sgrm.service.ModifiedComponentService;
import com.soin.sgrm.service.ReleaseObjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.corp.RMReleaseFileService;
import com.soin.sgrm.utils.BulkLoad;
import com.soin.sgrm.utils.ItemObject;
import com.soin.sgrm.utils.JsonAutocomplete;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;

@Controller
@RequestMapping("/release")
public class ReleaseComponentsController extends BaseController {

	@Autowired
	private DependencyService dependencyService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private AmbientService ambientService;
	@Autowired
	private ModifiedComponentService modifiedComponentService;
	@Autowired
	private ReleaseObjectService releaseObjectService;
	@Autowired
	private ConfigurationItemService configurationItemService;
	@Autowired
	private ActionEnvironmentService actionEnvironmentService;
	@Autowired
	private TypeObjectService typeObjectService;
	@Autowired
	private RMReleaseFileService rmReleaseSerivce;

	@RequestMapping(value = "/addReleaseAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse addReleaseAmbient(HttpServletRequest request, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			int release_id = Integer.parseInt(request.getParameter("release_id"));
			int ambient_id = Integer.parseInt(request.getParameter("ambient_id"));
			ambientService.addReleaseAmbient(release_id, ambient_id);
			res.setStatus("success");

		} catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al agregar el ambiente: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al agregar el ambiente. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deleteReleaseAmbient/{release_id}/{ambient_id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteReleaseAmbient(@PathVariable Integer release_id,
			@PathVariable Integer ambient_id, HttpServletRequest request, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			ambientService.deleteReleaseAmbient(release_id, ambient_id);
			res.setStatus("success");

		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar el ambiente: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al eliminar el ambiente. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/addConfigurationItem/{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse addReleaseObject(@PathVariable Integer id, HttpServletRequest request,
			@ModelAttribute("ItemObject") ItemObject rObj, ModelMap model) {
		JsonResponse res = new JsonResponse();

		try {
			Release release = releaseService.findReleaseById(id);

			if (release.existObject(rObj.getName(), Integer.parseInt(rObj.getObjectType()),
					Integer.parseInt(rObj.getObjectConfigurationItem()))) {
				res.setStatus("fail");
				res.setException("El objeto ya existe.");
				return res;
			}

			ReleaseObjectEdit rObjE = new ReleaseObjectEdit(rObj.getName(), rObj.getDescriptionItem(),
					rObj.getRevisionSVN(), release.getModule().getId());

			ConfigurationItem configurationItem = configurationItemService
					.findById(Integer.parseInt(rObj.getObjectConfigurationItem()));
			rObjE.setItemConfiguration(Integer.parseInt(rObj.getObjectConfigurationItem()));
			rObjE.setTypeObject(Integer.parseInt(rObj.getObjectType()));
			rObjE.setRevision_Date(getSqlDate(rObj.getRevisionDate()));
			rObjE.setIsSql((configurationItem.getName().equalsIgnoreCase("Base Datos")) ? 1 : 0);

			Release dependRelease = releaseObjectService.findReleaseToAddByObject(rObjE, release);
			ReleaseUser releaseUser = releaseService.findReleaseUserById(id);
			Dependency dependency = null;
			if (dependRelease != null) {
				dependency = new Dependency();
				dependency.setRelease(releaseUser);
				dependency.setTo_release(dependRelease);
				dependency.setMandatory(true);
				dependency.setIsFunctional(false);
				dependency.setId(release.getExistDependencyId(dependency));
				dependency = dependencyService.save(release, dependency);
			}
			rObjE = releaseObjectService.saveObject(rObjE, release);
			res.setStatus("success");
			res.setData(rObjE.getId() + "");
			res.setObj(dependRelease);

		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al agregar objeto: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al agregar el objeto. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deleteConfigurationItem/{releaseId}/{objectId}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteFileUpload(@PathVariable Integer releaseId,
			@PathVariable Integer objectId) {
		JsonResponse res = new JsonResponse();

		try {
			Boolean anotherDep = false;
			Release release = releaseService.findReleaseById(releaseId);
			ReleaseObject object = release.getObjectById(objectId);
			if (object == null) { // verificar si existe
				res.setStatus("fail");
				res.setException("El objeto seleccionado no existe.");
				return res;
			}

			Release releaseDependency = releaseObjectService.findReleaseToDeleteByObject(release, object);
			// verificar si otro objeto tiene la misma dependencia
			anotherDep = existAnotherDependency(release, object, releaseDependency);

			if (!anotherDep) { // si no tiene otras, se elimina
				Dependency toDelete = null;
				if (releaseDependency != null) {
					for (Dependency item : release.getDependencies()) {
						if (releaseDependency.getReleaseNumber().equals(item.getTo_release().getReleaseNumber())) {
							toDelete = (item.getMandatory() && !item.getIsFunctional()) ? item : null;
						}
					}
				}
				if (toDelete != null) {
					dependencyService.delete(toDelete);
					res.setObj(releaseDependency);
				}
			}
			releaseObjectService.deleteObject(releaseId, object);
			res.setStatus("success");

		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar el objeto: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al eliminar el objeto. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/releaseAutoComplete-{search}-{release_id}", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> requestAutoComplete(@PathVariable String search,
			@PathVariable String release_id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList<JsonAutocomplete>();
		try {
			List<ReleaseUser> list = releaseService.list(search, release_id);

			for (int i = 0; i < list.size(); i++) {
				listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "", list.get(i).getReleaseNumber(),
						list.get(i).getReleaseNumber() + ": " + list.get(i).getDescription()));
			}

		}catch (SQLException ex) {
			logs("SYSTEM_ERROR", "Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error en el método releaseAutoComplete." + getErrorFormat(e));
		}
		return listAutoComplete;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/modifiedComponentAutoComplete", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> modifiedComponentAutoComplete(HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList();
		try {
			List<ModifiedComponent> list = modifiedComponentService.list();
			for (int i = 0; i < list.size(); i++) {
				listAutoComplete.add(
						new JsonAutocomplete(list.get(i).getId() + "", list.get(i).getName(), list.get(i).getName()));
			}
			return listAutoComplete;
		}catch (SQLException ex) {
			logs("SYSTEM_ERROR", "Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error modifiedComponentAutoComplete. " + getErrorFormat(e));
		}
		return null;
	}

	@RequestMapping(value = "/addReleaseEnvironmentAction/{release_id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse addReleaseEnvironmentAction(@PathVariable Integer release_id,
			HttpServletRequest request, @ModelAttribute("ReleaseActionEdit") ReleaseActionEdit action, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			action = actionEnvironmentService.addReleaseAction(action, release_id);
			res.setStatus("success");
			res.setData(action.getId() + "");
		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al agregar la acción: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al agregar la acción. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deleteReleaseEnvironmentAction/{release_id}/{action_id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteReleaseEnvironmentAction(@PathVariable Integer release_id,
			@PathVariable Integer action_id, HttpServletRequest request, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			actionEnvironmentService.deleteReleaseDependency(action_id, release_id);
			res.setStatus("success");
		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar la acción: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al eliminar la acción. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/uploadCSV/{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse requestAutoComplete(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseEdit release = releaseService.findEditById(id);
			ArrayList<MyError> errors = new ArrayList<MyError>();
			boolean isEmpty = false;
			String[] lines = null;
			String[] line = null;
			String infoErrors = "";
			BulkLoad bl = new BulkLoad();
			String csvLines = request.getParameter("csv");
			res.setData(csvLines);
			lines = csvLines.split("\n");
			Map<String, Integer> duplicates = new HashMap<String, Integer>();
			for (int i = 0; i < lines.length; i++) {
				if (duplicates.containsKey(lines[i].split(";")[0])) {
					duplicates.put(lines[i].split(";")[0], duplicates.get(lines[i].split(";")[0]) + 1);
				} else {
					duplicates.put(lines[i].split(";")[0], 1);
				}
			}
			for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
				if (entry.getValue() > 1)
					errors.add(new MyError("Elemento " + entry.getKey(), " repetido"));
			}
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].split(";").length != 6) {
					errors.add(new MyError("Linea# " + (i + 1), "No cumple formato"));
				} else {
					line = lines[i].split(";");
					isEmpty = false;
					for (String field : line) {
						if (field.trim().equals(""))
							isEmpty = true;
					}
					if (isEmpty)
						errors.add(new MyError("Linea# " + (i + 1), "Tiene campos en blanco"));
					else
						validLine(errors, line, i, release);
				}
			}
			if (errors.size() != 0) {
				res.setStatus("fail");
				for (MyError error : errors) {
					infoErrors += error.getKey() + " " + error.getMessage() + "\n";
				}
				res.setException(infoErrors);
				res.setErrors(errors);
				return res;
			}
			ArrayList<ReleaseObjectEdit> objects = createObjects(csvLines, release);
			ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
			ArrayList<Release> releaseDependencies = new ArrayList<Release>();
			ReleaseUser releaseUser = releaseService.findReleaseUserById(id);
			Release releaseObjects = releaseService.findReleaseById(id);
			Dependency dependency = null;
			Release dependRelease = null;
			for (ReleaseObjectEdit rObjE : objects) {
				dependRelease = releaseObjectService.findReleaseToAddByObject(rObjE, releaseObjects);// Busco_dependencias
				if (dependRelease != null) {
					releaseDependencies.add(dependRelease);
				}
			}
			releaseDependencies = deleteReleaseDuplicate(releaseDependencies); // elimino dependencias repetidas
			for (Release item : releaseDependencies) { // verifica si ya existen
				dependency = new Dependency();
				dependency.setRelease(releaseUser);
				dependency.setTo_release(item);
				dependency.setMandatory(true);
				dependency.setIsFunctional(false);
				dependency.setId(item.getExistDependencyId(dependency));
				dependencies.add(dependency);
			}
			if (dependencies.size() != 0) {
				dependencies = dependencyService.save(releaseObjects, dependencies);
				bl.setDependencies(dependencies);
			}
			objects = releaseService.saveReleaseObjects(release.getId(), objects);
			bl.setObjects(objects);
			res.setObj(bl);
			res.setStatus("success");
		} catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al cargar csv: " + e);
			logs("RELEASE_ERROR", "Error al cargar csv. " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/synchronize/{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse synchronize(@PathVariable Integer id, HttpServletRequest request,
			ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseEdit release = releaseService.findEditById(id);
			List<RMReleaseFile> list = rmReleaseSerivce.listByRelease(release);
			ReleaseObjectEdit obj = null;
			ArrayList<ReleaseObjectEdit> objects = new ArrayList<ReleaseObjectEdit>();
			ConfigurationItem configurationItem = null;
			TypeObject type = null;
			BulkLoad bl = new BulkLoad();
			if (list.size() != 0) {
				for (RMReleaseFile rmReleaseFile : list) {
					obj = new ReleaseObjectEdit(rmReleaseFile.getFilename(), rmReleaseFile.getFilename(),
							rmReleaseFile.getRevision() + "", release.getModule_id());

					type = getTypeObjectSincronice(rmReleaseFile, release);
					configurationItem = getConfigurationItemSincronice(rmReleaseFile, release);
					obj.setTypeObject(type.getId());
					obj.setItemConfiguration(configurationItem.getId());
					obj.setModuleId(release.getModule_id());
					obj.setRevision_Date(getSqlDate(getSystemDate().toLowerCase()));
					obj.setIsSql((configurationItem.getName().equalsIgnoreCase("Base Datos")) ? 1 : 0);
					if (!release.existObject(obj)) {
						objects.add(obj);
					}
				}
			}
			ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
			ArrayList<Release> releaseDependencies = new ArrayList<Release>();
			ReleaseUser releaseUser = releaseService.findReleaseUserById(id);
			Release releaseObjects = releaseService.findReleaseById(id);
			Dependency dependency = null;
			Release dependRelease = null;
			for (ReleaseObjectEdit rObjE : objects) {
				dependRelease = releaseObjectService.findReleaseToAddByObject(rObjE, releaseObjects);// Busco_dependencias
				if (dependRelease != null) {
					releaseDependencies.add(dependRelease);
				}
			}
			releaseDependencies = deleteReleaseDuplicate(releaseDependencies); // elimino dependencias repetidas
			for (Release item : releaseDependencies) { // verifica si ya existen
				dependency = new Dependency();
				dependency.setRelease(releaseUser);
				dependency.setTo_release(item);
				dependency.setMandatory(true);
				dependency.setIsFunctional(false);
				dependency.setId(item.getExistDependencyId(dependency));
				dependencies.add(dependency);
			}
			if (dependencies.size() != 0) {
				dependencies = dependencyService.save(releaseObjects, dependencies);
				bl.setDependencies(dependencies);
			}

			if (objects.size() > 0) {
				objects = releaseService.saveReleaseObjects(release.getId(), objects);
			}
			res.setStatus("success");
			bl.setObjects(objects);
			res.setObj(bl);
		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al sincronizar los objetos: " + e.getMessage());
			logs("RELEASE_ERROR", "Error al sincronizar los objetos. " + getErrorFormat(e));
		}
		return res;
	}

	public ConfigurationItem getConfigurationItemSincronice(RMReleaseFile rmReleaseFile, ReleaseEdit release) {
		ConfigurationItem confItem = null;
		if (FilenameUtils.getExtension(rmReleaseFile.getFilename()).equalsIgnoreCase("SQL")) {
			confItem = configurationItemService.findByName("Base Datos", release.getSystem().getId());
		} else {
			confItem = configurationItemService.findByName("Objeto", release.getSystem().getId());
		}
		return confItem;
	}

	public TypeObject getTypeObjectSincronice(RMReleaseFile rmReleaseFile, ReleaseEdit release) {
		TypeObject type = null;
		if (FilenameUtils.getExtension(rmReleaseFile.getFilename()).equals("")) {
			// Si no hay extension se busca solo por nombre
			if (!FilenameUtils.getBaseName(rmReleaseFile.getFilename()).equals("")) {
				type = typeObjectService.findByName(FilenameUtils.getBaseName(rmReleaseFile.getFilename()),
						release.getSystem().getId());
			}

		} else {
			type = typeObjectService.findByName(FilenameUtils.getBaseName(rmReleaseFile.getFilename()),
					(FilenameUtils.getExtension(rmReleaseFile.getFilename())), release.getSystem().getId());
		}

		if (type == null) {
			type = typeObjectService.findByName("Objeto", release.getSystem().getId());
		}

		if (type == null) {
			type = new TypeObject();
			type.setName("Objeto");
			type.setExtension(".");
			type.setSystem_id(release.getSystem().getId());
			type.setDescription("Objeto");
			type = typeObjectService.save(type);
		}
		return type;
	}

	public ArrayList<ReleaseObjectEdit> createObjects(String csv, ReleaseEdit release) throws Exception {
		ArrayList<ReleaseObjectEdit> objects = new ArrayList<ReleaseObjectEdit>();
		String[] lines = csv.split("\n");
		ReleaseObjectEdit object = null;
		for (int i = 0; i < lines.length; i++) { // line
			object = new ReleaseObjectEdit();
			object.setName(lines[i].split(";")[0]); // name
			object.setDescription(lines[i].split(";")[1]); // description
			object.setRevision_SVN(lines[i].split(";")[2]); // Revision_SVN
			object.setRevision_Date(getSqlDate(lines[i].split(";")[3])); // Timestamp
			object.setItemConfiguration(
					configurationItemService.findByName(lines[i].split(";")[4], release.getSystem().getId()).getId());
			object.setTypeObject(
					typeObjectService.findByName(lines[i].split(";")[5], release.getSystem().getId()).getId());
			object.setIsSql((lines[i].split(";")[4].equals("Base Datos")) ? 1 : 0);
			object.setModuleId(release.getModule_id());
			objects.add(object);
		}
		return objects;
	}

	public ArrayList<MyError> validLine(ArrayList<MyError> errors, String[] line, int i, ReleaseEdit release) {
		if (isSqlDate(line[3].trim()) == null) {
			errors.add(new MyError("Linea# " + (i + 1), "Fecha en formato incorrecto"));
		}
		if (release.existObject(line[0].trim())) {// nombre no exista ya para el release
			errors.add(new MyError("Linea# " + (i + 1) + " Nombre " + (line[0].trim()), "Ya existe"));
		}
		if (!configurationItemService.existItem((line[4].trim()), release.getSystem().getId())) {// NombreItem exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreItemConfiguración " + (line[4].trim()), "inválido"));
		}
		if (!typeObjectService.existTypeObject((line[5].trim()), release.getSystem().getId())) {// NombreTipo exista
			errors.add(new MyError("Linea# " + (i + 1) + " NombreTipo " + (line[5].trim()), "inválido"));
		}
		return errors;
	}

	public ArrayList<Release> deleteReleaseDuplicate(ArrayList<Release> list) {
		Boolean exist = false;
		ArrayList<Release> newList = new ArrayList<Release>();
		for (Release release : list) {
			exist = false;
			for (Release releaseNew : newList) {
				if (releaseNew.equals(release)) {
					exist = true;
				}
			}
			if (!exist) {
				newList.add(release);
			}
		}
		return newList;
	}

	public Boolean existAnotherDependency(Release release, ReleaseObject object, Release releaseDependency) {
		Release anotherDependency = null;
		for (ReleaseObject item : release.getObjects()) {
			if (item.getId() != object.getId()) {
				anotherDependency = releaseObjectService.findReleaseToDeleteByObject(release, item);
				if (anotherDependency != null && releaseDependency != null) {
					if (anotherDependency.getReleaseNumber().equals(releaseDependency.getReleaseNumber())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
