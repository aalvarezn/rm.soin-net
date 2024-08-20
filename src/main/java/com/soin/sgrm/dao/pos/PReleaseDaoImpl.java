package com.soin.sgrm.dao.pos;

import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReleaseUserFast;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseEditWithOutObjects;
import com.soin.sgrm.model.pos.PReleaseError;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseReport;
import com.soin.sgrm.model.pos.PReleaseReportFast;
import com.soin.sgrm.model.pos.PReleaseSummary;
import com.soin.sgrm.model.pos.PReleaseSummaryFile;
import com.soin.sgrm.model.pos.PReleaseSummaryMin;
import com.soin.sgrm.model.pos.PReleaseTinySummary;
import com.soin.sgrm.model.pos.PReleaseTrackingShow;
import com.soin.sgrm.model.pos.PReleaseTrackingToError;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PReleaseUserFast;
import com.soin.sgrm.model.pos.PRelease_RFC;
import com.soin.sgrm.model.pos.PRelease_RFCFast;
import com.soin.sgrm.model.pos.PReleases_WithoutObj;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.ItemObject;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

@Repository
public class PReleaseDaoImpl implements PReleaseDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public Integer countByType(String name, String type, int query, Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
		switch (query) {
		case 1:
			// query #1 Obtiene mis releases
			crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
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
	public PReleaseSummary findById(Integer id) {
		PReleaseSummary release = (PReleaseSummary) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseSummary.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();

		Criteria crit = criteriaByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
				statusId, false);
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId,
				statusId, true);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleaseUserFast> aaData = crit.list();
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
				statusId, false);
		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch, ids, dateRange,
				systemId, statusId, true);
		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleaseUserFast> aaData = crit.list();
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
				systemId, statusId, false);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, true);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleaseUserFast> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings("unchecked")
	public Criteria addFilters(Criteria criteria, Map<String, Object> columns) {
		if (columns == null)
			return criteria;
		for (Map.Entry<String, Object> column : columns.entrySet()) {
			if (column.getValue().getClass().isArray())
				criteria.add(Restrictions.in(column.getKey(), (Object[]) column.getValue()));
			else if (column.getValue() instanceof List)
				criteria.add(Restrictions.in(column.getKey(), ((List<Object>) column.getValue()).toArray()));
			else if (column.getValue() instanceof Criterion)
				criteria.add((Criterion) column.getValue());
			else
				criteria.add(Restrictions.eq(column.getKey(), column.getValue()));
		}
		return criteria;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet listByAllSystemQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, false);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, true);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleaseUserFast> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer existNumRelease(String number_release) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRelease.class);
		crit.add(Restrictions.like("releaseNumber", number_release, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();

		return recordsTotal;

	}

	@Override
	public void save(PRelease release, String tpos) throws Exception {
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
			int releaseId = release.getId();
			if (!tpos.equalsIgnoreCase("-1")) {
				sessionObj.createSQLQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
				for (int i = 0; i < ids.length; i++) {
					id = Integer.parseInt(ids[i]);
					sql = String.format(
							"INSERT INTO \"RELEASES_RELEASE_REQUERIMIENTO\" ( \"RELEASE_ID\",\"REQUERIMIENTO_ID\") VALUES (  %s, %s ) ",
							releaseId, id);
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
	public PReleaseEdit findEditById(Integer id) throws SQLException {
		PReleaseEdit release = (PReleaseEdit) sessionFactory.getCurrentSession().get(PReleaseEdit.class, id);
		return release;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<PReleaseUser> list(String search, String release_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
		crit.setMaxResults(20);

		Criterion restReleaseNumber = Restrictions.like("releaseNumber", search, MatchMode.ANYWHERE).ignoreCase();
		crit.add(restReleaseNumber);
		if (release_id != null || !release_id.equals("")) {
			crit.add(Restrictions.ne("id", Integer.parseInt(release_id)));
		}
		crit.addOrder(Order.desc("createDate"));

		List<PReleaseUser> requestList = crit.list();
		return requestList;
	}

	@Override
	public void updateStatusRelease(PReleaseEdit release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			String dateChange = (release.getDateChange() != null && !release.getDateChange().equals("")
					? "to_timestamp('" + release.getDateChange() + "', 'DD-MM-YYYY HH:MI PM')"
					: "CURRENT_TIMESTAMP");
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"REINTENTOS\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = "
							+ dateChange + "  where \"ID\" = %s",
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
	public void insertReleaseError(PReleaseError release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			String dateChange = (release.getErrorDate() != null && !release.getErrorDate().equals("")
					? "to_timestamp('" + release.getErrorDate() + "', 'DD-MM-YYYY HH:MI PM')"
					: "CURRENT_TIMESTAMP");
			/*
			 * sql = String.format( "INSERT INTO RELEASE_ERROR (ID, RELEASE_ID, ERROR_ID,
			 * FECHA_ERROR, OBSERVACIONES, PROYECTO_ID, SISTEMA_ID) VALUES(0, 0, 0, '', '',
			 * 0, 0); , release.getStatus().getId(), release.getRetries(),
			 * release.getOperator(), release.getMotive(), release.getId());
			 */
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
	public PRelease findReleaseById(Integer id) {
		PRelease release = (PRelease) sessionFactory.getCurrentSession().get(PRelease.class, id);
		return release;
	}

	@Override
	public PRelease_RFC findRelease_RFCById(Integer id) {
		PRelease_RFC release = (PRelease_RFC) sessionFactory.getCurrentSession().get(PRelease_RFC.class, id);
		return release;
	}

	@Override
	public void saveRelease(PRelease release, ReleaseCreate rc) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "ID";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			release.setAttributes(rc);

			for (ItemObject item : rc.getObjectItemConfiguration()) {
				if (item.getIsSql().equals("1")) {
					sql = String.format(
							" UPDATE \"SISTEMAS_OBJETO\" "
									+ " SET \"OCUPA_EJECUTAR\" = %s , \"ESQUEMA\" = '%s' , \"PLAN_EJECUCION\" = %s "
									+ " WHERE \"ID\" = %s ",
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
	public void requestRelease(PRelease release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;

		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP where 	\"ID\" = %s",
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
	public ArrayList<PReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<PReleaseObjectEdit> objects) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			for (int i = 0; i < objects.size(); i++) {
				sessionObj.save(objects.get(i));
				 sessionObj.flush();
				sql = String.format(
						" INSERT INTO \"RELEASES_RELEASE_OBJETOS\" (  \"RELEASE_ID\",\"OBJETO_ID\" ) VALUES (  %s , %s )",
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
	public void copy(PReleaseEdit release, String tpos) throws Exception {
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
			sessionObj.flush();
			if (!tpos.equalsIgnoreCase("-1")) {
				for (int i = 0; i < ids.length; i++) {
					id = Integer.parseInt(ids[i]);
					sql = String.format(
							"INSERT INTO \"RELEASES_RELEASE_REQUERIMIENTO\" ( \"RELEASE_ID\",\"REQUERIMIENTO_ID\") VALUES (  %s, %s ) ",
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
	public void assignRelease(PReleaseEdit release, PUserInfo user) throws SQLException {
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
	public PReleaseUser findReleaseUserById(Integer id) throws SQLException {
		PReleaseUser release = (PReleaseUser) sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings("deprecation")
	public Criteria criteriaByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId, boolean count) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
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
		if (!count) {
			crit.addOrder(Order.desc("createDate"));

		}

		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId, boolean count)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
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
		if (!count) {
			crit.addOrder(Order.desc("createDate"));
		}

		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaBySystems(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, boolean count)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
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
		if (!count) {
			crit.addOrder(Order.desc("createDate"));
		}
		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaByReport(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId,boolean count)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseReportFast.class);
		crit.createAlias("system", "system");
		crit.createAlias("system.proyect", "proyect");
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
		if (projectId != 0) {
			crit.add(Restrictions.eq("proyect.id", projectId));
		}
		if(count) {
			crit.addOrder(Order.desc("createDate"));
		}
		

		return crit;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaBySystemsQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseUser.class);
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
		Criteria crit = criteriaBySystems1(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId, false);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaBySystems1(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId, true);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();
		if (recordsTotal == 1) {
			crit.uniqueResult();
		}
		List<PReleases_WithoutObj> aaData = crit.list();

		for (PReleases_WithoutObj release : aaData) {
			String sql = "";
			Query query = null;
			sql = String.format(
					"SELECT COUNT(rr.\"ID\") FROM \"RELEASES_RELEASE\" rr WHERE rr.\"ID\" IN (SELECT rrd.\"TO_RELEASE_ID\"  FROM \"RELEASES_RELEASE_DEPENDENCIAS\" rrd WHERE \"FROM_RELEASE_ID\" =%s) AND rr.\"ESTADO_ID\" IN(SELECT re.\"ID\" FROM \"RELEASES_ESTADO\" re WHERE re.\"NOMBRE\" IN('Borrador', 'Solicitado')) ",
					release.getId());
			query = getSession().createSQLQuery(sql);

			BigInteger result = (BigInteger) query.uniqueResult();
			long num = result.longValue();
			BigDecimal test = BigDecimal.valueOf(num);
			release.setHaveDependecy(test.intValueExact());

		}

		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer getDependency(int id) {
		String sql = "";
		Query query = null;
		sql = String.format(
				"SELECT COUNT(rr.\"ID\") FROM \"RELEASES_RELEASE\" rr WHERE rr.\"ID\" IN (SELECT rrd.\"TO_RELEASE_ID\"  FROM \"RELEASES_RELEASE_DEPENDENCIAS\" rrd WHERE \"FROM_RELEASE_ID\" =%s) AND rr.\"ESTADO_ID\" IN(SELECT re.ID FROM \"RELEASES_ESTADO\" re WHERE re.\"NOMBRE\" IN('Borrador', 'Solicitado'))",
				id);
		query = getSession().createSQLQuery(sql);

		BigDecimal test = (BigDecimal) query.uniqueResult();

		return test.intValueExact();

	}

	public Criteria criteriaBySystems1(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId, boolean count) throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleases_WithoutObj.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "statuses").add(Restrictions.or(Restrictions.eq("statuses.name", "Certificacion")))
				.add(Restrictions.eq("system.id", systemId));

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase());
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (!count) {
			crit.addOrder(Order.desc("createDate"));
		}

		return crit;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void updateStatusReleaseRFC(PRelease_RFCFast release, String operator) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update  \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s ,\"ESTADO_ANTERIOR\" = %s, \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP where \"ID\" = %s",
					release.getStatus().getId(), release.getStatusBefore().getId(), operator, release.getMotive(),
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
	public PReleases_WithoutObj findReleaseWithouObj(Integer id) {
		PReleases_WithoutObj release = (PReleases_WithoutObj) sessionFactory.getCurrentSession()
				.createCriteria(PReleases_WithoutObj.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@Override
	public PReleaseSummaryMin findByIdMin(Integer id) {
		PReleaseSummaryMin release = (PReleaseSummaryMin) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseSummaryMin.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@Override
	public PReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease) {

		PReleaseEditWithOutObjects release = (PReleaseEditWithOutObjects) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseEditWithOutObjects.class).add(Restrictions.eq("id", idRelease)).uniqueResult();
		return release;

	}

	@Override
	public PReleaseTinySummary findByIdTiny(int id) {
		PReleaseTinySummary release = (PReleaseTinySummary) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseTinySummary.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseTrackingToError.class);
		crit.createAlias("release", "release");
		crit.createAlias("release.system", "system");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					crit.add(Restrictions.ge("trackingDate", start));
					crit.add(Restrictions.le("trackingDate", end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		crit.add(Restrictions.eq("status", "Solicitado"));
		crit.addOrder(Order.desc("trackingDate"));

		return crit.list();
	}

	@Override
	public PReleaseReport findByIdReleaseReport(Integer id) {
		PReleaseReport release = (PReleaseReport) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseReport.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PReleaseReport> listReleaseReport() {

		List<PReleaseReport> releases = (List<PReleaseReport>) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseReport.class).list();
		return releases;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId,true);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId,false);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleases_WithoutObj> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseReportFast.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("system.proyect", "proyect");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					crit.add(Restrictions.ge("createDate", start));
					crit.add(Restrictions.le("createDate", end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (projectId != 0) {
			crit.add(Restrictions.eq("proyect.id", projectId));
		}

		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}

		crit.add(Restrictions.not(Restrictions.in("status.name", Constant.FILTRED)));
		crit.addOrder(Order.desc("createDate"));

		return crit.list();
	}

	@Override
	public PRelease_RFCFast findRelease_RFCByIdFast(int id) {
		PRelease_RFCFast release = (PRelease_RFCFast) sessionFactory.getCurrentSession().get(PRelease_RFCFast.class,
				id);
		return release;
	}

	@Override
	public PReleaseTrackingShow findReleaseTracking(int id) {
		PReleaseTrackingShow release = (PReleaseTrackingShow) sessionFactory.getCurrentSession()
				.get(PReleaseTrackingShow.class, id);
		return release;
	}

	@Override
	public PReleaseSummaryFile findByIdSummaryFile(Integer id) {
		PReleaseSummaryFile release = (PReleaseSummaryFile) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseSummaryFile.class).add(Restrictions.eq("id", id)).uniqueResult();
		return release;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId,true);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId,false);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PReleases_WithoutObj> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public PReleaseUserFast findByIdReleaseUserFast(Integer idRelease) {
		PReleaseUserFast release = (PReleaseUserFast) sessionFactory.getCurrentSession()
				.createCriteria(PReleaseUserFast.class).add(Restrictions.eq("id", idRelease)).uniqueResult();
		return release;
	}

	@Override
	public void updateStatusReleaseUser(PReleaseUserFast release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			String dateChange = (release.getDateChange() != null && !release.getDateChange().equals("")
					? "to_timestamp('" + release.getDateChange() + "', 'DD-MM-YYYY HH:MI PM')"
					: "CURRENT_TIMESTAMP");
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"REINTENTOS\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = "
							+ dateChange + "  where \"ID\" = %s",
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
	public void requestRelease(PReleaseEditWithOutObjects release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;

		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP where 	\"ID\" = %s",
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
	public String getLastStatusHistory(Integer releaseId) {
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			// Consulta SQL para obtener el último ID de historial basado en el ID de
			// release y la máxima fecha
			String sql = "SELECT \"ESTADO\" FROM \"RELEASES_RELEASE_HISTORIAL\" "
					+ "WHERE \"ID\" = (SELECT MAX(\"ID\") FROM \"RELEASES_RELEASE_HISTORIAL\" WHERE \"RELEASE_ID\" = :releaseId)";
			Query query = sessionObj.createSQLQuery(sql);
			query.setParameter("releaseId", releaseId);
			String nombreEstado = (String) query.uniqueResult();
			return nombreEstado != null ? nombreEstado : ""; // Retornar cadena vacía si no hay resultados
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public JsonSheet<T> findAllFastRelease(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias,
			Integer veri) {
		JsonSheet<T> sheet = new JsonSheet<>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PReleaseUserFast.class);
		if (veri == 1) {
			criteria.addOrder(Order.desc("createDate"));
		}

		if (alias != null)
			for (Map.Entry<String, String> aliasName : alias.entrySet())
				criteria.createAlias(aliasName.getKey(), (String) aliasName.getValue());

		for (Map.Entry<String, Object> column : columns.entrySet())
			criteria = addFilters(criteria, columns);
		if (iDisplayStart != null)
			criteria.setFirstResult(iDisplayStart);
		if (iDisplayLength != null)
			criteria.setMaxResults(iDisplayLength);

		Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(PReleaseUserFast.class);
		if (alias != null)
			for (Map.Entry<String, String> aliasName : alias.entrySet())
				criteriaCount.createAlias(aliasName.getKey(), (String) aliasName.getValue());
		for (Map.Entry<String, Object> column : columns.entrySet())
			criteriaCount = addFilters(criteriaCount, columns);

		if (qSrch != null) {
			criteria.add(qSrch);
			criteriaCount.add(qSrch);
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (fetchs != null)
			for (String itemModel : fetchs)
				criteria.setFetchMode(itemModel, FetchMode.SELECT);

		criteriaCount.setProjection(Projections.countDistinct("id"));

		List<T> list = (List<T>) criteria.list();

		Long count = (Long) criteriaCount.uniqueResult();
		int recordsTotal = count.intValue();

		if (sEcho != null)
			sheet.setDraw(sEcho);
		sheet.setRecordsTotal(recordsTotal);
		sheet.setRecordsFiltered(recordsTotal);
		sheet.setData(list);
		return sheet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findByIdManager(Integer idUser) {
		 Session sessionObj = null;
		    try {
		    	
		        sessionObj = sessionFactory.openSession();
		        // Define the SQL query with a placeholder for the parameter
		        String sql = "SELECT rrrc.\"RELEASE_ID\" " +
		                     "FROM \"RELEASES_RELEASE_REQUERIMIENTO\" rrrc " +
		                     "JOIN \"RELEASES_RELEASE\" rr ON rrrc.\"RELEASE_ID\" = rr.\"ID\" " +
		                     "WHERE rrrc.\"REQUERIMIENTO_ID\" IN (" +
		                     "    SELECT \"ID\" FROM \"REQUERIMIENTOS_REQUERIMIENTO\" WHERE \"GESTOR_ID\" = :idManager" +
		                     ") AND rr.\"NODO_ID\" IS NOT NULL";

		        // Create the SQLQuery object with the parameterized query
		        SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql)
		                .addScalar("\"RELEASE_ID\"", StandardBasicTypes.INTEGER)
		                .setParameter("idManager", idUser);

		        // Execute the query and retrieve the result list
		        List<Integer> idList = query.list();
		        return idList;

		    } catch (Exception e) {
		        Sentry.capture(e, "idRequest");
		    } finally {
		        // Close the session in the finally block to ensure resource cleanup
		        if (sessionObj != null) {
		            sessionObj.close();
		        }
		    }

		    return null; // Return null if an exception occurs
	}
}