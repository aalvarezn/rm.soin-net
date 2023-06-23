package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PSystemModule;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.exception.Sentry;

@Repository
public class PSystemDaoImpl implements PSystemDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;;

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemUser> listSystemByUser(String name) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
		crit.createAlias("user", "user");
		crit.createAlias("managers", "managers");
		crit.add(Restrictions.or(Restrictions.eq("user.username", name), Restrictions.eq("managers.username", name)));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<PSystemUser> systemList = crit.list();

		return systemList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object[] myTeams(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
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
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class);
		crit.addOrder(Order.asc("id"));
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PSystemInfo> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);

		return json;
	}

	@Override
	public PSystemInfo findByCode(String code) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class);
			crit.add(Restrictions.eq("code", code));
			PSystemInfo systemInfo = (PSystemInfo) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			Sentry.capture(e, "system");
			throw new Exception("No se encontro el Systema solicitado para el release");
		}

	}

	@Override
	public PSystemUser findSystemDocumentInfo(Integer systemId) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
			crit.add(Restrictions.eq("id", systemId));
			PSystemUser systemInfo = (PSystemUser) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			Sentry.capture(e, "system");
			throw new Exception("No se encontro el Sistema solicitado para el release");
		}
	}

	@Override
	public PSystemModule findModuleBySystem(Integer systemId) throws Exception {
		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemModule.class);
			crit.add(Restrictions.eq("system_id", systemId));
			PSystemModule systemInfo = (PSystemModule) crit.uniqueResult();
			return systemInfo;
		} catch (Exception e) {
			Sentry.capture(e, "system");
			throw new Exception("No se encontro el Sistema solicitado para el release");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemInfo> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class);
		List<PSystemInfo> systemInfo = crit.list();
		return systemInfo;
	}

	@Override
	public PSystemInfo findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class);
		crit.add(Restrictions.eq("id", id));
		return (PSystemInfo) crit.uniqueResult();
	}

	@Override
	public PSystemUser findSystemUserById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
		crit.add(Restrictions.eq("id", id));
		return (PSystemUser) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		return crit.list();
	}

	@Override
	public PSystem findSystemById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		crit.add(Restrictions.eq("id", id));
		return (PSystem) crit.uniqueResult();
	}

	@Override
	public void save(PSystem system) {
		sessionFactory.getCurrentSession().save(system);
	}

	@Override
	public void update(PSystem system) {
		sessionFactory.getCurrentSession().update(system);
	}

	@Override
	public void delete(Integer id) {
		PSystem system = findSystemById(id);
		sessionFactory.getCurrentSession().delete(system);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystemUser> listSystemUser() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
		return crit.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object[] myTeamsProyect(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
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
	public List<PSystemUser> listSystemUserByIds(Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemUser.class);
		crit.add(Restrictions.in("id", ids));
		return crit.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<PSystem> listProjects(int id) {
		   return sessionFactory.getCurrentSession().createCriteria(PSystem.class)
		    		.createAlias("managers","managers")
		    		.add(Restrictions.eq("managers.id", id))
		    		.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> findByUserIncidence(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		crit.createAlias("usersIncidence", "usersIncidence");
		crit.add( Restrictions.eq("usersIncidence.id", id));
		List<PSystem> systemList = crit.list();
		return systemList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> findByGroupIncidence(List<Long> listAttentionGroupId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		crit.createAlias("attentionGroup", "attentionGroup");
		crit.add( Restrictions.in("attentionGroup.id", listAttentionGroupId));
		List<PSystem> systemList = crit.list();
		return systemList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> findByManagerIncidence(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		crit.createAlias("managersIncidence", "managersIncidence");
		crit.add( Restrictions.eq("managersIncidence.id", id));
		List<PSystem> systemList = crit.list();

		return systemList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> getSystemByProject(Integer projectId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystem.class);
		crit.createAlias("proyect", "proyect");
		crit.add( Restrictions.eq("proyect.id", projectId));
		List<PSystem> systemList = crit.list();

		return systemList;
	}
}