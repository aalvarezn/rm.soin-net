package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystem_Priority;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PSystemService;

@Repository
public class PSystem_PriorityDaoImp extends AbstractDao<Long, PSystem_Priority> implements PSystem_PriorityDao{
	
	
	@Autowired
	PAttentionGroupService attentionGroupService;
	
	@Autowired
	PSystemService systemService;
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_Priority> listTypePetition(){
	    return getSession().createCriteria(PSystem_Priority.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_Priority> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(PSystem_Priority.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<PSystem_Priority> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public PSystem_Priority findByIdAndSys(Integer systemId, Long priorityId) {
		Criteria crit = getSession().createCriteria(PSystem_Priority.class);
		crit.createAlias("system", "system");
		crit.createAlias("priority", "priority");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("priority.id", priorityId));
		PSystem_Priority system_Priority = (PSystem_Priority) crit.uniqueResult();
		return system_Priority;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_Priority> findByManger(Integer idUser) {
		Criteria critSysStatus= getSession().createCriteria(PSystem_Priority.class); 
		List<Integer> listaId=new ArrayList<Integer>();
		List<PAttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(idUser);
		List<Long> listAttentionGroupId=new ArrayList<Long>();
		for(PAttentionGroup attentionGroup: attentionGroups) {
			listAttentionGroupId.add(attentionGroup.getId());
		}
		List<PSystem> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
		for(PSystem system: systemList) {
			listaId.add(system.getId());
		}
	
		critSysStatus.add(Restrictions.in("system.id", listaId));
		List<PSystem_Priority> systemStatusList = critSysStatus.list();
		return systemStatusList;
	}
}