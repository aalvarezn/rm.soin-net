package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PBaseKnowledge;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PSystemService;

@Repository
public class PBaseKnowledgeDaoImpl extends AbstractDao<Long,PBaseKnowledge> implements PBaseKnowledgeDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@Autowired
	PAttentionGroupService pattentionGroupService;
	
	@Autowired
	PSystemService psystemService;
	@Override
	public Integer existNumError(String numError) {
		Criteria crit = getSession().createCriteria(PBaseKnowledge.class);
		crit.add(Restrictions.like("numError", numError, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}

	@Override
	public void updateStatusBaseKnowledge(PBaseKnowledge baseKnowledge, String dateChange) throws Exception {
		String sql = "";
		Query query = null;
		try {
			

			String dateChangeUpdate = (dateChange != null && !dateChange.equals("")
					? "to_timestamp('" + dateChange + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update \"BASECONOCIMIENTO\" set \"ID_ESTADO\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHASOLICITUD\" = "
							+ dateChangeUpdate + "  where \"ID\" = %s",
					baseKnowledge.getStatus().getId(), baseKnowledge.getOperator(), baseKnowledge.getMotive(),
					baseKnowledge.getId());

			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			
		} catch (Exception e) {
			Sentry.capture(e, "BaseKnowledge");
			
			throw e;
		} 
	}


	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(PBaseKnowledge.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis BaseKnowledge
			List<PAttentionGroup> attentionGroups= pattentionGroupService.findGroupByUserId(id);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(PAttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<PSystem> systemList=psystemService.findByGroupIncidence(listAttentionGroupId);
			Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);
			List<Integer> systemIds=new ArrayList<Integer>();
			for(PSystem system: systemList) {
				systemIds.add(system.getId());
			}
			crit.add(Restrictions.eq("status.name", type));
			crit.add(Restrictions.in("system.id", systemIds));
			break;
		case 2:
			crit.add(Restrictions.eq("status.name", type));
		
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByManager(Integer id, Long idBaseKnowledge) {
		Criteria crit = getSession().createCriteria(PBaseKnowledge.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

			// query #1 Obtiene mis BaseKnowledge
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));
			crit.add(Restrictions.eq("id", idBaseKnowledge));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	
	

}
