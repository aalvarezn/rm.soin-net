package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PUser;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Long, PUser> implements UserDao {

}
