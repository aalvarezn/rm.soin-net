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
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.StatusKnowlege;
import com.soin.sgrm.model.pos.PStatusKnowlege;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusKnowlegeService;
import com.soin.sgrm.service.pos.PStatusKnowlegeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/statusKnowledge")
public class StatusKnowlegeController extends BaseController {
	public static final Logger logger = Logger.getLogger(StatusKnowlegeController.class);

	@Autowired
	StatusKnowlegeService statusKnowlegeService;

	@Autowired
	PStatusKnowlegeService pstatusKnowlegeService;
	private final Environment environment;

	@Autowired
	public StatusKnowlegeController(Environment environment) {
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

		return "/incidence/knowledge/status/statusKnowledge";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {

			if (profileActive().equals("oracle")) {
				JsonSheet<StatusKnowlege> StatusKnowleges = new JsonSheet<>();
				StatusKnowleges.setData(statusKnowlegeService.findAll());
				return StatusKnowleges;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PStatusKnowlege> StatusKnowleges = new JsonSheet<>();
				StatusKnowleges.setData(pstatusKnowlegeService.findAll());
				return StatusKnowleges;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusKnowlege addStatusKnowlege) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				statusKnowlegeService.save(addStatusKnowlege);
			} else if (profileActive().equals("postgres")) {
				PStatusKnowlege paddStatusKnowlege=new PStatusKnowlege();
				paddStatusKnowlege.setCode(addStatusKnowlege.getCode());
				paddStatusKnowlege.setDescription(addStatusKnowlege.getDescription());
				paddStatusKnowlege.setName(addStatusKnowlege.getName());
				paddStatusKnowlege.setReason(addStatusKnowlege.getReason());
				pstatusKnowlegeService.save(paddStatusKnowlege);
			}

			

			res.setMessage("Estado agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al agregar estado Request!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody StatusKnowlege uptStatusKnowlege) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			if (profileActive().equals("oracle")) {
				statusKnowlegeService.update(uptStatusKnowlege);
			} else if (profileActive().equals("postgres")) {
				PStatusKnowlege puptStatusKnowlege=new PStatusKnowlege();
				puptStatusKnowlege.setCode(uptStatusKnowlege.getCode());
				puptStatusKnowlege.setDescription(uptStatusKnowlege.getDescription());
				puptStatusKnowlege.setName(uptStatusKnowlege.getName());
				puptStatusKnowlege.setReason(uptStatusKnowlege.getReason());
				puptStatusKnowlege.setId(uptStatusKnowlege.getId());
				pstatusKnowlegeService.update(puptStatusKnowlege);
			}

			

			res.setMessage("Estado modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al modificar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				statusKnowlegeService.delete(id);
			} else if (profileActive().equals("postgres")) {
				pstatusKnowlegeService.delete(id);
			}

			
			res.setMessage("Estado eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al eliminar el estado de solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
