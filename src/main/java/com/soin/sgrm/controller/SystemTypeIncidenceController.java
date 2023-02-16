package com.soin.sgrm.controller;

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

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.SystemTypeIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.TypeIncidenceService;
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
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		Integer idUser = getUserLogin().getId();
		List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(idUser);
		List<Long> listAttentionGroupId=new ArrayList<Long>();
		for(AttentionGroup attentionGroup: attentionGroups) {
			listAttentionGroupId.add(attentionGroup.getId());
		}
		List<com.soin.sgrm.model.System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
		
		List<EmailTemplate> emailTemplates =emailTemplateService.listAll();
		model.addAttribute("emailTemplates", emailTemplates);
		model.addAttribute("systems", systemList);
		return "/systemTypeIncidence/systemTypeIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<SystemTypeIncidence> systemType = new JsonSheet<>();
		try {
			Integer idUser = getUserLogin().getId();
			systemType.setData(systemTypeService.findByManager(idUser));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return systemType;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody SystemTypeIncidence addSystemType) {
		JsonResponse res = new JsonResponse();
		try {
			
			SystemTypeIncidence systemPriority= systemTypeService.findByIdAndSys(addSystemType.getSystemId(),addSystemType.getTypeIncidenceId());
			if(systemPriority==null) {
			res.setStatus("success");
			addSystemType.setSystem(systemService.findSystemById(addSystemType.getSystemId()));
			addSystemType.setTypeIncidence(typeIncidenceService.findById(addSystemType.getTypeIncidenceId()));
			EmailTemplate emailTemplate=emailTemplateService.findById(addSystemType.getEmailId());
			addSystemType.setEmailTemplate(emailTemplate);
			systemTypeService.save(addSystemType);
			res.setMessage("Tipo de ticket agregada al sistema!");
			}else {
				
				res.setStatus("error");
				res.setMessage("Error al agregar el tipo ya se encuentra este tipo en este sistema!");
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
			
			SystemTypeIncidence systemTypeIncidence= systemTypeService.findByIdAndSys(uptSystemTypeIncidence.getSystemId(),uptSystemTypeIncidence.getTypeIncidenceId());
			if(systemTypeIncidence==null) {
			res.setStatus("success");
			uptSystemTypeIncidence.setSystem(systemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
			uptSystemTypeIncidence.setTypeIncidence(typeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
			EmailTemplate emailTemplate=emailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
			uptSystemTypeIncidence.setEmailTemplate(emailTemplate);
			systemTypeService.update(uptSystemTypeIncidence);
			res.setMessage("Tipo de ticket modificado!");
			}else if(systemTypeIncidence.getId()==uptSystemTypeIncidence.getId()) {
				res.setStatus("success");
				EmailTemplate emailTemplate=emailTemplateService.findById(uptSystemTypeIncidence.getEmailId());
				uptSystemTypeIncidence.setEmailTemplate(emailTemplate);
				uptSystemTypeIncidence.setSystem(systemService.findSystemById(uptSystemTypeIncidence.getSystemId()));
				uptSystemTypeIncidence.setTypeIncidence(typeIncidenceService.findById(uptSystemTypeIncidence.getTypeIncidenceId()));
				systemTypeService.update(uptSystemTypeIncidence);
				res.setMessage("Tipo de ticket modificado!");
			}else {
				res.setStatus("error");
				res.setMessage("Error al actualizar el tipo ya se encuentra un tipo en este sistema!");
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
			systemTypeService.delete(id);
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
	public @ResponseBody List<TypeIncidence> changePriority(@PathVariable Integer id, Locale locale, Model model) {
		List<TypeIncidence> listTypeIncidence = new ArrayList<TypeIncidence>();
		List<TypeIncidence> listAllTypeIncidence = typeIncidenceService.findAll();
		List<SystemTypeIncidence> listSystemPriority = systemTypeService.findBySystem(id);

		try {
			for (TypeIncidence typeIncidence : listAllTypeIncidence) {
				Boolean veri = false;
				if(listSystemPriority.size()>0) {
				for (SystemTypeIncidence systemPriority : listSystemPriority) {
					
					if (systemPriority.getTypeIncidence().getId() == typeIncidence.getId()) {
						veri = true;
					}
				}
				}else {
					veri=false;
				}
				if (!veri) {
					listTypeIncidence.add(typeIncidence);
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return listTypeIncidence;
	}
	
	@RequestMapping(value = { "/getypeIncidence/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<SystemTypeIncidence> getType(@PathVariable Integer id, Locale locale, Model model) {
	
		List<SystemTypeIncidence> listSystemType = null;

		try {
			 listSystemType = systemTypeService.findBySystem(id);
			
		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return listSystemType;
	}
}
