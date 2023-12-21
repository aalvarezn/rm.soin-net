package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.NodeName;
import com.soin.sgrm.model.wf.NodeRFC;

public interface NodeDao {

	List<Node> list();

	Node findById(Integer id);

	Node save(Node node);

	Node update(Node node);

	void delete(Integer id) throws Exception;
	
	Node existWorkFlow(Release release);

	boolean verifyStartNode(Node node);

	NodeRFC saveNodeRFC(NodeRFC node);

	NodeRFC updateNodeRFC(NodeRFC node);

	List<NodeRFC> listNodeRFC();

	NodeRFC findByIdNoRFC(Integer id);

	void deleteNodeRFC(Integer id) throws Exception;

	NodeRFC existWorkFlowNodeRFC(RFC rfc);

	boolean verifyStartNodeRFC(NodeRFC node);

	NodeIncidence saveNodeIncidence(NodeIncidence node);

	List<NodeIncidence> listNodeIncidence();

	NodeIncidence findByIdNoInci(Integer id);

	NodeIncidence updateNodeIncidence(NodeIncidence node);

	void deleteNodeIncidence(Integer id);

	boolean verifyStartNodeIncidence(NodeIncidence node);

	NodeIncidence existWorkFlowNodeIn(Incidence incidence);

	List<NodeName> listNodeNames(Integer i);
}