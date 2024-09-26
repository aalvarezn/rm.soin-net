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
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.pos.PButtonInfraService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
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

		String profile = profileActive();

		model.addAttribute("systems", psystemService.listAll());
		model.addAttribute("system", new PProject());
		model.addAttribute("emailTemplate", new PEmailTemplate());

		return "/admin/siges/siges";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			String profile = profileActive();

			JsonSheet<PButtonInfra> buttons = new JsonSheet<>();
			buttons.setData(pButtonInfraService.findAll());
			return buttons;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Siges addSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();

		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al agregar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody Siges uptSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();

		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al modificar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			pButtonInfraService.delete(id);

			res.setMessage("Siges eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al eliminar el siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
