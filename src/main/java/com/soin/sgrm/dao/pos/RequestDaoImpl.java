package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequest;

@Repository("requestDao")
public class RequestDaoImpl extends AbstractDao<Long, PRequest> implements RequestDao {

}
