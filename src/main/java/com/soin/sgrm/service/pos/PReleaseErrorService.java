package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PReleaseError;
import com.soin.sgrm.response.JsonSheet;

public interface PReleaseErrorService extends BaseService<Long, PReleaseError>{

	JsonSheet<PReleaseError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, int projectId, int systemId);

	List<PReleaseError> findAllList(Long errorId, String dateRange, int projectId, int systemId);

}
