package com.soin.sgrm.service.pos;

import java.sql.Timestamp;
import java.util.List;

import com.soin.sgrm.dao.BaseDao;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.model.pos.wf.PWFRFC;
import com.soin.sgrm.model.pos.wf.PWFRelease;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.security.UserLogin;

public interface PEmailTemplateService extends BaseDao<Integer, PEmailTemplate> {

	List<PEmailTemplate> listAll();

	PEmailTemplate findById(Integer id);

	void sendMail(String to, String cc, String subject, String html) throws Exception;

	void updateEmail(PEmailTemplate email);

	void saveEmail(PEmailTemplate email);

	void deleteEmail(Integer id);

	void sendMail(PRelease release, PEmailTemplate email) throws Exception;

	void sendMail(PUserInfo user, String password, PEmailTemplate email) throws Exception;

	boolean existEmailTemplate(String name);

	boolean existEmailTemplate(String name, Integer id);

	void sendMail(PWFRelease releaseEmail, PEmailTemplate email, String motive);

	void sendMailActor(PWFRelease releaseEmail, PEmailTemplate email);

	void sendMailRFC(PRFC rfcEmail, PEmailTemplate email) throws Exception;
	
	void sendMailRequestR4(PRequestBaseR1 requestEmail, PEmailTemplate email) throws Exception;
	

	void sendMailNotify(PWFRelease releaseEmail, PEmailTemplate email,String user);

	void sendMailActorRFC(PWFRFC rfcEmail, PEmailTemplate emailActor);

	void sendMailNotifyRFC(PWFRFC rfcEmail, PEmailTemplate emailNotify, String user);

	void sendMailRFC(WFRFC rfcEmail, PEmailTemplate email, String motive);

	void sendMailActorIncidence(WFIncidence incidenceEmail, PEmailTemplate emailActor);

	void sendMailNotify(WFIncidence incidenceEmail, PEmailTemplate emailNotify, String userS);

	void sendMailIncidence(Incidence incidenceEmail, PEmailTemplate email) throws Exception;

	void sendMailIncidence(WFIncidence incidenceEmail, PEmailTemplate email, String motive);

	void sendMailNotifyChangeStatus(String numRequest, String type, String statusName, String operator,
			Timestamp requestDate, UserLogin userLogin, String senders, PEmailTemplate emailNotify,String subject, String motive, String note, String title);

	void sendMailNotifyChangeStatusError(String typeError, String numRequest, String type, String statusName,
			String operator, Timestamp requestDate, UserLogin userLogin, String senders, PEmailTemplate emailNotify,String subject,
			String motive, String note, String title);

	void sendMailNotifyChangeUserIncidence(String numTicket, User userOperator, String motive, Timestamp timestamp,
			User newUser, PEmailTemplate emailNotify);

}

