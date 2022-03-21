package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PEnvironment;

@Repository("environmentDao")
public class EnvironmentDaoImpl extends AbstractDao<Long, PEnvironment> implements EnvironmentDao{

}
