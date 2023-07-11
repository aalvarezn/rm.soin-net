package com.soin.sgrm.controller.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PUserInfoService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/system")
public class SystemController extends BaseController {

	public static final Logger logger = Logger.getLogger(SystemController.class);

	@Autowired
	SystemService systemService;

	@Autowired
	UserInfoService userService;

	@Autowired
	ProjectService projectService;

	@Autowired
	EmailTemplateService emailService;

	@Autowired
	AttentionGroupService attentionGroupService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PUserInfoService puserService;

	@Autowired
	PProjectService pprojectService;

	@Autowired
	PEmailTemplateService pemailService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	private final Environment environment;

	@Autowired
	public SystemController(Environment environment) {
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
		if (profile.equals("oracle")) {
			model.addAttribute("systems", systemService.list());
			model.addAttribute("system", new System());
			model.addAttribute("users", userService.list());
			model.addAttribute("user", new UserInfo());
			model.addAttribute("projects", projectService.listAll());
			model.addAttribute("project", new Project());
			model.addAttribute("emails", emailService.listAll());
			model.addAttribute("email", new EmailTemplate());
		} else if (profile.equals("postgres")) {
			model.addAttribute("systems", psystemService.list());
			model.addAttribute("system", new PSystem());
			model.addAttribute("users", puserService.list());
			model.addAttribute("user", new PUserInfo());
			model.addAttribute("projects", pprojectService.listAll());
			model.addAttribute("project", new PProject());
			model.addAttribute("emails", pemailService.listAll());
			model.addAttribute("email", new PEmailTemplate());
		}

		return "/admin/system/system";
	}

	@RequestMapping(value = { "", "/ticket" }, method = RequestMethod.GET)
	public String systemTicket(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("systems", systemService.list());
			model.addAttribute("system", new System());

			List<UserInfo> listUser = userService.list();
			List<UserInfo> userManagerIncidence = new ArrayList<UserInfo>();
			List<UserInfo> userIncidence = new ArrayList<UserInfo>();

			for (UserInfo user : listUser) {
				Set<Authority> roles = user.getAuthorities();
				for (Authority rol : roles) {
					if (rol.getName().equals("Incidencias")) {
						userIncidence.add(user);
					} else if (rol.getName().equals("Gestor Incidencias")) {
						userManagerIncidence.add(user);
					}
				}
			}

			model.addAttribute("userManager", userManagerIncidence);
			model.addAttribute("userIncidence", userIncidence);
			model.addAttribute("attentionGroups", attentionGroupService.findAll());
			model.addAttribute("user", new UserInfo());
			model.addAttribute("projects", projectService.listAll());
			model.addAttribute("project", new Project());
			model.addAttribute("emails", emailService.listAll());
			model.addAttribute("email", new EmailTemplate());
		} else if (profile.equals("postgres")) {
			model.addAttribute("systems", psystemService.list());
			model.addAttribute("system", new PSystem());

			List<PUserInfo> listUser = puserService.list();
			List<PUserInfo> puserManagerIncidence = new ArrayList<PUserInfo>();
			List<PUserInfo> puserIncidence = new ArrayList<PUserInfo>();

			for (PUserInfo user : listUser) {
				Set<PAuthority> roles = user.getAuthorities();
				for (PAuthority rol : roles) {
					if (rol.getName().equals("Incidencias")) {
						puserIncidence.add(user);
					} else if (rol.getName().equals("Gestor Incidencias")) {
						puserManagerIncidence.add(user);
					}
				}
			}

			model.addAttribute("userManager", puserManagerIncidence);
			model.addAttribute("userIncidence", puserIncidence);
			model.addAttribute("attentionGroups", pattentionGroupService.findAll());
			model.addAttribute("user", new PUserInfo());
			model.addAttribute("projects", pprojectService.listAll());
			model.addAttribute("project", new PProject());
			model.addAttribute("emails", pemailService.listAll());
			model.addAttribute("email", new PEmailTemplate());
		}

		return "/admin/systemTickets/system";
	}

	@RequestMapping(value = "/findSystem/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findSystem(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				System system = systemService.findSystemById(id);
				return system;
			} else if (profile.equals("postgres")) {
				PSystem system = psystemService.findSystemById(id);
				return system;
			}

		} catch (Exception e) {
			Sentry.capture(e, "system");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveSystem", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveSystem(HttpServletRequest request,
			@Valid @ModelAttribute("System") System system, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (system.getLeaderId() == null) {
				res.setStatus("fail");
				res.addError("leaderId", "Seleccione una opción");
			}

			if (system.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {

				system.setProyect(projectService.findById(system.getProyectId()));
				system.setLeader(userService.findUserById(system.getLeaderId()));

				// se agregan los usuarios de equipo
				User temp = null;
				Set<User> usersNews = new HashSet<>();
				for (Integer index : system.getUserTeamId()) {
					temp = userService.findUserById(index);
					if (temp != null)
						usersNews.add(temp);
				}
				system.checkTeamsExists(usersNews);
				// se agregan los usuarios de gestion
				temp = null;
				Set<User> managersNews = new HashSet<>();
				for (Integer index : system.getManagersId()) {
					temp = userService.findUserById(index);
					if (temp != null)
						managersNews.add(temp);
				}
				system.checkManagersExists(managersNews);
				if (system.getEmailId() != null) {
					EmailTemplate email = emailService.findById(system.getEmailId());
					system.changeEmail(email);
				} else {
					system.changeEmail(null);

				String profile = profileActive();
				if (profile.equals("oracle")) {

					system.setProyect(projectService.findById(system.getProyectId()));
					system.setLeader(userService.findUserById(system.getLeaderId()));

					// se agregan los usuarios de equipo
					User temp = null;
					Set<User> usersNews = new HashSet<>();
					for (Integer index : system.getUserTeamId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							usersNews.add(temp);
					}
					// se agregan los usuarios de gestion
					temp = null;
					Set<User> managersNews = new HashSet<>();
					for (Integer index : system.getManagersId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							managersNews.add(temp);
					}

					if (system.getEmailId() != null) {
						EmailTemplate email = emailService.findById(system.getEmailId());
						system.changeEmail(email);
					} else {
						system.changeEmail(null);
					}

					systemService.save(system);
					res.setObj(system);
				} else if (profile.equals("postgres")) {

					PSystem psystem = new PSystem();

					psystem.setProyect(pprojectService.findById(system.getProyectId()));
					psystem.setLeader(puserService.findUserById(system.getLeaderId()));
					psystem.setName(system.getName());
					psystem.setCode(system.getCode());
					psystem.setNomenclature(system.getNomenclature());
					psystem.setImportObjects(system.getImportObjects());
					psystem.setIsBO(system.getIsBO());
					psystem.setIsAIA(system.getIsAIA());
					psystem.setCustomCommands(system.getCustomCommands());
					psystem.setInstallationInstructions(system.getInstallationInstructions());
					psystem.setAdditionalObservations(system.getAdditionalObservations());
					psystem.setProyectId(system.getProyectId());
					// psystem.setUserTeamId(system.getUserTeamId());
					// psystem.setManagersId(system.getManagersId());
					psystem.setEmailId(system.getEmailId());
					// se agregan los usuarios de equipo
					PUser temp = null;
					Set<PUser> pusersNews = new HashSet<>();
					for (Integer index : system.getUserTeamId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pusersNews.add(temp);
					}
					psystem.setUserTeam(pusersNews);
					// se agregan los usuarios de gestion
					temp = null;
					Set<PUser> pmanagersNews = new HashSet<>();
					for (Integer index : system.getManagersId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pmanagersNews.add(temp);
					}
					psystem.setManagers(pmanagersNews);
					if (psystem.getEmailId() != null) {
						PEmailTemplate email = pemailService.findById(psystem.getEmailId());
						psystem.changeEmail(email);
					} else {
						psystem.changeEmail(null);
					}

					psystemService.save(psystem);
					res.setObj(psystem);

				}

			}
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setException("Error al crear sistema: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateSystem", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateSystem(HttpServletRequest request,
			@Valid @ModelAttribute("System") System system, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (system.getLeaderId() == null) {
				res.setStatus("fail");
				res.addError("leaderId", "Seleccione una opción");
			}

			if (system.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					System systemOrigin = systemService.findSystemById(system.getId());
					systemOrigin.setName(system.getName());
					systemOrigin.setCode(system.getCode());
					systemOrigin.setNomenclature(system.getNomenclature());
					systemOrigin.setImportObjects(system.getImportObjects());
					systemOrigin.setIsBO(system.getIsBO());
					systemOrigin.setIsAIA(system.getIsAIA());
					systemOrigin.setCustomCommands(system.getCustomCommands());
					systemOrigin.setInstallationInstructions(system.getInstallationInstructions());
					systemOrigin.setAdditionalObservations(system.getAdditionalObservations());

					systemOrigin.setProyect(projectService.findById(system.getProyectId()));
					systemOrigin.setLeader(userService.findUserById(system.getLeaderId()));

					if (system.getEmailId() != null) {
						EmailTemplate email = emailService.findById(system.getEmailId());
						systemOrigin.changeEmail(email);
					} else {
						systemOrigin.changeEmail(null);
					}

					// se agregan los usuarios de equipo
					User temp = null;
					Set<User> usersNews = new HashSet<>();
					for (Integer index : system.getUserTeamId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							usersNews.add(temp);
					}
					systemOrigin.checkTeamsExists(usersNews);

					// se agregan los usuarios de gestion
					temp = null;
					Set<User> managersNews = new HashSet<>();
					for (Integer index : system.getManagersId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							managersNews.add(temp);
					}
					systemOrigin.checkManagersExists(managersNews);

					systemService.update(systemOrigin);
					res.setObj(system);
				} else if (profile.equals("postgres")) {
					PSystem psystemOrigin = psystemService.findSystemById(system.getId());
					psystemOrigin.setName(system.getName());
					psystemOrigin.setCode(system.getCode());
					psystemOrigin.setNomenclature(system.getNomenclature());
					psystemOrigin.setImportObjects(system.getImportObjects());
					psystemOrigin.setIsBO(system.getIsBO());
					psystemOrigin.setIsAIA(system.getIsAIA());
					psystemOrigin.setCustomCommands(system.getCustomCommands());
					psystemOrigin.setInstallationInstructions(system.getInstallationInstructions());
					psystemOrigin.setAdditionalObservations(system.getAdditionalObservations());

					psystemOrigin.setProyect(pprojectService.findById(system.getProyectId()));
					psystemOrigin.setLeader(puserService.findUserById(system.getLeaderId()));

					if (system.getEmailId() != null) {
						PEmailTemplate email = pemailService.findById(system.getEmailId());
						psystemOrigin.changeEmail(email);
					} else {
						psystemOrigin.changeEmail(null);
					}

					// se agregan los usuarios de equipo
					PUser temp = null;
					Set<PUser> pusersNews = new HashSet<>();
					for (Integer index : system.getUserTeamId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pusersNews.add(temp);
					}
					psystemOrigin.checkTeamsExists(pusersNews);

					// se agregan los usuarios de gestion
					temp = null;
					Set<PUser> pmanagersNews = new HashSet<>();
					for (Integer index : system.getManagersId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pmanagersNews.add(temp);
					}
					psystemOrigin.checkManagersExists(pmanagersNews);

					psystemService.update(psystemOrigin);
					res.setObj(system);
				}
				
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setException("Error al modificar sistema: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateSystemIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateSystemIncidence(HttpServletRequest request,
			@Valid @ModelAttribute("System") System system, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					System systemOrigin = systemService.findSystemById(system.getId());
					// se agregan los usuarios de equipo
					User temp = null;
					Set<User> usersNews = new HashSet<>();
					for (Integer index : system.getUserIncidenceId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							usersNews.add(temp);
					}
					systemOrigin.checkUserIncidenceExists(usersNews);

					// se agregan los usuarios de gestion
					temp = null;
					Set<User> managersNews = new HashSet<>();
					for (Integer index : system.getManagersIncidenceId()) {
						temp = userService.findUserById(index);
						if (temp != null)
							managersNews.add(temp);
					}
					systemOrigin.checkManagersIncidenceExists(managersNews);

					// se agregan los grupos de atencion
					AttentionGroup tempGroup = null;
					Set<AttentionGroup> attentionGroupNew = new HashSet<>();
					for (Long index : system.getAttentionGroupId()) {
						tempGroup = attentionGroupService.findById(index);
						if (tempGroup != null)
							attentionGroupNew.add(tempGroup);
					}
					systemOrigin.checkattentionGroupExists(attentionGroupNew);

					systemService.update(systemOrigin);
					res.setObj(system);
				} else if (profile.equals("postgres")) {
					PSystem psystemOrigin = psystemService.findSystemById(system.getId());
					// se agregan los usuarios de equipo
					PUser temp = null;
					Set<PUser> pusersNews = new HashSet<>();
					for (Integer index : system.getUserIncidenceId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pusersNews.add(temp);
					}
					psystemOrigin.checkUserIncidenceExists(pusersNews);

					// se agregan los usuarios de gestion
					temp = null;
					Set<PUser> pmanagersNews = new HashSet<>();
					for (Integer index : system.getManagersIncidenceId()) {
						temp = puserService.findUserById(index);
						if (temp != null)
							pmanagersNews.add(temp);
					}
					psystemOrigin.checkManagersIncidenceExists(pmanagersNews);

					// se agregan los grupos de atencion
					PAttentionGroup ptempGroup = null;
					Set<PAttentionGroup> pattentionGroupNew = new HashSet<>();
					for (Long index : system.getAttentionGroupId()) {
						ptempGroup = pattentionGroupService.findById(index);
						if (ptempGroup != null)
							pattentionGroupNew.add(ptempGroup);
					}
					psystemOrigin.checkattentionGroupExists(pattentionGroupNew);

					psystemService.update(psystemOrigin);
					res.setObj(system);
				}
				
				
				
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "system");
			res.setStatus("exception");
			res.setException("Error al modificar sistema: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteSystem/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteSystem(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				systemService.delete(id);
			} else if (profile.equals("postgres")) {
				psystemService.delete(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar sistema: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar sistema: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "system");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}