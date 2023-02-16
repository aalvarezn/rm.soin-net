package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.SystemService;

@Repository
public class System_StatusInDaoImp extends AbstractDao<Long, System_StatusIn> implements System_StatusInDao{
	
	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	SystemService systemService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<System_StatusIn> listTypePetition(){
	    return getSession().createCriteria(System_StatusIn.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<System_StatusIn> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(System_StatusIn.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<System_StatusIn> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public System_StatusIn findByIdAndSys(Integer systemId, Long priorityId) {
		Criteria crit = getSession().createCriteria(System_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.id", priorityId));
		System_StatusIn System_StatusIn = (System_StatusIn) crit.uniqueResult();
		return System_StatusIn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<System_StatusIn> findByManger(Integer idUser) {
		Criteria critSysStatus= getSession().createCriteria(System_StatusIn.class); 
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
		List<System_StatusIn> systemStatusList = critSysStatus.list();
		return systemStatusList;
	}

	@Override
	public System_StatusIn findByIdByCode(int systemId,String code) {
		Criteria crit = getSession().createCriteria(System_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.code", code));
		System_StatusIn System_StatusIn = (System_StatusIn) crit.uniqueResult();
		return System_StatusIn;
	}
	
	@Override
	public System_StatusIn findByIdByName(int systemId,String name) {
		Criteria crit = getSession().createCriteria(System_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.name", name));
		System_StatusIn System_StatusIn = (System_StatusIn) crit.uniqueResult();
		return System_StatusIn;
	}
}
