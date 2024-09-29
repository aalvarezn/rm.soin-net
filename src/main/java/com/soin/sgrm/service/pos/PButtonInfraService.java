package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PButtonInfra;

public interface PButtonInfraService extends BaseService<Long, PButtonInfra>{
	public List<PButtonInfra> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String codeSiges);

	boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

	boolean veryUpdateSigesCode(Long id, String codeSiges);

	public boolean existsBySystemId(Integer systemId);

	public boolean existsBySystemIdandId(Long sId, Integer systemId);


}
