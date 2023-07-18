package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.StatusRequest;

public interface StatusRequestService extends BaseService<Long, StatusRequest>{

	List<StatusRequest> findWithFilter();

}
