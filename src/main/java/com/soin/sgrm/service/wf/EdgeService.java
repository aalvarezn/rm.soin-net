package com.soin.sgrm.service.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Edge;

public interface EdgeService {

	List<Edge> list();

	Edge findById(Integer id);

	Edge save(Edge edge);

	Edge update(Edge edge);

	void delete(Integer id) throws Exception;
}
