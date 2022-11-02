package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;

public interface NodeDao {

	List<Node> list();

	Node findById(Integer id);

	Node save(Node node);

	Node update(Node node);

	void delete(Integer id) throws Exception;
	
	Node existWorkFlow(Release release);

	NodeIncidence saveNodeIncidence(NodeIncidence node);

	NodeIncidence existWorkFlowNodeIn(Incidence incidence);

	void deleteNodeIncidence(Integer id) throws Exception;

	NodeIncidence updateNodeIncidence(NodeIncidence node);

	NodeIncidence findByIdNoInci(Integer id);

	List<NodeIncidence> listNodeIncidence();
}
