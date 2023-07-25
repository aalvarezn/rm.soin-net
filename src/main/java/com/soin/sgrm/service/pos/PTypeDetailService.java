package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.model.pos.PTypeDetail;

public interface PTypeDetailService {
	
	List<PTypeDetail> list();
	
	PTypeDetail findByName(String name);

}
