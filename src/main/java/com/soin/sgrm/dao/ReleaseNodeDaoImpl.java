package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ReleaseNode;

@Repository
public class ReleaseNodeDaoImpl extends AbstractDao<Long,ReleaseNode> implements ReleaseNodeDao {

	@Autowired
	private SessionFactory sessionFactory;
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean findReleaseNode(Integer idRelease,Integer idNode) {
		List<ReleaseNode> result =  sessionFactory.getCurrentSession().createCriteria(ReleaseNode.class)
				.add(Restrictions.eq("idRelease", idRelease))
			    .add(Restrictions.eq("idNode", idNode)) // 
			    .list();
		return !result.isEmpty();
	
	}


	
	
	
	

}