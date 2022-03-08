package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PUser;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Long, PUser> implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PUser> findAllColumns(String[] columns) {
		Criteria crit = createEntityCriteria();
		ProjectionList pList = Projections.projectionList();
		for (String col : columns)
			pList.add(Projections.property(col).as(col));
		crit.setProjection(pList);
		crit.setResultTransformer(Transformers.aliasToBean(PUser.class)).list();
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PUser> findbyUserName(String[] userNames) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.in("userName", userNames));
		return crit.list();
	}

}
