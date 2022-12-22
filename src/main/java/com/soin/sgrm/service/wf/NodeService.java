package com.soin.sgrm.service.wf;

import java.util.List;

//import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;
//import com.soin.sgrm.model.wf.NodeIncidence;

public interface NodeService {

	List<Node> list();

	Node findById(Integer id);

	Node save(Node node);
	
	Node update(Node node);

	void delete(Integer id) throws Exception;
	
	Node existWorkFlow(Release release);
	
	boolean verifyStartNode(Node node);

	//List<NodeIncidence> listNodeIncidence();
	
	//NodeIncidence findByIdNoInci(Integer id);
	
	//NodeIncidence saveNodeIncidence(NodeIncidence node);
	
	//NodeIncidence updateNodeIncidence(NodeIncidence node);

	//void deleteNodeIncidence(Integer id) throws Exception;
	
	//NodeIncidence existWorkFlowNodeIn(Incidence incidence);


	//boolean verifyStartNodeIncidence(NodeIncidence node);

}