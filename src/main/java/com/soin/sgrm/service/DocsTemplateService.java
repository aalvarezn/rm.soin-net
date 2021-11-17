package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.DocsTemplate;

public interface DocsTemplateService {
	
	List<DocsTemplate> findBySystem(Integer id);
	
	DocsTemplate findByTemplateName(String templateName);
	
	DocsTemplate findById(Integer docId);

}
