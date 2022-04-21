package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.utils.JsonSheet;

@Repository("releaseDao")
public class ReleaseDaoImpl extends AbstractDao<Long, PRelease> implements ReleaseDao{

	@SuppressWarnings("unchecked")
	public List<PRelease> listReleasesBySystem1(Long id){
	    return getSession().createCriteria(PRelease.class)
	    		.createAlias("system","systems")
	    		.createAlias("status", "statuses")
	    		.add(Restrictions.eq("systems.id", id))
	    		.add(Restrictions.or(Restrictions.eq("statuses.name","Certificacion"),Restrictions.eq("statuses.name","Solicitado")))
	    		.list();
		}
	
	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaBySystems(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Long systemId)
			throws SQLException, ParseException {

		Criteria crit = getSession().createCriteria(PRelease.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "statuses").add(Restrictions.or(Restrictions.eq("statuses.name","Certificacion"),
				Restrictions.eq("statuses.name","Solicitado")))
		.add(Restrictions.eq("system.id", systemId));
		

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("numRelease", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		
		return crit;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet listReleasesBySystem( int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, Long systemId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaBySystems( sEcho, iDisplayStart, iDisplayLength, sSearch,
				systemId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems( sEcho, iDisplayStart, iDisplayLength, sSearch,
				systemId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PRelease> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}
}
