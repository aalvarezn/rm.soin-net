package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.EmailTemplate;

public interface EmailTemplateDao  extends BaseDao<Integer, EmailTemplate>{

	List<EmailTemplate> listAll();

	EmailTemplate findById(Integer id);

	void updateEmail(EmailTemplate email);

	void saveEmail(EmailTemplate email);

	void deleteEmail(Integer id);

	boolean existEmailTemplate(String name);

	boolean existEmailTemplate(String name, Integer id);

}
