package com.soin.sgrm.service;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingShow;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.response.JsonSheet;

public interface RequestBaseR1Service extends BaseService<Long, RequestBaseR1>{

	JsonSheet<RequestBaseR1> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);


	JsonSheet<RequestBaseR1> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 findByR1(Long id);


	@SuppressWarnings("rawtypes")
	com.soin.sgrm.utils.JsonSheet findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, Integer systemId, Long typePetitionId) throws ParseException;


	@SuppressWarnings("rawtypes")
	com.soin.sgrm.utils.JsonSheet findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException;


	List<RequestReport> listRequestReportFilter(int projectId, int systemId, Long typePetitionId, String dateRange) throws ParseException;


	RequestBaseTrackingShow findRequestTracking(Long id);

}
