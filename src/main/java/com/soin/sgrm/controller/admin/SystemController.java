package com.soin.sgrm.controller.admin;

import java.util.ArrayList;
import java.util.List;
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

import com.google.common.collect.Sets;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.EmailService;
import com.soin.sgrm.service.pos.EmailTemplateService;
import com.soin.sgrm.service.pos.ProjectService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.service.pos.UserService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/system")
public class SystemController extends BaseController {

	public static final Logger logger = Logger.getLogger(SystemController.class);

	@Autowired
	SystemService systemService;

	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;
	
	@Autowired 
	EmailTemplateService emailService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("projects", projectService.findAll());
		model.addAttribute("emails",emailService.findAll());
		
		
		String[] columns = { "id", "userName", "name" };
		List<PUser> users = userService.findAllColumns(columns);
		model.addAttribute("users", users);
		
		return "/admin/system/system";
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		JsonSheet<PSystem> list = new JsonSheet<>();
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sProject = request.getParameter("sProject");

			String sSearch = request.getParameter("sSearch");
			String dateRange = request.getParameter("dateRange");
			list = systemService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, sProject);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PSystem uptSystem) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystem system = systemService.findById(uptSystem.getId());
			system.setCode(uptSystem.getCode());
			system.setName(uptSystem.getName());

			PProject project = projectService.findByKey("code", uptSystem.getProjectCode());
			system.setProject(project);
			PUser leader = userService.findByKey("userName", uptSystem.getLeaderUserName());
			system.setLeader(leader);

			system.setImportObjects(uptSystem.getImportObjects());
			system.setCustomCommands(uptSystem.getCustomCommands());

			List<PUser> managers = new ArrayList<PUser>();
			if (uptSystem.getStrManagers() != null)
				managers = userService.findbyUserName(uptSystem.getStrManagers());
			system.setManagers(Sets.newHashSet(managers));

			List<PUser> team = new ArrayList<PUser>();
			if (uptSystem.getStrTeam() != null)
				team = userService.findbyUserName(uptSystem.getStrTeam());
			system.setTeam(Sets.newHashSet(team));

			systemService.update(system);
			res.setMessage("Sistema modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setMessage("Error al modificar sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PSystem addSystem) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			PProject project = projectService.findByKey("code", addSystem.getProjectCode());
			addSystem.setProject(project);
			PUser leader = userService.findByKey("userName", addSystem.getLeaderUserName());
			addSystem.setLeader(leader);

			List<PUser> managers = new ArrayList<PUser>();
			if (addSystem.getStrManagers() != null)
				managers = userService.findbyUserName(addSystem.getStrManagers());
			addSystem.setManagers(Sets.newHashSet(managers));

			List<PUser> team = new ArrayList<PUser>();
			if (addSystem.getStrTeam() != null)
				team = userService.findbyUserName(addSystem.getStrTeam());
			addSystem.setTeam(Sets.newHashSet(team));

			systemService.save(addSystem);
			res.setMessage("Sistema agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setMessage("Error al agregar sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteAuthority(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			systemService.delete(id);
			res.setMessage("Sistema eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setMessage("Error al eliminar sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
