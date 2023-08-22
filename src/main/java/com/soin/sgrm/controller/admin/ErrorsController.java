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
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PDocTemplate;
import com.soin.sgrm.model.pos.PErrors_RFC;
import com.soin.sgrm.model.pos.PErrors_Release;
import com.soin.sgrm.model.pos.PErrors_Requests;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ErrorRequestService;
import com.soin.sgrm.service.pos.PErrorRFCService;
import com.soin.sgrm.service.pos.PErrorReleaseService;
import com.soin.sgrm.service.pos.PErrorRequestService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/error")
public class ErrorsController extends BaseController {
	public static final Logger logger = Logger.getLogger(ErrorsController.class);

	@Autowired
	ErrorReleaseService errorReleaseService;

	@Autowired
	ErrorRFCService errorRFCService;

	@Autowired
	ErrorRequestService errorRequestService;

	@Autowired
	PErrorReleaseService perrorReleaseService;

	@Autowired
	PErrorRFCService perrorRFCService;

	@Autowired
	PErrorRequestService perrorRequestService;

	private final Environment environment;

	@Autowired
	public ErrorsController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = { "", "/release" }, method = RequestMethod.GET)
	public String indexRelease(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/errors/errorsRelease";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/release/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRelease(HttpServletRequest request, Locale locale, Model model) {
		String profile = profileActive();
		if (profile.equals("oracle")) {
			JsonSheet<Errors_Release> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(errorReleaseService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		} else if (profile.equals("postgres")) {
			JsonSheet<PErrors_Release> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(perrorReleaseService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		}
		return null;

	}

	@RequestMapping(path = "/release", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody Errors_Release addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorReleaseService.save(addError);
			} else if (profile.equals("postgres")) {
				PErrors_Release perrorRelease = new PErrors_Release();
				perrorRelease.setDescription(addError.getDescription());
				perrorRelease.setName(addError.getName());
				perrorReleaseService.save(perrorRelease);
			}

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al agregar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/release", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse updateRelease(HttpServletRequest request, @RequestBody Errors_Release uptError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorReleaseService.update(uptError);
			} else if (profile.equals("postgres")) {
				PErrors_Release puptError = new PErrors_Release();
				puptError.setDescription(uptError.getDescription());
				puptError.setName(uptError.getName());
				puptError.setId(uptError.getId());
				perrorReleaseService.update(puptError);
			}

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al modificar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/release/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRelease(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorReleaseService.delete(id);
			} else if (profile.equals("postgres")) {
				perrorReleaseService.delete(id);
			}

			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("error");
			res.setMessage("Error al eliminar el Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "", "/rfc" }, method = RequestMethod.GET)
	public String indexRFC(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/errors/errorsRFC";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/rfc/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRFC(HttpServletRequest request, Locale locale, Model model) {
		String profile = profileActive();
		if (profile.equals("oracle")) {
			JsonSheet<Errors_RFC> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(errorRFCService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		} else if (profile.equals("postgres")) {
			JsonSheet<PErrors_RFC> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(perrorRFCService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		}
		return null;

	}

	@RequestMapping(path = "/rfc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRFC(HttpServletRequest request, @RequestBody Errors_RFC addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRFCService.save(addError);
			} else if (profile.equals("postgres")) {
				PErrors_RFC paddError=new PErrors_RFC();
				paddError.setDescription(addError.getDescription());
				paddError.setName(addError.getName());
				perrorRFCService.save(paddError);
			}
			

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al agregar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/rfc", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse updateRFC(HttpServletRequest request, @RequestBody Errors_RFC uptError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRFCService.update(uptError);
			} else if (profile.equals("postgres")) {
				PErrors_RFC puptError=new PErrors_RFC();
				puptError.setId(uptError.getId());
				puptError.setName(uptError.getName());
				puptError.setDescription(uptError.getDescription());
				perrorRFCService.update(puptError);
			}
			

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al modificar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/rfc/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRFCService.delete(id);
			} else if (profile.equals("postgres")) {
				perrorRFCService.delete(id);
			}
			
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("error");
			res.setMessage("Error al eliminar el Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "", "/request" }, method = RequestMethod.GET)
	public String indexRequest(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/errors/errorsRequest";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/request/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRequest(HttpServletRequest request, Locale locale, Model model) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			JsonSheet<Errors_Requests> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(errorRequestService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		} else if (profile.equals("postgres")) {
			JsonSheet<PErrors_Requests> statusRFCs = new JsonSheet<>();
			try {
				statusRFCs.setData(perrorRequestService.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return statusRFCs;
		}
		return null;
		
	}

	@RequestMapping(path = "/request", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRequest(HttpServletRequest request, @RequestBody Errors_Requests addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRequestService.save(addError);
			} else if (profile.equals("postgres")) {
				PErrors_Requests paddError=new PErrors_Requests();
				paddError.setDescription(addError.getDescription());
				paddError.setName(addError.getName());
				perrorRequestService.save(paddError);
			}
			
			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al agregar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/request", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse updateRequest(HttpServletRequest request, @RequestBody Errors_Requests uptError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRequestService.update(uptError);
			} else if (profile.equals("postgres")) {
				PErrors_Requests puptError=new PErrors_Requests();
				puptError.setDescription(uptError.getDescription());
				puptError.setName(uptError.getName());
				puptError.setId(uptError.getId());
				perrorRequestService.update(puptError);
			}
			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("error");
			res.setMessage("Error al modificar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/request/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRequest(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				errorRequestService.delete(id);
			} else if (profile.equals("postgres")) {
				perrorRequestService.delete(id);
			}
			
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("error");
			res.setMessage("Error al eliminar el Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}