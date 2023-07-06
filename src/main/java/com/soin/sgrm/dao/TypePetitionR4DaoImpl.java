package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;

@Repository
public class TypePetitionR4DaoImpl extends AbstractDao<Long, TypePetitionR4> implements TypePetitionR4Dao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TypePetitionR4> listTypePetition(){
	    return getSession().createCriteria(TypePetitionR4.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
