package com.soin.sgrm.service.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.SystemDao;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;

@Service("systemService")
@Transactional("transactionManagerPos")
public class SystemServiceImpl implements SystemService {

	@Autowired
	SystemDao dao;

	@Override
	public PSystem findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PSystem> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PSystem model) {
		dao.saveOrUpdate(model);
	}

	@Override
	public void delete(Long id) {
		PSystem model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PSystem model) {
		dao.update(model);
	}

	@Override
	public PSystem findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public JsonSheet<PSystem> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String sProject) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();
		alias.put("project", "project");

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(Restrictions.like("code", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("name", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}

		if (sProject != null && !sProject.equals("")) {
			columns.put("projectCode", Restrictions.eq("project.code", sProject));
		}

		List<String> fetchs = new ArrayList<String>();

		JsonSheet<PSystem> list = dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias);

		return list;
	}

	@Override
	public List<PSystem> listWithProject() {
		return dao.listWithProject();
	}

}