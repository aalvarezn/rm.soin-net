package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PReportFile;

public interface PReportFileService extends BaseService<Long, PReportFile> {
	void saveReportFile(Integer id, PReportFile rfcFile) throws Exception;
	void deleteReport(PReportFile reportFile) throws Exception;
	PReportFile findReportFileById(Integer id);
}
