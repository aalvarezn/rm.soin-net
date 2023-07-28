package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PCrontab;
import com.soin.sgrm.model.pos.PDetailButtonCommand;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PTypeDetail;
import com.soin.sgrm.service.ButtonCommandService;
import com.soin.sgrm.service.CrontabService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.TypeDetailService;
import com.soin.sgrm.service.pos.PButtonCommandService;
import com.soin.sgrm.service.pos.PCrontabService;
import com.soin.sgrm.service.pos.PReleaseService;
import com.soin.sgrm.service.pos.PTypeDetailService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/crontab")
public class CrontabController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(CrontabController.class);

	@Autowired
	private ReleaseService releaseService;
	@Autowired
	TypeDetailService typeDetailService;
	@Autowired
	ButtonCommandService buttonService;
	@Autowired
	CrontabService crontabService;
	
	@Autowired
	PReleaseService preleaseService;
	@Autowired
	PTypeDetailService ptypeDetailService;
	@Autowired
	PButtonCommandService pbuttonService;
	@Autowired
	PCrontabService pcrontabService;


	private final Environment environment;

	@Autowired
	public CrontabController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = "/saveCrontab-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveCrontab(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("Crontab") Crontab crontab, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			
			if(profileActive().equals("oracle")) {
				ReleaseUser release = releaseService.findReleaseUserById(Integer.parseInt(id));
				TypeDetail typeDetail = null;
				res.setStatus("success");

				if (errors.hasErrors()) {
					res.setStatus("fail");
					for (FieldError error : errors.getFieldErrors()) {
						res.addError(error.getField(), error.getDefaultMessage());
					}
				}
				if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
					res.addError("directoryName", Constant.EMPTY);
				}

				if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
					res.addError("userName", Constant.EMPTY);
				}

				if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
					res.addError("timeCommand", Constant.EMPTY);
				}

				if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
					res.addError("pageName", Constant.EMPTY);
				}

				if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
					return res;
				}
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					typeDetail = typeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				crontab.getButton().setRelease(release);
				crontab.setRelease(release);
				crontab.getButton().setHaveCrontab(true);
				crontab = crontabService.saveCrontab(crontab);
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(crontab);
			}else if(profileActive().equals("postgres")) {
				PCrontab pcrontrab=new PCrontab();
				PButtonCommand pbutton=new PButtonCommand();
				ButtonCommand button=crontab.getButton();
				List<PDetailButtonCommand>listDetail=new ArrayList<PDetailButtonCommand>();
				for(DetailButtonCommand detail: button.getDetailsButtonCommands()) {
					PDetailButtonCommand pdetail=new PDetailButtonCommand();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					listDetail.add(pdetail);
				}
				pbutton.setDetailsButtonCommands(listDetail);
				pbutton.setId(button.getId());
				PReleaseUser prelease=new PReleaseUser();
				prelease.setId(button.getRelease().getId());
				pbutton.setRelease(prelease);
				
				pcrontrab.setButton(pbutton);
				pcrontrab.setActive(crontab.getActive());
				pcrontrab.setCommandCron(crontab.getCommandCron());
				pcrontrab.setCommandEntry(crontab.getCommandEntry());
				pcrontrab.setDays(crontab.getDays());
				pcrontrab.setDescriptionCron(crontab.getDescriptionCron());
				pcrontrab.setHour(crontab.getHour());
				pcrontrab.setMinutes(crontab.getMinutes());
				pcrontrab.setMonth(crontab.getMonth());
		
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				res.setStatus("success");

				if (errors.hasErrors()) {
					res.setStatus("fail");
					for (FieldError error : errors.getFieldErrors()) {
						res.addError(error.getField(), error.getDefaultMessage());
					}
				}
				if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
					res.addError("directoryName", Constant.EMPTY);
				}

				if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
					res.addError("userName", Constant.EMPTY);
				}

				if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
					res.addError("timeCommand", Constant.EMPTY);
				}

				if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
					res.addError("pageName", Constant.EMPTY);
				}

				if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
					return res;
				}
				for (PDetailButtonCommand detail : pcrontrab.getButton().getDetailsButtonCommands()) {
					typeDetail = ptypeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				pcrontrab.getButton().setRelease(release);
				pcrontrab.setRelease(release);
				pcrontrab.getButton().setHaveCrontab(true);
				pcrontrab = pcrontabService.saveCrontab(pcrontrab);
				for (PDetailButtonCommand detail : pcrontrab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(crontab);
			}
		
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteCrontab/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteCrontab(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			res.setStatus("success");
			if(profileActive().equals("oracle")) {
				Crontab crontab = crontabService.findById(id);
				crontabService.deleteCrontab(crontab);
				res.setData(crontab.getButton().getId()+"");
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}

				
				res.setObj(crontab);
			}else if(profileActive().equals("postgres")) {
				PCrontab pcrontab = pcrontabService.findById(id);
				pcrontabService.deleteCrontab(pcrontab);
				res.setData(pcrontab.getButton().getId()+"");
				for (PDetailButtonCommand detail : pcrontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}

				
				res.setObj(pcrontab);
			}
			
			
			
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		
		return res;
	}

	@RequestMapping(value = "/findCrontab/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findCrontab(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		
		try {
			if(profileActive().equals("oracle")) {
				Crontab crontab = null;
				crontab = crontabService.findById(id);
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				return crontab;
			}else if(profileActive().equals("postgres")) {
				PCrontab crontab = null;
				crontab = pcrontabService.findById(id);
				for (PDetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				return crontab;
			}

			
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			logger.log(MyLevel.RELEASE_ERROR, ex.toString());
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;
	}

	@RequestMapping(value = "/updateCrontab-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateCrontab(@PathVariable String id, HttpServletResponse response,
			@Valid @ModelAttribute("Crontab") Crontab crontab, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			
			if(profileActive().equals("oracle")) {
				ReleaseUser release = releaseService.findReleaseUserById(Integer.parseInt(id));
				TypeDetail typeDetail = null;
				res.setStatus("success");

				if (errors.hasErrors()) {
					res.setStatus("fail");
					for (FieldError error : errors.getFieldErrors()) {
						res.addError(error.getField(), error.getDefaultMessage());
					}
				}
				if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
					res.addError("directoryName", Constant.EMPTY);
				}

				if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
					res.addError("userName", Constant.EMPTY);
				}

				if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
					res.addError("timeCommand", Constant.EMPTY);
				}

				if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
					res.addError("pageName", Constant.EMPTY);
				}

				if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
					return res;
				}
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					typeDetail = typeDetailService.findByName(detail.getTypeName());
					detail.setTypeDetail(typeDetail);
				}
				crontab.getButton().setRelease(release);
				crontab.getButton().setHaveCrontab(true);
				crontab.setRelease(release);
				ButtonCommand old = buttonService.findById(crontab.getButton().getId());
				crontab.getButton().setRelease(release);
				crontab = crontabService.updateCrontab(crontab, old);
				for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(crontab);
			}else if(profileActive().equals("postgres")) {
				PReleaseUser release = preleaseService.findReleaseUserById(Integer.parseInt(id));
				PTypeDetail typeDetail = null;
				res.setStatus("success");

				if (errors.hasErrors()) {
					res.setStatus("fail");
					for (FieldError error : errors.getFieldErrors()) {
						res.addError(error.getField(), error.getDefaultMessage());
					}
				}
				if (crontab.getButton().getExecuteDirectory() && crontab.getButton().getDirectoryName().trim().equals("")) {
					res.addError("directoryName", Constant.EMPTY);
				}

				if (crontab.getButton().getExecuteUser() && crontab.getButton().getUserName().trim().equals("")) {
					res.addError("userName", Constant.EMPTY);
				}

				if (crontab.getButton().getWaitCommand() && crontab.getButton().getTimeCommand().trim().equals("")) {
					res.addError("timeCommand", Constant.EMPTY);
				}

				if (crontab.getButton().getPrincipalPage() && crontab.getButton().getPageName().trim().equals("")) {
					res.addError("pageName", Constant.EMPTY);
				}

				if (res.getStatus().equals("fail") || (res.getErrors().size() > 0)) {
					return res;
				}
				
				PCrontab pcrontrab=new PCrontab();
				PButtonCommand pbutton=new PButtonCommand();
				ButtonCommand button=crontab.getButton();
				List<PDetailButtonCommand>listDetail=new ArrayList<PDetailButtonCommand>();
				for(DetailButtonCommand detail: button.getDetailsButtonCommands()) {
					PDetailButtonCommand pdetail=new PDetailButtonCommand();
					pdetail.setId(detail.getId());
					pdetail.setName(detail.getName());
					listDetail.add(pdetail);
				}
				pbutton.setDetailsButtonCommands(listDetail);
				pbutton.setId(button.getId());
				PReleaseUser prelease=new PReleaseUser();
				prelease.setId(button.getRelease().getId());
				pbutton.setRelease(prelease);
				
				pcrontrab.setButton(pbutton);
				pcrontrab.setActive(crontab.getActive());
				pcrontrab.setCommandCron(crontab.getCommandCron());
				pcrontrab.setCommandEntry(crontab.getCommandEntry());
				pcrontrab.setDays(crontab.getDays());
				pcrontrab.setDescriptionCron(crontab.getDescriptionCron());
				pcrontrab.setHour(crontab.getHour());
				pcrontrab.setMinutes(crontab.getMinutes());
				pcrontrab.setMonth(crontab.getMonth());
				PReleaseUser preleaseCron=new PReleaseUser();
				preleaseCron.setId(release.getId());
				pcrontrab.getButton().setRelease(preleaseCron);
				pcrontrab.getButton().setHaveCrontab(true);
				pcrontrab.setRelease(preleaseCron);
				PButtonCommand old = pbuttonService.findById(crontab.getButton().getId());
				pcrontrab.getButton().setRelease(preleaseCron);
				pcrontrab = pcrontabService.updateCrontab(pcrontrab, old);
				for (PDetailButtonCommand detail : pcrontrab.getButton().getDetailsButtonCommands()) {
					detail.setButton(null);
				}
				res.setObj(pcrontrab);
			}
			
		} catch (SQLException ex) {
			Sentry.capture(ex, "crontab");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			res.setStatus("exception");
			res.setException("Error al guardar el release: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}
