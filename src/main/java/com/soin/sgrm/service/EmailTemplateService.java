package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.dao.BaseDao;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.model.wf.WFRelease;

public interface EmailTemplateService extends BaseDao<Integer, EmailTemplate> {

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

	void sendMail(WFRelease releaseEmail, EmailTemplate email, String motive);

	void sendMailActor(WFRelease releaseEmail, EmailTemplate email);

	void sendMailRFC(RFC rfcEmail, EmailTemplate email) throws Exception;
	
	void sendMailRequestR4(RequestBaseR1 requestEmail, EmailTemplate email) throws Exception;
	

	void sendMailNotify(WFRelease releaseEmail, EmailTemplate email,String user);

	void sendMailActorRFC(WFRFC rfcEmail, EmailTemplate emailActor);

	void sendMailNotifyRFC(WFRFC rfcEmail, EmailTemplate emailNotify, String user);

	void sendMailRFC(WFRFC rfcEmail, EmailTemplate email, String motive);

}
