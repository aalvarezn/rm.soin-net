package com.soin.sgrm.dao.pos;

import java.text.ParseException;
import java.util.List;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingShow;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.utils.JsonSheet;

public interface PRequestBaseR1Dao extends BaseDao<Long, PRequestBaseR1> {

	
	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	PRequestBaseR1 getByIdR1(Long id);

	JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId, Long typePetitionId) throws ParseException;

	JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException;

	List<PRequestReport> listRequestReportFilter(int projectId, int systemId, Long typePetitionId, String dateRange) throws ParseException;
	
	PRequestBaseTrackingShow findRequestTracking(Long id); 

}
