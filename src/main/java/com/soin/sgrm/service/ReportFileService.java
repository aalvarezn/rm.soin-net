package com.soin.sgrm.service;

import com.soin.sgrm.model.ReportFile;

public interface ReportFileService extends BaseService<Long, ReportFile> {
	void saveReportFile(Integer id, ReportFile rfcFile) throws Exception;
	void deleteReport(ReportFile reportFile) throws Exception;
	ReportFile findReportFileById(Integer id);
}
