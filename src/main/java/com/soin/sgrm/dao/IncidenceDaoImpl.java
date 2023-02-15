package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.IncidenceResume;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.SystemService;

@Repository
public class IncidenceDaoImpl extends AbstractDao<Long, Incidence> implements IncidenceDao{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	SystemService systemService;
	@Autowired
	AttentionGroupService attentionGroupService;
	@Override
	public Integer existNumTicket(String nameSystem) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		crit.add(Restrictions.like("numTicket", nameSystem, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<System> systems = systemService.findByUserIncidence(userLogin);
			List<Integer> listaId=new ArrayList<Integer>();
			for(System system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("system", "system");
			crit.add(Restrictions.in("system.id", listaId));
			crit.add(Restrictions.eq("statusFinal.name", type));
			crit.add(Restrictions.like("createFor",email,MatchMode.ANYWHERE));
			break;
		case 2:
			crit.add(Restrictions.eq("statusFinal.name", type));
		
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	@Override
	public Integer countByTypeBySystem(Integer id, String type, int query, Object[] ids,Integer userLogin,String email) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			
			List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(userLogin);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(AttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
		
			List<Integer> listaId=new ArrayList<Integer>();
			for(System system: systemList) {
				listaId.add(system.getId());
			}
			crit.createAlias("system", "system");
			crit.add(Restrictions.in("system.id", listaId));
			crit.add(Restrictions.eq("statusFinal.name", type));
			break;
		case 2:
			crit.add(Restrictions.eq("statusFinal.name", type));
		
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByManager(Integer id, Long idRequest) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
			// query #1 Obtiene mis request
			List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(SystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));
			crit.add(Restrictions.eq("id", idRequest));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Incidence getIncidences(Long id) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		List<String> fetchs=new ArrayList<String>();
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		fetchs.add("attentionGroup");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);
		
	    return (Incidence)
	    		crit.add(Restrictions.eq("id", id))
	    		.uniqueResult();
	    
	}
	@Override
	public Incidence getIncidenceByName(String numTicket) {
		Criteria crit = getSession().createCriteria(Incidence.class);
		List<String> fetchs=new ArrayList<String>();
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);
		
	    return (Incidence)
	    		crit.add(Restrictions.eq("numTicket", numTicket))
	    		.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IncidenceResume> getListIncideSLA() {
		Criteria crit = getSession().createCriteria(IncidenceResume.class);
		crit.add(Restrictions.eq("slaActive",1));
		crit.add(Restrictions.isNotNull("timeMili"));
	    return crit.list();
	}

	@Override
	public void updateIncidenceResume(IncidenceResume incidenceResume) {
		getSession().update(incidenceResume);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IncidenceResume> getListIncideRequest() {
		Criteria crit = getSession().createCriteria(IncidenceResume.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		crit.add(Restrictions.eq("statusFinal.name","Solicitado"));
		crit.add(Restrictions.isNotNull("timeMili"));
	    return crit.list();
	}
}
