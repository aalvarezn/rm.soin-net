package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PRequestRM_P1_R4;



public interface PRequestRM_P1_R4Dao extends BaseDao<Long, PRequestRM_P1_R4>{

	List<PRequestRM_P1_R4> listRequestRm4(Long id);

}
