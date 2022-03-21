package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PPriority;

@Repository("priorityDao")
public class PriorityDaoImpl extends AbstractDao<Long, PPriority> implements PriorityDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PPriority> findAll() {
		Criteria crit = createEntityCriteria();
		List<PPriority> list = crit.list();
		return list;
	}

}
