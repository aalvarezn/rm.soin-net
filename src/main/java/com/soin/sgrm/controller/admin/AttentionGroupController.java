package com.soin.sgrm.controller.admin;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/admin/attentionGroup")
public class AttentionGroupController extends BaseController {

	@Autowired
	UserInfoService userService;

	@Autowired
	AttentionGroupService attentionGroupService;

	public static final Logger logger = Logger.getLogger(AttentionGroupController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("users", userService.list());
		} catch (Exception e) {
			Sentry.capture(e, "attentionGroup");
			e.printStackTrace();
		}
		return "/admin/attentionGroup/attentionGroup";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<AttentionGroup> attentionGroups = new JsonSheet<>();
		try {

			attentionGroups.setData(attentionGroupService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return attentionGroups;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody AttentionGroup addAttentionGroup) {
		JsonResponse res = new JsonResponse();
		try {
			User temp = null;
			User lead=userService.findUserById(addAttentionGroup.getLeaderId());
			addAttentionGroup.setLead(lead);
			Set<User> authsUser = new HashSet<>();
			List<Integer> listUser=addAttentionGroup.getUsersAttentionId();
			if(!listUser.contains(addAttentionGroup.getLeaderId())) {
				listUser.add(addAttentionGroup.getLeaderId());
			}
			for (Integer index : listUser) {
				temp = userService.findUserById(index);
				if (temp != null) {
					authsUser.add(temp);
				}
			}
			addAttentionGroup.checkUserExists(authsUser);

			res.setStatus("success");

			attentionGroupService.save(addAttentionGroup);

			res.setMessage("Grupo de atencion agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "attentionGroup");
			res.setStatus("exception");
			res.setMessage("Error al agregar grupo de atencion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody AttentionGroup uptAttentionGroup) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			User temp = null;
			User lead=userService.findUserById(uptAttentionGroup.getLeaderId());
			uptAttentionGroup.setLead(lead);
			Set<User> authsUser = new HashSet<>();
			List<Integer> listUser=uptAttentionGroup.getUsersAttentionId();
			if(!listUser.contains(uptAttentionGroup.getLeaderId())) {
				listUser.add(uptAttentionGroup.getLeaderId());
			}
			for (Integer index : listUser) {
				temp = userService.findUserById(index);
				if (temp != null) {
					authsUser.add(temp);
				}
			}
			uptAttentionGroup.checkUserExists(authsUser);
			attentionGroupService.update(uptAttentionGroup);

			res.setMessage("Grupo de atencion modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "attentionGroup");
			res.setStatus("error");
			res.setMessage("Error al modificar el grupo de atencion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			attentionGroupService.delete(id);
			res.setMessage("Grupo de atencion eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "attentioGroup");
			res.setStatus("error");
			res.setMessage("Error al eliminar el grupo de atencion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

	
}

	