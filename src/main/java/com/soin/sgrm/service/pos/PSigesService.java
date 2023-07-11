package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSiges;

public interface PSigesService extends BaseService<Long, PSiges>{
	public List<PSiges> listCodeSiges(Integer id);


}
