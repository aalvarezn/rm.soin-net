package com.soin.sgrm.dao;


import com.soin.sgrm.model.ReportFile;

public interface ReportFileDao extends BaseDao<Long, ReportFile> {

	ReportFile findReportFile(String path);

	void saveReportFile(Integer id, ReportFile reportFile) throws Exception;

	void deleteReport(ReportFile reportFile) throws Exception;

	ReportFile findReportFileById(Integer id);
}
