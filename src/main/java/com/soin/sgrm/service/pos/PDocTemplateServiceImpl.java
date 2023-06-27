package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.DocTemplateDao;
import com.soin.sgrm.dao.pos.PDocTemplateDao;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.pos.PDocTemplate;


@Transactional("transactionManagerPos")
@Service("PDocTemplateService")
public class PDocTemplateServiceImpl implements PDocTemplateService {
	
	@Autowired
	PDocTemplateDao dao;

	@Override
	public List<PDocTemplate> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public PDocTemplate findByTemplateName(String templateName) {
		return dao.findByTemplateName(templateName);
	}

	@Override
	public PDocTemplate findById(Integer docId) {
		return dao.findById(docId);
	}

	@Override
	public List<PDocTemplate> list() {
		return dao.list();
	}

	@Override
	public void save(PDocTemplate docTemplate) {
		dao.save(docTemplate);
	}

	@Override
	public void update(PDocTemplate docTemplate) {
		dao.update(docTemplate);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
