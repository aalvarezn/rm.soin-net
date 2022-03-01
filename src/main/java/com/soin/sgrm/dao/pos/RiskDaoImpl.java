package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRisk;

@Repository("riskDao")
public class RiskDaoImpl extends AbstractDao<Long, PRisk> implements RiskDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PRisk> findAll() {
		Criteria crit = createEntityCriteria();
		List<PRisk> list = crit.list();
		return list;
	}

}
