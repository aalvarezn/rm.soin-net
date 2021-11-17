package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.model.Password;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.utils.JsonAutocomplete;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/request")
public class RequestController extends BaseController {
	
	@Autowired
	private RequestService requestService;
	
	@RequestMapping(value = "/requestList", method = RequestMethod.GET)
	public @ResponseBody List<Request> requestList(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();	
		try {
			res.setStatus("success");
			List<Request> list = requestService.list("");
			return list;
		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException(e.getMessage());
			logs("RELEASE_ERROR", "Error requestList. " + getErrorFormat(e));
		}
		return null;
	}
	
	@RequestMapping(value = "/requestAutoComplete-{search}", method = RequestMethod.GET)
	public @ResponseBody List<JsonAutocomplete> requestAutoComplete(@PathVariable String search, HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		List<JsonAutocomplete> listAutoComplete = new ArrayList();
		try {			
			List<Request> list = requestService.list(search);
			
			for(int i = 0; i < list.size(); i++) {
				listAutoComplete.add(new JsonAutocomplete(list.get(i).getId()+"",
						list.get(i).getCode_soin() +" "+ list.get(i).getCode_ice()+" "+list.get(i).getDescription(),
						list.get(i).getCode_soin() +" "+ list.get(i).getCode_ice()+" "+list.get(i).getDescription()));
			}
			return listAutoComplete;
		}catch (SQLException ex) {
			logs("SYSTEM_ERROR", "Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error requestAutoComplete. " + getErrorFormat(e));
		}
		return null;
	}

}
