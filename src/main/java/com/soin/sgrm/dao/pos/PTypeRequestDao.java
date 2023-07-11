package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PTypeRequest;

public interface PTypeRequestDao {

	List<PTypeRequest> list();

	PTypeRequest findById(Integer id);

	PTypeRequest save(PTypeRequest typeRequest);

	void update(PTypeRequest typeRequest);

	void delete(Integer id);

}
