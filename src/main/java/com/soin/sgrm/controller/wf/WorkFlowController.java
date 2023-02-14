package com.soin.sgrm.controller.wf;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
//import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.System_StatusIn;
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
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.StatusRFCService;
//import com.soin.sgrm.model.wf.WorkFlowIncidence;
//import com.soin.sgrm.service.AttentionGroupService;
//import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
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

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("workFlows", workFlowService.list());
		model.addAttribute("workFlow", new WorkFlow());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("types", typeService.list());
		model.addAttribute("system", new SystemInfo());
		return "/wf/workFlow/workFlow";
	}

	@RequestMapping(value = "workFlowEdit/{id}", method = RequestMethod.GET)
	public String workFlowEdit(HttpServletRequest request, @PathVariable Integer id, Locale locale, Model model,
			HttpSession session) {
		WorkFlow workFlow = workFlowService.findById(id);
		
		if(workFlow.getType().getId()==1) {
			model.addAttribute("workFlow", workFlow);
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("status", new Status());
			model.addAttribute("users", wfUserService.list());
			model.addAttribute("user", new WFUser());
			return "/wf/workFlow/workFlowEdit";
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
		
	}

	@RequestMapping(value = "/loadWorkFlow/{id}", method = RequestMethod.GET)
	public @ResponseBody WorkFlow findWorkFlow(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			WorkFlow workFlow = workFlowService.findById(id);
			return workFlow;
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	
	@RequestMapping(value = "/loadWorkFlowRFC/{id}", method = RequestMethod.GET)
	public @ResponseBody WorkFlowRFC findWorkFlowRFC(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			WorkFlowRFC workFlow = workFlowService.findByIdRFC(id);
			return workFlow;
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	@RequestMapping(value = "/loadWorkFlowIncidence/{id}", method = RequestMethod.GET)
	public @ResponseBody WorkFlowIncidence findWorkFlowIncidence(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			WorkFlowIncidence workFlow = workFlowService.findByIdIncidence(id);
			return workFlow;
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
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
				WorkFlow workFlow = new WorkFlow();
				workFlow.setId(node.getWorkFlowId());
				node.setSendEmail(false);
				node.setWorkFlow(workFlow);
				node = nodeService.save(node);
				res.setObj(node);
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
				WorkFlowRFC workFlow = new WorkFlowRFC();
				workFlow.setId(node.getWorkFlowId());
				node.setSendEmail(false);
				node.setWorkFlow(workFlow);
				node = nodeService.saveNodeRFC(node);
				res.setObj(node);
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
				WorkFlowIncidence workFlow = new WorkFlowIncidence();
				workFlow.setId(node.getWorkFlowId());
				node.setSendEmail(false);
				node.setWorkFlow(workFlow);
				node = nodeService.saveNodeIncidence(node);
				res.setObj(node);
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
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}else {
				if(node.getGroup().equals("start"))
				if(nodeService.verifyStartNode(node)) {
					
					res.setStatus("exception");
					res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
				}
			}
			if (res.getStatus().equals("success")) {
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
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}else {
				if(node.getGroup().equals("start"))
				if(nodeService.verifyStartNodeRFC(node)) {
					
					res.setStatus("exception");
					res.setException("Error al crear el nodo ya hay un nodo inicio para este tramite.");
				}
			}
			if (res.getStatus().equals("success")) {
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
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
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
			Node oldNode = nodeService.findById(node.getId());
			oldNode.setX(node.getX());
			oldNode.setY(node.getY());
			node = nodeService.update(oldNode);
			res.setObj(node);

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
			NodeRFC oldNode = nodeService.findByIdNoRFC(node.getId());
			oldNode.setX(node.getX());
			oldNode.setY(node.getY());
			node = nodeService.updateNodeRFC(oldNode);
			res.setObj(node);

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
			NodeIncidence oldNode = nodeService.findByIdNoInci(node.getId());
			oldNode.setX(node.getX());
			oldNode.setY(node.getY());
			node = nodeService.updateNodeIncidence(oldNode);
			res.setObj(node);

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
			nodeService.delete(id);
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
			nodeService.deleteNodeRFC(id);
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
			nodeService.deleteNodeIncidence(id);
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
				edge.setNodeFrom(new Node(edge.getNodeFromId()));
				edge.setNodeTo(new Node(edge.getNodeToId()));
				edge = edgeService.save(edge);
				res.setObj(edge);
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
				edge.setNodeFrom(new NodeRFC(edge.getNodeFromId()));
				edge.setNodeTo(new NodeRFC(edge.getNodeToId()));
				edge = edgeService.saveEdgeRFC(edge);
				res.setObj(edge);
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
				edge.setNodeFrom(new NodeIncidence(edge.getNodeFromId()));
				edge.setNodeTo(new NodeIncidence(edge.getNodeToId()));
				edge = edgeService.saveEdgeIncidence(edge);
				res.setObj(edge);
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
			edgeService.delete(id);
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
			edgeService.deleteEdgeRFC(id);
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
			edgeService.deleteEdgeIncidence(id);
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
			workFlowService.delete(id);
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

}