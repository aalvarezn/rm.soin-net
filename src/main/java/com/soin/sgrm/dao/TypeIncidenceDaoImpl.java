package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypeIncidence;

@Repository
public class TypeIncidenceDaoImpl extends AbstractDao<Long, TypeIncidence> implements TypeIncidenceDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypeIncidence> listTypePetition(){
	    return getSession().createCriteria(TypeIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
