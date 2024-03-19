package com.soin.sgrm.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class RequestNewDaoImpl extends AbstractDao<Integer,Request> implements RequestNewDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SystemService systemService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			int proyectId, int typeId, Integer userLogin) throws ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaList( sEcho, iDisplayStart, iDisplayLength, sSearch, proyectId,
				typeId,userLogin);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaList( sEcho, iDisplayStart, iDisplayLength, sSearch, proyectId,
				typeId,userLogin);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<Request> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	private Criteria criteriaList(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			int proyectId, int typeId, Integer userLogin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.createAlias("proyect", "proyect");
		crit.createAlias("typeRequest", "typeRequest");
	

	

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("code_soin", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("code_ice", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("typeRequest.code", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("proyect.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		

		if (proyectId != 0) {
			crit.add(Restrictions.eq("proyect.id", proyectId));
		}
		if (typeId != 0) {
			crit.add(Restrictions.eq("typeRequest.id", typeId));
		}
		crit.add(Restrictions.eq("userManager", userLogin));
		return crit;
	}
	

	
	
	
	

}