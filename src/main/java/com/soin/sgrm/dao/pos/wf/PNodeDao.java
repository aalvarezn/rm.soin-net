package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.pos.wf.PNodeIncidence;
import com.soin.sgrm.model.pos.wf.PNodeRFC;
import com.soin.sgrm.model.wf.NodeName;

public interface PNodeDao {

	List<PNode> list();

	PNode findById(Integer id);

	PNode save(PNode node);

	PNode update(PNode node);

	void delete(Integer id) throws Exception;
	
	PNode existWorkFlow(PRelease release);

	boolean verifyStartNode(PNode node);

	PNodeRFC saveNodeRFC(PNodeRFC node);

	PNodeRFC updateNodeRFC(PNodeRFC node);

	List<PNodeRFC> listNodeRFC();

	PNodeRFC findByIdNoRFC(Integer id);

	void deleteNodeRFC(Integer id) throws Exception;

	PNodeRFC existWorkFlowNodeRFC(PRFC rfc);

	boolean verifyStartNodeRFC(PNodeRFC node);

	PNodeIncidence saveNodeIncidence(PNodeIncidence node);

	List<PNodeIncidence> listNodeIncidence();

	PNodeIncidence findByIdNoInci(Integer id);

	PNodeIncidence updateNodeIncidence(PNodeIncidence node);

	void deleteNodeIncidence(Integer id);

	boolean verifyStartNodeIncidence(PNodeIncidence node);

	PNodeIncidence existWorkFlowNodeIn(PIncidence incidence);

	PNode findByIdAndWorkFlow(String nodeName, Integer id);

	List<NodeName> listNodeNames(Integer id, Integer userLogin);
}