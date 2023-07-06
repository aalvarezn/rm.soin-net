package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ErrorRequestService;
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
	
	
	
	@RequestMapping(value = { "", "/release" }, method = RequestMethod.GET)
	public String indexRelease(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/errors/errorsRelease";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/release/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRelease(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<Errors_Release> statusRFCs = new JsonSheet<>();
		try {
			statusRFCs.setData(errorReleaseService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusRFCs;
	}

	@RequestMapping(path = "/release", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody Errors_Release addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			errorReleaseService.save(addError);

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorReleaseService.update(uptError);

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorReleaseService.delete(id);
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("exception");
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
		JsonSheet<Errors_RFC> statusRFCs = new JsonSheet<>();
		try {
			statusRFCs.setData(errorRFCService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusRFCs;
	}

	@RequestMapping(path = "/rfc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRFC(HttpServletRequest request, @RequestBody Errors_RFC addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			errorRFCService.save(addError);

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorRFCService.update(uptError);

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorRFCService.delete(id);
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("exception");
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
		JsonSheet<Errors_Requests> statusRFCs = new JsonSheet<>();
		try {
			statusRFCs.setData(errorRequestService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusRFCs;
	}

	@RequestMapping(path = "/request", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRequest(HttpServletRequest request, @RequestBody Errors_Requests addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			errorRequestService.save(addError);

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorRequestService.update(uptError);

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
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
			errorRequestService.delete(id);
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}