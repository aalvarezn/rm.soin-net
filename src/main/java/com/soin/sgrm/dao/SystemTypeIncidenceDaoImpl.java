package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;

@Repository
public class SystemTypeIncidenceDaoImpl extends AbstractDao<Long, SystemTypeIncidence> implements SystemTypeIncidenceDao{
	

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemTypeIncidence> listTypePetition(){
	    return getSession().createCriteria(SystemTypeIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemTypeIncidence> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(SystemTypeIncidence.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<SystemTypeIncidence> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public SystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId) {
		Criteria crit = getSession().createCriteria(SystemTypeIncidence.class);
		crit.createAlias("system", "system");
		crit.createAlias("priority", "priority");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("typeIncidence.id", typeIncidenceId));
		SystemTypeIncidence system_Priority = (SystemTypeIncidence) crit.uniqueResult();
		return system_Priority;
	}
}
