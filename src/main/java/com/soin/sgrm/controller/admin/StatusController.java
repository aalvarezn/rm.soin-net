package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.soin.sgrm.model.Parameter;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PParameter;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.utils.JsonResponse;

import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/status")
public class StatusController extends BaseController{
	
	@Autowired
	StatusService statusService;
	
	@Autowired
	PStatusService pstatusService;
	
	private final Environment environment;
	
	
	@Autowired
	public StatusController(Environment environment) {
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
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("status", new Status());
		} else if (profile.equals("postgres")) {
			model.addAttribute("statuses", pstatusService.list());
			model.addAttribute("status", new Status());
		}
		
		
		return "/admin/status/status";
	}
	
	@RequestMapping(value = "/findStatus/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findStatus(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Status status = statusService.findById(id);
				return status;
			} else if (profile.equals("postgres")) {
				PStatus status = pstatusService.findById(id);
				return status;
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "status");
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveStatus(HttpServletRequest request,

			@Valid @ModelAttribute("Status") Status status, BindingResult errors, ModelMap model, Locale locale,
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
			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					status.setFinished(false);
					status.setInProgress(false);
					statusService.save(status);
					res.setObj(status);
				} else if (profile.equals("postgres")) {
					PStatus pstatus=new PStatus();
					pstatus.setMotive(status.getMotive());
					pstatus.setDescription(status.getDescription());
					pstatus.setName(status.getName());
					pstatus.setFinished(false);
					pstatus.setInProgress(false);
					pstatusService.save(pstatus);
					res.setObj(pstatus);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear estado: " + e.toString());
			Sentry.capture(e, "status");
		}
		return res;
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateStatus(HttpServletRequest request, @Valid @ModelAttribute("Status") Status status,
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
			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Status statusOrigin = statusService.findById(status.getId());
					statusOrigin.setName(status.getName());
					statusOrigin.setDescription(status.getDescription());
					statusService.update(statusOrigin);
					res.setObj(status);

				} else if (profile.equals("postgres")) {
					PStatus pstatusOrigin = pstatusService.findById(status.getId());
					pstatusOrigin.setName(status.getName());
					pstatusOrigin.setDescription(status.getDescription());
					pstatusOrigin.setMotive(status.getMotive());
					pstatusService.update(pstatusOrigin);
					res.setObj(status);

				}
							}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar estado: " + e.toString());
			Sentry.capture(e, "status");
		}
		return res;
	}

	@RequestMapping(value = "/deleteStatus/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteStatus(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				statusService.delete(id);
			} else if (profile.equals("postgres")) {
				pstatusService.delete(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar estado: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			
			if(e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar estado: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e, "status");
			}
		}
		return res;
	}
}
