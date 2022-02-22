package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.DocTemplateDao;
import com.soin.sgrm.model.DocTemplate;


@Transactional("transactionManager")
@Service("DocTemplateService")
public class DocTemplateServiceImpl implements DocTemplateService {
	
	@Autowired
	DocTemplateDao dao;

	@Override
	public List<DocTemplate> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public DocTemplate findByTemplateName(String templateName) {
		return dao.findByTemplateName(templateName);
	}

	@Override
	public DocTemplate findById(Integer docId) {
		return dao.findById(docId);
	}

	@Override
	public List<DocTemplate> list() {
		return dao.list();
	}

	@Override
	public void save(DocTemplate docTemplate) {
		dao.save(docTemplate);
	}

	@Override
	public void update(DocTemplate docTemplate) {
		dao.update(docTemplate);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
