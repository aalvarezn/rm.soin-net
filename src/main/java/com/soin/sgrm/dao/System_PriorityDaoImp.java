package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.System;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.System_StatusIn;

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
	@SuppressWarnings("unchecked")
	@Override
	public List<System_Priority> findByManger(Integer idUser) {
		Criteria crit =getSession().createCriteria(System.class); 
		crit.createAlias("managersIncidence", "managersIncidence");
		crit.add( Restrictions.eq("managersIncidence.id", idUser));
		List<System> systemList = crit.list();
		List<Integer> listaId=new ArrayList<Integer>();
		for(System system: systemList) {
			listaId.add(system.getId());
		}
		Criteria critSysStatus= getSession().createCriteria(System_Priority.class); 
		critSysStatus.add(Restrictions.in("system.id", listaId));
		List<System_Priority> systemStatusList = critSysStatus.list();
		return systemStatusList;
	}
}
