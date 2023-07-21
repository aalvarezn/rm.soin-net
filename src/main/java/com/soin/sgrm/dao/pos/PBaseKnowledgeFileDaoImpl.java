package com.soin.sgrm.dao.pos;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PBaseKnowledgeFile;

@Repository
public class PBaseKnowledgeFileDaoImpl extends AbstractDao<Long, PBaseKnowledgeFile> implements PBaseKnowledgeFileDao    {

	@Override
	public PBaseKnowledgeFile findBaseKnowledgeFile(String path) {
		PBaseKnowledgeFile BaseKnowledgeFile=(PBaseKnowledgeFile) getSession().createCriteria(PBaseKnowledgeFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return BaseKnowledgeFile;
	}

	@Override
	public void saveBaseKnowledgeFile(Long id, PBaseKnowledgeFile BaseKnowledgeFile) throws Exception {
		String sql = "";
		Query query = null;
		PBaseKnowledgeFile file = null;
		try {
			
		
			file = findBaseKnowledgeFile(BaseKnowledgeFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(BaseKnowledgeFile.getRevisionDate());
				getSession().saveOrUpdate(file);
			} else {
				// si no existe, se crea y se agrega la referencia intermedia
				getSession().saveOrUpdate(BaseKnowledgeFile);

				sql = String.format(
						"INSERT INTO \"BASECONO_ARCHIVOBASE\" ( \"BASE_ID\", \"ARCHIVOBASECONO_ID\") VALUES ( %s, %s ) ",
						id, BaseKnowledgeFile.getId());
				query = getSession().createSQLQuery(sql);
				query.executeUpdate();
			}
		} catch (Exception e) {
			Sentry.capture(e, "BaseKnowledgeFile");
			getSession().getTransaction().rollback();
			throw new Exception("Error al procesar la solicitud de agregar.", e);
		} 
	}

	@Override
	public void deleteBaseKnowLedgeFile(PBaseKnowledgeFile baseKnowledgeFile) throws Exception {
		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM \"BASECONO_ARCHIVOBASE\" WHERE \"ARCHIVOBASECONO_ID\" = %s ", baseKnowledgeFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(baseKnowledgeFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledgeFile");
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} 
	}

}
