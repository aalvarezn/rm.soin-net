package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PSystem;

@Repository("systemDao")
public class SystemDaoImpl extends AbstractDao<Long, PSystem> implements SystemDao {

}
