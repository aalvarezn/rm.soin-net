package com.soin.sgrm.dao.pos;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCReport;
import com.soin.sgrm.model.pos.PRFCReportComplete;
import com.soin.sgrm.model.pos.PRFCTrackingShow;
import com.soin.sgrm.model.pos.PRFCTrackingToError;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface PRFCDao extends BaseDao<Long, PRFC>{
	public Integer existNumRFC(String number_release);
	
	void updateStatusRFC(PRFC rfc, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);

	Integer countByManager(Integer id, Long idRFC);

	public PRFC_WithoutRelease findRfcWithRelease(Long id);

	public List<PRFCTrackingToError> listByAllSystemError(String dateRange, int systemId);

	public PRFCTrackingShow findRFCTracking(Long id);


	public JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, int systemId, Long sigesId) throws ParseException;

	public JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long sigesId) throws ParseException;

	public List<PRFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException;

	public PRFCReportComplete findByIdRFCReport(Long id);
}
