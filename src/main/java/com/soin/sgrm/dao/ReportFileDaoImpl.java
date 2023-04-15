package com.soin.sgrm.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReportFile;

@Repository
public class ReportFileDaoImpl extends AbstractDao<Long, ReportFile> implements ReportFileDao    {

	@Override
	public ReportFile findReportFile(String path) {
		ReportFile reportFile=(ReportFile) getSession().createCriteria(ReportFile.class).add(Restrictions.eq("path", path)).uniqueResult();
		return reportFile;
	}
	
	@Override
	public ReportFile findReportFileById(Integer id) {
		ReportFile reportFile=(ReportFile) getSession().createCriteria(ReportFile.class).add(Restrictions.eq("idRelease", id)).uniqueResult();
		return reportFile;
	}


	@Override
	public void saveReportFile(Integer id, ReportFile reportFile) throws Exception {

		ReportFile file = null;
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
	public void deleteReport(ReportFile reportFile) throws Exception {
	
		try {
			getSession().delete(reportFile);

		} catch (Exception e) {
			Sentry.capture(e, "reportFile");
			throw new Exception("Error al procesar la solicitud de eliminar archivo.", e);
		} 
	}

}
