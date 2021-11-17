package com.soin.sgrm.controller.admin;

import java.util.Locale;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.service.SystemConfigurationService;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/systemConfig")
public class SystemConfigurationController extends BaseController {

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("listSystemConfig", systemConfigurationService.list());
		model.addAttribute("systemConfig", new SystemConfiguration());
		return "/admin/systemConfig/systemConfig";
	}

	@RequestMapping(value = "/findSystemConfig/{id}", method = RequestMethod.GET)
	public @ResponseBody SystemConfiguration findSystemConfig(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			SystemConfiguration systemConfig = systemConfigurationService.findById(id);
			return systemConfig;
		} catch (Exception e) {
			logs("ADMIN_ERROR", "Error findSystemConfig. " + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(value = "/updateSystemConfig", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateSystemConfig(HttpServletRequest request,
			@ModelAttribute("SystemConfiguration") SystemConfiguration sys, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			SystemConfiguration systemConfig = systemConfigurationService.findById(sys.getId());
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
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al guardar configuración: " + e.toString());
			logs("ADMIN_ERROR", "Error al guardar configuración: " + getErrorFormat(e));
		}
		return res;
	}

}
