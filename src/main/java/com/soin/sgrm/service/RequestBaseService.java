package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingToError;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.response.JsonSheet;

public interface RequestBaseService extends BaseService<Long, RequestBase>{

	JsonSheet<RequestBase> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);

	String generateRequestNumber(String codeProyect,String description,String codeSystem);

	JsonSheet<RequestBase> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 findByR1(Long id);

	List<RequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	RequestReport findByReport(Long id);

}