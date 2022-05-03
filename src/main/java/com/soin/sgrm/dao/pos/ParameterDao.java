package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PParameter;

public interface ParameterDao extends BaseDao<Long, PParameter> {

	PParameter getParameterByCode(Long code);

}
