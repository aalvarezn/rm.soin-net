package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PParameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends AbstractDao<Long, PParameter> implements ParameterDao {

}
