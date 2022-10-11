package com.soin.sgrm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ReleaseErrorDao;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.response.JsonSheet;

@Transactional("transactionManager")
@Service("ReleaseErrorService")
public class ReleaseErrorServiceImpl  implements ReleaseErrorService{
	@Autowired
	ReleaseErrorDao dao;
	
	@Override
	public ReleaseError findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public ReleaseError findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<ReleaseError> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(ReleaseError model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		ReleaseError model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(ReleaseError model) {
		dao.update(model);
	}

	@Override
	public JsonSheet<ReleaseError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, int projectId, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("release", "release");
		alias.put("release.user", "user");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("errorDate", Restrictions.between("errorDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(
					Restrictions.like("release.releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()
					);
		}
		if (errorId == 0) {
			errorId = null;
		}
		if (errorId != null) {
			
			columns.put("error", Restrictions.eq("error.id", errorId));
		}

		if (projectId != 0) {
			alias.put("project", "project");
			columns.put("project", Restrictions.eq("project.id", projectId));
		}
		if (systemId != 0) {
			alias.put("system", "system");
			columns.put("system", Restrictions.eq("system.id", systemId));

		}
		 

		List<String> fetchs = new ArrayList<String>();
		fetchs.add("system");
		fetchs.add("error");
		fetchs.add("release");
		fetchs.add("project");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,2);
	
	}

}
