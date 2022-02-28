package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PRisk;
import com.soin.sgrm.response.JsonSheet;

public class RiskDaoImpl extends AbstractDao<Long, PRisk> implements RiskDao {

	@Override
	public PRisk getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PRisk model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PRisk model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PRisk model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PRisk model) {
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
	public void delete(PRisk model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PRisk> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PRisk> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PRisk> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PRisk> findAll() {
		Criteria crit = getSession().createCriteria(PRisk.class);
		List<PRisk> list = crit.list();
		return list;
	}

}
