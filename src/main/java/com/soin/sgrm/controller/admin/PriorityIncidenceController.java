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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/priorityIncidence")
public class PriorityIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(PriorityIncidenceController.class);

	@Autowired
	PriorityIncidenceService priorityIncidenceService;
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/priorityIncidence/priorityIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PriorityIncidence> priorityIncidence = new JsonSheet<>();
		try {
			priorityIncidence.setData(priorityIncidenceService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return priorityIncidence;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PriorityIncidence addPriorityIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			PriorityIncidence codeIncidence= priorityIncidenceService.findByKey("name", addPriorityIncidence.getName());
			if(codeIncidence==null) {
				priorityIncidenceService.save(addPriorityIncidence);
				res.setMessage("Tipo incidencia agregada!");
			}else {
				res.setStatus("error");
				res.setMessage("Error al agregar prioridad incidencia nombre ya utilizado!");
			}
			

			
		} catch (Exception e) {
			Sentry.capture(e, "priorityIncidence");
			res.setStatus("error");
			res.setMessage("Error al agregar Prioridad incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PriorityIncidence uptPriorityIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
		
			PriorityIncidence petitionOld=priorityIncidenceService.findById(uptPriorityIncidence.getId());
			
			
			if(petitionOld.getName()!=uptPriorityIncidence.getName()){
				
				PriorityIncidence incidenceVerification=priorityIncidenceService.findByKey("name", uptPriorityIncidence.getName());
				if(incidenceVerification==null) {
					priorityIncidenceService.update(uptPriorityIncidence);
					res.setMessage("Prioridad incidencia modificada!");
				}else {
					if(incidenceVerification.getId()==uptPriorityIncidence.getId()) {
						priorityIncidenceService.update(uptPriorityIncidence);
						res.setMessage("Prioridad incidencia modificado!");
					}else {
						res.setStatus("error");
						res.setMessage("Error al modificar prioridad de incidencia este nombre ya pertenece a otro!");
					}
				}
			}else {
				priorityIncidenceService.update(uptPriorityIncidence);
				res.setMessage("Prioridad incidencia modificada!");
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "priorityIncidence");
			res.setStatus("error");
			res.setMessage("Error al modificar Prioridad incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			
			priorityIncidenceService.delete(id);
			res.setMessage("Prioridad incidencia eliminada!");
		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al eliminar la prioridad incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
