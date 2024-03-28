package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PTypePetition;

@Repository
public class PTypePetitionDaoImpl extends AbstractDao<Long, PTypePetition> implements PTypePetitionDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PTypePetition> listTypePetition(){
	    return getSession().createCriteria(PTypePetition.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
