package com.soin.sgrm.controller;

import java.util.ArrayList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.PriorityIncidence;

import com.soin.sgrm.model.System;

import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemTypeIncidence;
import com.soin.sgrm.model.pos.PTypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.SystemTypeIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PSystemTypeIncidenceService;
import com.soin.sgrm.service.pos.PTypeIncidenceService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/systemTypeIn")
public class SystemTypeIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(SystemTypeIncidenceController.class);

	@Autowired
	SystemTypeIncidenceService systemTypeService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeIncidenceService typeIncidenceService;

	@Autowired
	EmailTemplateService emailTemplateService;

	@Autowired
	AttentionGroupService attentionGroupService;

	@Autowired
	PSystemTypeIncidenceService psystemTypeService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PTypeIncidenceService ptypeIncidenceService;

	@Autowired
	PEmailTemplateService pemailTemplateService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	private final Environment environment;

	@Autowired
	public SystemTypeIncidenceController(Environment environment) {
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
			List<AttentionGroup> attentionGroups = attentionGroupService.findGroupByUserId(idUser);
			List<Long> listAttentionGroupId = new ArrayList<Long>();
			for (AttentionGroup attentionGroup : attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<com.soin.sgrm.model.System> systemList = systemService.findByGroupIncidence(listAttentionGroupId);

			Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);

			List<EmailTemplate> emailTemplates = emailTemplateService.listAll();
			model.addAttribute("emailTemplates", emailTemplates);
			model.addAttribute("systems", systemList);
		} else if (profileActive().equals("postgres")) {
			List<PAttentionGroup> attentionGroups = pattentionGroupService.findGroupByUserId(idUser);
			List<Long> listAttentionGroupId = new ArrayList<Long>();
			for (PAttentionGroup attentionGroup : attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<PSystem> systemList = psystemService.findByGroupIncidence(listAttentionGroupId);

			Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);

			List<PEmailTemplate> emailTemplates = pemailTemplateService.listAll();
			model.addAttribute("emailTemplates", emailTemplates);
			model.addAttribute("systems", systemList);
		}

		return "/systemTypeIncidence/systemTypeIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			Integer idUser = getUserLogin().getId();
			if (profileActive().equals("oracle")) {
				JsonSheet<SystemTypeIncidence> systemType = new JsonSheet<>();
				systemType.setData(systemTypeService.findByManager(idUser));
				return systemType;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PSystemTypeIncidence> systemType = new JsonSheet<>();
				systemType.setData(psystemTypeService.findByManager(idUser));
				return systemType;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody SystemTypeIncidence addSystemType) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				SystemTypeIncidence systemPriority = systemTypeService.findByIdAndSys(addSystemType.getSystemId(),
						addSystemType.getTypeIncidenceId());
				if (systemPriority == null) {
					res.setStatus("success");
					addSystemType.setSystem(systemService.findSystemById(addSystemType.getSystemId()));
					addSystemType.setTypeIncidence(typeIncidenceService.findById(addSystemType.getTypeIncidenceId()));
					EmailTemplate emailTemplate = emailTemplateService.findById(addSystemType.getEmailId());
					addSystemType.setEmailTemplate(emailTemplate);
					systemTypeService.save(addSystemType);
					res.setMessage("Tipo de ticket agregada al sistema!");
				} else {

					res.setStatus("error");
					res.setMessage("Error al agregar el tipo ya se encuentra este tipo en este sistema!");
				}
			} else if (profileActive().equals("postgres")) {
				PSystemTypeIncidence systemPriority = psystemTypeService.findByIdAndSys(addSystemType.getSystemId(),
						addSystemType.getTypeIncidenceId());
				if (systemPriority == null) {
					res.setStatus("success");
					PSystemTypeIncidence paddSystemType = new PSystemTypeIncidence();
					paddSystemType.setSystem(psystemService.findSystemById(addSystemType.getSystemId()));
					paddSystemType.setTypeIncidence(ptypeIncidenceService.findById(addSystemType.getTypeIncidenceId()));
					PEmailTemplate emailTemplate = pemailTemplateService.findById(addSystemType.getEmailId());
					paddSystemType.setEmailTemplate(emailTemplate);
					psystemTypeService.save(paddSystemType);
					res.setMessage("Tipo de ticket agregada al sistema!");
				} else {

					res.setStatus("error");
					res.setMessage("Error al agregar el tipo ya se encuentra este tipo en este sistema!");
				}
			}

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
			@RequestBody SystemTypeIncidence uptSystemTypeIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				SystemTypeIncidence systemTypeIncidence = systemTypeService.findByIdAndSys(
						uptSystemTypeIncidence.getSystemId(), uptSystemTypeIncidence.getTypeIncidenceId());
				if (systemTypeIncidence == null) {
					res.setStatus("success");
					uptSystemTypeIncidence
							.setSystem(systemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
					uptSystemTypeIncidence.setTypeIncidence(
							typeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
					EmailTemplate emailTemplate = emailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
					uptSystemTypeIncidence.setEmailTemplate(emailTemplate);
					systemTypeService.update(uptSystemTypeIncidence);
					res.setMessage("Tipo de ticket modificado!");
				} else if (systemTypeIncidence.getId() == uptSystemTypeIncidence.getId()) {
					res.setStatus("success");
					EmailTemplate emailTemplate = emailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
					uptSystemTypeIncidence.setEmailTemplate(emailTemplate);
					uptSystemTypeIncidence
							.setSystem(systemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
					uptSystemTypeIncidence.setTypeIncidence(
							typeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
					systemTypeService.update(uptSystemTypeIncidence);
					res.setMessage("Tipo de ticket modificado!");
				} else {
					res.setStatus("error");
					res.setMessage("Error al actualizar el tipo ya se encuentra un tipo en este sistema!");
				}
			} else if (profileActive().equals("postgres")) {
				PSystemTypeIncidence systemTypeIncidence = psystemTypeService.findByIdAndSys(
						uptSystemTypeIncidence.getSystemId(), uptSystemTypeIncidence.getTypeIncidenceId());
				PSystemTypeIncidence puptSystemTypeIncidence = new PSystemTypeIncidence();
				puptSystemTypeIncidence.setId(uptSystemTypeIncidence.getId());
				if (systemTypeIncidence == null) {
					res.setStatus("success");
					puptSystemTypeIncidence
							.setSystem(psystemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
					puptSystemTypeIncidence.setTypeIncidence(
							ptypeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
					PEmailTemplate emailTemplate = pemailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
					puptSystemTypeIncidence.setEmailTemplate(emailTemplate);
					psystemTypeService.update(puptSystemTypeIncidence);
					res.setMessage("Tipo de ticket modificado!");
				} else if (systemTypeIncidence.getId() == uptSystemTypeIncidence.getId()) {
					res.setStatus("success");
					PEmailTemplate emailTemplate = pemailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
					puptSystemTypeIncidence.setEmailTemplate(emailTemplate);
					puptSystemTypeIncidence
							.setSystem(psystemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
					puptSystemTypeIncidence.setTypeIncidence(
							ptypeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
					psystemTypeService.update(puptSystemTypeIncidence);
					res.setMessage("Tipo de ticket modificado!");
				} else {
					res.setStatus("error");
					res.setMessage("Error al actualizar el tipo ya se encuentra un tipo en este sistema!");
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");
			res.setStatus("error");
			res.setMessage("Error al modificar el tipo!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				systemTypeService.delete(id);
			} else if (profileActive().equals("postgres")) {
				psystemTypeService.delete(id);
			}
			res.setStatus("success");
			res.setMessage("Se elimino el tipo correctamente!");
		} catch (Exception e) {
			Sentry.capture(e, "systemTypeIncidence");
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
				List<SystemTypeIncidence> listSystemPriority = systemTypeService.findBySystem(id);
				for (TypeIncidence typeIncidence : listAllTypeIncidence) {
					Boolean veri = false;
					if (listSystemPriority.size() > 0) {
						for (SystemTypeIncidence systemPriority : listSystemPriority) {

							if (systemPriority.getTypeIncidence().getId() == typeIncidence.getId()) {
								veri = true;
							}
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
				List<PSystemTypeIncidence> listSystemPriority = psystemTypeService.findBySystem(id);
				for (PTypeIncidence typeIncidence : listAllTypeIncidence) {
					Boolean veri = false;
					if (listSystemPriority.size() > 0) {
						for (PSystemTypeIncidence systemPriority : listSystemPriority) {
							if (systemPriority.getTypeIncidence().getId() == typeIncidence.getId()) {
								veri = true;
							}
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
				List<SystemTypeIncidence> listSystemType = null;
				listSystemType = systemTypeService.findBySystem(id);
				return listSystemType;
			} else if (profileActive().equals("postgres")) {
				List<PSystemTypeIncidence> listSystemType = null;
				listSystemType = psystemTypeService.findBySystem(id);
				return listSystemType;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return null;
	}
}