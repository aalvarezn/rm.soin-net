package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PReportFile;

@Repository
public class PReportFileDaoImpl extends AbstractDao<Long, PReportFile> implements PReportFileDao    {

	@Override
	public PReportFile findReportFile(String path) {
		PReportFile reportFile=(PReportFile) getSession().createCriteria(PReportFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return reportFile;
	}
	
	@Override
	public PReportFile findReportFileById(Integer id) {
		PReportFile reportFile=(PReportFile) getSession().createCriteria(PReportFile.class).add(Restrictions.eq("idRelease", id)).uniqueResult();
		return reportFile;
	}


	@Override
	public void saveReportFile(Integer id, PReportFile reportFile) throws Exception {

		PReportFile file = null;
		try {
			
		
			file = findReportFile(reportFile.getPath());

			// si ya existe solo se actualiza la fecha
			if (file != null) {
				file.setRevisionDate(reportFile.getRevisionDate());
				getSession().saveOrUpdate(file);
			}else {
				getSession().saveOrUpdate(reportFile);
			}
		} catch (Exception e) {
			Sentry.capture(e, "reportFile");
			getSession().getTransaction().rollback();
			throw new Exception("Error al procesar la solicitud de agregar.", e);
		} 
	}

	@Override
	public void deleteReport(PReportFile reportFile) throws Exception {
	
		try {
			getSession().delete(reportFile);

		} catch (Exception e) {
			Sentry.capture(e, "reportFile");
			throw new Exception("Error al procesar la solicitud de eliminar archivo.", e);
		} 
	}

}
