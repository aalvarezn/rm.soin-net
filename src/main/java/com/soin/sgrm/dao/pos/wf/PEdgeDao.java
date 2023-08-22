package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import com.soin.sgrm.model.pos.wf.PEdge;
import com.soin.sgrm.model.pos.wf.PEdgeIncidence;
import com.soin.sgrm.model.pos.wf.PEdgeRFC;


public interface PEdgeDao {

	List<PEdge> list();

	PEdge findById(Integer id);

	PEdge save(PEdge edge);

	PEdge update(PEdge edge);

	void delete(Integer id) throws Exception;

	List<PEdgeRFC> listEdgeRFC();

	PEdgeRFC findByIdEdgeRFC(Integer id);

	PEdgeRFC saveEdgeRFC(PEdgeRFC edge);

	PEdgeRFC updateEdgeRFC(PEdgeRFC edge);

	void deleteEdgeRFC(Integer id) throws Exception;

	List<PEdgeIncidence> listEdgeIncidence();

	PEdgeIncidence findByIdEdgeIncidence(Integer id);

	PEdgeIncidence saveEdgeIncidence(PEdgeIncidence edge);

	PEdgeIncidence updateEdgeIncidence(PEdgeIncidence edge);

	void deleteEdgeIncidence(Integer id) throws Exception;

	
}