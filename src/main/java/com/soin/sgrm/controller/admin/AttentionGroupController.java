package com.soin.sgrm.controller.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PUserInfoService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/admin/attentionGroup")
public class AttentionGroupController extends BaseController {

	@Autowired
	UserInfoService userService;

	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	PUserInfoService puserService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	private final Environment environment;
	
	public static final Logger logger = Logger.getLogger(AttentionGroupController.class);
	
	@Autowired
	public AttentionGroupController(Environment environment) {
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
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				model.addAttribute("users", userService.list());
			} else if (profile.equals("postgres")) {
				model.addAttribute("users", puserService.list());
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "attentionGroup");
			e.printStackTrace();
		}
		return "/admin/attentionGroup/attentionGroup";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<AttentionGroup> attentionGroups = new JsonSheet<>();
				attentionGroups.setData(attentionGroupService.findAll());
				return attentionGroups;
			} else if (profile.equals("postgres")) {
				JsonSheet<PAttentionGroup> attentionGroups = new JsonSheet<>();
				attentionGroups.setData(pattentionGroupService.findAll());
				return attentionGroups;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody AttentionGroup addAttentionGroup) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
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
			} else if (profile.equals("postgres")) {
				PAttentionGroup paddAttentionGroup=new PAttentionGroup();
				PUser temp = null;
				PUser lead=puserService.findUserById(addAttentionGroup.getLeaderId());
				paddAttentionGroup.setLead(lead);
			
				paddAttentionGroup.setName(addAttentionGroup.getName());
				paddAttentionGroup.setCode(addAttentionGroup.getCode());
				paddAttentionGroup.setLeaderId(addAttentionGroup.getLeaderId());
				paddAttentionGroup.setUsersAttentionId(addAttentionGroup.getUsersAttentionId());
				
				Set<PUser> pauthsUser = new HashSet<>();
				List<Integer> listUser=paddAttentionGroup.getUsersAttentionId();
				if(!listUser.contains(addAttentionGroup.getLeaderId())) {
					listUser.add(paddAttentionGroup.getLeaderId());
				}
				for (Integer index : listUser) {
					temp = puserService.findUserById(index);
					if (temp != null) {
						pauthsUser.add(temp);
					}
				}
				paddAttentionGroup.checkUserExists(pauthsUser);

				res.setStatus("success");

				pattentionGroupService.save(paddAttentionGroup);
			}
			


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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				
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
			} else if (profile.equals("postgres")) {
				PUser temp = null;
				PUser lead=puserService.findUserById(uptAttentionGroup.getLeaderId());
				PAttentionGroup puptAttentionGroup=new PAttentionGroup();
				puptAttentionGroup.setLead(lead);
				puptAttentionGroup.setId(uptAttentionGroup.getId());
				puptAttentionGroup.setName(uptAttentionGroup.getName());
				puptAttentionGroup.setCode(uptAttentionGroup.getCode());
				puptAttentionGroup.setLeaderId(uptAttentionGroup.getLeaderId());
				puptAttentionGroup.setUsersAttentionId(uptAttentionGroup.getUsersAttentionId());
				Set<PUser> pauthsUser = new HashSet<>();
				List<Integer> listUser=puptAttentionGroup.getUsersAttentionId();
				if(!listUser.contains(puptAttentionGroup.getLeaderId())) {
					listUser.add(puptAttentionGroup.getLeaderId());
				}
				for (Integer index : listUser) {
					temp = puserService.findUserById(index);
					if (temp != null) {
						pauthsUser.add(temp);
					}
				}
				puptAttentionGroup.checkUserExists(pauthsUser);
				pattentionGroupService.update(puptAttentionGroup);
			}
			

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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				attentionGroupService.delete(id);
			} else if (profile.equals("postgres")) {
				pattentionGroupService.delete(id);
			}
			
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

	