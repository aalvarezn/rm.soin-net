package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PTypeRequest;
import com.soin.sgrm.response.JsonSheet;

public class TypeRequestDaoImpl extends AbstractDao<Long, PTypeRequest> implements TypeRequestDao {

	@Override
	public PTypeRequest getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTypeRequest model) {
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
	public void delete(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PTypeRequest> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeRequest> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeRequest> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PTypeRequest> findAll() {
		Criteria crit = getSession().createCriteria(PTypeRequest.class);
		List<PTypeRequest> list = crit.list();
		return list;
	}

}
