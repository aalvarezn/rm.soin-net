package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.pos.PTypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PTypeIncidenceService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeIncidence")
public class TypeIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypeIncidenceController.class);

	@Autowired
	TypeIncidenceService typeIncidenceService;

	@Autowired
	EmailTemplateService emailTemplateService;

	@Autowired
	PTypeIncidenceService ptypeIncidenceService;

	@Autowired
	PEmailTemplateService pemailTemplateService;
	private final Environment environment;

	@Autowired
	public TypeIncidenceController(Environment environment) {
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
		return "/admin/typeIncidence/typeIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<TypeIncidence> typeIncidence = new JsonSheet<>();
				typeIncidence.setData(typeIncidenceService.findAll());
				return typeIncidence;
			} else if (profile.equals("postgres")) {
				JsonSheet<PTypeIncidence> typeIncidence = new JsonSheet<>();
				typeIncidence.setData(ptypeIncidenceService.findAll());
				return typeIncidence;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypeIncidence addTypeIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			// addTypeIncidence.setStatus(1);
			String profile = profileActive();
			if (profile.equals("oracle")) {
				TypeIncidence codeIncidence = typeIncidenceService.findByKey("code", addTypeIncidence.getCode());
				if (codeIncidence == null) {
					typeIncidenceService.save(addTypeIncidence);
					res.setMessage("Tipo incidencia agregada!");
				} else {
					res.setStatus("error");
					res.setMessage("Error al agregar incidencia codigo ya utilizado!");
				}
			} else if (profile.equals("postgres")) {
				PTypeIncidence paddTypeIncidence = new PTypeIncidence();
				paddTypeIncidence.setCode(addTypeIncidence.getCode());
				paddTypeIncidence.setDescription(addTypeIncidence.getDescription());
				PTypeIncidence pcodeIncidence = ptypeIncidenceService.findByKey("code", addTypeIncidence.getCode());
				if (pcodeIncidence == null) {
					ptypeIncidenceService.save(paddTypeIncidence);
					res.setMessage("Tipo incidencia agregada!");
				} else {
					res.setStatus("error");
					res.setMessage("Error al agregar incidencia codigo ya utilizado!");
				}
			}

		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al agregar Tipo incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody TypeIncidence uptTypeIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				TypeIncidence petitionOld = typeIncidenceService.findById(uptTypeIncidence.getId());
				// uptTypeIncidence.setStatus(petitionOld.getStatus());

				if (petitionOld.getCode() != uptTypeIncidence.getCode()) {

					TypeIncidence incidenceVerification = typeIncidenceService.findByKey("code",
							uptTypeIncidence.getCode());
					if (incidenceVerification == null) {
						typeIncidenceService.update(uptTypeIncidence);
						res.setMessage("Siges modificado!");
					} else {
						if (incidenceVerification.getId() == uptTypeIncidence.getId()) {
							typeIncidenceService.update(uptTypeIncidence);
							res.setMessage("Siges modificado!");
						} else {
							res.setStatus("error");
							res.setMessage("Error al modificar tipo de incidencia este codigo ya pertenece a otro!");
						}
					}
				} else {
					typeIncidenceService.update(uptTypeIncidence);
					res.setMessage("Siges modificado!");
				}
				res.setMessage("Tipo solicitud modificada!");
			} else if (profile.equals("postgres")) {
				
				PTypeIncidence puptTypeIncidence = new PTypeIncidence();
				puptTypeIncidence.setId(uptTypeIncidence.getId());
				puptTypeIncidence.setDescription(uptTypeIncidence.getDescription());
				puptTypeIncidence.setCode(uptTypeIncidence.getCode());
				
				PTypeIncidence ppetitionOld = ptypeIncidenceService.findById(uptTypeIncidence.getId());
				// uptTypeIncidence.setStatus(petitionOld.getStatus());

				if (ppetitionOld.getCode() != puptTypeIncidence.getCode()) {

					PTypeIncidence pincidenceVerification = ptypeIncidenceService.findByKey("code",
							puptTypeIncidence.getCode());
					if (pincidenceVerification == null) {
						ptypeIncidenceService.update(puptTypeIncidence);
						res.setMessage("Siges modificado!");
					} else {
						if (pincidenceVerification.getId() == puptTypeIncidence.getId()) {
							ptypeIncidenceService.update(puptTypeIncidence);
							res.setMessage("Siges modificado!");
						} else {
							res.setStatus("error");
							res.setMessage("Error al modificar tipo de incidencia este codigo ya pertenece a otro!");
						}
					}
				} else {
					ptypeIncidenceService.update(puptTypeIncidence);
					res.setMessage("Siges modificado!");
				}
				res.setMessage("Tipo solicitud modificada!");
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("error");
			res.setMessage("Error al modificar Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	/*
	 * @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	 * public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model)
	 * { JsonResponse res = new JsonResponse(); try { TypeIncidence
	 * typeUpt=typeIncidenceService.findById(id); if(typeUpt.getStatus()==1) {
	 * typeUpt.setStatus(0); res.setMessage("Tipo incidencia desactivado!"); }else {
	 * typeUpt.setStatus(1); res.setMessage("Tipo incidencia activada!"); }
	 * res.setStatus("success"); typeIncidenceService.update(typeUpt);
	 * 
	 * } catch (Exception e) { Sentry.capture(e, "typeIncidence");
	 * res.setStatus("error");
	 * res.setMessage("Error al modificar el Tipo incidencia!");
	 * logger.log(MyLevel.RELEASE_ERROR, e.toString()); } return res; }
	 */
}