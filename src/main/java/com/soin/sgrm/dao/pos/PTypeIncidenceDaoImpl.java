package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PTypeIncidence;

@Repository
public class PTypeIncidenceDaoImpl extends AbstractDao<Long, PTypeIncidence> implements PTypeIncidenceDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PTypeIncidence> listTypePetition(){
	    return getSession().createCriteria(PTypeIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
