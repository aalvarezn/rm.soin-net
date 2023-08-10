package com.soin.sgrm.service.pos.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PEdgeDao;
import com.soin.sgrm.model.pos.wf.PEdge;
import com.soin.sgrm.model.pos.wf.PEdgeIncidence;
import com.soin.sgrm.model.pos.wf.PEdgeRFC;

@Transactional("transactionManagerPos")
@Service("PEdgeService")
public class PEdgeServiceImpl implements PEdgeService {

	@Autowired
	PEdgeDao dao;

	@Override
	public List<PEdge> list() {
		return dao.list();
	}

	@Override
	public PEdge findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PEdge save(PEdge edge) {
		return dao.save(edge);
	}

	@Override
	public PEdge update(PEdge edge) {
		return dao.update(edge);
	}

	@Override
	public void delete(Integer id) throws Exception {
		dao.delete(id);
	}

	@Override
	public List<PEdgeRFC> listEdgeRFC() {
		
		return dao.listEdgeRFC();
	}

	@Override
	public PEdgeRFC findByIdEdgeRFC(Integer id) {
		
		return dao.findByIdEdgeRFC(id);
	}

	@Override
	public PEdgeRFC saveEdgeRFC(PEdgeRFC edge) {
		
		return dao.saveEdgeRFC(edge);
	}

	@Override
	public PEdgeRFC updateEdgeRFC(PEdgeRFC edge) {
		
		return dao.updateEdgeRFC(edge);
	}

	@Override
	public void deleteEdgeRFC(Integer id) throws Exception {
		dao.deleteEdgeRFC(id);
		
	}

	@Override
	public List<PEdgeIncidence> listEdgeIncidence() {
		return dao.listEdgeIncidence();
	}

	@Override
	public PEdgeIncidence findByIdEdgeIncidence(Integer id) {
		return dao.findByIdEdgeIncidence(id);
	}

	@Override
	public PEdgeIncidence saveEdgeIncidence(PEdgeIncidence edge) {
		return dao.saveEdgeIncidence(edge);
	}

	@Override
	public PEdgeIncidence updateEdgeIncidence(PEdgeIncidence edge) {
		return dao.updateEdgeIncidence(edge);
	}

	@Override
	public void deleteEdgeIncidence(Integer id) throws Exception {
		dao.deleteEdgeIncidence(id);
	}

}