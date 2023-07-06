package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class ReleaseObjectDaoImpl implements ReleaseObjectDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(rObj);
			sql = String.format(
					"INSERT INTO RELEASES_RELEASE_OBJETOS ( id, release_id, objeto_id) VALUES ( null, %s, %s ) ",
					release.getId(), rObj.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
			return rObj;
		} catch (Exception e) {
			Sentry.capture(e, "releaseObject");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteObject(Integer releaseId, ReleaseObject object) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sql = String.format("DELETE FROM releases_release_objetos WHERE release_id = %s AND objeto_id = %s ",
					releaseId, object.getId());

			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(object);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "releaseObject");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public ReleaseObjectEdit findById(Integer id) {
		return (ReleaseObjectEdit) sessionFactory.getCurrentSession().get(ReleaseObjectEdit.class, id);
	}

	@Override
	public ReleaseUser findReleaseToAddByObject(ReleaseObjectEdit obj, Release release) {
		String sql = String.format(
				"select * from (  " + 
						"select r.id, r.numero_release from releases_release r  " + 
						"inner join releases_estado e on e.id = r.estado_id " + 
						"inner join sistemas_sistema s on s.id = r.sistema_id " + 
						"inner join releases_release_objetos rob on rob.release_id = r.id " + 
						"inner join sistemas_objeto obj on rob.objeto_id = obj.id " + 
						"where r.id <> :id and  e.nombre <> 'Anulado' and  e.nombre <> 'Borrador' and obj.nombre = :nombre and obj.item_de_configuracion_id = :tipoItem " + 
						"and obj.tipo_objeto_id = :tipoObjeto and r.fecha_creacion < :fechaRelease " + 
				"order by r.fecha_creacion desc ) where rownum <= 1");

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("ID", new IntegerType())
				.addScalar("NUMERO_RELEASE", new StringType());
		query.setInteger("id", release.getId());
		query.setString("nombre", obj.getName());
		query.setInteger("tipoItem", obj.getItemConfiguration());
		query.setInteger("tipoObjeto", obj.getTypeObject());
		query.setDate("fechaRelease", release.getCreateDate());
		Object[] result = (Object[]) query.uniqueResult();
		ReleaseUser rUser = null;
		if (result != null) {
			rUser = new ReleaseUser();
			rUser.setId((Integer) result[0]);
			rUser.setReleaseNumber((String) result[1]);
		}
		return rUser;
	}

	@Override
	public ReleaseUser findReleaseToDeleteByObject(Release release, ReleaseObject obj) {
		String sql = String.format(
				"select * from (  " + 
						"select r.id, r.numero_release, s.id as sistema, r.fecha_creacion from releases_release r  " + 
						"inner join releases_estado e on e.id = r.estado_id " + 
						"inner join sistemas_sistema s on s.id = r.sistema_id " + 
						"inner join releases_release_objetos rob on rob.release_id = r.id " + 
						"inner join sistemas_objeto obj on rob.objeto_id = obj.id " + 
						"where r.id <> :id and  e.nombre <> 'Anulado' and obj.nombre = :nombre and obj.item_de_configuracion_id = :tipoItem " + 
						"and obj.tipo_objeto_id = :tipoObjeto and r.fecha_creacion < :fechaRelease " + 
				"order by r.fecha_creacion desc ) where rownum <= 1");

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("ID", new IntegerType())
				.addScalar("NUMERO_RELEASE", new StringType()).addScalar("SISTEMA", new IntegerType())
				.addScalar("FECHA_CREACION", new DateType());
		query.setInteger("id", release.getId());
		query.setString("nombre", obj.getName());
		query.setInteger("tipoItem", obj.getConfigurationItem().getId());
		query.setInteger("tipoObjeto", obj.getTypeObject().getId());
		query.setDate("fechaRelease", release.getCreateDate());
		Object[] result = (Object[]) query.uniqueResult();
		ReleaseUser rUser = null;
		if (result != null) {
			rUser = new ReleaseUser();
			rUser.setId((Integer) result[0]);
			rUser.setReleaseNumber((String) result[1]);
			SystemInfo system = new SystemInfo();
			system.setId((Integer) result[2]);
			rUser.setSystem(system);
			rUser.setCreateDate((Date) result[3]);
		}
		return rUser;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> findReleaseToAddByObjectList(ArrayList<ReleaseObjectEdit> objects, ReleaseEdit release) {
		String concatObjet = "";
		for (int i = 0; i < objects.size(); i++) {
			concatObjet = concatObjet + "'" + objects.get(i).getName() + "," + objects.get(i).getItemConfiguration()
					+ "," + objects.get(i).getTypeObject() + "'" + (((i + 1) == objects.size()) ? "" : ",");
		}

 		String sql = String.format(
				"select DISTINCT(d.id) , r1.numero_release from releases_release r1 " + "inner join "
						+ "(select nombre, max(id) id from  ( "
						+ "select DISTINCT(r.id), r.numero_release, o.nombre, o.fecha_revision from releases_release r "
						+ "inner join releases_release_objetos ro " 
						+ "	on ro.release_id = r.id "
						+ "inner join sistemas_objeto o " 
						+ "	on o.id = ro.objeto_id "
						+ "inner join releases_estado e " 
						+ "	on e.id = r.estado_id  where r.id != :id and "
						+ "o.nombre || ',' || o.item_de_configuracion_id || ',' || o.tipo_objeto_id in ( %s ) "
						+ "and r.sistema_id = :system and r.fecha_creacion < :fecha and e.nombre <> 'Anulado' and e.nombre <> 'Borrador'  ) " 
						+ "group by nombre) d " 
						+ "on r1.id = d.id ", concatObjet);

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("ID", new IntegerType())
				.addScalar("NUMERO_RELEASE", new StringType());
		query.setInteger("id", release.getId());
		query.setInteger("system", release.getSystem().getId());
		query.setDate("fecha", release.getCreateDate());
		List<Object[]> list = query.list();
		if (list.isEmpty())
			return new ArrayList<Object[]>();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCoDependencies(ArrayList<ReleaseObject> objects, ReleaseUser release) {
		String concatObjet = "";
		for (int i = 0; i < objects.size(); i++) {
			concatObjet = concatObjet + "'" + objects.get(i).getName() + ","
					+ objects.get(i).getConfigurationItem().getId() + "," + objects.get(i).getTypeObject().getId() + "'"
					+ (((i + 1) == objects.size()) ? "" : ",");
		}

		if(objects.size() == 0)
			concatObjet = "''";

		String sql = String.format(
				"select DISTINCT(r.id), r.numero_release from releases_release r "
						+ "inner join releases_release_objetos ro " + "    on ro.release_id = r.id "
						+ "inner join sistemas_objeto o " + "    on o.id = ro.objeto_id "
						+ "inner join releases_estado e " + "    on e.id = r.estado_id " + "where r.id != :id and "
						+ "o.nombre || ',' || o.item_de_configuracion_id || ',' || o.tipo_objeto_id in ( %s ) "
						+ "and r.sistema_id = :system and r.fecha_creacion < :fecha and e.nombre <> 'Anulado' and e.nombre <> 'Borrador' ",
						concatObjet);

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("ID", new IntegerType())
				.addScalar("NUMERO_RELEASE", new StringType());
		query.setInteger("id", release.getId());
		query.setInteger("system", release.getSystem().getId());
		query.setDate("fecha", release.getCreateDate());
		List<Object[]> list = query.list();
		if (list.isEmpty())
			return null;
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listObjectsByReleases(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer releaseId, Integer sql) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaObjects( sEcho, iDisplayStart, iDisplayLength, sSearch,
				releaseId,sql);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaObjects( sEcho, iDisplayStart, iDisplayLength, sSearch,
				releaseId,sql);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();
		List<ReleaseObjectEdit> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}
	public Criteria criteriaObjects(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId,Integer sql)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", systemId));

		if(sql==1) {
			crit.add(Restrictions.eq("objects.isSql", sql));
		}

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.like("objects.name", sSearch, MatchMode.ANYWHERE).ignoreCase());


		return crit;
	}

	@Override
	public Integer listCountByReleases(Integer releaseId) throws ParseException, SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", releaseId));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
	
		return recordsTotal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Release_Objects> listObjectsSql(Integer idRelease) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", idRelease));
			crit.add(Restrictions.eq("objects.isSql", 1));
		
		return crit.list();
	}

}
