package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypePetition;

@Repository
public class TypePetitionDaoImpl extends AbstractDao<Long, TypePetition> implements TypePetitionDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypePetition> listTypePetition(){
	    return getSession().createCriteria(TypePetition.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
