package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PUser;

public interface UserDao extends BaseDao<Long, PUser> {

	List<PUser> findAllColumns(String[] columns);

	List<PUser> findbyUserName(String[] userNames);

}
