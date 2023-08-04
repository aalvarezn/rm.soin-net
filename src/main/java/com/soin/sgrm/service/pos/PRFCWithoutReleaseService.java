package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.response.JsonSheet;

public interface PRFCWithoutReleaseService extends BaseService<Long, PRFC_WithoutRelease>{
	
	JsonSheet<PRFC_WithoutRelease> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, int priorityId,int systemId);

	public JsonSheet<PRFC_WithoutRelease> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, int priorityId, int systemId);


}