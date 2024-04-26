package com.soin.sgrm.service.pos;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingShow;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.response.JsonSheet;

public interface PRequestBaseR1Service extends BaseService<Long, PRequestBaseR1>{

	JsonSheet<PRequestBaseR1> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);


	JsonSheet<PRequestBaseR1> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	PRequestBaseR1 findByR1(Long id);


	@SuppressWarnings("rawtypes")
	com.soin.sgrm.utils.JsonSheet findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, Integer systemId, Long typePetitionId) throws ParseException;


	@SuppressWarnings("rawtypes")
	com.soin.sgrm.utils.JsonSheet findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException;


	List<PRequestReport> listRequestReportFilter(int projectId, int systemId, Long typePetitionId, String dateRange) throws ParseException;


	PRequestBaseTrackingShow findRequestTracking(Long id);

}
