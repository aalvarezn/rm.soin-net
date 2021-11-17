package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Impact;

public interface ImpactService {

	List<Impact> list();

	Impact findById(Integer id);

	void save(Impact impact);

	void update(Impact impact);

	void delete(Integer id);

}
