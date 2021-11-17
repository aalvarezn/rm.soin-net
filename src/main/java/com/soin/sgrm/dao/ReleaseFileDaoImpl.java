package com.soin.sgrm.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hslf.record.Sound;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ReleaseFile;

@Repository
public class ReleaseFileDaoImpl implements ReleaseFileDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void save(Integer id, ReleaseFile releaseFile) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		ReleaseFile file = null;
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
						"INSERT INTO releases_release_archivos ( id, release_id, archivo_id) VALUES ( null, %s, %s ) ",
						id, releaseFile.getId());
				query = sessionObj.createSQLQuery(sql);
				query.executeUpdate();
			}
			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de agregar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public ReleaseFile findReleaseFile(String path) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseFile.class);
		crit.add(Restrictions.eq("path", path));
		ReleaseFile releaseFile = (ReleaseFile) crit.uniqueResult();
		return releaseFile;
	}

	@Override
	public ReleaseFile findReleaseFileById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseFile.class);
		crit.add(Restrictions.eq("id", id));
		ReleaseFile releaseFile = (ReleaseFile) crit.uniqueResult();
		return releaseFile;
	}

	@Override
	public void deleteReleaseFile(ReleaseFile releaseFile) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		ReleaseFile file = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sql = String.format("DELETE FROM releases_release_archivos WHERE archivo_id = %s ", releaseFile.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(releaseFile);

			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} finally {
			sessionObj.close();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> ImpactObjects(Integer release_id) {
		Session sessionObj = null;
		String sql = "";
		try {
			sessionObj = sessionFactory.openSession();
			sql = String.format(
					" SELECT concat(concat(concat(top.nombre, '^'), concat(so.nombre, '^')), concat(concat(so.revision_respositorio,'^'), "
							+ " concat((CASE (SELECT 1 FROM releases_release r1 "
							+ " INNER JOIN releases_release_objetos ro1 " + "    ON ro1.release_id = r1.id "
							+ " INNER JOIN sistemas_objeto so1 " + "    ON ro1.objeto_id = so1.id "
							+ " INNER JOIN releases_estado e1 " + "    on r.estado_id = e1.id "
							+ " WHERE r1.id <> %s AND so.item_de_configuracion_id = so1.item_de_configuracion_id "
							+ " AND so.tipo_objeto_id = so1.tipo_objeto_id AND r1.sistema_id = r.sistema_id AND so1.nombre = so.nombre AND e1.nombre <> 'Anulado' "
							+ " AND r1.fecha_creacion < r.fecha_creacion AND ROWNUM = 1) WHEN 1 THEN 'MODIFICADO' ELSE 'NUEVO' END),'^'))) item "
							+ " FROM releases_release r " + " INNER JOIN releases_release_objetos ro "
							+ "    ON ro.release_id = r.id " + " INNER JOIN sistemas_objeto so "
							+ "    ON ro.objeto_id = so.id " + " INNER JOIN sistemas_tipoobjecto top "
							+ "    ON so.tipo_objeto_id = top.id " + " WHERE r.id = %s ",
					release_id, release_id);

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql).addScalar("item", StandardBasicTypes.STRING);

			List<String> items = query.list();

			return items;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sessionObj.close();
		}

		return null;
	}
}
