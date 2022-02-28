package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;


import com.soin.sgrm.model.pos.PTypeChange;
import com.soin.sgrm.response.JsonSheet;

public class TypeChangeDaoImpl extends AbstractDao<Long, PTypeChange> implements TypeChangeDao {

	@Override
	public PTypeChange getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTypeChange model) {
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
	public void delete(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PTypeChange> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeChange> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeChange> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PTypeChange> findAll() {
		Criteria crit = getSession().createCriteria(PTypeChange.class);
		List<PTypeChange> list = crit.list();
		return list;
	}

}
