package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.UserInfo;

public interface EmailTemplateService {

	List<EmailTemplate> listAll();

	EmailTemplate findById(Integer id);

	void sendMail(String to, String cc, String subject, String html) throws Exception;

	void updateEmail(EmailTemplate email);

	void saveEmail(EmailTemplate email);

	void deleteEmail(Integer id);

	void sendMail(Release release, EmailTemplate email) throws Exception;

	void sendMail(UserInfo user, String password, EmailTemplate email) throws Exception;

	boolean existEmailTemplate(String name);

	boolean existEmailTemplate(String name, Integer id);

}
