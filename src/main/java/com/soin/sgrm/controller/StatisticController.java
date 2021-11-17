package com.soin.sgrm.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.service.StatisticService;
import com.soin.sgrm.utils.ReleaseByTime;

@Controller
@RequestMapping("/statistic")
public class StatisticController extends BaseController{
	@Autowired
	StatisticService statistic;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/statistic/statistic";
	}
	
	@RequestMapping(path="/getLastFourYears", method=RequestMethod.GET)
	public @ResponseBody List<ReleaseByTime> getLastFourYears(HttpServletRequest request, Locale locale, Model model, HttpSession session){
		List<ReleaseByTime> list = statistic.getLastFourYears();
		return list;
	}
	
	@RequestMapping(path="/getLastMonth", method=RequestMethod.GET)
	public @ResponseBody List<ReleaseByTime> getLastMonth(HttpServletRequest request, Locale locale, Model model, HttpSession session){
		List<ReleaseByTime> list = statistic.getLastMonth();
		return list;
	}


}
