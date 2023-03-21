package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.wf.WorkFlow;
import com.soin.sgrm.model.wf.WorkFlowIncidence;
import com.soin.sgrm.model.wf.WorkFlowRFC;

public interface WorkFlowDao {

	List<WorkFlow> list();

	WorkFlow findById(Integer id);

	void save(WorkFlow workFlow);

	void update(WorkFlow workFlow);

	void delete(Integer id);

	boolean verifyCreation(Integer systemId, Integer typeId);

	WorkFlowRFC findByIdRFC(Integer id);

	WorkFlowIncidence findByIdIncidence(Integer id);

}