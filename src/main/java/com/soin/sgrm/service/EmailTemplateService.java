package com.soin.sgrm.service;

import java.sql.Timestamp;
import java.util.List;

import javax.mail.MessagingException;

import com.soin.sgrm.dao.BaseDao;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.security.UserLogin;

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

	void sendMailActorIncidence(WFIncidence incidenceEmail, EmailTemplate emailActor);

	void sendMailNotify(WFIncidence incidenceEmail, EmailTemplate emailNotify, String userS);

	void sendMailIncidence(Incidence incidenceEmail, EmailTemplate email) throws Exception;

	void sendMailIncidence(WFIncidence incidenceEmail, EmailTemplate email, String motive);

	void sendMailNotifyChangeStatus(String numRequest, String type, String statusName, String operator,
			Timestamp requestDate, UserLogin userLogin, String senders, EmailTemplate emailNotify,String subject, String motive, String note, String title);

	void sendMailNotifyChangeStatusError(String typeError, String numRequest, String type, String statusName,
			String operator, Timestamp requestDate, UserLogin userLogin, String senders, EmailTemplate emailNotify,String subject,
			String motive, String note, String title);

	void sendMailNotifyChangeUserIncidence(String numTicket, User userOperator, String motive, Timestamp timestamp,
			User newUser, EmailTemplate emailNotify);

	void sendMailNotifyChangeStatusWebService(String releaseNumber, String type, String statusName, String operator,
			Timestamp convertStringToTimestamp, UserInfo user, String senders, EmailTemplate emailNotify,
			String subject, String motive, String link, String title);
}

