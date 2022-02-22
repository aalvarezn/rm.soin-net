package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.System;

@Repository
public class SystemDaoImpl implements SystemDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> listSystemByUser(String name) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.createAlias("user", "user");
		crit.createAlias("managers", "managers");
		crit.add(Restrictions.or(Restrictions.eq("user.username", name), Restrictions.eq("managers.username", name)));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<SystemUser> systemList = crit.list();

		return systemList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object[] myTeams(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.createAlias("user", "user");
		crit.createAlias("managers", "managers");
		crit.add(Restrictions.or(Restrictions.eq("user.username", name), Restrictions.eq("managers.username", name)));
		crit.setProjection(Projections.property("id"));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List systemList = crit.list();

		if (systemList.size() == 0) {
			systemList.add(0);
		}
		Object[] list = systemList.toArray();
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			Sentry.capture(e, "system");
			throw new Exception("No se encontro el Systema solicitado para el release");
		}

	}

	@Override
	public SystemUser findSystemDocumentInfo(Integer systemId) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
			crit.add(Restrictions.eq("id", systemId));
			SystemUser systemInfo = (SystemUser) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			Sentry.capture(e, "system");
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
			Sentry.capture(e, "system");
			throw new Exception("No se encontro el Systema solicitado para el release");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemInfo> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
		List<SystemInfo> systemInfo = crit.list();
		return systemInfo;
	}

	@Override
	public SystemInfo findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class);
		crit.add(Restrictions.eq("id", id));
		return (SystemInfo) crit.uniqueResult();
	}

	@Override
	public SystemUser findSystemUserById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.add(Restrictions.eq("id", id));
		return (SystemUser) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<System> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(System.class);
		return crit.list();
	}

	@Override
	public System findSystemById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(System.class);
		crit.add(Restrictions.eq("id", id));
		return (System) crit.uniqueResult();
	}

	@Override
	public void save(System system) {
		sessionFactory.getCurrentSession().save(system);
	}

	@Override
	public void update(System system) {
		sessionFactory.getCurrentSession().update(system);
	}

	@Override
	public void delete(Integer id) {
		System system = findSystemById(id);
		sessionFactory.getCurrentSession().delete(system);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> listSystemUser() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		return crit.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object[] myTeamsProyect(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.createAlias("user", "user");
		crit.createAlias("managers", "managers");
		crit.add(Restrictions.or(Restrictions.eq("user.username", name), Restrictions.eq("managers.username", name)));
		crit.setProjection(Projections.property("proyectId"));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List systemList = crit.list();

		if (systemList.size() == 0) {
			systemList.add(0);
		}
		Object[] list = systemList.toArray();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> listSystemUserByIds(Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemUser.class);
		crit.add(Restrictions.in("id", ids));
		return crit.list();
	}

}
