package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.pos.PImpact;

public interface PImpactDao {
	
	List<PImpact> list();
	
	PImpact findById(Integer id);

	void save(PImpact impact);

	void update(PImpact impact);

	void delete(Integer id);

}
