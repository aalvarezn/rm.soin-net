package com.soin.sgrm.service.pos.wf;

import java.util.List;

import com.soin.sgrm.model.pos.wf.PWorkFlow;
import com.soin.sgrm.model.pos.wf.PWorkFlowIncidence;
import com.soin.sgrm.model.pos.wf.PWorkFlowRFC;

public interface PWorkFlowService {

	List<PWorkFlow> list();

	PWorkFlow findById(Integer id);

	void save(PWorkFlow workFlow);

	void update(PWorkFlow workFlow);

	void delete(Integer id);

	PWorkFlowIncidence findByIdIncidence(Integer id);

	boolean verifyCreation(Integer systemId, Integer typeId);

	PWorkFlowRFC findByIdRFC(Integer id);

}