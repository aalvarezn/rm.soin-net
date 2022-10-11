package com.soin.sgrm.dao;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;

public interface RequestBaseR1Dao extends BaseDao<Long, RequestBaseR1> {

	
	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 getByIdR1(Long id); 

}
