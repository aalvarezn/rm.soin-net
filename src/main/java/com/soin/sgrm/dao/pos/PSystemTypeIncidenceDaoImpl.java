package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.System;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemTypeIncidence;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PSystemService;

@Repository
public class PSystemTypeIncidenceDaoImpl extends AbstractDao<Long, PSystemTypeIncidence> implements PSystemTypeIncidenceDao{
	
	@Autowired
	PAttentionGroupService attentionGroupService;
	
	@Autowired
	PSystemService systemService;
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemTypeIncidence> listTypePetition(){
	    return getSession().createCriteria(PSystemTypeIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemTypeIncidence> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(PSystemTypeIncidence.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<PSystemTypeIncidence> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public PSystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId) {
		Criteria crit = getSession().createCriteria(PSystemTypeIncidence.class);
		crit.createAlias("system", "system");
		crit.createAlias("typeIncidence", "typeIncidence");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("typeIncidence.id", typeIncidenceId));
		PSystemTypeIncidence system_Priority = (PSystemTypeIncidence) crit.uniqueResult();
		return system_Priority;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemTypeIncidence> findByManager(Integer idUser) {

		Criteria critSysStatus= getSession().createCriteria(PSystemTypeIncidence.class); 
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
		List<PSystemTypeIncidence> systemTypeList = critSysStatus.list();
		return systemTypeList;
	}
}