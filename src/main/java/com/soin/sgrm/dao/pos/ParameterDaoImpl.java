package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PParameter;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.response.JsonSheet;

public class ParameterDaoImpl extends AbstractDao<Long, PParameter> implements ParameterDao {

	@Override
	public PParameter getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PParameter model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PParameter model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PParameter model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PParameter model) {
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
	public void delete(PParameter model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PParameter> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PParameter> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PParameter> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PParameter> findAll() {
		Criteria crit = getSession().createCriteria(PParameter.class);
		List<PParameter> list = crit.list();
		return list;
	}

}
