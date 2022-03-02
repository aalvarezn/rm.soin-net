package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PAuthority;

public interface AuthorityDao extends BaseDao<Long, PAuthority> {
	
	List<PAuthority> findByCode(String[] roles);

}
