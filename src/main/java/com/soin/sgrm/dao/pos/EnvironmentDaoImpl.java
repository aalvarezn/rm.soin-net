package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PEnvironment;

import com.soin.sgrm.response.JsonSheet;

@Repository("environmentDao")
public class EnvironmentDaoImpl extends AbstractDao<Long, PEnvironment> implements EnvironmentDao {

	@Override
	public PEnvironment getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PEnvironment model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void merge(PEnvironment model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate(PEnvironment model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PEnvironment model) {
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
	public void delete(PEnvironment model) {
		// TODO Auto-generated method stub

	}

	@Override
	public JsonSheet<PEnvironment> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PEnvironment> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PEnvironment> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PEnvironment> findAll() {
		Criteria crit = getSession().createCriteria(PEnvironment.class);
		List<PEnvironment> list = crit.list();
		return list;
	}

}
