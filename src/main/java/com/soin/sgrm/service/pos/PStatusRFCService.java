package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.pos.PStatusRFC;

public interface PStatusRFCService extends BaseService<Long, PStatusRFC>{

	List<PStatusRFC> findWithFilter();

}
