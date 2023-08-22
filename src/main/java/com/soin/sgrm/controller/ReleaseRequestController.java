package com.soin.sgrm.controller;

import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PRequestService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonAutocomplete;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/request")
public class ReleaseRequestController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReleaseRequestController.class);

	@Autowired
	private RequestService requestService;

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PRequestService prequestService;

	@Autowired
	private PSystemService psystemService;
	
	private final Environment environment;

	@Autowired
	public ReleaseRequestController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = "/requestList", method = RequestMethod.GET)
	public @ResponseBody List<?> requestList(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String name = getUserLogin().getUsername();
			if(profileActive().equals("oracle")) {
				List<Request> list = requestService.list("", systemService.myTeamsProyect(name));
				return list;
			}else if(profileActive().equals("postgres")) {
				List<PRequest> list = prequestService.list("", psystemService.myTeamsProyect(name));
				return list;
			}
			
		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseRequest");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseRequest");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/requestAutoComplete-{search}", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> requestAutoComplete(@PathVariable String search,
			HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList();
		try {
			String name = getUserLogin().getUsername();
			if(profileActive().equals("oracle")) {
				List<Request> list = requestService.list(search, systemService.myTeamsProyect(name));

				for (int i = 0; i < list.size(); i++) {
					listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "",
							list.get(i).getCode_soin() + " " + list.get(i).getCode_ice() + " "
									+ list.get(i).getDescription(),
							list.get(i).getCode_soin() + " " + list.get(i).getCode_ice() + " "
									+ list.get(i).getDescription()));
				}
			}else if(profileActive().equals("postgres")) {
				List<PRequest> list = prequestService.list(search, psystemService.myTeamsProyect(name));

				for (int i = 0; i < list.size(); i++) {
					listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "",
							list.get(i).getCode_soin() + " " + list.get(i).getCode_ice() + " "
									+ list.get(i).getDescription(),
							list.get(i).getCode_soin() + " " + list.get(i).getCode_ice() + " "
									+ list.get(i).getDescription()));
				}
			}
			
			return listAutoComplete;
		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseRequest");
			logger.log(MyLevel.RELEASE_ERROR, ex.toString());
		} catch (Exception e) {
			Sentry.capture(e, "releaseRequest");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return null;
	}

}
