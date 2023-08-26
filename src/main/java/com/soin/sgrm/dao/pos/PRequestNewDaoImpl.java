package com.soin.sgrm.dao.pos;

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
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PRequestNewDaoImpl extends AbstractDao<Integer,PRequest> implements PRequestNewDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@Autowired
	private PSystemService systemService;
	
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
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
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
		}else {
			List<PSystem> systems=systemService.findByUserIncidence(userLogin);
			List<PProject>projects=new ArrayList<>();
			
			for(PSystem system: systems) {
				system.getProyect();
				boolean veri=true;
				for(PProject project: projects) {
					if(project.getId()==system.getProyect().getId()) {
						veri=false;
					}
				}
				if(veri) {
					projects.add(system.getProyect());
				}
			}
			List<Integer> projectsId= new ArrayList<>();
			for(PProject project: projects) {
				projectsId.add(project.getId());
			}
			crit.add(Restrictions.in("proyect.id", projectsId));
			
		}
		if (typeId != 0) {
			crit.add(Restrictions.eq("typeRequest.id", typeId));
		}

		return crit;
	}
	

	
	
	
	

}