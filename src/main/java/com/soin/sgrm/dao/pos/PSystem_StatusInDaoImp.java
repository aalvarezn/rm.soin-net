package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystem_StatusIn;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PSystemService;

@Repository
public class PSystem_StatusInDaoImp extends AbstractDao<Long, PSystem_StatusIn> implements PSystem_StatusInDao{
	
	@Autowired
	PAttentionGroupService attentionGroupService;
	
	@Autowired
	PSystemService systemService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_StatusIn> listTypePetition(){
	    return getSession().createCriteria(PSystem_StatusIn.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_StatusIn> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(PSystem_StatusIn.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<PSystem_StatusIn> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public PSystem_StatusIn findByIdAndSys(Integer systemId, Long priorityId) {
		Criteria crit = getSession().createCriteria(PSystem_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.id", priorityId));
		PSystem_StatusIn PSystem_StatusIn = (PSystem_StatusIn) crit.uniqueResult();
		return PSystem_StatusIn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem_StatusIn> findByManger(Integer idUser) {
		Criteria critSysStatus= getSession().createCriteria(PSystem_StatusIn.class); 
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
		List<PSystem_StatusIn> systemStatusList = critSysStatus.list();
		return systemStatusList;
	}

	@Override
	public PSystem_StatusIn findByIdByCode(int systemId,String code) {
		Criteria crit = getSession().createCriteria(PSystem_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.code", code));
		PSystem_StatusIn PSystem_StatusIn = (PSystem_StatusIn) crit.uniqueResult();
		return PSystem_StatusIn;
	}
	
	@Override
	public PSystem_StatusIn findByIdByName(int systemId,String name) {
		Criteria crit = getSession().createCriteria(PSystem_StatusIn.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("status.name", name));
		PSystem_StatusIn PSystem_StatusIn = (PSystem_StatusIn) crit.uniqueResult();
		return PSystem_StatusIn;
	}
}
