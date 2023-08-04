package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Siges;

public interface SigesDao extends BaseDao<Long, Siges> {
	public List<Siges> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String sigesCode);

	public boolean veryUpdateSigesCode(Long id, String codeSiges);

	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

}