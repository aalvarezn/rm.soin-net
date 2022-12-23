package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;

public interface NodeDao {

	List<Node> list();

	Node findById(Integer id);

	Node save(Node node);

	Node update(Node node);

	void delete(Integer id) throws Exception;
	
	Node existWorkFlow(Release release);

	boolean verifyStartNode(Node node);
}
