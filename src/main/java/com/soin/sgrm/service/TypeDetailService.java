package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypeDetail;

public interface TypeDetailService {
	
	List<TypeDetail> list();
	
	TypeDetail findByName(String name);

}
