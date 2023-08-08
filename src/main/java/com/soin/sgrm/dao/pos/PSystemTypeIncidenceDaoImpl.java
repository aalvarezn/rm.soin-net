package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.SystemService;

@Repository
public class PSystemTypeIncidenceDaoImpl extends AbstractDao<Long, SystemTypeIncidence> implements PSystemTypeIncidenceDao{
	
	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	SystemService systemService;
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
		crit.createAlias("typeIncidence", "typeIncidence");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("typeIncidence.id", typeIncidenceId));
		SystemTypeIncidence system_Priority = (SystemTypeIncidence) crit.uniqueResult();
		return system_Priority;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemTypeIncidence> findByManager(Integer idUser) {

		Criteria critSysStatus= getSession().createCriteria(SystemTypeIncidence.class); 
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
		List<SystemTypeIncidence> systemTypeList = critSysStatus.list();
		return systemTypeList;
	}
}