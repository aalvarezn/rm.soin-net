package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.SystemInfo;

@Repository
public class RequestBaseDaoImpl extends AbstractDao<Long, RequestBase> implements RequestBaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public Integer existNumRequest(String numRequest) {
		Criteria crit = getSession().createCriteria(RequestBase.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(RequestBase.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(SystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));

			
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
	public Integer countByManager(Integer id, Long idRequest) {
		Criteria crit = getSession().createCriteria(RequestBase.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

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
}
