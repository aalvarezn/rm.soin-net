package com.soin.sgrm.dao;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFCFile;

@Repository
public class RFCFileDaoImpl extends AbstractDao<Long, RFCFile> implements RFCFileDao    {

	@Override
	public RFCFile findRFCFile(String path) {
		RFCFile rfcFile=(RFCFile) getSession().createCriteria(RFCFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return rfcFile;
	}

	@Override
	public void saveRFCFile(Long id, RFCFile rfcFile) throws Exception {
		String sql = "";
		Query query = null;
		RFCFile file = null;
		try {
			
		
			file = findRFCFile(rfcFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(rfcFile.getRevisionDate());
				getSession().saveOrUpdate(file);
			} else {
				// si no existe, se crea y se agrega la referencia intermedia
				getSession().saveOrUpdate(rfcFile);

				sql = String.format(
						"INSERT INTO sgrm.\"RFC_ARCHIVORFC\" ( \"RFC_ID\", \"ARCHIVORFC_ID\") VALUES ( %s, %s ) ",
						id, rfcFile.getId());
				query = getSession().createSQLQuery(sql);
				query.executeUpdate();
			}
		} catch (Exception e) {
			Sentry.capture(e, "rfcFile");
			getSession().getTransaction().rollback();
			throw new Exception("Error al procesar la solicitud de agregar.", e);
		} 
	}

	@Override
	public void deleteRFC(RFCFile rfcFile) throws Exception {
		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM sgrm.\"RFC_ARCHIVORFC\" WHERE \"ARCHIVORFC_ID\" = %s ", rfcFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(rfcFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "rfcFile");
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} 
	}

}
