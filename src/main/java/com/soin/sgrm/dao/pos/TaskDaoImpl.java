package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PTask;
import com.soin.sgrm.response.JsonSheet;

public class TaskDaoImpl extends AbstractDao<Long, PTask> implements TaskDao{

	@Override
	public PTask getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PTask model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PTask model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PTask model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTask model) {
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
	public void delete(PTask model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PTask> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTask> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PTask> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PTask> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
