package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;

public interface PButtonInfraService extends BaseService<Long, PButtonInfra>{
	public List<PButtonInfra> listCodeSiges(Integer id);

	public boolean checkUniqueCode(String codeSiges);

	boolean veryUpdateSigesCodeDif(Long id, String codeSiges);

	boolean veryUpdateSigesCode(Long id, String codeSiges);

	public boolean existsBySystemId(Integer systemId);

	public boolean existsBySystemIdandId(Long sId, Integer systemId);

	public JsonSheet<?> findAllButton(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId, List<PSystem> systems);


}
