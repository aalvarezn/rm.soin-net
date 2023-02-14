package com.soin.sgrm.service.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.wf.EdgeIncidence;
import com.soin.sgrm.model.wf.EdgeRFC;

public interface EdgeService {

	List<Edge> list();

	Edge findById(Integer id);

	Edge save(Edge edge);

	Edge update(Edge edge);

	void delete(Integer id) throws Exception;
	
	List<EdgeRFC> listEdgeRFC();

	EdgeRFC findByIdEdgeRFC(Integer id);

	EdgeRFC saveEdgeRFC(EdgeRFC edge);

	EdgeRFC updateEdgeRFC(EdgeRFC edge);

	void deleteEdgeRFC(Integer id) throws Exception;

	void deleteEdgeIncidence(Integer id);

	EdgeIncidence saveEdgeIncidence(EdgeIncidence edge);
}
