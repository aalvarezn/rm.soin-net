package com.soin.sgrm.service;

import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.response.JsonSheet;

public interface RFCWithoutReleaseService extends BaseService<Long, RFC_WithoutRelease>{
	
	JsonSheet<RFC_WithoutRelease> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, int priorityId,int systemId);

	public JsonSheet<RFC_WithoutRelease> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, int priorityId, int systemId);


}