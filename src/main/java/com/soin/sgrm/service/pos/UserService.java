package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;

public interface UserService extends BaseService<Long, PUser> {

	JsonSheet<PUser> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Integer sStatus, String dateRange);

}
