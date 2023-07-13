package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PStatusRequest;

public interface PStatusRequestService extends BaseService<Long, PStatusRequest>{

	List<PStatusRequest> findWithFilter();

}
