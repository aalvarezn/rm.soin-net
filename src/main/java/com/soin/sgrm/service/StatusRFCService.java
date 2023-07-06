package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.StatusRFC;

public interface StatusRFCService extends BaseService<Long, StatusRFC>{

	List<StatusRFC> findWithFilter();

}
