package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Siges;

@Repository
public class SigesDaoImpl extends  AbstractDao<Long, Siges> implements SigesDao{

	@SuppressWarnings("unchecked")
	public List<Siges> listCodeSiges(Integer id){
    return getSession().createCriteria(Siges.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}



}
