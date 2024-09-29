package com.soin.sgrm.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.PButtonInfraService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/buttonInfra")
public class ButtonManagerController extends BaseController {
	public static final Logger logger = Logger.getLogger(ButtonManagerController.class);

	@Autowired
	PButtonInfraService pButtonInfraService;

	@Autowired
	PSystemService psystemService;

	private final Environment environment;

	@Autowired
	public ButtonManagerController(Environment environment) {
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

	
		model.addAttribute("systems", psystemService.listAll());
		model.addAttribute("system", new PProject());
		model.addAttribute("emailTemplate", new PEmailTemplate());

		return "buttons/button";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			JsonSheet<PButtonInfra> buttons = new JsonSheet<>();
			buttons.setData(pButtonInfraService.findAll());
			return buttons;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PButtonInfra pbuttonInfra) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			boolean exists = pButtonInfraService.existsBySystemId(pbuttonInfra.getSystemId());
			if(!exists) {
				PSystemInfo system=psystemService.findById(pbuttonInfra.getSystemId());
				pbuttonInfra.setSystem(system);
				pButtonInfraService.save(pbuttonInfra);
				res.setMessage("Se agrego correctamente el boton!");
			}else {
				res.setStatus("error");
				res.setMessage("Error al agrega boton ya hay un boton para este sistema!");
			}
		
		} catch (Exception e) {
			Sentry.capture(e, "pbuttonInfra");
			res.setStatus("error");
			res.setMessage("Error al agregar boton!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,@RequestBody PButtonInfra uptpbuttonInfra) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystemInfo system=psystemService.findById(uptpbuttonInfra.getSystemId());
			uptpbuttonInfra.setSystem(system);
			pButtonInfraService.update(uptpbuttonInfra);
			res.setMessage("Se modifico correctamente el boton!");
		} catch (Exception e) {
			Sentry.capture(e, "pbuttonInfra");
			res.setStatus("error");
			res.setMessage("Error al modificar boton!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			pButtonInfraService.delete(id);

			res.setMessage("Boton eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "pbuttonInfra");
			res.setStatus("error");
			res.setMessage("Error al eliminar el boton!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(path = "/validateSystemId", method = RequestMethod.POST)
	public @ResponseBody String  checkSysName(HttpServletRequest request, @RequestParam("systemId") Integer systemId,@RequestParam("sId") Long sId) {

		try {
			boolean exists=false;
			if(sId==null) {
				exists = pButtonInfraService.existsBySystemId(systemId);
			}else {
				 exists = pButtonInfraService.existsBySystemIdandId(sId,systemId);
			}
			 
			 return exists ? "false" : "true";
		} catch (Exception e) {
			Sentry.capture(e, "request");

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "false" ;
	}
}
