package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemConfiguration;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PSystemConfigurationService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/systemConfig")
public class SystemConfigurationController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(SystemConfigurationController.class);

	@Autowired
	private SystemConfigurationService systemConfigurationService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PSystemConfigurationService psystemConfigurationService;
	@Autowired
	private PSystemService psystemService;

	
	private final Environment environment;

	@Autowired
	public SystemConfigurationController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("listSystemConfig", systemConfigurationService.list());
			model.addAttribute("systemConfig", new SystemConfiguration());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("system", new SystemInfo());
		} else if (profile.equals("postgres")) {
			model.addAttribute("listSystemConfig", psystemConfigurationService.list());
			model.addAttribute("systemConfig", new PSystemConfiguration());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("system", new PSystemInfo());
		}
		
		
		return "/admin/systemConfig/systemConfig";
	}

	@RequestMapping(value = "/findSystemConfig/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findSystemConfig(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				SystemConfiguration systemConfig = systemConfigurationService.findById(id);
				return systemConfig;
			} else if (profile.equals("postgres")) {
				PSystemConfiguration psystemConfig = psystemConfigurationService.findById(id);
				return psystemConfig;
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "systemConfig");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(value = "/updateSystemConfig", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateSystemConfig(HttpServletRequest request,
			@ModelAttribute("SystemConfiguration") SystemConfiguration sys, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				SystemConfiguration systemConfig = systemConfigurationService.findById(sys.getId());
				systemConfig.setObservations(sys.getObservations());
				systemConfig.setSolutionInfo(sys.getSolutionInfo());
				systemConfig.setDefinitionEnvironment(sys.getDefinitionEnvironment());
				systemConfig.setInstalationData(sys.getInstalationData());
				systemConfig.setDataBaseInstructions(sys.getDataBaseInstructions());
				systemConfig.setDownEnvironment(sys.getDownEnvironment());
				systemConfig.setEnvironmentObservations(sys.getEnvironmentObservations());
				systemConfig.setSuggestedTests(sys.getSuggestedTests());
				systemConfig.setConfigurationItems(sys.getConfigurationItems());
				systemConfig.setDependencies(sys.getDependencies());
				systemConfig.setAttachmentFiles(sys.getAttachmentFiles());
				systemConfig.setApplicationVersion(sys.isApplicationVersion());
				systemConfig = systemConfigurationService.update(systemConfig);
				res.setObj(systemConfig);
			} else if (profile.equals("postgres")) {
				PSystemConfiguration psystemConfig = psystemConfigurationService.findById(sys.getId());
				psystemConfig.setObservations(sys.getObservations());
				psystemConfig.setSolutionInfo(sys.getSolutionInfo());
				psystemConfig.setDefinitionEnvironment(sys.getDefinitionEnvironment());
				psystemConfig.setInstalationData(sys.getInstalationData());
				psystemConfig.setDataBaseInstructions(sys.getDataBaseInstructions());
				psystemConfig.setDownEnvironment(sys.getDownEnvironment());
				psystemConfig.setEnvironmentObservations(sys.getEnvironmentObservations());
				psystemConfig.setSuggestedTests(sys.getSuggestedTests());
				psystemConfig.setConfigurationItems(sys.getConfigurationItems());
				psystemConfig.setDependencies(sys.getDependencies());
				psystemConfig.setAttachmentFiles(sys.getAttachmentFiles());
				psystemConfig.setApplicationVersion(sys.isApplicationVersion());
				psystemConfig = psystemConfigurationService.update(psystemConfig);
				res.setObj(psystemConfig);
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemConfig");
			res.setStatus("exception");
			res.setException("Error al modificar configuración: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/createSystemConfig", method = RequestMethod.POST)
	public @ResponseBody JsonResponse createSystemConfig(HttpServletRequest request,
			@ModelAttribute("SystemConfiguration") SystemConfiguration sys, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			if(sys.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
				return res;
			}
			String profile = profileActive();
			if (profile.equals("oracle")) {
				SystemConfiguration systemConfig = systemConfigurationService.findBySystemId(sys.getSystemId());

				if (systemConfig != null) {
					res.setStatus("fail");
					res.addError("systemId", "Ya existe una configuración para el sistema");
				} else {
					systemConfig = new SystemConfiguration();
					systemConfig.setObservations(sys.getObservations());
					systemConfig.setSolutionInfo(sys.getSolutionInfo());
					systemConfig.setDefinitionEnvironment(sys.getDefinitionEnvironment());
					systemConfig.setInstalationData(sys.getInstalationData());
					systemConfig.setDataBaseInstructions(sys.getDataBaseInstructions());
					systemConfig.setDownEnvironment(sys.getDownEnvironment());
					systemConfig.setEnvironmentObservations(sys.getEnvironmentObservations());
					systemConfig.setSuggestedTests(sys.getSuggestedTests());
					systemConfig.setConfigurationItems(sys.getConfigurationItems());
					systemConfig.setDependencies(sys.getDependencies());
					systemConfig.setAttachmentFiles(sys.getAttachmentFiles());
					systemConfig.setApplicationVersion(sys.isApplicationVersion());
					SystemInfo system = new SystemInfo();
					system.setId(sys.getSystemId());
					systemConfig.setSystem(system);
					systemConfigurationService.save(systemConfig);
				}
			} else if (profile.equals("postgres")) {
				PSystemConfiguration psystemConfig = psystemConfigurationService.findBySystemId(sys.getSystemId());

				if (psystemConfig != null) {
					res.setStatus("fail");
					res.addError("systemId", "Ya existe una configuración para el sistema");
				} else {
					psystemConfig = new PSystemConfiguration();
					psystemConfig.setObservations(sys.getObservations());
					psystemConfig.setSolutionInfo(sys.getSolutionInfo());
					psystemConfig.setDefinitionEnvironment(sys.getDefinitionEnvironment());
					psystemConfig.setInstalationData(sys.getInstalationData());
					psystemConfig.setDataBaseInstructions(sys.getDataBaseInstructions());
					psystemConfig.setDownEnvironment(sys.getDownEnvironment());
					psystemConfig.setEnvironmentObservations(sys.getEnvironmentObservations());
					psystemConfig.setSuggestedTests(sys.getSuggestedTests());
					psystemConfig.setConfigurationItems(sys.getConfigurationItems());
					psystemConfig.setDependencies(sys.getDependencies());
					psystemConfig.setAttachmentFiles(sys.getAttachmentFiles());
					psystemConfig.setApplicationVersion(sys.isApplicationVersion());
					PSystemInfo psystem = new PSystemInfo();
					psystem.setId(sys.getSystemId());
					psystemConfig.setSystem(psystem);
					psystemConfigurationService.save(psystemConfig);
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemConfig");
			res.setStatus("exception");
			res.setException("Error al guardar configuración: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
