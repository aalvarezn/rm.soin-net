package com.soin.sgrm.dao;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface RFCDao extends BaseDao<Long, RFC>{
	public Integer existNumRFC(String number_release);
	
	void updateStatusRFC(RFC rfc, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);

	Integer countByManager(Integer id, Long idRFC);

	public RFC_WithoutRelease findRfcWithRelease(Long id);

	public List<RFCTrackingToError> listByAllSystemError(String dateRange, int systemId);

	public RFCTrackingShow findRFCTracking(Long id);

	public JsonSheet<?> findAllReportRFC(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, int priorityId, int systemId) throws ParseException;
}
