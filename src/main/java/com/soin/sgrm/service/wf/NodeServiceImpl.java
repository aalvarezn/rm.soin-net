package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.NodeDao;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;

@Transactional("transactionManager")
@Service("NodeService")
public class NodeServiceImpl implements NodeService {
	
	@Autowired
	NodeDao dao;

	@Override
	public List<Node> list() {
		return dao.list();
	}

	@Override
	public Node findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Node save(Node node) {
		return dao.save(node);
	}

	@Override
	public Node update(Node node) {
		return dao.update(node);
	}

	@Override
	public void delete(Integer id) throws Exception {
		dao.delete(id);
	}

	@Override
	public Node existWorkFlow(Release release) {
		return dao.existWorkFlow(release);
	}

}
