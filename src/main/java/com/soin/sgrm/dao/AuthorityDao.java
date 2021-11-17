package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Authority;

public interface AuthorityDao {
	
	List<Authority> list();
	
	Authority findById(Integer id);

}
