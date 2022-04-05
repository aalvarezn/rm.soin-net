package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PEmailTemplate;

public interface EmailTemplateService extends BaseService<Long, PEmailTemplate>{


	

	void sendMail(String to, String cc, String subject, String body)  throws Exception;

}