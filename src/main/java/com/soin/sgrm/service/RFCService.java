package com.soin.sgrm.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.response.JsonSheet;

public interface RFCService extends BaseService<Long, RFC>{
	public Integer existNumRFC(String number_release) throws SQLException;
	
	String generateRFCNumber(String codeProject, String codeSystem);
	JsonSheet<RFC> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, int priorityId,int systemId);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	public JsonSheet<RFC> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, int priorityId, int systemId);

	Integer countByManager(Integer id, Long idRFC);

	public RFC_WithoutRelease findRfcWithRelease(Long id);

	public List<RFCTrackingToError> listByAllSystemError(String dateRange, int systemId);

	public RFCTrackingShow findRFCTracking(Long id);

	public com.soin.sgrm.utils.JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, String dateRange, int systemId, Long sigesId) throws ParseException;

	public com.soin.sgrm.utils.JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, String dateRange, List<Integer> systemsId,Long sigesId) throws ParseException;

	public List<RFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException;

	public RFCReportComplete findByIdRFCReport(Long id);


}