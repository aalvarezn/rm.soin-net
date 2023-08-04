package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Siges;

public interface SigesService extends BaseService<Long, Siges>{
	public List<Siges> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String sigesCode);

	public boolean veryUpdateSigesCode(Long id, String codeSiges);

	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges);


}