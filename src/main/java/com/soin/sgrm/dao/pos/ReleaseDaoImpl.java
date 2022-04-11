package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PSiges;

@Repository("releaseDao")
public class ReleaseDaoImpl extends AbstractDao<Long, PRelease> implements ReleaseDao{

	@SuppressWarnings("unchecked")
	public List<PRelease> listReleasesBySystem(Long id){
	    return getSession().createCriteria(PRelease.class)
	    		.createAlias("system","systems")
	    		.createAlias("status", "statuses")
	    		.add(Restrictions.eq("systems.id", id))
	    		.add(Restrictions.or(Restrictions.eq("statuses.name","Certificacion"),Restrictions.eq("statuses.name","Solicitado")))
	    		.list();
		}
}
