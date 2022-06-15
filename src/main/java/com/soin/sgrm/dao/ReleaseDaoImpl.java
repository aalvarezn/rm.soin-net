package com.soin.sgrm.dao;

import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.ItemObject;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

@Repository
public class ReleaseDaoImpl implements ReleaseDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer countByType(String name, String type, int query, Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		switch (query) {
		case 1:
			// query #1 Obtiene mis releases
			crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
			crit.createCriteria("user").add(Restrictions.eq("username", name));
			crit.createCriteria("status").add(Restrictions.eq("name", type));
			break;

		case 2:
			// query #1 Obtiene mis releases de equipo
			crit.createCriteria("system").add(Restrictions.in("id", ids));
			crit.createCriteria("status").add(Restrictions.not(Restrictions.in("name", Constant.FILTRED)))
					.add(Restrictions.eq("name", type));
			break;

		case 3:
			// query #1 Obtiene mis releases de sistemas
			crit.createCriteria("status").add(Restrictions.eq("name", type));
			break;
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}

	@Override
	public ReleaseSummary findById(Integer id) {
		ReleaseSummary release = (ReleaseSummary) sessionFactory.getCurrentSession()
				.createCriteria(ReleaseSummary.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();

		Criteria crit = criteriaByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
				statusId);
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
				statusId);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<ReleaseUser> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch, ids, dateRange, systemId,
				statusId);
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch, ids, dateRange,
				systemId, statusId);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<ReleaseUser> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<ReleaseUser> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByAllSystemQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<ReleaseUser> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer existNumRelease(String number_release) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release.class);
		crit.add(Restrictions.like("releaseNumber", number_release, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();

		return recordsTotal;

	}

	@Override
	public void save(Release release, String tpos) throws Exception {
		String[] ids = tpos.split(",");
		int id = 0;
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.saveOrUpdate(release);

			if (!tpos.equalsIgnoreCase("-1")) {
				for (int i = 0; i < ids.length; i++) {
					id = Integer.parseInt(ids[i]);
					sql = String.format(
							"INSERT INTO releases_release_requerimi6ceb ( id, release_id, requerimiento_id) VALUES ( null, %s, %s ) ",
							release.getId(), id);
					query = sessionObj.createSQLQuery(sql);
					query.executeUpdate();
				}
			}
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public ReleaseEdit findEditById(Integer id) throws SQLException {
		ReleaseEdit release = (ReleaseEdit) sessionFactory.getCurrentSession().get(ReleaseEdit.class, id);
		return release;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<ReleaseUser> list(String search, String release_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		crit.setMaxResults(20);

		Criterion restReleaseNumber = Restrictions.like("releaseNumber", search, MatchMode.ANYWHERE).ignoreCase();
		crit.add(restReleaseNumber);
		if (release_id != null || !release_id.equals("")) {
			crit.add(Restrictions.ne("id", Integer.parseInt(release_id)));
		}
		crit.addOrder(Order.desc("createDate"));

		List<ReleaseUser> requestList = crit.list();
		return requestList;
	}

	@Override
	public void updateStatusRelease(ReleaseEdit release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			String dateChange = (release.getDateChange() != null && !release.getDateChange().equals("")
					? "to_date('" + release.getDateChange() + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update releases_release set estado_id = %s , reintentos = %s , operador = '%s' , motivo = '%s' , fecha_creacion = "
							+ dateChange + "  where id = %s",
					release.getStatus().getId(), release.getRetries(), release.getOperator(), release.getMotive(),
					release.getId());

			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public Release findReleaseById(Integer id) {
		Release release = (Release) sessionFactory.getCurrentSession().get(Release.class, id);
		return release;
	}
	@Override
	public Release_RFC findRelease_RFCById(Integer id) {
		Release_RFC release = (Release_RFC) sessionFactory.getCurrentSession().get(Release_RFC.class, id);
		return release;
	}

	@Override
	public void saveRelease(Release release, ReleaseCreate rc) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			release.setAttributes(rc);

			for (ItemObject item : rc.getObjectItemConfiguration()) {
				if (item.getIsSql().equals("1")) {
					sql = String.format(" UPDATE sistemas_objeto "
							+ " SET ocupa_ejecutar = %s , esquema = '%s' , plan_ejecucion = %s " + " WHERE id = %s ",
							item.getExecute(), item.getDbScheme(), item.getExecutePlan(), item.getId());
					query = sessionObj.createSQLQuery(sql);
					query.executeUpdate();
				}
			}
			sessionObj.update(release);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void requestRelease(Release release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update releases_release set estado_id = %s , nodo_id = %s , operador = '%s' , motivo = '%s' , fecha_creacion = sysdate where id = %s",
					release.getStatus().getId(), (release.getNode() != null ? release.getNode().getId() : null),
					release.getOperator(), release.getMotive(), release.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public ArrayList<ReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<ReleaseObjectEdit> objects) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			for (int i = 0; i < objects.size(); i++) {
				sessionObj.save(objects.get(i));
				sql = String.format(
						" INSERT INTO releases_release_objetos ( id, release_id, objeto_id ) VALUES ( RELEASES_RELEASE_OBJETOS_SQ.nextval , %s , %s )",
						release_id, objects.get(i).getId());
				query = sessionObj.createSQLQuery(sql);
				query.executeUpdate();
			}
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
		return objects;
	}

	@Override
	public void copy(ReleaseEdit release, String tpos) throws Exception {
		String[] ids = tpos.split(",");
		int id = 0;
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(release);
			if (!tpos.equalsIgnoreCase("-1")) {
				for (int i = 0; i < ids.length; i++) {
					id = Integer.parseInt(ids[i]);
					sql = String.format(
							"INSERT INTO releases_release_requerimi6ceb ( id, release_id, requerimiento_id) VALUES ( null, %s, %s ) ",
							release.getId(), id);
					query = sessionObj.createSQLQuery(sql);
					query.executeUpdate();
				}
			}
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void assignRelease(ReleaseEdit release, UserInfo user) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			release.setUser(user);
			sessionObj.update(release);
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public ReleaseUser findReleaseUserById(Integer id) throws SQLException {
		ReleaseUser release = (ReleaseUser) sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings("deprecation")
	public Criteria criteriaByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");

		crit.add(Restrictions.eq("user.username", name));
		crit.add(Restrictions.not(Restrictions.in("status.name", Constant.FILTRED)));

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));

		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("createDate", start));
				crit.add(Restrictions.le("createDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		crit.addOrder(Order.desc("createDate"));

		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");

		crit.add(Restrictions.in("system.id", ids));
		crit.add(Restrictions.not(Restrictions.in("status.name", Constant.FILTRED)));

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));

		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("createDate", start));
				crit.add(Restrictions.le("createDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		crit.addOrder(Order.desc("createDate"));

		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaBySystems(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");

		if (filtred != null) {
			crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
		}

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));

		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("createDate", start));
				crit.add(Restrictions.le("createDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		crit.addOrder(Order.desc("createDate"));

		return crit;
	}
	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaBySystemsQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseUser.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");

		if (filtred != null) {
			crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
		}

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));

		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("createDate", start));
				crit.add(Restrictions.le("createDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		crit.addOrder(Order.desc("createDate"));

		return crit;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listReleasesBySystem(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaBySystems1( sEcho, iDisplayStart, iDisplayLength, sSearch,
				systemId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems1( sEcho, iDisplayStart, iDisplayLength, sSearch,
				systemId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();
		if(recordsTotal==1) {
			crit.uniqueResult();
		}
		List<Releases_WithoutObj> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}
	

	public Criteria criteriaBySystems1(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Releases_WithoutObj.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "statuses").add(Restrictions.or(Restrictions.eq("statuses.name","Certificacion"),
				Restrictions.eq("statuses.name","Solicitado")))
		.add(Restrictions.eq("system.id", systemId));
		

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase());
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		
		return crit;
	}
}
