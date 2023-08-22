package com.soin.sgrm.dao.pos;


import com.soin.sgrm.model.pos.PReportFile;

public interface PReportFileDao extends BaseDao<Long, PReportFile> {

	PReportFile findReportFile(String path);

	void saveReportFile(Integer id, PReportFile reportFile) throws Exception;

	void deleteReport(PReportFile reportFile) throws Exception;

	PReportFile findReportFileById(Integer id);
}
