package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.pos.PRisk;

public interface PRiskService {

	List<PRisk> list();
	
	PRisk findById(Integer id);

	void save(PRisk risk);

	void update(PRisk risk);

	void delete(Integer id);
}
