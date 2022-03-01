package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PTypeProcess;
import com.soin.sgrm.response.JsonSheet;

public class TypeProcessDaoImpl extends AbstractDao<Long, PTypeProcess> implements TypeProcessDao{

	@Override
	public PTypeProcess getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PTypeProcess model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PTypeProcess model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PTypeProcess model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTypeProcess model) {
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
	public void delete(PTypeProcess model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PTypeProcess> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeProcess> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTypeProcess> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PTypeProcess> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
