package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.pos.PGDoc;

public interface PGDocService {
	
	List<PGDoc> list();

	PGDoc findById(Integer id);

	void save(PGDoc gDoc);

	void update(PGDoc gDoc);

	void delete(Integer id);

}