package com.soin.sgrm.dao;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.RequestBaseFile;

@Repository
public class RequestBaseFileDaoImpl extends AbstractDao<Long, RequestBaseFile> implements RequestBaseFileDao    {


	@Override
	public RequestBaseFile findRequestFile(String path) {
		RequestBaseFile requestFile=(RequestBaseFile) getSession().createCriteria(RequestBaseFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return requestFile;
	}

	@Override
	public void saveRequestFile(Long id, RequestBaseFile requestFile) throws Exception {
		String sql = "";
		Query query = null;
		RequestBaseFile file = null;
		try {
			
		
			file = findRequestFile(requestFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(requestFile.getRevisionDate());
				getSession().saveOrUpdate(file);
			} else {
				// si no existe, se crea y se agrega la referencia intermedia
				getSession().saveOrUpdate(requestFile);

				sql = String.format(
						"INSERT INTO \"SOLICITUD_ARCHIVOSOLICITUD\" ( \"ID_SOLICITUD\", \"ARCHIVO_ID\") VALUES ( %s, %s ) ",
						id, requestFile.getId());
				query = getSession().createSQLQuery(sql);
				query.executeUpdate();
			}
		} catch (Exception e) {
			Sentry.capture(e, "requestFile");
			getSession().getTransaction().rollback();
			throw new Exception("Error al procesar la solicitud de agregar.", e);
		} 
	}

	@Override
	public void deleteRequest(RequestBaseFile requestFile) throws Exception {
		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM \"SOLICITUD_ARCHIVOSOLICITUD\" WHERE \"ARCHIVO_ID\" = %s ", requestFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(requestFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "requestFile");
			throw new Exception("Error al procesar la solicitud de eliminar archivo.", e);
		} 
	}

}
