package com.soin.sgrm.controller.admin;

import java.io.IOException;
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
import com.soin.sgrm.model.pos.PDocument;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.DocumentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/docTemplate")
public class DocTemplateController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(DocTemplateController.class);

	@Autowired
	DocumentService docService;

	@Autowired
	SystemService systemService;
	

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/docTemplate/docTemplate";
	}
	


	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PDocument> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(docService.findAll());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PDocument addDocument) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			docService.save(addDocument);
            res.setData(addDocument.getId().toString());
			res.setMessage("Plantilla release agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "document");
			res.setStatus("exception");
			res.setMessage("Error al agregar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PDocument uptDocument) throws IOException {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			docService.update(uptDocument);

			res.setMessage("Plantilla release modificada!");
		}catch (Exception e) {
			Sentry.capture(e, "document");
			res.setStatus("exception");
			res.setMessage("Error al modificar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			docService.delete(id);
			res.setMessage("Plantilla release eliminada!");
		} catch (Exception e) {
			Sentry.capture(e, "emailTemplate");
			res.setStatus("exception");
			res.setMessage("Error al eliminar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

/*
 	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("docTemplates", docTemplateService.list());
		model.addAttribute("docTemplate", new DocTemplate());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		return "/admin/docTemplate/docTemplate";
	}

	@RequestMapping(value = "/findDocTemplate/{id}", method = RequestMethod.GET)
	public @ResponseBody DocTemplate findDocTemplate(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			DocTemplate docTemplate = docTemplateService.findById(id);
			return docTemplate;
		} catch (Exception e) {
			Sentry.capture(e, "docTemplate");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveDocTemplate", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveDocTemplate(HttpServletRequest request,

			@Valid @ModelAttribute("DocTemplate") DocTemplate docTemplate, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (docTemplate.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				docTemplate.setSystem(systemService.findById(docTemplate.getSystemId()));
				docTemplateService.save(docTemplate);
				res.setObj(docTemplate);
			}
		} catch (Exception e) {
			Sentry.capture(e, "docTemplate");
			res.setStatus("exception");
			res.setException("Error al crear plantilla de release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateDocTemplate", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateDocTemplate(HttpServletRequest request,
			@Valid @ModelAttribute("DocTemplate") DocTemplate docTemplate, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (docTemplate.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				DocTemplate docTemplateOrigin = docTemplateService.findById(docTemplate.getId());
				docTemplateOrigin.setName(docTemplate.getName());
				docTemplateOrigin.setComponentGenerator(docTemplate.getComponentGenerator());
				docTemplateOrigin.setTemplateName(docTemplate.getTemplateName());
				docTemplateOrigin.setSufix(docTemplate.getSufix());
				docTemplateOrigin.setSystem(systemService.findById(docTemplate.getSystemId()));
				docTemplateService.update(docTemplateOrigin);
				res.setObj(docTemplate);
			}
		} catch (Exception e) {
			Sentry.capture(e, "docTemplate");
			res.setStatus("exception");
			res.setException("Error al modificar plantilla de release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteDocTemplate/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteDocTemplate(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			docTemplateService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar plantilla de release: "
					+ e.getCause().getCause().getCause().getMessage() + ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar plantilla de release: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e, "docTemplate");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}*/

}
