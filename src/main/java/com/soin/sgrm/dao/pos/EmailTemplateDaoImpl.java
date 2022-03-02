package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PEmailTemplate;

@Repository("emailTemplateDao")
public class EmailTemplateDaoImpl extends AbstractDao<Long, PEmailTemplate> implements EmailTemplateDao  {

}
