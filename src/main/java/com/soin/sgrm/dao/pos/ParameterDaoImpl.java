package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PParameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends AbstractDao<Long, PParameter> implements ParameterDao {

	
	@Override
	public PParameter getParameterByCode(Long code){
    		return  (PParameter) getSession().createCriteria(PParameter.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
}
