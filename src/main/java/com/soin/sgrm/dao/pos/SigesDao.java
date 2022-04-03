package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSiges;

public interface SigesDao extends BaseDao<Long, PSiges>{
	public List<PSiges> listCodeSiges(Long id);
}
