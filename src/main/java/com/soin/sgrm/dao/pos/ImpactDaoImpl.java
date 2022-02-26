package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PImpact;

@Repository("impactDao")
public class ImpactDaoImpl extends AbstractDao<Long, PImpact> implements ImpactDao {

}
