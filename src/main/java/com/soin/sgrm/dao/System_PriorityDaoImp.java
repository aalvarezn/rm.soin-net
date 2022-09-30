package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.System_Priority;

@Repository
public class System_PriorityDaoImp extends AbstractDao<Long, System_Priority> implements System_PriorityDao{
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<System_Priority> listTypePetition(){
	    return getSession().createCriteria(System_Priority.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<System_Priority> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(System_Priority.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<System_Priority> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public System_Priority findByIdAndSys(Integer systemId, Long priorityId) {
		Criteria crit = getSession().createCriteria(System_Priority.class);
		crit.createAlias("system", "system");
		crit.createAlias("priority", "priority");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("priority.id", priorityId));
		System_Priority system_Priority = (System_Priority) crit.uniqueResult();
		return system_Priority;
	}
}
