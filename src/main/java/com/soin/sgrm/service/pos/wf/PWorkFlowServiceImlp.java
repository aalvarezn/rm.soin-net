package com.soin.sgrm.service.pos.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PWorkFlowDao;
import com.soin.sgrm.dao.wf.WorkFlowDao;
import com.soin.sgrm.model.pos.wf.PWorkFlow;
import com.soin.sgrm.model.pos.wf.PWorkFlowIncidence;
import com.soin.sgrm.model.pos.wf.PWorkFlowRFC;


@Transactional("transactionManagerPos")
@Service("PWorkFlowService")
public class PWorkFlowServiceImlp implements PWorkFlowService {

	@Autowired
	PWorkFlowDao dao;

	@Override
	public List<PWorkFlow> list() {
		return dao.list();
	}

	@Override
	public PWorkFlow findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PWorkFlow workFlow) {
		dao.save(workFlow);
	}

	@Override
	public void update(PWorkFlow workFlow) {
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
	public PWorkFlowRFC findByIdRFC(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByIdRFC(id);
	}

	@Override
	public PWorkFlowIncidence findByIdIncidence(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByIdIncidence(id);
	}

}