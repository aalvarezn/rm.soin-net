package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PImpact;

import com.soin.sgrm.response.JsonSheet;

@Repository("impactDao")
public class ImpactDaoImpl extends AbstractDao<Long, PImpact> implements ImpactDao{

	@Override
	public PImpact getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByKey(Long key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PImpact> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PImpact> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PImpact> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PImpact> findAll() {
		
		Criteria crit = getSession().createCriteria(PImpact.class);
		List<PImpact> list = crit.list();
		return list;
	}

}
