package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.EmailIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.pos.PCrontab;
import com.soin.sgrm.model.pos.PDetailButtonCommand;
import com.soin.sgrm.model.pos.PEmailIncidence;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PTypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.EmailIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.pos.PEmailIncidenceService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypeIncidenceService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/emailIn")
public class EmailIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(EmailIncidenceController.class);

	@Autowired
	EmailIncidenceService emailIncidenceService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeIncidenceService typeIncidenceService;

	@Autowired
	EmailTemplateService emailTemplateService;

	@Autowired
	PEmailIncidenceService pemailIncidenceService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PTypeIncidenceService ptypeIncidenceService;

	@Autowired
	PEmailTemplateService pemailTemplateService;

	private final Environment environment;

	@Autowired
	public EmailIncidenceController(Environment environment) {
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
		Integer idUser = getUserLogin().getId();
		if (profileActive().equals("oracle")) {
			List<com.soin.sgrm.model.System> systems = systemService.findByManagerIncidence(idUser);
			List<EmailTemplate> emailTemplates = emailTemplateService.listAll();
			model.addAttribute("emailTemplates", emailTemplates);
			model.addAttribute("systems", systems);
		} else if (profileActive().equals("postgres")) {
			List<PSystem> systems = psystemService.findByManagerIncidence(idUser);
			List<PEmailTemplate> emailTemplates = pemailTemplateService.listAll();
			model.addAttribute("emailTemplates", emailTemplates);
			model.addAttribute("systems", systems);
		}

		return "/EmailIncidence/EmailIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			if (profileActive().equals("oracle")) {
				JsonSheet<EmailIncidence> systemType = new JsonSheet<>();
				systemType.setData(emailIncidenceService.findAll());
				return systemType;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<EmailIncidence> systemType = new JsonSheet<>();
				systemType.setData(emailIncidenceService.findAll());
				return systemType;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody EmailIncidence addSystemType) {
		JsonResponse res = new JsonResponse();
		try {

		} catch (Exception e) {
			Sentry.capture(e, "systemType");
			res.setStatus("error");
			res.setMessage("Error al agregar el tipo al sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody EmailIncidence uptEmailIncidence) {
		JsonResponse res = new JsonResponse();

		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				emailIncidenceService.delete(id);
			} else if (profileActive().equals("postgres")) {
				emailIncidenceService.delete(id);
			}

			res.setStatus("success");
			res.setMessage("Se elimino el tipo correctamente!");
		} catch (Exception e) {
			Sentry.capture(e, "EmailIncidence");
			res.setStatus("error");
			res.setMessage("Error al eliminar el tipo!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "/changeTypeIncidence/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> changePriority(@PathVariable Integer id, Locale locale, Model model) {

		try {
			if (profileActive().equals("oracle")) {
				List<TypeIncidence> listTypeIncidence = new ArrayList<TypeIncidence>();
				List<TypeIncidence> listAllTypeIncidence = typeIncidenceService.findAll();
				List<EmailIncidence> listSystemPriority = emailIncidenceService.findBySystem(id);
				for (TypeIncidence typeIncidence : listAllTypeIncidence) {
					Boolean veri = false;
					if (listSystemPriority.size() > 0) {
						for (EmailIncidence systemPriority : listSystemPriority) {

						}
					} else {
						veri = false;
					}
					if (!veri) {
						listTypeIncidence.add(typeIncidence);
					}
				}
				return listTypeIncidence;
			} else if (profileActive().equals("postgres")) {
				List<PTypeIncidence> listTypeIncidence = new ArrayList<PTypeIncidence>();
				List<PTypeIncidence> listAllTypeIncidence = ptypeIncidenceService.findAll();
				List<PEmailIncidence> listSystemPriority = pemailIncidenceService.findBySystem(id);
				for (PTypeIncidence typeIncidence : listAllTypeIncidence) {
					Boolean veri = false;
					if (listSystemPriority.size() > 0) {
						for (PEmailIncidence systemPriority : listSystemPriority) {

						}
					} else {
						veri = false;
					}
					if (!veri) {
						listTypeIncidence.add(typeIncidence);
					}
				}
				return listTypeIncidence;
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = { "/getypeIncidence/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getType(@PathVariable Integer id, Locale locale, Model model) {
		try {
			if (profileActive().equals("oracle")) {
				List<EmailIncidence> listSystemType = null;
				listSystemType = emailIncidenceService.findBySystem(id);
				return listSystemType;
			} else if (profileActive().equals("postgres")) {
				List<PEmailIncidence> listSystemType = null;
				listSystemType = pemailIncidenceService.findBySystem(id);
				return listSystemType;
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return null;
	}
}
