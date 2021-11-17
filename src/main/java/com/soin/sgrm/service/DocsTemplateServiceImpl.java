package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.DocsTemplateDao;
import com.soin.sgrm.model.DocsTemplate;


@Transactional("transactionManager")
@Service("DocsTemplateService")
public class DocsTemplateServiceImpl implements DocsTemplateService {
	
	@Autowired
	DocsTemplateDao templateDao;

	@Override
	public List<DocsTemplate> findBySystem(Integer id) {
		return templateDao.findBySystem(id);
	}

	@Override
	public DocsTemplate findByTemplateName(String templateName) {
		return templateDao.findByTemplateName(templateName);
	}

	@Override
	public DocsTemplate findById(Integer docId) {
		return templateDao.findById(docId);
	}

}
