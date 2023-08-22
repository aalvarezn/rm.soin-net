package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PRFCError;
import com.soin.sgrm.response.JsonSheet;

public interface PRFCErrorService extends BaseService<Long, PRFCError>{

	JsonSheet<PRFCError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long sigesId, int systemId);

	List<PRFCError> findAllList(Long errorId, String dateRange, Long sigesId, int systemId);

	JsonSheet<PRFCError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long sigesId, List<Integer> systemsId);

}