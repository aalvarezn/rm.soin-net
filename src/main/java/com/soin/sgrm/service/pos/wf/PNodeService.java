package com.soin.sgrm.service.pos.wf;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.wf.NodeIncidence;
//import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.NodeRFC;

public interface PNodeService {

	List<PNode> list();

	PNode findById(Integer id);

	PNode save(PNode node);
	
	PNode update(PNode node);

	void delete(Integer id) throws Exception;
	
	PNode existWorkFlow(PRelease release);
	
	boolean verifyStartNode(PNode node);

	NodeRFC saveNodeRFC(NodeRFC node);
	
	NodeRFC updateNodeRFC(NodeRFC node);
	
	List<NodeRFC> listNodeRFC();
	
	NodeRFC findByIdNoRFC(Integer id);
	
	void deleteNodeRFC(Integer id) throws Exception;
	
	NodeRFC existWorkFlowNodeRFC(RFC rfc);
	
	boolean verifyStartNodeRFC(NodeRFC node);

	List<NodeIncidence> listNodeIncidence();
	
	NodeIncidence findByIdNoInci(Integer id);
	
	NodeIncidence saveNodeIncidence(NodeIncidence node);
	
	NodeIncidence updateNodeIncidence(NodeIncidence node);

	void deleteNodeIncidence(Integer id) throws Exception;
	
	NodeIncidence existWorkFlowNodeIn(Incidence incidence);


	boolean verifyStartNodeIncidence(NodeIncidence node);

}