package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PReleaseFile;

@Repository
public class PReleaseFileDaoImpl implements PReleaseFileDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public void save(Integer id, PReleaseFile releaseFile) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		PReleaseFile file = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			file = findReleaseFile(releaseFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(releaseFile.getRevisionDate());
				sessionObj.saveOrUpdate(file);
			} else {
				// si no existe, se crea y se agrega la referencia intermedia
				sessionObj.saveOrUpdate(releaseFile);

				sql = String.format(
						"INSERT INTO \"RELEASES_RELEASE_ARCHIVOS\" ( \"RELEASE_ID\", \"ARCHIVO_ID\") VALUES (  %s, %s ) ",
						id, releaseFile.getId());
				query = sessionObj.createSQLQuery(sql);
				query.executeUpdate();
			}
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "releaseFile");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de agregar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PReleaseFile findReleaseFile(String path) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseFile.class);
		crit.add(Restrictions.eq("path", path));
		PReleaseFile releaseFile = (PReleaseFile) crit.uniqueResult();
		return releaseFile;
	}

	@Override
	public PReleaseFile findReleaseFileById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PReleaseFile.class);
		crit.add(Restrictions.eq("id", id));
		PReleaseFile releaseFile = (PReleaseFile) crit.uniqueResult();
		return releaseFile;
	}

	@Override
	public void deleteReleaseFile(PReleaseFile releaseFile) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sql = String.format("DELETE FROM \"RELEASES_RELEASE_ARCHIVOS\" WHERE \"ARCHIVO_ID\" = %s ", releaseFile.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(releaseFile);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "releaseFile");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} finally {
			sessionObj.close();
		}

	}

	@SuppressWarnings({ "unchecked" })
	public List<String> ImpactObjects(Integer release_id) {
		Session sessionObj = null;
		String sql = "ESTADO_ID";
		try {
			sessionObj = sessionFactory.openSession();
			sql = String.format(
					" SELECT concat(concat(concat(top.\"NOMBRE\", '^'), concat(so.\"NOMBRE\", '^')), concat(concat(so.\"REVISION_RESPOSITORIO\",'^'), "
							+ " concat((CASE (SELECT 1 FROM \"RELEASES_RELEASE\" r1 "
							+ " INNER JOIN \"RELEASES_RELEASE_OBJETOS\" ro1 " + "    ON ro1.\"RELEASE_ID\" = r1.\"ID\" "
							+ " INNER JOIN \"SISTEMAS_OBJETO\" so1 " + "    ON ro1.\"OBJETO_ID\" = so1.\"ID\" "
							+ " INNER JOIN \"RELEASES_ESTADO\" e1 " + "    on r.\"ESTADO_ID\" = e1.\"ID\" "
							+ " WHERE r1.\"ID\" <> %s AND so.\"ITEM_DE_CONFIGURACION_ID\" = so1.\"ITEM_DE_CONFIGURACION_ID\" "
							+ " AND so.\"TIPO_OBJETO_ID\" = so1.\"TIPO_OBJETO_ID\" AND r1.\"SISTEMA_ID\" = r.\"SISTEMA_ID\" AND so1.\"NOMBRE\" = so.\"NOMBRE\" AND e1.\"NOMBRE\" <> 'Anulado' "
							+ " AND r1.\"FECHA_CREACION\" < r.\"FECHA_CREACION\" LIMIT 1) WHEN 1 THEN 'MODIFICADO' ELSE 'NUEVO' END),'^'))) item "
							+ " FROM \"RELEASES_RELEASE\" r " + " INNER JOIN \"RELEASES_RELEASE_OBJETOS\" ro "
							+ "    ON ro.\"RELEASE_ID\" = r.\"ID\" " + " INNER JOIN \"SISTEMAS_OBJETO\" so "
							+ "    ON ro.\"OBJETO_ID\" = so.\"ID\" " + " INNER JOIN \"SISTEMAS_TIPOOBJECTO\" top "
							+ "    ON so.\"TIPO_OBJETO_ID\" = top.\"ID\" " + " WHERE r.\"ID\" = %s ",
					release_id, release_id);

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql).addScalar("item", StandardBasicTypes.STRING);

			List<String> items = query.list();

			return items;

		} catch (Exception e) {
			Sentry.capture(e, "releaseFile");
		} finally {
			sessionObj.close();
		}

		return null;
	}
}
