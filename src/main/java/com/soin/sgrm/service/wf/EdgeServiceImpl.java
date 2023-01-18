package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.EdgeDao;
import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.wf.EdgeRFC;

@Transactional("transactionManager")
@Service("EdgeService")
public class EdgeServiceImpl implements EdgeService {

	@Autowired
	EdgeDao dao;

	@Override
	public List<Edge> list() {
		return dao.list();
	}

	@Override
	public Edge findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Edge save(Edge edge) {
		return dao.save(edge);
	}

	@Override
	public Edge update(Edge edge) {
		return dao.update(edge);
	}

	@Override
	public void delete(Integer id) throws Exception {
		dao.delete(id);
	}

	@Override
	public List<EdgeRFC> listEdgeRFC() {
		
		return dao.listEdgeRFC();
	}

	@Override
	public EdgeRFC findByIdEdgeRFC(Integer id) {
		
		return dao.findByIdEdgeRFC(id);
	}

	@Override
	public EdgeRFC saveEdgeRFC(EdgeRFC edge) {
		
		return dao.saveEdgeRFC(edge);
	}

	@Override
	public EdgeRFC updateEdgeRFC(EdgeRFC edge) {
		
		return dao.updateEdgeRFC(edge);
	}

	@Override
	public void deleteEdgeRFC(Integer id) throws Exception {
		dao.deleteEdgeRFC(id);
		
	}

}
