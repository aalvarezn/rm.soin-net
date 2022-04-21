package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.pos.PImpact;
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
import com.soin.sgrm.model.pos.PTypeChange;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.service.pos.StatusService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.service.pos.TypeChangeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.ReleaseCreate;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {

	@Autowired
	PriorityService priorityService;

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
			List<PPriority> priorities = priorityService.findAll();
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
	

	
	@RequestMapping(value = { "/changeRelease" }, method = RequestMethod.GET)
	public @ResponseBody com.soin.sgrm.utils.JsonSheet<?> changeRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
	
		try {
			Long systemId;
			String sSearch = request.getParameter("sSearch");
			if(request.getParameter("systemId").equals("")) {
				systemId=(long) 0;
			}else {
			     systemId =(long)  Integer.parseInt(request.getParameter("systemId"));
			}
			
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listReleasesBySystem( sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
		}catch(Exception e) {
			Sentry.capture(e, "siges");
			
			return null;
		}
		
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
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/saveRFC",method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody PRFC addRFC) {

		JsonResponse res = new JsonResponse();
		PPriority priority=null;
		PImpact impact=null;
		PTypeChange typeChange=null;
		PRelease[] arrayRelease=null;
		List<PRelease> listRelease=null;
		try {

			PRFC rfcMod = rfcService.findById(addRFC.getId());
			addRFC.setUser(rfcMod.getUser());
			addRFC.setNumRequest(rfcMod.getNumRequest());
			addRFC.setCodeProyect(rfcMod.getCodeProyect());
			addRFC.setStatus(rfcMod.getStatus());
			addRFC.setRequestDate(rfcMod.getRequestDate());
			if(addRFC.getImpactId()!=null){
				impact= impactService.findById(addRFC.getImpactId());
				addRFC.setImpact(impact);
			}
			
			if(addRFC.getPriorityId()!=null){
				priority= priorityService.findById(addRFC.getPriorityId());
				addRFC.setPriority(priority);
			}
			if(addRFC.getTypeChangeId()!=null){
				typeChange= typeChangeService.findById(addRFC.getTypeChangeId());
				addRFC.setTypeChange(typeChange);
				
			}
			if(addRFC.getReleasesList()!=null) {
				arrayRelease=addRFC.getReleasesList();
				if(arrayRelease.length!=0) {
					//for(PRelease release: arrayRelease) {
					//	listRelease.add(releaseService.findById(release.getId()));
						
				//	}
					addRFC.setReleases(Sets.newHashSet(listRelease));
				}
			}
		
		
			rfcService.update(addRFC);
			res.setStatus("success");
		
		}  catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setException("Error al guardar el rfc: " + e.getMessage());
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
		List<PSystem> systems= systemService.listProjects(user.getId());
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
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.findAll());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.findAll());
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