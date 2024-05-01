package com.soin.sgrm.controller.wf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
//import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PSystem_StatusIn;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.wf.PEdge;
import com.soin.sgrm.model.pos.wf.PEdgeIncidence;
import com.soin.sgrm.model.pos.wf.PEdgeRFC;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.pos.wf.PNodeIncidence;
import com.soin.sgrm.model.pos.wf.PNodeRFC;
import com.soin.sgrm.model.pos.wf.PType;
import com.soin.sgrm.model.pos.wf.PWFSystem;
import com.soin.sgrm.model.pos.wf.PWFUser;
import com.soin.sgrm.model.pos.wf.PWorkFlow;
import com.soin.sgrm.model.pos.wf.PWorkFlowIncidence;
import com.soin.sgrm.model.pos.wf.PWorkFlowRFC;
import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.wf.EdgeIncidence;
import com.soin.sgrm.model.wf.EdgeRFC;
//import com.soin.sgrm.model.wf.EdgeIncidence;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.NodeRFC;
//import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.Type;
import com.soin.sgrm.model.wf.WFSystem;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.model.wf.WorkFlow;
import com.soin.sgrm.model.wf.WorkFlowIncidence;
import com.soin.sgrm.model.wf.WorkFlowRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.StatusRFCService;
//import com.soin.sgrm.model.wf.WorkFlowIncidence;
//import com.soin.sgrm.service.AttentionGroupService;
//import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.System_StatusInService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PStatusRFCService;
import com.soin.sgrm.service.pos.PStatusService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PSystem_StatusInService;
import com.soin.sgrm.service.pos.wf.PEdgeService;
import com.soin.sgrm.service.pos.wf.PNodeService;
import com.soin.sgrm.service.pos.wf.PTypeService;
import com.soin.sgrm.service.pos.wf.PWFUserService;
import com.soin.sgrm.service.pos.wf.PWorkFlowService;
import com.soin.sgrm.service.wf.EdgeService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.service.wf.TypeService;
import com.soin.sgrm.service.wf.WFUserService;
import com.soin.sgrm.service.wf.WorkFlowService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/wf/workFlow")
public class WorkFlowController extends BaseController {

	public static final Logger logger = Logger.getLogger(WorkFlowController.class);

	@Autowired
	WorkFlowService workFlowService;
	@Autowired
	StatusService statusService;
	@Autowired
	StatusRFCService statusRFCService;
	@Autowired
	WFUserService wfUserService;
	@Autowired
	NodeService nodeService;
	@Autowired
	EdgeService edgeService;
	@Autowired
	SystemService systemService;
	@Autowired
	TypeService typeService;
	@Autowired
	AttentionGroupService attentionGroupService;
	@Autowired
	System_StatusInService statusSystemIncidenceService;
	
	
	@Autowired
	PWorkFlowService pworkFlowService;
	@Autowired
	PStatusService pstatusService;
	@Autowired
	PStatusRFCService pstatusRFCService;
	@Autowired
	PWFUserService pwfUserService;
	@Autowired
	PNodeService pnodeService;
	@Autowired
	PEdgeService pedgeService;
	@Autowired
	PSystemService psystemService;
	@Autowired
	PTypeService ptypeService;
	@Autowired
	PAttentionGroupService pattentionGroupService;
	@Autowired
	PSystem_StatusInService pstatusSystemIncidenceService;
	
	private final Environment environment;
	

	@Autowired
	public WorkFlowController(Environment environment) {
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
		
		if (profileActive().equals("oracle")) {
			model.addAttribute("workFlows", workFlowService.list());
			model.addAttribute("workFlow", new WorkFlow());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("types", typeService.list());
			model.addAttribute("system", new SystemInfo());
		} else if (profileActive().equals("postgres")) {
			model.addAttribute("workFlows", pworkFlowService.list());
			model.addAttribute("workFlow", new PWorkFlow());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("types", ptypeService.list());
			model.addAttribute("system", new PSystemInfo());
		}
	
		return "/wf/workFlow/workFlow";
	}

	@RequestMapping(value = "workFlowEdit/{id}", method = RequestMethod.GET)
	public String workFlowEdit(HttpServletRequest request, @PathVariable Integer id, Locale locale, Model model,
			HttpSession session) {
		if (profileActive().equals("oracle")) {
			WorkFlow workFlow = workFlowService.findById(id);
			if(workFlow.getType().getId()==1) {
				model.addAttribute("workFlow", workFlow);
				model.addAttribute("statuses", statusService.list());
				model.addAttribute("status", new Status());
				model.addAttribute("users", wfUserService.list());
				model.addAttribute("user", new WFUser());
				return "/wf/workFlow/workFlowEdit";
			}else if(workFlow.getType().getId()==2) {
				WorkFlowIncidence workFlowIncidence = workFlowService.findByIdIncidence(id);
				model.addAttribute("workFlow", workFlowIncidence);
				//VA STATUS_SYS
				com.soin.sgrm.model.System system=systemService.findSystemById(workFlowIncidence.getSystem().getId());
			
				List<System_StatusIn> listStatus=statusSystemIncidenceService.findBySystem(workFlowIncidence.getSystem().getId());
				model.addAttribute("statuses", listStatus);
				model.addAttribute("status", new System_StatusIn());
				model.addAttribute("users", wfUserService.list());
				model.addAttribute("groups", system.getAttentionGroup());
				model.addAttribute("user", new WFUser());
				return "/wf/workFlow/workFlowEditIncidence";
			}else if(workFlow.getType().getId()==3) {
				WorkFlowRFC workFlowRFC = workFlowService.findByIdRFC(id);
				model.addAttribute("workFlow", workFlowRFC);
				model.addAttribute("statuses", statusRFCService.findAll());
				model.addAttribute("status", new StatusRFC());
				model.addAttribute("users", wfUserService.list());
				model.addAttribute("user", new WFUser());
				return "/wf/workFlow/workFlowEditRFC";
				
			}else {
				model.addAttribute("workFlow", workFlow);
				model.addAttribute("statuses", statusService.list());
				model.addAttribute("status", new Status());
				model.addAttribute("users", wfUserService.list());
				model.addAttribute("user", new WFUser());
				return "/plantilla/404";
				
			}
		} else if (profileActive().equals("postgres")) {
			PWorkFlow workFlow = pworkFlowService.findById(id);
			if(workFlow.getType().getId()==1) {
				model.addAttribute("workFlow", workFlow);
				model.addAttribute("statuses", pstatusService.list());
				model.addAttribute("status", new PStatus());
				model.addAttribute("users", pwfUserService.list());
				model.addAttribute("user", new PWFUser());
				return "/wf/workFlow/workFlowEdit";
			}else if(workFlow.getType().getId()==2) {
				PWorkFlowIncidence workFlowIncidence = pworkFlowService.findByIdIncidence(id);
				model.addAttribute("workFlow", workFlowIncidence);
				//VA STATUS_SYS
				PSystem system=psystemService.findSystemById(workFlowIncidence.getSystem().getId());
			
				List<PSystem_StatusIn> listStatus=pstatusSystemIncidenceService.findBySystem(workFlowIncidence.getSystem().getId());
				model.addAttribute("statuses", listStatus);
				model.addAttribute("status", new PSystem_StatusIn());
				model.addAttribute("users", pwfUserService.list());
				model.addAttribute("groups", system.getAttentionGroup());
				model.addAttribute("user", new PWFUser());
				return "/wf/workFlow/workFlowEditIncidence";
			}else if(workFlow.getType().getId()==3) {
				PWorkFlowRFC workFlowRFC = pworkFlowService.findByIdRFC(id);
				model.addAttribute("workFlow", workFlowRFC);
				model.addAttribute("statuses", pstatusRFCService.findAll());
				model.addAttribute("status", new PStatusRFC());
				model.addAttribute("users", pwfUserService.list());
				model.addAttribute("user", new PWFUser());
				return "/wf/workFlow/workFlowEditRFC";
				
			}else {
				model.addAttribute("workFlow", workFlow);
				model.addAttribute("statuses", pstatusService.list());
				model.addAttribute("status", new PStatus());
				model.addAttribute("users", pwfUserService.list());
				model.addAttribute("user", new PWFUser());
				return "/plantilla/404";
				
			}
		}
		return "/plantilla/404";
		
		
	}

	@RequestMapping(value = "/loadWorkFlow/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findWorkFlow(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			if (profileActive().equals("oracle")) {
				WorkFlow workFlow = workFlowService.findById(id);
				return workFlow;
			} else if (profileActive().equals("postgres")) {
				PWorkFlow workFlow = pworkFlowService.findById(id);
				return workFlow;
			}
		
			
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}
	
	@RequestMapping(value = "/loadWorkFlowRFC/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findWorkFlowRFC(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			if (profileActive().equals("oracle")) {
				WorkFlowRFC workFlow = workFlowService.findByIdRFC(id);
				return workFlow;
			} else if (profileActive().equals("postgres")) {
				WorkFlowRFC workFlow = workFlowService.findByIdRFC(id);
				return workFlow;
			}
		
			
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}
	@RequestMapping(value = "/loadWorkFlowIncidence/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findWorkFlowIncidence(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			if (profileActive().equals("oracle")) {
				WorkFlowIncidence workFlow = workFlowService.findByIdIncidence(id);
				return workFlow;
			} else if (profileActive().equals("postgres")) {
				PWorkFlowIncidence workFlow = pworkFlowService.findByIdIncidence(id);
				return workFlow;
			}
		
			
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}


	@RequestMapping(path = "/saveWorkFlow", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveWorkFlow(HttpServletRequest request,
			@Valid @ModelAttribute("WorkFlow") WorkFlow workFlow, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (workFlow.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}
			
			if (workFlow.getTypeId() == null) {
				res.setStatus("fail");
				res.addError("typeId", "Seleccione una opción");
			}
			if (profileActive().equals("oracle")) {
				if(res.getStatus().equals("success")) {
					
					if (workFlowService.verifyCreation(workFlow.getSystemId(),workFlow.getTypeId())) {
						res.setStatus("exception");
						res.setException("Error al crear el tramite ya hay uno creado para este sistema.");
					}
			}


			if (res.getStatus().equals("success")) {
				WFSystem system = new WFSystem();
				Type type=new Type();
				type.setId(workFlow.getTypeId());
				system.setId(workFlow.getSystemId());
				workFlow.setSystem(system);
				workFlow.setType(type);
				workFlowService.save(workFlow);
				res.setObj(workFlow);
			}
			} else if (profileActive().equals("postgres")) {
				if(res.getStatus().equals("success")) {
					
					if (pworkFlowService.verifyCreation(workFlow.getSystemId(),workFlow.getTypeId())) {
						res.setStatus("exception");
						res.setException("Error al crear el tramite ya hay uno creado para este sistema.");
					}
			}


			if (res.getStatus().equals("success")) {
				PWFSystem system = new PWFSystem();
				PType type=new PType();
				PWorkFlow pworkFlow=new PWorkFlow();
				type.setId(workFlow.getTypeId());
				system.setId(workFlow.getSystemId());
				pworkFlow.setName(workFlow.getName());
				pworkFlow.setSystem(system);
				pworkFlow.setType(type);
				pworkFlowService.save(pworkFlow);
				res.setObj(pworkFlow);
			}
			}
		
			
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear trémite: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}


	@RequestMapping(path = "/saveNode", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveNode(HttpServletRequest request, @Valid @ModelAttribute("Node") Node node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					WorkFlow workFlow = new WorkFlow();
					workFlow.setId(node.getWorkFlowId());
					node.setSendEmail(false);
					node.setSkipNode(false);
					node.setSkipByRequest(false);
					node.setSkipReapprove(false);
					node.setWorkFlow(workFlow);
					node = nodeService.save(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlow workFlow = new PWorkFlow();
					PNode pnode=new PNode();
					workFlow.setId(node.getWorkFlowId());
					pnode.setSendEmail(false);
					pnode.setSkipNode(false);
					pnode.setSkipByRequest(false);
					pnode.setSkipReapprove(false);
					pnode.setWorkFlow(workFlow);
					Set<PWFUser> pactors=new HashSet<PWFUser>();
					for(WFUser actor: node.getActors()) {
						PWFUser pactor= new PWFUser();
						pactor.setId(actor.getId());
						pactor.setFullName(actor.getFullName());
						pactor.setIsActive(actor.getIsActive()==1?true:false);
						pactor.setShortName(actor.getShortName());
						pactor.setUsername(actor.getUsername());
						pactor.setEmail(actor.getEmail());	
						pactors.add(pactor);
					}
					
					List<PEdge> pedges=new ArrayList<PEdge>();
					for(Edge edge: node.getEdges()) {
						PEdge pedge= new PEdge();
						pedge.setId(edge.getId());
						PNode pnodeFrom=new PNode();
						pnodeFrom.setId(edge.getNodeFrom().getId());
						pedge.setNodeFrom(pnodeFrom);
						PNode pnodeTo=new PNode();
						pnodeTo.setId(edge.getNodeTo().getId());
						pedge.setNodeTo(pnodeTo);
						pedges.add(pedge);
					}
					pnode.setActors(pactors);
					pnode.setEdges(pedges);
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setY(node.getY());
					pnode.setGroup(node.getGroup());
					pnode = pnodeService.save(pnode);
					res.setObj(pnode);
				}
			
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(path = "/saveNodeRFC", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveNodeRFC(HttpServletRequest request, @Valid @ModelAttribute("NodeRFC") NodeRFC node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				
				if (profileActive().equals("oracle")) {
					WorkFlowRFC workFlow = new WorkFlowRFC();
					workFlow.setId(node.getWorkFlowId());
					node.setSendEmail(false);
					node.setWorkFlow(workFlow);
					node = nodeService.saveNodeRFC(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlowRFC workFlow = new PWorkFlowRFC();
					workFlow.setId(node.getWorkFlowId());
					PNodeRFC  pnode=new PNodeRFC();
					pnode.setSendEmail(false);
					pnode.setWorkFlow(workFlow);
					Set<PWFUser> pactors=new HashSet<PWFUser>();
					for(WFUser actor: node.getActors()) {
						PWFUser pactor= new PWFUser();
						pactor.setId(actor.getId());
						pactor.setFullName(actor.getFullName());
						pactor.setIsActive(actor.getIsActive()==1?true:false);
						pactor.setShortName(actor.getShortName());
						pactor.setUsername(actor.getUsername());
						pactor.setEmail(actor.getEmail());	
						pactors.add(pactor);
					}
					
					List<PEdgeRFC> pedges=new ArrayList<PEdgeRFC>();
					for(EdgeRFC edge: node.getEdges()) {
						PEdgeRFC pedge= new PEdgeRFC();
						pedge.setId(edge.getId());
						PNodeRFC pnodeFrom=new PNodeRFC();
						pnodeFrom.setId(edge.getNodeFrom().getId());
						pedge.setNodeFrom(pnodeFrom);
						PNodeRFC pnodeTo=new PNodeRFC();
						pnodeTo.setId(edge.getNodeTo().getId());
						pedge.setNodeTo(pnodeTo);
						pedges.add(pedge);
					}
					pnode.setActors(pactors);
					pnode.setEdges(pedges);
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setGroup(node.getGroup());
					pnode = pnodeService.saveNodeRFC(pnode);
					res.setObj(pnode);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	
	
	@RequestMapping(path = "/saveNodeIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveNodeIncidence(HttpServletRequest request, @Valid @ModelAttribute("NodeIncidence") NodeIncidence node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				
				if (profileActive().equals("oracle")) {
					WorkFlowIncidence workFlow = new WorkFlowIncidence();
					workFlow.setId(node.getWorkFlowId());
					node.setSendEmail(false);
					node.setWorkFlow(workFlow);
					node = nodeService.saveNodeIncidence(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlowIncidence workFlow = new PWorkFlowIncidence();
					workFlow.setId(node.getWorkFlowId());
					PNodeIncidence pnode=new PNodeIncidence();
					pnode.setSendEmail(false);
					pnode.setWorkFlow(workFlow);
					Set<PAttentionGroup> pattentionGroups=new HashSet<PAttentionGroup>();
					for(AttentionGroup attentionGroup: node.getActors()) {
						PAttentionGroup pattentionGroup= new PAttentionGroup();
						pattentionGroup.setId(attentionGroup.getId());
						pattentionGroup.setCode(attentionGroup.getCode());
						pattentionGroup.setName(attentionGroup.getName());
						
						Set<PUser> pusers=new HashSet<PUser>();
						for(User user:attentionGroup.getUserAttention()) {
							PUser puser=new PUser();
							puser.setId(user.getId());
							pusers.add(puser);
						}
						PUser plead=new PUser();
						plead.setId(attentionGroup.getLead().getId());
						pattentionGroup.setUserAttention(pusers);
						pattentionGroup.setLead(plead);
						pattentionGroups.add(pattentionGroup);
					}
					
					List<PEdgeIncidence> pedges=new ArrayList<PEdgeIncidence>();
					for(EdgeIncidence edge: node.getEdges()) {
						PEdgeIncidence pedge= new PEdgeIncidence();
						pedge.setId(edge.getId());
						PNodeIncidence pnodeFrom=new PNodeIncidence();
						pnodeFrom.setId(edge.getNodeFrom().getId());
						pedge.setNodeFrom(pnodeFrom);
						PNodeIncidence pnodeTo=new PNodeIncidence();
						pnodeTo.setId(edge.getNodeTo().getId());
						pedge.setNodeTo(pnodeTo);
						pedges.add(pedge);
					}
					pnode.setActors(pattentionGroups);
					pnode.setEdges(pedges);
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setY(node.getY());
					pnode.setGroup(node.getGroup());
					pnode = pnodeService.saveNodeIncidence(pnode);
					res.setObj(pnode);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

	@RequestMapping(value = "/updateNode", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNode(HttpServletRequest request, @Valid @ModelAttribute("Node") Node node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		PNode pnode=new PNode();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}else {
				if (profileActive().equals("oracle")) {
					if(node.getGroup().equals("start"))
						if(nodeService.verifyStartNode(node)) {
							
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
				} else if (profileActive().equals("postgres")) {
					if(node.getGroup().equals("start"))
						
						pnode=pnodeService.findById(node.getId());
						if(pnodeService.verifyStartNode(pnode)) {	
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
				}
				
			}
			if(node.getSkipByRequest()==true) {
				if(node.getSkipByRequestId()==null) {
					res.setStatus("exception");
					res.setException("Error al se debe seleccionar el estado a saltar.");
				}
			}else {
				node.setSkipByRequestId(null);
				node.setMotiveSkipR(null);
			}
			
			if(node.getSkipReapprove()==true) {
				if(node.getSkipReapproveId()==null) {
					res.setStatus("exception");
					res.setException("Error al se debe seleccionar el estado a saltar.");
				}
			}else {
				node.setSkipReapproveId(null);
				node.setMotiveSkipRA(null);
			}
			
			if(node.getSkipNode()==true) {
				if(node.getSkipId()==null) {
					res.setStatus("exception");
					res.setException("Error al se debe seleccionar el estado a saltar.");
				}
			}else {
				node.setSkipId(null);
				node.setMotiveSkip(null);
			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					WorkFlow workFlow = new WorkFlow();
					workFlow.setId(node.getWorkFlowId());
					node.setWorkFlow(workFlow);
					if (node.getStatusId() != null) {
						Status status = new Status();
						status.setId(node.getStatusId());
						node.setStatus(status);
					}
					
					
					// se agregan los usuarios actores
					node.clearActors();
					WFUser actor = null;
					for (Integer index : node.getActorsIds()) {
						actor = wfUserService.findWFUserById(index);
						if (actor != null)
							node.addActor(actor);
					}

					// se agregan los usuarios a notificar
					node.clearUsers();
					WFUser temp = null;
					for (Integer index : node.getUsersIds()) {
						temp = wfUserService.findWFUserById(index);
						if (temp != null)
							node.addUser(temp);
					}

					node = nodeService.update(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlow workFlow = new PWorkFlow();
					workFlow.setId(node.getWorkFlowId());
					pnode.setWorkFlow(workFlow);
					if (node.getStatusId() != null) {
						PStatus status = new PStatus();
						status.setId(node.getStatusId());
						pnode.setStatus(status);
					}
					
					
					// se agregan los usuarios actores
					pnode.clearActors();
					PWFUser actor = null;
					for (Integer index : node.getActorsIds()) {
						actor = pwfUserService.findWFUserById(index);
						if (actor != null)
							pnode.addActor(actor);
					}

					// se agregan los usuarios a notificar
					pnode.clearUsers();
					PWFUser temp = null;
					for (Integer index : node.getUsersIds()) {
						temp = pwfUserService.findWFUserById(index);
						if (temp != null)
							pnode.addUser(temp);
					}
					
					pnode.setId(node.getId());
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setY(node.getY());
					pnode.setGroup(node.getGroup());
					pnode.setSendEmail(node.getSendEmail());
					pnode = pnodeService.update(pnode);
					res.setObj(pnode);
				}
				
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/updateNodeRFC", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNodeRFC(HttpServletRequest request, @Valid @ModelAttribute("NodeRFC") NodeRFC node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		PNodeRFC pnode=new PNodeRFC();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}else {
				if(node.getGroup().equals("start"))
					if (profileActive().equals("oracle")) {
						if(nodeService.verifyStartNodeRFC(node)) {
							
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
					} else if (profileActive().equals("postgres")) {
						if(node.getGroup().equals("start"))
							
							pnode=pnodeService.findByIdNoRFC(node.getId());
						if(pnodeService.verifyStartNodeRFC(pnode)) {
							
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
					}

			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					WorkFlowRFC workFlow = new WorkFlowRFC();
					workFlow.setId(node.getWorkFlowId());
					node.setWorkFlow(workFlow);
					if (node.getStatusId() != null) {
						StatusRFC status = new StatusRFC();
						status.setId(node.getStatusId());
						node.setStatus(status);
					}
					
					
					// se agregan los usuarios actores
					node.clearActors();
					WFUser actor = null;
					for (Integer index : node.getActorsIds()) {
						actor = wfUserService.findWFUserById(index);
						if (actor != null)
							node.addActor(actor);
					}

					// se agregan los usuarios a notificar
					node.clearUsers();
					WFUser temp = null;
					for (Integer index : node.getUsersIds()) {
						temp = wfUserService.findWFUserById(index);
						if (temp != null)
							node.addUser(temp);
					}

					node = nodeService.updateNodeRFC(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlowRFC workFlow = new PWorkFlowRFC();
					workFlow.setId(node.getWorkFlowId());
					pnode.setWorkFlow(workFlow);
					if (node.getStatusId() != null) {
						PStatusRFC status = new PStatusRFC();
						status.setId(node.getStatusId());
						pnode.setStatus(status);
					}
					
					
					// se agregan los usuarios actores
					pnode.clearActors();
					PWFUser actor = null;
					for (Integer index : node.getActorsIds()) {
						actor = pwfUserService.findWFUserById(index);
						if (actor != null)
							pnode.addActor(actor);
					}

					// se agregan los usuarios a notificar
					pnode.clearUsers();
					PWFUser temp = null;
					for (Integer index : node.getUsersIds()) {
						temp = pwfUserService.findWFUserById(index);
						if (temp != null)
							pnode.addUser(temp);
					}
					pnode.setId(node.getId());
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setY(node.getY());
					pnode.setGroup(node.getGroup());
					pnode.setSendEmail(node.getSendEmail());
					pnode = pnodeService.updateNodeRFC(pnode);
					res.setObj(pnode);
				}
			
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/updateNodeIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNodeIncidence(HttpServletRequest request, @Valid @ModelAttribute("NodeIncidence") NodeIncidence node,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		PNodeIncidence pnode=new PNodeIncidence();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}else {
				if(node.getGroup().equals("start"))
					if (profileActive().equals("oracle")) {
						if(nodeService.verifyStartNodeIncidence(node)) {
							
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
					} else if (profileActive().equals("postgres")) {
						if(node.getGroup().equals("start"))
							pnode=pnodeService.findByIdNoInci(node.getId());
						if(pnodeService.verifyStartNodeIncidence(pnode)) 
							res.setStatus("exception");
							res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
						}
					}
			
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					WorkFlowIncidence workFlow = new WorkFlowIncidence();
					workFlow.setId(node.getWorkFlowId());
					node.setWorkFlow(workFlow);
					

					if (node.getStatusId() != null) {
						System_StatusIn status = new System_StatusIn();
						status.setId(node.getStatusId());
						node.setStatus(status);
					}else {
						if(node.getGroup().equals("start"))
							if(nodeService.verifyStartNodeIncidence(node)) {
								
								res.setStatus("exception");
								res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
							}
						}
					// se agregan los grupos actores
					node.clearActors();
					AttentionGroup actor = null;
					for (Long index : node.getActorsIds()) {
						actor = attentionGroupService.findById(index);
						if (actor != null)
							node.addActor(actor);
					}

					// se agregan los grupos a notificar
					node.clearUsers();
					AttentionGroup temp = null;
					for (Long index : node.getUsersIds()) {
						temp = attentionGroupService.findById(index);
						if (temp != null)
							node.addUser(temp);
					}

					node = nodeService.updateNodeIncidence(node);
					res.setObj(node);
				} else if (profileActive().equals("postgres")) {
					PWorkFlowIncidence workFlow = new PWorkFlowIncidence();
					workFlow.setId(node.getWorkFlowId());
					pnode.setWorkFlow(workFlow);
					

					if (node.getStatusId() != null) {
						PSystem_StatusIn status = new PSystem_StatusIn();
						status.setId(node.getStatusId());
						pnode.setStatus(status);
					}
					// se agregan los grupos actores
					pnode.clearActors();
					PAttentionGroup actor = null;
					for (Long index : node.getActorsIds()) {
						actor = pattentionGroupService.findById(index);
						if (actor != null)
							pnode.addActor(actor);
					}

					// se agregan los grupos a notificar
					pnode.clearUsers();
					PAttentionGroup temp = null;
					for (Long index : node.getUsersIds()) {
						temp = pattentionGroupService.findById(index);
						if (temp != null)
							pnode.addUser(temp);
					}
					pnode.setId(node.getId());
					pnode.setLabel(node.getLabel());
					pnode.setX(node.getX());
					pnode.setY(node.getY());
					pnode.setGroup(node.getGroup());
					pnode.setSendEmail(node.getSendEmail());
					pnode = pnodeService.updateNodeIncidence(pnode);
					res.setObj(pnode);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/updateNodePosition", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNodePosition(HttpServletRequest request,
			@Valid @ModelAttribute("Node") Node node, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				Node oldNode = nodeService.findById(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				node = nodeService.update(oldNode);
				res.setObj(node);
			} else if (profileActive().equals("postgres")) {
				PNode oldNode = pnodeService.findById(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				oldNode = pnodeService.update(oldNode);
				res.setObj(oldNode);
			}
			

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/updateNodeRFCPosition", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNodeRFCPosition(HttpServletRequest request,
			@Valid @ModelAttribute("NodeRFC") NodeRFC node, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				NodeRFC oldNode = nodeService.findByIdNoRFC(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				node = nodeService.updateNodeRFC(oldNode);
				res.setObj(node);
			} else if (profileActive().equals("postgres")) {
				PNodeRFC oldNode = pnodeService.findByIdNoRFC(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				oldNode = pnodeService.updateNodeRFC(oldNode);
				res.setObj(oldNode);
			}
			

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/updateNodeInPosition", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateNodeIncidencePosition(HttpServletRequest request,
			@Valid @ModelAttribute("NodeIncidence") NodeIncidence node, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (profileActive().equals("oracle")) {
				NodeIncidence oldNode = nodeService.findByIdNoInci(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				node = nodeService.updateNodeIncidence(oldNode);
				res.setObj(node);
			} else if (profileActive().equals("postgres")) {
				PNodeIncidence oldNode = pnodeService.findByIdNoInci(node.getId());
				oldNode.setX(node.getX());
				oldNode.setY(node.getY());
				oldNode = pnodeService.updateNodeIncidence(oldNode);
				res.setObj(oldNode);
			}
			

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar nodo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteNode/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteWorkFlow(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				nodeService.delete(id);
			} else if (profileActive().equals("postgres")) {
				pnodeService.delete(id);
			}
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar nodo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar nodo: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/deleteNodeRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteWorkFlowRFC(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				nodeService.deleteNodeRFC(id);
			} else if (profileActive().equals("postgres")) {
				pnodeService.deleteNodeRFC(id);
			}
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar nodo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar nodo: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteNodeIncidence/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteWorkFlowIncidence(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				nodeService.deleteNodeIncidence(id);
			} else if (profileActive().equals("postgres")) {
				pnodeService.deleteNodeIncidence(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar nodo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar nodo: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/saveEdge", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEdge(HttpServletRequest request, @Valid @ModelAttribute("Edge") Edge edge,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					edge.setNodeFrom(new Node(edge.getNodeFromId()));
					edge.setNodeTo(new Node(edge.getNodeToId()));
					edge = edgeService.save(edge);
					res.setObj(edge);
				} else if (profileActive().equals("postgres")) {
					PEdge pedge=new PEdge();
					pedge.setNodeFrom(new PNode(edge.getNodeFromId()));
					pedge.setNodeTo(new PNode(edge.getNodeToId()));
					pedge = pedgeService.save(pedge);
					res.setObj(pedge);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear enlace: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(path = "/saveEdgeRFC", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEdgeRFC(HttpServletRequest request, @Valid @ModelAttribute("EdgeRFC") EdgeRFC edge,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					edge.setNodeFrom(new NodeRFC(edge.getNodeFromId()));
					edge.setNodeTo(new NodeRFC(edge.getNodeToId()));
					edge = edgeService.saveEdgeRFC(edge);
					res.setObj(edge);
				} else if (profileActive().equals("postgres")) {
					PEdgeRFC pedgeRFC=new  PEdgeRFC();
					pedgeRFC.setNodeFrom(new PNodeRFC(edge.getNodeFromId()));
					pedgeRFC.setNodeTo(new PNodeRFC(edge.getNodeToId()));
					pedgeRFC = pedgeService.saveEdgeRFC(pedgeRFC);
					res.setObj(pedgeRFC);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear enlace: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(path = "/saveEdgeIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEdge(HttpServletRequest request, @Valid @ModelAttribute("EdgeIncidence") EdgeIncidence edge,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				if (profileActive().equals("oracle")) {
					edge.setNodeFrom(new NodeIncidence(edge.getNodeFromId()));
					edge.setNodeTo(new NodeIncidence(edge.getNodeToId()));
					edge = edgeService.saveEdgeIncidence(edge);
					res.setObj(edge);
				} else if (profileActive().equals("postgres")) {
					PEdgeIncidence pedgeIncidence=new  PEdgeIncidence();
					pedgeIncidence.setNodeFrom(new PNodeIncidence(edge.getNodeFromId()));
					pedgeIncidence.setNodeTo(new PNodeIncidence(edge.getNodeToId()));
					pedgeIncidence = pedgeService.saveEdgeIncidence(pedgeIncidence);
					res.setObj(pedgeIncidence);
				}
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear enlace: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteEdge/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEdge(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				edgeService.delete(id);
			} else if (profileActive().equals("postgres")) {
				pedgeService.delete(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar enlace: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar enlace: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/deleteEdgeRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEdgeRFC(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				edgeService.deleteEdgeRFC(id);
			} else if (profileActive().equals("postgres")) {
				pedgeService.deleteEdgeRFC(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar enlace: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar enlace: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteEdgeIncidence/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEdgeIncidence(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				edgeService.deleteEdgeIncidence(id);
			} else if (profileActive().equals("postgres")) {
				pedgeService.deleteEdgeIncidence(id);
			}
			
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar enlace: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar enlace: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/deleteWorkFlow/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteWorkFlowT(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				workFlowService.delete(id);
			} else if (profileActive().equals("postgres")) {
				pworkFlowService.delete(id);
			}
			
			//edgeService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar enlace: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar enlace: Existen referencias que debe eliminar antes");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	/*
	@RequestMapping(value = "/nextNode/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse nextNode(@PathVariable int id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseTrackingShow tracking = releaseService.findReleaseTracking(id);
			res.setStatus("success");
			res.setObj(tracking);
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}*/

}