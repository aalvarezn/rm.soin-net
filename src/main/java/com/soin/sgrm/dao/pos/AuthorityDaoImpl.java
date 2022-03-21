package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAuthority;

@Repository("authorityDao")
public class AuthorityDaoImpl extends AbstractDao<Long, PAuthority> implements AuthorityDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PAuthority> findByCode(String[] roles) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.in("code", roles));
		return crit.list();
	}

}
