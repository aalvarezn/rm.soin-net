package com.soin.sgrm.dao;

import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.utils.JsonSheet;

public interface RequestBaseR1Dao extends BaseDao<Long, RequestBaseR1> {

	
	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 getByIdR1(Long id);

	JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId, Long typePetitionId) throws ParseException;

	JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException;

	List<RequestReport> listRequestReportFilter(int projectId, int systemId, Long typePetitionId, String dateRange) throws ParseException; 

}
