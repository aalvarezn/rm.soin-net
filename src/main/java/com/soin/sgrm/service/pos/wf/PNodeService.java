package com.soin.sgrm.service.pos.wf;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.pos.wf.PNodeRFC;
import com.soin.sgrm.model.pos.wf.PNodeIncidence;

public interface PNodeService {

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

	List<PNodeIncidence> listNodeIncidence();
	
	PNodeIncidence findByIdNoInci(Integer id);
	
	PNodeIncidence saveNodeIncidence(PNodeIncidence node);
	
	PNodeIncidence updateNodeIncidence(PNodeIncidence node);

	void deleteNodeIncidence(Integer id) throws Exception;
	
	PNodeIncidence existWorkFlowNodeIn(PIncidence incidence);


	boolean verifyStartNodeIncidence(PNodeIncidence node);

	

}