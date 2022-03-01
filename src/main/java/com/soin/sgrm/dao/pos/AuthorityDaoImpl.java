package com.soin.sgrm.dao.pos;


import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAuthority;

@Repository("authorityDao")
public class AuthorityDaoImpl extends AbstractDao<Long, PAuthority> implements AuthorityDao {


}
