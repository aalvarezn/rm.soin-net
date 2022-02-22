package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.Risk;

public interface AuthorityDao {
	
	List<Authority> list();
	
	Authority findById(Integer id);
	
	void save(Authority authority);

	void update(Authority authority);

	void delete(Integer id);

}
