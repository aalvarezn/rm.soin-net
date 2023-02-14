package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.EmailIncidence;
import com.soin.sgrm.model.System_Priority;

@Repository
public class EmailIncidenceDaoImpl extends AbstractDao<Long, EmailIncidence> implements EmailIncidenceDao{
	

	@SuppressWarnings("unchecked")
	@Override
	public List<EmailIncidence> listTypePetition(){
	    return getSession().createCriteria(EmailIncidence.class)
	    		.add(Restrictions.eq("status", 1))
	    		.list();
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmailIncidence> findBySystem(Integer id) {
		
		Criteria crit = getSession().createCriteria(EmailIncidence.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<EmailIncidence> systemList = crit.list();
		return systemList;
		
	}

	@Override
	public EmailIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId) {
		Criteria crit = getSession().createCriteria(EmailIncidence.class);
		crit.createAlias("system", "system");
		crit.createAlias("typeIncidence", "typeIncidence");
		crit.add( Restrictions.eq("system.id", systemId));
		crit.add( Restrictions.eq("typeIncidence.id", typeIncidenceId));
		EmailIncidence system_Priority = (EmailIncidence) crit.uniqueResult();
		return system_Priority;
	}
}
