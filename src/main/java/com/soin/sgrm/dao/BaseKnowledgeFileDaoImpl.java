package com.soin.sgrm.dao;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.BaseKnowledgeFile;

@Repository
public class BaseKnowledgeFileDaoImpl extends AbstractDao<Long, BaseKnowledgeFile> implements BaseKnowledgeFileDao    {

	@Override
	public BaseKnowledgeFile findBaseKnowledgeFile(String path) {
		BaseKnowledgeFile BaseKnowledgeFile=(BaseKnowledgeFile) getSession().createCriteria(BaseKnowledgeFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return BaseKnowledgeFile;
	}

	@Override
	public void saveBaseKnowledgeFile(Long id, BaseKnowledgeFile BaseKnowledgeFile) throws Exception {
		String sql = "";
		Query query = null;
		BaseKnowledgeFile file = null;
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
						"INSERT INTO \"RFC_ARCHIVORFC\" ( \"RFC_ID\", \"ARCHIVORFC_ID\") VALUES ( %s, %s ) ",
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
	public void deleteBaseKnowLedgeFile(BaseKnowledgeFile baseKnowledgeFile) throws Exception {
		String sql = "";
		Query query = null;
		try {
			sql = String.format("DELETE FROM \"RFC_ARCHIVORFC\" WHERE \"ARCHIVORFC_ID\" = %s ", baseKnowledgeFile.getId());
			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			getSession().delete(baseKnowledgeFile);

		
		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledgeFile");
			throw new Exception("Error al procesar la solucitud de eliminar archivo.", e);
		} 
	}

}
