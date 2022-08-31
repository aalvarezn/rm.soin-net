package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.RequestRM_P1_R4;

public interface RequestRM_P1_R4Dao extends BaseDao<Long, RequestRM_P1_R4>{

	List<RequestRM_P1_R4> listRequestRm4(Long id);

}
