package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Siges;

public interface SigesService extends BaseService<Long, Siges>{
	public List<Siges> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String sigesCode);

	boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

	boolean veryUpdateSigesCode(Long id, String codeSiges);


}
