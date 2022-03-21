package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;

public interface SystemService extends BaseService<Long, PSystem> {

	JsonSheet<PSystem> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String sProject);
	
	List<PSystem> listWithProject();
}
