package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PComponent;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.ComponentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PComponentService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/component")
public class ComponentController extends BaseController {
	public static final Logger logger = Logger.getLogger(ComponentController.class);

	@Autowired
	ComponentService componentService;

	@Autowired
	AttentionGroupService attentionGroupService;

	@Autowired
	SystemService systemService;

	@Autowired
	PComponentService pcomponentService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	@Autowired
	PSystemService psystemService;
	
	private final Environment environment;

	@Autowired
	public ComponentController(Environment environment) {
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
		if(profileActive().equals("oracle")) {
			Integer userLogin = getUserLogin().getId();
			boolean rolRM = false;
			List<System> systemList = new ArrayList<System>();
			if (request.isUserInRole("ROLE_Release Manager")) {
				rolRM = true;
			}
			if (rolRM) {
				systemList = systemService.list();
			} else {
				List<AttentionGroup> attentionGroups = attentionGroupService.findGroupByUserId(userLogin);
				List<Long> listAttentionGroupId = new ArrayList<Long>();
				for (AttentionGroup attentionGroup : attentionGroups) {
					listAttentionGroupId.add(attentionGroup.getId());
				}
				systemList = systemService.findByGroupIncidence(listAttentionGroupId);
				Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
				systemList.clear();
				systemList.addAll(systemWithRepeat);
			}
			model.addAttribute("system", systemList);
		}else if(profileActive().equals("postgres")) {
			Integer userLogin = getUserLogin().getId();
			boolean rolRM = false;
			List<PSystem> systemList = new ArrayList<PSystem>();
			if (request.isUserInRole("ROLE_Release Manager")) {
				rolRM = true;
			}
			if (rolRM) {
				systemList = psystemService.list();
			} else {
				List<PAttentionGroup> attentionGroups = pattentionGroupService.findGroupByUserId(userLogin);
				List<Long> listAttentionGroupId = new ArrayList<Long>();
				for (PAttentionGroup attentionGroup : attentionGroups) {
					listAttentionGroupId.add(attentionGroup.getId());
				}
				systemList = psystemService.findByGroupIncidence(listAttentionGroupId);
				Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
				systemList.clear();
				systemList.addAll(systemWithRepeat);
			}
			model.addAttribute("system", systemList);
		}
	

		return "/incidence/knowledge/component/component";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			
			if(profileActive().equals("oracle")) {
				JsonSheet<Component> components = new JsonSheet<>();
				Integer userLogin = getUserLogin().getId();
				boolean rolRM = false;
				List<System> systemList = new ArrayList<System>();
				if (request.isUserInRole("ROLE_Release Manager")) {
					rolRM = true;
				}
				if (rolRM) {
					components.setData(componentService.findAll());
					return components;
				} else {
					List<AttentionGroup> attentionGroups = attentionGroupService.findGroupByUserId(userLogin);
					List<Long> listAttentionGroupId = new ArrayList<Long>();
					for (AttentionGroup attentionGroup : attentionGroups) {
						listAttentionGroupId.add(attentionGroup.getId());
					}
					systemList = systemService.findByGroupIncidence(listAttentionGroupId);
					Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
					systemList.clear();
					systemList.addAll(systemWithRepeat);
					List<Integer> systemIds = new ArrayList<Integer>();
					for (System system : systemList) {
						systemIds.add(system.getId());
					}
					components.setData(componentService.findBySystem(systemIds));
					return components;
				}
			}else if(profileActive().equals("postgres")) {
				JsonSheet<PComponent> components = new JsonSheet<>();
				Integer userLogin = getUserLogin().getId();
				boolean rolRM = false;
				List<PSystem> systemList = new ArrayList<PSystem>();
				if (request.isUserInRole("ROLE_Release Manager")) {
					rolRM = true;
				}
				if (rolRM) {
					components.setData(pcomponentService.findAll());
					return components;
				} else {
					List<PAttentionGroup> pattentionGroups = pattentionGroupService.findGroupByUserId(userLogin);
					List<Long> listAttentionGroupId = new ArrayList<Long>();
					for (PAttentionGroup attentionGroup : pattentionGroups) {
						listAttentionGroupId.add(attentionGroup.getId());
					}
					systemList = psystemService.findByGroupIncidence(listAttentionGroupId);
					Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
					systemList.clear();
					systemList.addAll(systemWithRepeat);
					List<Integer> systemIds = new ArrayList<Integer>();
					for (PSystem system : systemList) {
						systemIds.add(system.getId());
					}
					components.setData(pcomponentService.findBySystem(systemIds));
					return components;
				}
			}
		
			
	

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Component addComponent) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if(profileActive().equals("oracle")) {
				addComponent.setName(addComponent.getName().toUpperCase());
				SystemInfo system = new SystemInfo();
				system.setId(addComponent.getSystemId());
				addComponent.setSystem(system);
				Component componentVerify = componentService.findByKey("name", addComponent.getName().trim());
				if (componentVerify == null) {
					componentService.save(addComponent);
					res.setMessage("Componente agregado!");
				} else {
					if (componentVerify.getSystem().getId() == addComponent.getSystemId()) {
						res.setStatus("error");
						res.setMessage("Error al agregar componente nombre ya utilizado para este sistema!");
					} else {
						componentService.save(addComponent);
						res.setMessage("Componente agregado!");
					}

				}
			}else if(profileActive().equals("postgres")) {
				PComponent paddComponent=new PComponent();
				paddComponent.setName(addComponent.getName().toUpperCase());
				paddComponent.setSystemId(addComponent.getSystemId());

				PSystemInfo system = new PSystemInfo();
				system.setId(paddComponent.getSystemId());
				paddComponent.setSystem(system);
				PComponent componentVerify = pcomponentService.findByKey("name", paddComponent.getName().trim());
				if (componentVerify == null) {
					pcomponentService.save(paddComponent);
					res.setMessage("Componente agregado!");
				} else {
					if (componentVerify.getSystem().getId() == paddComponent.getSystemId()) {
						res.setStatus("error");
						res.setMessage("Error al agregar componente nombre ya utilizado para este sistema!");
					} else {
						pcomponentService.save(paddComponent);
						res.setMessage("Componente agregado!");
					}

				}
			}
		
	

		} catch (Exception e) {
			Sentry.capture(e, "component");
			res.setStatus("error");
			res.setMessage("Error al agregar componente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody Component uptComponent) {
		JsonResponse res = new JsonResponse();
		try {
			if(profileActive().equals("oracle")) {
				Component component = componentService.findById(uptComponent.getId());
				SystemInfo system = new SystemInfo();
				system.setId(uptComponent.getSystemId());
				uptComponent.setSystem(system);
				if (component.getName() != uptComponent.getName()) {

					Component componentVerify = componentService.findByKey("name", uptComponent.getName().trim());
					if (componentVerify != null) {
						if (componentVerify.getId() == uptComponent.getId()) {
							componentService.update(uptComponent);
							res.setMessage("Componente modificado!");
							res.setStatus("success");
						} else {
							if (componentVerify.getSystem().getId() == uptComponent.getSystemId()) {
								res.setStatus("error");
								res.setMessage("Error al modificar componente este nombre ya pertenece a otro!");
							} else {
								componentService.update(uptComponent);
								res.setMessage("Componente modificado!");
								res.setStatus("success");
							}

						}
					} else {
						componentService.update(uptComponent);
						res.setMessage("Componente modificado!");
						res.setStatus("success");
					}
				} else {
					componentService.update(uptComponent);
					res.setMessage("Componente modificado!");
					res.setStatus("success");
				}
			}else if(profileActive().equals("postgres")) {
				PComponent puptComponent=new PComponent();
				puptComponent.setId(uptComponent.getId());
				puptComponent.setName(uptComponent.getName());
				puptComponent.setSystemId(uptComponent.getSystemId());
				PComponent component = pcomponentService.findById(uptComponent.getId());
				PSystemInfo system = new PSystemInfo();
				system.setId(puptComponent.getSystemId());
				puptComponent.setSystem(system);
				if (component.getName() != puptComponent.getName()) {

					PComponent componentVerify = pcomponentService.findByKey("name", puptComponent.getName().trim());
					if (componentVerify != null) {
						if (componentVerify.getId() == puptComponent.getId()) {
							pcomponentService.update(puptComponent);
							res.setMessage("Componente modificado!");
							res.setStatus("success");
						} else {
							if (componentVerify.getSystem().getId() == puptComponent.getSystemId()) {
								res.setStatus("error");
								res.setMessage("Error al modificar componente este nombre ya pertenece a otro!");
							} else {
								pcomponentService.update(puptComponent);
								res.setMessage("Componente modificado!");
								res.setStatus("success");
							}

						}
					} else {
						pcomponentService.update(puptComponent);
						res.setMessage("Componente modificado!");
						res.setStatus("success");
					}
				} else {
					pcomponentService.update(puptComponent);
					res.setMessage("Componente modificado!");
					res.setStatus("success");
				}
			}
		
	

		} catch (Exception e) {
			Sentry.capture(e, "Component");
			res.setStatus("error");
			res.setMessage("Error al modificar componente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if(profileActive().equals("oracle")) {
				componentService.delete(id);
			}else if(profileActive().equals("postgres")) {
				pcomponentService.delete(id);
			}
		
			
			res.setMessage("Componente eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "component");
			res.setStatus("error");
			res.setMessage("Error al eliminar el componente !");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
