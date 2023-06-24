package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PSiges;

@Repository
public class PSigesDaoImpl extends  AbstractDao<Long, PSiges> implements PSigesDao{

	@SuppressWarnings("unchecked")
	public List<PSiges> listCodeSiges(Integer id){
    return getSession().createCriteria(PSiges.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}



}
