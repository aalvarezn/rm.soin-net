package com.soin.sgrm.dao.pos;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReleaseFile;
import com.soin.sgrm.model.pos.PRFCFile;

@Repository("rfcFileDao")
public class RFCFileDaoImpl  extends AbstractDao<Long, PRFCFile> implements RFCFileDao   {

	
	
	@Override
	public void saveRFCFile(Long id, PRFCFile rfcFile) throws Exception {

		String sql = "";
		Query query = null;
		PRFCFile file = null;
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
						"INSERT INTO public.\"RFC_ARCHIVORFC\" ( \"RFC_ID\", \"ARCHIVORFC_ID\") VALUES ( %s, %s ) ",
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
	public PRFCFile findRFCFile(String path) {
		
		PRFCFile rfcFile=(PRFCFile) getSession().createCriteria(PRFCFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		
		return rfcFile;
	}
	
	@Override
	public void deleteRFC(PRFCFile rfcFile) throws Exception {

		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM public.\"RFC_ARCHIVORFC\" WHERE \"ARCHIVORFC_ID\" = %s ", rfcFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(rfcFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "rfcFile");
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} 

	}
}
