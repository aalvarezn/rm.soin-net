package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.pos.PSiges;

public interface PSigesDao extends BaseDao<Long, PSiges> {
	public List<PSiges> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String codeSiges);

	boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

	boolean veryUpdateSigesCode(Long id, String codeSiges);

}
