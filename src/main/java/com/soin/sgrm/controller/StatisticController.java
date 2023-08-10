package com.soin.sgrm.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.pos.PReleaseByTime;
import com.soin.sgrm.service.StatisticService;
import com.soin.sgrm.service.pos.PStatisticService;
import com.soin.sgrm.utils.ReleaseByTime;

@Controller
@RequestMapping("/statistic")
public class StatisticController extends BaseController{
	@Autowired
	StatisticService statistic;
	
	@Autowired
	PStatisticService pstatistic;
	
	private final Environment environment;

	@Autowired
	public StatisticController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/statistic/statistic";
	}
	
	@RequestMapping(path="/getLastFourYears", method=RequestMethod.GET)
	public @ResponseBody List<?> getLastFourYears(HttpServletRequest request, Locale locale, Model model, HttpSession session){
		if (profileActive().equals("oracle")) {
			List<ReleaseByTime> list = statistic.getLastFourYears();
			return list;
		} else if (profileActive().equals("postgres")) {
			List<PReleaseByTime> list = pstatistic.getLastFourYears();
			return list;
		}
		return null;
	
	}
	
	@RequestMapping(path="/getLastMonth", method=RequestMethod.GET)
	public @ResponseBody List<?> getLastMonth(HttpServletRequest request, Locale locale, Model model, HttpSession session){
		if (profileActive().equals("oracle")) {
			List<ReleaseByTime> list = statistic.getLastMonth();
			return list;
		} else if (profileActive().equals("postgres")) {
			List<PReleaseByTime> list = pstatistic.getLastMonth();
			return list;
		}
		
		return null;
	}


}
