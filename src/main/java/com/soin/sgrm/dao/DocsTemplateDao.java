package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.DocsTemplate;

public interface DocsTemplateDao {
	
	List<DocsTemplate> findBySystem(Integer id);
	
	DocsTemplate findByTemplateName(String templateName);
	
	DocsTemplate findById(Integer docId);

}
