package com.soin.sgrm.service.pos;

import java.util.List;



import com.soin.sgrm.model.pos.PRequestBase;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.response.JsonSheet;

public interface PRequestBaseService extends BaseService<Long, PRequestBase>{

	JsonSheet<PRequestBase> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);

	String generateRequestNumber(String codeProyect,String description,String codeSystem);

	JsonSheet<PRequestBase> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	PRequestBaseR1 findByR1(Long id);

	List<PRequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	PRequestReport findByReport(Long id);

}
