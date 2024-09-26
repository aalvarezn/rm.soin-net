package com.soin.sgrm.dao.pos;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseObject;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PRelease_Objects;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PReleaseObjectDaoImpl implements PReleaseObjectDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public PReleaseObjectEdit saveObject(PReleaseObjectEdit rObj, PRelease release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(rObj);
			sessionObj.flush();
			sql = String.format(
					"INSERT INTO \"RELEASES_RELEASE_OBJETOS\" ( \"RELEASE_ID\", \"OBJETO_ID\") VALUES (  %s, %s ) ",
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
	public void deleteObject(Integer releaseId, PReleaseObject object) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sql = String.format("DELETE FROM \"RELEASES_RELEASE_OBJETOS\" WHERE \"RELEASE_ID\" = %s AND \"OBJETO_ID\" = %s ",
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
	public PReleaseObjectEdit findById(Integer id) {
		return (PReleaseObjectEdit) sessionFactory.getCurrentSession().get(PReleaseObjectEdit.class, id);
	}

	@Override
	public PReleaseUser findReleaseToAddByObject(PReleaseObjectEdit obj, PRelease release) {
		
		String sql = String.format(
				"select * from (  " + 
						"select r.\"ID\", r.\"NUMERO_RELEASE\" from \"RELEASES_RELEASE\" r  " + 
						"inner join \"RELEASES_ESTADO\" e on e.\"ID\" = r.\"ESTADO_ID\" " + 
						"inner join \"SISTEMAS_SISTEMA\" s on s.\"ID\" = r.\"SISTEMA_ID\" " + 
						"inner join \"RELEASES_RELEASE_OBJETOS\" rob on rob.\"RELEASE_ID\" = r.\"ID\" " + 
						"inner join \"SISTEMAS_OBJETO\" obj on rob.\"OBJETO_ID\" = obj.\"ID\" " + 
						"where r.\"ID\" <> :id and  e.\"NOMBRE\" <> 'Anulado' and  e.\"NOMBRE\" <> 'Borrador' and obj.\"NOMBRE\" = :nombre and obj.\"ITEM_DE_CONFIGURACION_ID\" = :tipoItem " + 
						"and obj.\"TIPO_OBJETO_ID\" = :tipoObjeto and r.\"FECHA_CREACION\" < :fechaRelease " + 
				"order by r.\"FECHA_CREACION\" desc ) as test LIMIT 1");

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("\"ID\"", new IntegerType())
				.addScalar("\"NUMERO_RELEASE\"", new StringType());
		query.setInteger("id", release.getId());
		query.setString("nombre", obj.getName());
		query.setInteger("tipoItem", obj.getItemConfiguration());
		query.setInteger("tipoObjeto", obj.getTypeObject());
		query.setDate("fechaRelease", release.getCreateDate());
		Object[] result = (Object[]) query.uniqueResult();
		PReleaseUser rUser = null;
		if (result != null) {
			rUser = new PReleaseUser();
			rUser.setId((Integer) result[0]);
			rUser.setReleaseNumber((String) result[1]);
		}
		return rUser;
	}

	@Override
	public PReleaseUser findReleaseToDeleteByObject(PRelease release, PReleaseObject obj) {
		
		String sql = String.format(
				"select * from (  " + 
						"select r.\"ID\", r.\"NUMERO_RELEASE\", s.\"ID\" as \"SISTEMA\", r.\"FECHA_CREACION\" from \"RELEASES_RELEASE\" r  " + 
						"inner join \"RELEASES_ESTADO\" e on e.\"ID\" = r.\"ESTADO_ID\" " + 
						"inner join \"SISTEMAS_SISTEMA\" s on s.\"ID\" = r.\"SISTEMA"
						+ "_ID\" " + 
						"inner join \"RELEASES_RELEASE_OBJETOS\" rob on rob.\"RELEASE_ID\" = r.\"ID\" " + 
						"inner join \"SISTEMAS_OBJETO\" obj on rob.\"OBJETO_ID\" = obj.\"ID\" " + 
						"where r.\"ID\" <> :id and  e.\"NOMBRE\" <> 'Anulado' and obj.\"NOMBRE\" = :nombre and obj.\"ITEM_DE_CONFIGURACION_ID\" = :tipoItem " + 
						"and obj.\"TIPO_OBJETO_ID\" = :tipoObjeto and r.\"FECHA_CREACION\" < :fechaRelease " + 
				"order by r.\"FECHA_CREACION\" desc ) as test LIMIT 1");

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("\"ID\"", new IntegerType())
				.addScalar("\"NUMERO_RELEASE\"", new StringType()).addScalar("\"SISTEMA\"", new IntegerType())
				.addScalar("\"FECHA_CREACION\"", new DateType());
		query.setInteger("id", release.getId());
		query.setString("nombre", obj.getName());
		query.setInteger("tipoItem", obj.getConfigurationItem().getId());
		query.setInteger("tipoObjeto", obj.getTypeObject().getId());
		query.setDate("fechaRelease", release.getCreateDate());
		Object[] result = (Object[]) query.uniqueResult();
		PReleaseUser rUser = null;
		if (result != null) {
			rUser = new PReleaseUser();
			rUser.setId((Integer) result[0]);
			rUser.setReleaseNumber((String) result[1]);
			PSystemInfo system = new PSystemInfo();
			system.setId((Integer) result[2]);
			rUser.setSystem(system);
			rUser.setCreateDate((Date) result[3]);
		}
		return rUser;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> findReleaseToAddByObjectList(ArrayList<PReleaseObjectEdit> objects, PReleaseEdit release) {
		String concatObjet = "";
		for (int i = 0; i < objects.size(); i++) {
			concatObjet = concatObjet + "'" + objects.get(i).getName() + "," + objects.get(i).getItemConfiguration()
					+ "," + objects.get(i).getTypeObject() + "'" + (((i + 1) == objects.size()) ? "" : ",");
		}
		
 		String sql = String.format(
				"select DISTINCT(d.\"ID\") , r1.\"NUMERO_RELEASE\" from \"RELEASES_RELEASE\" r1 " + "inner join "
						+ "(select \"NOMBRE\", max(\"ID\") \"ID\" from  ( "
						+ "select DISTINCT(r.\"ID\"), r.\"NUMERO_RELEASE\", o.\"NOMBRE\", o.\"FECHA_REVISION\" from \"RELEASES_RELEASE\" r "
						+ "inner join \"RELEASES_RELEASE_OBJETOS\" ro " 
						+ "	on ro.\"RELEASE_ID\" = r.\"ID\" "
						+ "inner join \"SISTEMAS_OBJETO\" o " 
						+ "	on o.\"ID\" = ro.\"OBJETO_ID\" "
						+ "inner join \"RELEASES_ESTADO\" e " 
						+ "	on e.\"ID\" = r.\"ESTADO_ID\"  where r.\"ID\" != :id and "
						+ "o.\"NOMBRE\" || ',' || o.\"ITEM_DE_CONFIGURACION_ID\" || ',' || o.\"TIPO_OBJETO_ID\" in ( %s ) "
						+ "and r.\"SISTEMA_ID\" = :system and r.\"FECHA_CREACION\" < :fecha and e.\"NOMBRE\" <> 'Anulado' and e.\"NOMBRE\" <> 'Borrador'  ) " 
						+ " as test group by \"NOMBRE\") d " 
						+ "on r1.\"ID\" = d.\"ID\" ", concatObjet);

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("\"ID\"", new IntegerType())
				.addScalar("\"NUMERO_RELEASE\"", new StringType());
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
	public List<Object[]> findCoDependencies(ArrayList<PReleaseObject> objects, PReleaseUser release) {
		String concatObjet = "";
		for (int i = 0; i < objects.size(); i++) {
			concatObjet = concatObjet + "'" + objects.get(i).getName() + ","
					+ objects.get(i).getConfigurationItem().getId() + "," + objects.get(i).getTypeObject().getId() + "'"
					+ (((i + 1) == objects.size()) ? "" : ",");
		}

		if(objects.size() == 0)
			concatObjet = "''";

		String sql = String.format(
				"select DISTINCT(r.\"ID\"), r.\"NUMERO_RELEASE\" from \"RELEASES_RELEASE\" r "
						+ "inner join \"RELEASES_RELEASE_OBJETOS\" ro " + "    on ro.\"RELEASE_ID\" = r.\"ID\" "
						+ "inner join \"SISTEMAS_OBJETO\" o " + "    on o.\"ID\" = ro.\"OBJETO_ID\" "
						+ "inner join \"RELEASES_ESTADO\" e " + "    on e.\"ID\" = r.\"ESTADO_ID\" " + "where r.\"ID\" != :id and "
						+ "o.\"NOMBRE\" || ',' || o.\"ITEM_DE_CONFIGURACION_ID\" || ',' || o.\"TIPO_OBJETO_ID\" in ( %s ) "
						+ "and r.\"SISTEMA_ID\" = :system and r.\"FECHA_CREACION\" < :fecha and e.\"NOMBRE\" <> 'Anulado' and e.\"NOMBRE\" <> 'Borrador' ",
						concatObjet);

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("\"ID\"", new IntegerType())
				.addScalar("\"NUMERO_RELEASE\"", new StringType());
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
		List<PReleaseObjectEdit> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}
	public Criteria criteriaObjects(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId,Integer sql)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRelease_Objects.class);
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
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRelease_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", releaseId));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
	
		return recordsTotal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PRelease_Objects> listObjectsSql(Integer idRelease) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRelease_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", idRelease));
			crit.add(Restrictions.eq("objects.isSql", 1));
		
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PRelease_Objects> listObjects(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRelease_Objects.class);
		crit.createAlias("objects", "objects")
		.add(Restrictions.eq("releaseId", id));
		
		return crit.list();
	}

}
