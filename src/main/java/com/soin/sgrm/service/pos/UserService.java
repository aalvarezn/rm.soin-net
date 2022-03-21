package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;

public interface UserService extends BaseService<Long, PUser> {

	JsonSheet<PUser> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Integer sStatus, String dateRange, PUser userLogin);
	
	List<PUser> findAllColumns(String[] columns);
	
	List<PUser> findbyUserName(String[] userNames);

}
