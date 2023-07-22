package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PTypeDetail;

public interface PTypeDetailDao {
	
	List<PTypeDetail> list();
	
	PTypeDetail findByName(String name);

}
