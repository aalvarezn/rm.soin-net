package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.utils.JsonAutocomplete;

@Controller
@RequestMapping("/ambient")
public class AmbientController extends BaseController {

	@Autowired
	AmbientService ambientService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/ambientAutoComplete-{search}-{system}", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> requestAutoComplete(@PathVariable String search,
			@PathVariable String system, HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList();
		try {
			List<Ambient> list = ambientService.list(search, system);

			for (int i = 0; i < list.size(); i++) {
				listAutoComplete.add(new JsonAutocomplete(list.get(i).getId() + "",
						list.get(i).getName() + " " + list.get(i).getDetails(),
						list.get(i).getName() + " " + list.get(i).getDetails()));
			}
			return listAutoComplete;
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error ambientAutoComplete. " + getErrorFormat(e));
			return null;
		}
	}

}
