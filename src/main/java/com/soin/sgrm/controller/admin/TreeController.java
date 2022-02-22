package com.soin.sgrm.controller.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Tree;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/tree")
public class TreeController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(TreeController.class);

	@Autowired
	TreeService treeService;
	@Autowired
	private ReleaseService releaseService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/tree/tree";
	}

	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String index(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		ReleaseUser release;
		try {
			release = releaseService.findReleaseUserById(id);
			model.addAttribute("release", release);
		} catch (SQLException e) {
			Sentry.capture(e, "admin");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/admin/tree/tree";
	}

	@RequestMapping(value = "/tree/{releaseNumber}/{depth}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tree(@PathVariable String releaseNumber, @PathVariable Integer depth,
			HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			List<Tree> treeList = treeService.findTree(releaseNumber, depth);
			res.setStatus("success");
			res.setObj(treeList);
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
