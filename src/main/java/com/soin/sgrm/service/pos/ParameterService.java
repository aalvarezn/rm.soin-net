package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PParameter;

public interface ParameterService extends BaseService<Long, PParameter> {

	PParameter getParameterByCode(Long code);

}
