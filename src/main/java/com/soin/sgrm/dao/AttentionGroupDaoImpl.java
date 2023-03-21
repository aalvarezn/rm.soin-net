package com.soin.sgrm.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.response.JsonSheet;


@Repository
public class AttentionGroupDaoImpl extends AbstractDao<Long,AttentionGroup> implements AttentionGroupDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AttentionGroup> findGroupByUserId(Integer id) {
		Criteria crit = getSession().createCriteria(AttentionGroup.class);
		crit.createAlias("userAttention","userAttention");
		crit.add(Restrictions.eq("userAttention.id", id));
		return crit.list();
	}





}
								