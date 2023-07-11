package com.soin.sgrm.controller.admin;

import java.util.Locale;

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
import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PGDoc;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.service.GDocService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.pos.PGDocService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/gDoc")
public class GDocController extends BaseController {

	public static final Logger logger = Logger.getLogger(GDocController.class);

	@Autowired
	GDocService gDocService;

	@Autowired
	ProjectService projectService;
	
	@Autowired
	PGDocService pgDocService;

	@Autowired
	PProjectService pprojectService;

	private final Environment environment;

	@Autowired
	public GDocController(Environment environment) {
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
			model.addAttribute("gDocs", gDocService.list());
			model.addAttribute("gDoc", new GDoc());
			model.addAttribute("projects", projectService.listAll());
			model.addAttribute("project", new Project());
		} else if (profile.equals("postgres")) {
			model.addAttribute("gDocs", pgDocService.list());
			model.addAttribute("gDoc", new PGDoc());
			model.addAttribute("projects", pprojectService.listAll());
			model.addAttribute("project", new PProject());
		}
		
		return "/admin/gDoc/gDoc";
	}

	@RequestMapping(value = "/findGDoc/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findGDoc(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				GDoc gDoc = gDocService.findById(id);
				return gDoc;
			} else if (profile.equals("postgres")) {
				PGDoc pgDoc = pgDocService.findById(id);
				return pgDoc;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveGDoc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveGDoc(HttpServletRequest request,

			@Valid @ModelAttribute("GDoc") GDoc gDoc, BindingResult errors, ModelMap model, Locale locale,
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

			if (gDoc.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					
					gDoc.setProyect(projectService.findById(gDoc.getProyectId()));
					gDoc.setNextSincronization(CommonUtils.getSqlDate());
					gDocService.save(gDoc);
					res.setObj(gDoc);
				} else if (profile.equals("postgres")) {
					PGDoc pgDoc= new PGDoc();
					pgDoc.setCredentials(gDoc.getCredentials());
					pgDoc.setDescription(gDoc.getDescription());
					pgDoc.setSpreadSheet(gDoc.getSpreadSheet());
					pgDoc.setProyect(pprojectService.findById(gDoc.getProyectId()));
					pgDoc.setNextSincronization(CommonUtils.getSqlDate());
					pgDocService.save(pgDoc);
					res.setObj(pgDoc);
				}
	
			}
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			res.setStatus("exception");
			res.setException("Error al crear gDoc: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateGDoc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateGDoc(HttpServletRequest request, @Valid @ModelAttribute("GDoc") GDoc gDoc,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (gDoc.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					GDoc gDocOrigin = gDocService.findById(gDoc.getId());
					gDocOrigin.setCredentials(gDoc.getCredentials());
					gDocOrigin.setSpreadSheet(gDoc.getSpreadSheet());
					gDocOrigin.setDescription(gDoc.getDescription());
					gDocOrigin.setProyect(projectService.findById(gDoc.getProyectId()));
					gDocService.update(gDocOrigin);
					res.setObj(gDoc);
				} else if (profile.equals("postgres")) {
					PGDoc pgDocOrigin = pgDocService.findById(gDoc.getId());
					pgDocOrigin.setCredentials(gDoc.getCredentials());
					pgDocOrigin.setSpreadSheet(gDoc.getSpreadSheet());
					pgDocOrigin.setDescription(gDoc.getDescription());
					pgDocOrigin.setProyect(pprojectService.findById(gDoc.getProyectId()));
					pgDocService.update(pgDocOrigin);
					res.setObj(gDoc);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			res.setStatus("exception");
			res.setException("Error al modificar gDoc: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteGDoc/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteGDoc(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				gDocService.delete(id);
			} else if (profile.equals("postgres")) {
				pgDocService.delete(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar gDoc: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar gDoc: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "gDocs");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
