package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PSystem;

@Repository("sigesDao")
public class SigesDaoImpl extends AbstractDao<Long, PSiges> implements SigesDao{

	@SuppressWarnings("unchecked")
	public List<PSiges> listCodeSiges(Long id){
    return getSession().createCriteria(PSiges.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}
}
