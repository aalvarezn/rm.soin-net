package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.SystemInfo;

@Repository
public class BaseKnowledgeDaoImpl extends AbstractDao<Long,BaseKnowledge> implements BaseKnowledgeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer existNumError(String numError) {
		Criteria crit = getSession().createCriteria(BaseKnowledge.class);
		crit.add(Restrictions.like("numError", numError, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}

	@Override
	public void updateStatusBaseKnowledge(BaseKnowledge baseKnowledge, String dateChange) throws Exception {
		String sql = "";
		Query query = null;
		try {
			

			String dateChangeUpdate = (dateChange != null && !dateChange.equals("")
					? "to_date('" + dateChange + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update sgrm.\"BASECONOCIMIENTO\" set \"ID_ESTADO\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHASOLICITUD\" = "
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

	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(BaseKnowledge.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis BaseKnowledge

			crit.add(Restrictions.eq("status.name", type));
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
		Criteria crit = getSession().createCriteria(BaseKnowledge.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

			// query #1 Obtiene mis BaseKnowledge
			List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(SystemInfo system: systems) {
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
