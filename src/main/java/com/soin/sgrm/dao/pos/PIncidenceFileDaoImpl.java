package com.soin.sgrm.dao.pos;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PIncidenceFile;


@Repository
public class PIncidenceFileDaoImpl extends AbstractDao<Long, PIncidenceFile> implements PIncidenceFileDao    {

	@Override
	public PIncidenceFile findIncidenceFile(String path) {
		PIncidenceFile incidenceFile=(PIncidenceFile) getSession().createCriteria(PIncidenceFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return incidenceFile;
	}

	@Override
	public void saveIncidenceFile(Long id, PIncidenceFile incidenceFile) throws Exception {
		String sql = "";
		Query query = null;
		PIncidenceFile file = null;
		try {
			
		
			file = findIncidenceFile(incidenceFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(incidenceFile.getRevisionDate());
				getSession().saveOrUpdate(file);
			} else {
				// si no existe, se crea y se agrega la referencia intermedia
				getSession().saveOrUpdate(incidenceFile);

				sql = String.format(
						"INSERT INTO \"INCIDENCIA_ARCHIVOINCIDENCIA\" ( \"ID_INCIDENCIA\", \"ARCHIVO_ID\") VALUES ( %s, %s ) ",
						id, incidenceFile.getId());
				query = getSession().createSQLQuery(sql);
				query.executeUpdate();
			}
		} catch (Exception e) {
			Sentry.capture(e, "PIncidenceFile");
			getSession().getTransaction().rollback();
			throw new Exception("Error al procesar la solicitud de agregar.", e);
		} 
	}

	@Override
	public void deleteIncidence(PIncidenceFile incidenceFile) throws Exception {
		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM \"INCIDENCIA_ARCHIVOINCIDENCIA\" WHERE \"ARCHIVO_ID\" = %s ", incidenceFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(incidenceFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "PIncidenceFile");
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} 
	}

}
