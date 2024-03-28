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
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.pos.PStatusIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.pos.PStatusIncidenceService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/statusIncidence")
public class StatusIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(StatusIncidenceController.class);

	@Autowired
	StatusIncidenceService statusIncidenceService;
	
	@Autowired
	PStatusIncidenceService pstatusIncidenceService;
	
	private final Environment environment;
	
	@Autowired
	public StatusIncidenceController(Environment environment) {
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

		return "/admin/statusIncidence/statusIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<StatusIncidence> statusIncidence = new JsonSheet<>();
				statusIncidence.setData(statusIncidenceService.findAll());
				return statusIncidence;
			} else if (profile.equals("postgres")) {
				JsonSheet<PStatusIncidence> statusIncidence = new JsonSheet<>();
				statusIncidence.setData(pstatusIncidenceService.findAll());
				return statusIncidence;
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusIncidence addStatusIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusIncidenceService.save(addStatusIncidence);
			} else if (profile.equals("postgres")) {
				PStatusIncidence paddStatusIncidence=new PStatusIncidence();
				paddStatusIncidence.setCode(addStatusIncidence.getCode());
				paddStatusIncidence.setDescription(addStatusIncidence.getDescription());
				paddStatusIncidence.setName(addStatusIncidence.getName());
				paddStatusIncidence.setReason(addStatusIncidence.getReason());
				pstatusIncidenceService.save(paddStatusIncidence);
			}
			
			

			res.setMessage("Estado incidencia agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRequest");
			res.setStatus("exception");
			res.setMessage("Error al agregar estado incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody StatusIncidence uptStatusIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusIncidenceService.update(uptStatusIncidence);
			} else if (profile.equals("postgres")) {
				PStatusIncidence puptStatusIncidence=new PStatusIncidence();
				puptStatusIncidence.setId(uptStatusIncidence.getId());
				puptStatusIncidence.setCode(uptStatusIncidence.getCode());
				puptStatusIncidence.setDescription(uptStatusIncidence.getDescription());
				puptStatusIncidence.setName(uptStatusIncidence.getName());
				puptStatusIncidence.setReason(uptStatusIncidence.getReason());
				pstatusIncidenceService.update(puptStatusIncidence);
			}
			
			
			res.setMessage("Estado incidencia modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusIncidence");
			res.setStatus("exception");
			res.setMessage("Error al modificar Estado  de la incidencia!");
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
				statusIncidenceService.delete(id);
			} else if (profile.equals("postgres")) {
				pstatusIncidenceService.delete(id);
			}
			
			res.setMessage("Estado incidencia eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el estado de incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

}