package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.pos.PDocTemplate;

public interface PDocTemplateService {
	
	List<PDocTemplate> findBySystem(Integer id);

	PDocTemplate findByTemplateName(String templateName);

	List<PDocTemplate> list();

	PDocTemplate findById(Integer docId);

	void save(PDocTemplate docTemplate);

	void update(PDocTemplate docTemplate);

	void delete(Integer id);

}
