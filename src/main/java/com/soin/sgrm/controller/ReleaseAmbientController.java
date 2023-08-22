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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.pos.PAmbient;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.pos.PAmbientService;
import com.soin.sgrm.utils.JsonAutocomplete;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/ambient")
public class ReleaseAmbientController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(ReleaseAmbientController.class);

	@Autowired
	AmbientService ambientService;
	
	@Autowired
	PAmbientService pambientService;

	private final Environment environment;

	@Autowired
	public ReleaseAmbientController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/ambientAutoComplete-{search}-{system}", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> requestAutoComplete(@PathVariable String search,
			@PathVariable Integer system, HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList();
		try {
			
			if (profileActive().equals("oracle")) {
				List<Ambient> list = ambientService.list(system);

				for (int i = 0; i < list.size(); i++) {
					listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "",
							list.get(i).getName() + " " + list.get(i).getDetails(),
							list.get(i).getName() + " " + list.get(i).getDetails()));
				}
			} else if (profileActive().equals("postgres")) {
				List<PAmbient> list = pambientService.list(system);

				for (int i = 0; i < list.size(); i++) {
					listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "",
							list.get(i).getName() + " " + list.get(i).getDetails(),
							list.get(i).getName() + " " + list.get(i).getDetails()));
				}
			}
			
			
			return listAutoComplete;
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

}
