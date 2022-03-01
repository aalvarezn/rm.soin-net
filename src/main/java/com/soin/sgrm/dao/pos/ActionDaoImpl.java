package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PAction;
import com.soin.sgrm.response.JsonSheet;

public class ActionDaoImpl extends AbstractDao<Long, PAction> implements ActionDao {

	@Override
	public PAction getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PAction model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PAction model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PAction model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PAction model) {
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
	public void delete(PAction model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PAction> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PAction> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PAction> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PAction> findAll() {
		Criteria crit = getSession().createCriteria(PAction.class);
		List<PAction> list = crit.list();
		return list;
	}

}
