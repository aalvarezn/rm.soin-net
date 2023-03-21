package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.SystemService;

@Repository
public class System_PriorityDaoImp extends AbstractDao<Long, System_Priority> implements System_PriorityDao{
	
	
	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	SystemService systemService;
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
		Criteria critSysStatus= getSession().createCriteria(System_Priority.class); 
		List<Integer> listaId=new ArrayList<Integer>();
		List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(idUser);
		List<Long> listAttentionGroupId=new ArrayList<Long>();
		for(AttentionGroup attentionGroup: attentionGroups) {
			listAttentionGroupId.add(attentionGroup.getId());
		}
		List<System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
		for(System system: systemList) {
			listaId.add(system.getId());
		}
	
		critSysStatus.add(Restrictions.in("system.id", listaId));
		List<System_Priority> systemStatusList = critSysStatus.list();
		return systemStatusList;
	}
}