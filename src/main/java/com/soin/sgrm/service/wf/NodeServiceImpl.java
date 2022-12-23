package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.NodeDao;
//import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;
//import com.soin.sgrm.model.wf.NodeIncidence;

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
/*
	@Override
	public NodeIncidence saveNodeIncidence(NodeIncidence node) {
		
		return dao.saveNodeIncidence(node);
	}

	@Override
	public List<NodeIncidence> listNodeIncidence() {
		return dao.listNodeIncidence();
	}

	@Override
	public NodeIncidence findByIdNoInci(Integer id) {
		return dao.findByIdNoInci(id);
	}

	@Override
	public NodeIncidence updateNodeIncidence(NodeIncidence node) {
		return dao.updateNodeIncidence(node);
	}

	@Override
	public void deleteNodeIncidence(Integer id) throws Exception {
		dao.deleteNodeIncidence(id);
	}

	@Override
	public NodeIncidence existWorkFlowNodeIn(Incidence incidence) {
		return dao.existWorkFlowNodeIn(incidence);
	}
		@Override
	public boolean verifyStartNodeIncidence(NodeIncidence node) {
		// TODO Auto-generated method stub
		return dao.verifyStartNodeIncidence(node);
	}
*/
	@Override
	public boolean verifyStartNode(Node node) {
		
		return dao.verifyStartNode(node);
	}



}