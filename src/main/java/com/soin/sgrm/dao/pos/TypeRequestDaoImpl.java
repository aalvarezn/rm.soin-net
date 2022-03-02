package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PTypeRequest;

@Repository("typeRequestDao")
public class TypeRequestDaoImpl extends AbstractDao<Long, PTypeRequest> implements TypeRequestDao {

}
