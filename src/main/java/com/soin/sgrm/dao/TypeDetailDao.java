package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypeDetail;

public interface TypeDetailDao {
	
	List<TypeDetail> list();
	
	TypeDetail findByName(String name);

}
