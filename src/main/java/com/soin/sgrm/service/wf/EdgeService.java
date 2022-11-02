package com.soin.sgrm.service.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.wf.EdgeIncidence;

public interface EdgeService {

	List<Edge> list();

	Edge findById(Integer id);

	Edge save(Edge edge);

	Edge update(Edge edge);

	void delete(Integer id) throws Exception;
	
	List<EdgeIncidence> listEdgeIncidence();

	EdgeIncidence findByIdEdgeIncidence(Integer id);

	EdgeIncidence saveEdgeIncidence(EdgeIncidence edge);

	EdgeIncidence updateEdgeIncidence(EdgeIncidence edge);

	void deleteEdgeIncidence(Integer id) throws Exception;
}
