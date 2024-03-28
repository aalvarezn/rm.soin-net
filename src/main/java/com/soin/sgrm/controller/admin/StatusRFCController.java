package com.soin.sgrm.controller.admin;

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
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/statusRFC")
public class StatusRFCController extends BaseController {
	public static final Logger logger = Logger.getLogger(StatusRFCController.class);

	@Autowired
	StatusRFCService statusRFCService;

	@Autowired
	PStatusRFCService pstatusRFCService;

	private final Environment environment;

	@Autowired
	public StatusRFCController(Environment environment) {
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

		return "/admin/statusRFC/statusRFC";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<StatusRFC> statusRFCs = new JsonSheet<>();
				statusRFCs.setData(statusRFCService.findAll());
				return statusRFCs;

			} else if (profile.equals("postgres")) {
				JsonSheet<PStatusRFC> statusRFCs = new JsonSheet<>();
				statusRFCs.setData(pstatusRFCService.findAll());
				return statusRFCs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusRFC addStatusRFC) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusRFCService.save(addStatusRFC);
			} else if (profile.equals("postgres")) {
				PStatusRFC paddStatusRFC=new PStatusRFC();
				paddStatusRFC.setName(addStatusRFC.getName());
				paddStatusRFC.setCode(addStatusRFC.getCode());
				paddStatusRFC.setReason(addStatusRFC.getReason());
				paddStatusRFC.setDescription(addStatusRFC.getDescription());
				
				pstatusRFCService.save(paddStatusRFC);
			}
			

			res.setMessage("Estado RFC agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRFC");
			res.setStatus("exception");
			res.setMessage("Error al agregar Estado RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody StatusRFC uptStatusRFC) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusRFCService.update(uptStatusRFC);

			} else if (profile.equals("postgres")) {
				PStatusRFC puptStatusRFC=new PStatusRFC();
				puptStatusRFC.setCode(uptStatusRFC.getCode());
				puptStatusRFC.setDescription(uptStatusRFC.getDescription());
				puptStatusRFC.setId(uptStatusRFC.getId());
				puptStatusRFC.setReason(uptStatusRFC.getReason());
				puptStatusRFC.setName(uptStatusRFC.getName());
				pstatusRFCService.update(puptStatusRFC);

			}
			

			res.setMessage("Estado RFC modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRFC");
			res.setStatus("exception");
			res.setMessage("Error al modificar estado RFC!");
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
			if (profile.equals("oracle")) {
				statusRFCService.delete(id);
			} else if (profile.equals("postgres")) {
				pstatusRFCService.delete(id);
			}
			
			res.setMessage("Estado RFC eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRFC");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el estado RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
