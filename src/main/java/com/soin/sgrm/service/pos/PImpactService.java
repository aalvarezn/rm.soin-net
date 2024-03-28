package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.pos.PImpact;

public interface PImpactService {

	List<PImpact> list();

	PImpact findById(Integer id);

	void save(PImpact impact);

	void update(PImpact impact);

	void delete(Integer id);

}
