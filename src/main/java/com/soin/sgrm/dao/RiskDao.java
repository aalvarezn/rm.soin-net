package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Risk;

public interface RiskDao {
	
	List<Risk> list();
	
	Risk findById(Integer id);

	void save(Risk risk);

	void update(Risk risk);

	void delete(Integer id);

}
