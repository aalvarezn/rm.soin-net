package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.DocTemplate;

public interface DocTemplateService {
	
	List<DocTemplate> findBySystem(Integer id);

	DocTemplate findByTemplateName(String templateName);

	List<DocTemplate> list();

	DocTemplate findById(Integer docId);

	void save(DocTemplate docTemplate);

	void update(DocTemplate docTemplate);

	void delete(Integer id);

}
