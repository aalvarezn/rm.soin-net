package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Siges;

public interface SigesService extends BaseService<Long, Siges>{
	public List<Siges> listCodeSiges(Long id);
}
