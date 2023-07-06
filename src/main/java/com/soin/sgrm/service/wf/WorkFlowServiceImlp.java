package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.WorkFlowDao;
import com.soin.sgrm.model.wf.WorkFlow;
import com.soin.sgrm.model.wf.WorkFlowIncidence;
import com.soin.sgrm.model.wf.WorkFlowRFC;

@Transactional("transactionManager")
@Service("WorkFlowService")
public class WorkFlowServiceImlp implements WorkFlowService {

	@Autowired
	WorkFlowDao dao;

	@Override
	public List<WorkFlow> list() {
		return dao.list();
	}

	@Override
	public WorkFlow findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(WorkFlow workFlow) {
		dao.save(workFlow);
	}

	@Override
	public void update(WorkFlow workFlow) {
		dao.update(workFlow);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public boolean verifyCreation(Integer systemId, Integer typeId) {
		return dao.verifyCreation(systemId,typeId);
	}

	@Override
	public WorkFlowRFC findByIdRFC(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByIdRFC(id);
	}

	@Override
	public WorkFlowIncidence findByIdIncidence(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByIdIncidence(id);
	}

}