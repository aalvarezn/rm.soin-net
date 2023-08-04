package com.soin.sgrm.dao;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
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


	public JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, int systemId, Long sigesId) throws ParseException;

	public JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long sigesId) throws ParseException;

	public List<RFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException;

	public RFCReportComplete findByIdRFCReport(Long id);

}

