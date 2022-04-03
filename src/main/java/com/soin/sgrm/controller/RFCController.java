package com.soin.sgrm.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PPriority;

import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.ImpactService;
import com.soin.sgrm.service.pos.PriorityService;
import com.soin.sgrm.service.pos.RFCService;
import com.soin.sgrm.service.pos.ReleaseService;
import com.soin.sgrm.service.pos.SigesService;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.service.pos.StatusService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.service.pos.TypeChangeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {

	@Autowired
	PriorityService priority;

	@Autowired
	RFCService rfcService;

	@Autowired
	StatusService statusService;

	@Autowired
	SystemService systemService;
	
	@Autowired
	SigesService sigeService;
	
	@Autowired
	ImpactService impactService;
	
	@Autowired
	TypeChangeService typeChangeService;
	
	@Autowired
	ReleaseService releaseService;
	

	public static final Logger logger = Logger.getLogger(RFCController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			PUser userLogin = getUserLogin();
			List<PSystem> systems= systemService.listProjects(userLogin.getId());
			List<PPriority> priorities = priority.findAll();
			List<PStatus> statuses = statusService.findAll();

			model.addAttribute("priorities", priorities);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PRFC> rfcs = new JsonSheet<>();
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer sStatus = Integer.parseInt(request.getParameter("sEcho"));

			String sSearch = request.getParameter("sStatus");
			String dateRange = request.getParameter("dateRange");

			rfcs = rfcService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, sStatus, dateRange);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(value = { "/changeProject/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<PSiges> changeProject(@PathVariable Long id, Locale locale, Model model) {
		List<PSiges> codeSiges =null;
		try {
			codeSiges =sigeService.listCodeSiges(id);
		}catch(Exception e) {
			Sentry.capture(e, "siges");
			
			e.printStackTrace();
		}
		
		return codeSiges;
	}
	
	@RequestMapping(value = { "/changeRelease/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<PRelease> changeRelease(@PathVariable Long id, Locale locale, Model model) {
		List<PRelease> releases =null;
		try {
			releases =releaseService.listReleasesBySystem(id);
		}catch(Exception e) {
			Sentry.capture(e, "siges");
			
			e.printStackTrace();
		}
		
		return releases;
	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PRFC addRFC) {
		JsonResponse res = new JsonResponse();
		try {
			PUser userLogin = getUserLogin();
			PStatus status=statusService.findByKey("code", "draft");
			addRFC.setStatus(status);
			addRFC.setUser(userLogin);
			addRFC.setRequestDate(CommonUtils.getSystemTimestamp());
			res.setStatus("success");
		
			addRFC.setNumRequest(rfcService.generateRFCNumber(addRFC.getCodeProyect()));
			
			rfcService.save(addRFC);
			res.setData(addRFC.getId().toString());
			res.setMessage("Se creo correctamente el RFC!");
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setMessage("Error al crear RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/editRFC-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		PRFC rfcEdit = new PRFC();
		//SystemConfiguration systemConfiguration = new SystemConfiguration();
		PUser user = getUserLogin();
		try {
			if (id == null) {
				return "redirect:/";
			}

			
			rfcEdit = rfcService.findById(id);

			if (rfcEdit == null) {
				return "/plantilla/404";
			}

			if (!rfcEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "RFC no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}

			if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
/*
			systemConfiguration = systemConfigurationService.findBySystemId(release.getSystem().getId());
			List<DocTemplate> docs = docsTemplateService.findBySystem(release.getSystem().getId());
			*/
/*
			if (release.getSystem().getImportObjects()) {
				model.addAttribute("typeDetailList", typeDetail.list());
			}
*/
			model.addAttribute("systems", systemService.findAll());
			model.addAttribute("impacts", impactService.findAll());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priority.findAll());
			model.addAttribute("rfc",rfcEdit);
			/*
			model.addAttribute("doc", new DocTemplate());
			model.addAttribute("docs", docs);
			model.addAttribute("release", release);
			*/
			return "/rfc/editRFC";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

}