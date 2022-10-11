package com.soin.sgrm.service;

import com.soin.sgrm.model.Errors;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.response.JsonSheet;

public interface ReleaseErrorService extends BaseService<Long, ReleaseError>{

	JsonSheet<ReleaseError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, int projectId, int systemId);

}
