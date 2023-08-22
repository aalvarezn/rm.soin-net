package com.soin.sgrm.service.pos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCFileDao;
import com.soin.sgrm.dao.ReportFileDao;
import com.soin.sgrm.dao.pos.PReportFileDao;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.ReportFile;
import com.soin.sgrm.model.pos.PReportFile;

@Service("PReportFileService")
@Transactional("transactionManagerPos")
public class PReportFileServiceImpl implements PReportFileService {
	
	@Autowired
	PReportFileDao dao;
	@Override
	public PReportFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PReportFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PReportFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PReportFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PReportFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PReportFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveReportFile(Integer id, PReportFile rfcFile) throws Exception{
		dao.saveReportFile(id,rfcFile);
	}

	@Override
	public void deleteReport(PReportFile reportFile) throws Exception {
		dao.deleteReport(reportFile);
	}

	@Override
	public PReportFile findReportFileById(Integer id) {
		
		return dao.findReportFileById(id);
	}

}
