package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Edge;

public interface EdgeDao {

	List<Edge> list();

	Edge findById(Integer id);

	Edge save(Edge edge);

	Edge update(Edge edge);

	void delete(Integer id) throws Exception;
}
