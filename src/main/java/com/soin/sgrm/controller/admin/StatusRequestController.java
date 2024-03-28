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
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PStatusRequest;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.pos.PStatusRequestService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/statusRequest")
public class StatusRequestController extends BaseController {
	public static final Logger logger = Logger.getLogger(StatusRequestController.class);

	@Autowired
	StatusRequestService statusRequestService;
	
	@Autowired
	PStatusRequestService pstatusRequestService;
	private final Environment environment;

	@Autowired
	public StatusRequestController(Environment environment) {
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

		return "/admin/statusRequest/statusRequest";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<StatusRequest> statusRequests = new JsonSheet<>();
				statusRequests.setData(statusRequestService.findAll());
				return statusRequests;

			} else if (profile.equals("postgres")) {
				JsonSheet<PStatusRequest> pstatusRequests = new JsonSheet<>();
				pstatusRequests.setData(pstatusRequestService.findAll());
				return pstatusRequests;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusRequest addStatusRequest) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusRequestService.save(addStatusRequest);

			} else if (profile.equals("postgres")) {
				PStatusRequest paddStatusRequest= new PStatusRequest();
				paddStatusRequest.setCode(addStatusRequest.getCode());
				paddStatusRequest.setDescription(addStatusRequest.getDescription());
				paddStatusRequest.setName(addStatusRequest.getName());
				paddStatusRequest.setReason(addStatusRequest.getReason());
				pstatusRequestService.save(paddStatusRequest);
			}
			

			res.setMessage("Estado Solicitado agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRequest");
			res.setStatus("exception");
			res.setMessage("Error al agregar Status Request!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody StatusRequest uptStatusRequest) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusRequestService.update(uptStatusRequest);
			} else if (profile.equals("postgres")) {
				PStatusRequest puptStatusRequest= new PStatusRequest();
				puptStatusRequest.setCode(uptStatusRequest.getCode());
				puptStatusRequest.setDescription(uptStatusRequest.getDescription());
				puptStatusRequest.setName(uptStatusRequest.getName());
				puptStatusRequest.setReason(uptStatusRequest.getReason());
				puptStatusRequest.setId(uptStatusRequest.getId());
				pstatusRequestService.update(puptStatusRequest);
			}
			res.setMessage("Estado solicitud modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRequest");
			res.setStatus("exception");
			res.setMessage("Error al modificar Estado  de la Solicitud!");
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
				statusRequestService.delete(id);

			} else if (profile.equals("postgres")) {
				pstatusRequestService.delete(id);
			}
			
			res.setMessage("Estado solicitado eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el estado de solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
