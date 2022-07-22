package com.soin.sgrm.dao;

import com.soin.sgrm.model.RequestBase;

public interface RequestBaseDao extends BaseDao<Long, RequestBase> {

	Integer existNumRequest(String numRequest); 

}
