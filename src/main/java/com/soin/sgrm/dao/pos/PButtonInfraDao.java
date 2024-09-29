package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PSiges;

public interface PButtonInfraDao extends BaseDao<Long, PButtonInfra> {
	public List<PButtonInfra> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String codeSiges);

	boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

	boolean veryUpdateSigesCode(Long id, String codeSiges);

	public boolean existsBySystemId(Integer systemId);

	public boolean existsBySystemIdandId(Long sId, Integer systemId);

}
