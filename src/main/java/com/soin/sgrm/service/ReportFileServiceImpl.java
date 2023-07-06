package com.soin.sgrm.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCFileDao;
import com.soin.sgrm.dao.ReportFileDao;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.ReportFile;

@Service("ReportFileService")
@Transactional("transactionManager")
public class ReportFileServiceImpl implements ReportFileService {
	
	@Autowired
	ReportFileDao dao;
	@Override
	public ReportFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public ReportFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<ReportFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(ReportFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		ReportFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(ReportFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveReportFile(Integer id, ReportFile rfcFile) throws Exception{
		dao.saveReportFile(id,rfcFile);
	}

	@Override
	public void deleteReport(ReportFile reportFile) throws Exception {
		dao.deleteReport(reportFile);
	}

	@Override
	public ReportFile findReportFileById(Integer id) {
		
		return dao.findReportFileById(id);
	}

}
