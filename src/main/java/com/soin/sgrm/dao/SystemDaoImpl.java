package com.soin.sgrm.dao;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.User;
import com.soin.sgrm.utils.JsonSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;

@Repository
public class SystemDaoImpl implements SystemDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> listSystemByUser(String name) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
//		crit.setProjection(Projections.distinct(Projections.property("code")));
		crit.createCriteria("user").add(Restrictions.eq("username", name));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<SystemUser> systemList = crit.list();

		return systemList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object[] myTeams(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.createCriteria("user").add(Restrictions.eq("username", name));
		crit.setProjection(Projections.property("id"));
		List systemList = crit.list();

		if (systemList.size() == 0) {
			systemList.add(0);
		}
		Object[] list = systemList.toArray();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<?> listSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch) {
		JsonSheet json = new JsonSheet();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
		crit.addOrder(Order.asc("id"));
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<SystemInfo> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);

		return json;
	}

	@Override
	public SystemInfo findByCode(String code) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
			crit.add(Restrictions.eq("code", code));
			SystemInfo systemInfo = (SystemInfo) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			throw new Exception("No se encontro el Systema solicitado para el release");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public SystemUser findSystemDocumentInfo(Integer systemId) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
			crit.add(Restrictions.eq("id", systemId));
			SystemUser systemInfo = (SystemUser) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			throw new Exception("No se encontro el Systema solicitado para el release");
		}
	}

	@Override
	public SystemModule findModuleBySystem(Integer systemId) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemModule.class);
			crit.add(Restrictions.eq("system_id", systemId));
			SystemModule systemInfo = (SystemModule) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			throw new Exception("No se encontro el Systema solicitado para el release");
		}
	}

	@Override
	public List<SystemInfo> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
		List<SystemInfo> systemInfo = crit.list();
		return systemInfo;
	}

}
