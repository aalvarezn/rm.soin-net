package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PAuthority;

public interface PAuthorityDao {
	
	List<PAuthority> list();
	
	PAuthority findById(Integer id);
	
	void save(PAuthority authority);

	void update(PAuthority authority);

	void delete(Integer id);

}
