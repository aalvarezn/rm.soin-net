package com.soin.sgrm.dao.pos;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAttentionGroup;


@Repository
public class PAttentionGroupDaoImpl extends AbstractDao<Long,PAttentionGroup> implements PAttentionGroupDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PAttentionGroup> findGroupByUserId(Integer id) {
		Criteria crit = getSession().createCriteria(PAttentionGroup.class);
		crit.createAlias("userAttention","userAttention");
		crit.add(Restrictions.eq("userAttention.id", id));
		return crit.list();
	}





}
								