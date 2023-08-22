package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PTypePetitionR4;

@Repository
public class PTypePetitionR4DaoImpl extends AbstractDao<Long, PTypePetitionR4> implements PTypePetitionR4Dao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PTypePetitionR4> listTypePetition(){
	    return getSession().createCriteria(PTypePetitionR4.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}
}
