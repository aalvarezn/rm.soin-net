package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PIncidenceResume;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PSystemService;

@Repository
public class PIncidenceDaoImpl extends AbstractDao<Long, PIncidence> implements PIncidenceDao{
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@Autowired
	PSystemService systemService;
	@Autowired
	PAttentionGroupService attentionGroupService;
	@Override
	public Integer existNumTicket(String nameSystem) {
		Criteria crit = getSession().createCriteria(PIncidence.class);
		crit.add(Restrictions.like("numTicket", nameSystem, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email) {
		Criteria crit = getSession().createCriteria(PIncidence.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<PSystem> systems = systemService.findByUserIncidence(userLogin);
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystem system: systems) {
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
		Criteria crit = getSession().createCriteria(PIncidence.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			
			List<PAttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(userLogin);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(PAttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<PSystem> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
		
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystem system: systemList) {
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
		Criteria crit = getSession().createCriteria(PIncidence.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
			// query #1 Obtiene mis request
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));
			crit.add(Restrictions.eq("id", idRequest));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	@Override
	public PIncidence getIncidences(Long id) {
		Criteria crit = getSession().createCriteria(PIncidence.class);
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
		
	    return (PIncidence)
	    		crit.add(Restrictions.eq("id", id))
	    		.uniqueResult();
	    
	}
	@Override
	public PIncidence getIncidenceByName(String numTicket) {
		Criteria crit = getSession().createCriteria(PIncidence.class);
		List<String> fetchs=new ArrayList<String>();
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);
		
	    return (PIncidence)
	    		crit.add(Restrictions.eq("numTicket", numTicket))
	    		.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PIncidenceResume> getListIncideSLA() {
		Criteria crit = getSession().createCriteria(PIncidenceResume.class);
		crit.add(Restrictions.eq("slaActive",1));
		crit.add(Restrictions.isNotNull("timeMili"));
	    return crit.list();
	}

	@Override
	public void updateIncidenceResume(PIncidenceResume incidenceResume) {
		getSession().update(incidenceResume);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PIncidenceResume> getListIncideRequest() {
		Criteria crit = getSession().createCriteria(PIncidenceResume.class);
		crit.createAlias("status", "status");
		crit.createAlias("status.status", "statusFinal");
		crit.add(Restrictions.eq("statusFinal.name","Solicitado"));
		crit.add(Restrictions.isNotNull("timeMili"));
	    return crit.list();
	}
}