package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PDocTemplate;

public interface PDocTemplateDao {

	List<PDocTemplate> findBySystem(Integer id);

	PDocTemplate findByTemplateName(String templateName);

	List<PDocTemplate> list();

	PDocTemplate findById(Integer docId);

	void save(PDocTemplate docTemplate);

	void update(PDocTemplate docTemplate);

	void delete(Integer id);

}
