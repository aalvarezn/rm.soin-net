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
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.AuthorityService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/authority")
public class AuthorityController extends BaseController {

	public static final Logger logger = Logger.getLogger(AuthorityController.class);

	@Autowired
	AuthorityService authority;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/authority/authority";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PAuthority> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(authority.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveAuthority(HttpServletRequest request, @RequestBody PAuthority addAuthority) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			authority.save(addAuthority);

			res.setMessage("Rol agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			res.setStatus("exception");
			res.setMessage("Error al agregar role!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse updateAuthority(HttpServletRequest request,
			@RequestBody PAuthority uptAuthority) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			authority.update(uptAuthority);

			res.setMessage("Rol modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			res.setStatus("exception");
			res.setMessage("Error al modificar role!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteAuthority(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			authority.delete(id);
			res.setMessage("Rol eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			res.setStatus("exception");
			res.setMessage("Error al eliminar role!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
