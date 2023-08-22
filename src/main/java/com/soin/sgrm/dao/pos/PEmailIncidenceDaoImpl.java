package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PEmailIncidence;

@Repository
public class PEmailIncidenceDaoImpl extends AbstractDao<Long, PEmailIncidence> implements PEmailIncidenceDao{
	

	@SuppressWarnings("unchecked")
	@Override
	public List<PEmailIncidence> listTypePetition(){
	    return getSession().createCriteria(PEmailIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<PEmailIncidence> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(PEmailIncidence.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<PEmailIncidence> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public PEmailIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId) {
		Criteria crit = getSession().createCriteria(PEmailIncidence.class);
		crit.createAlias("system", "system");
		crit.createAlias("typeIncidence", "typeIncidence");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("typeIncidence.id", typeIncidenceId));
		PEmailIncidence system_Priority = (PEmailIncidence) crit.uniqueResult();
		return system_Priority;
	}
}
