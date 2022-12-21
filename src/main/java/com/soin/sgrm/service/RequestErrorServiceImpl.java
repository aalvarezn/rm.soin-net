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

import com.soin.sgrm.dao.RequestErrorDao;
import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.response.JsonSheet;

@Transactional("transactionManager")
@Service("RequestErrorService")
public class RequestErrorServiceImpl  implements RequestErrorService{
	@Autowired
	RequestErrorDao dao;
	
	@Override
	public RequestError findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public RequestError findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<RequestError> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(RequestError model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		RequestError model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(RequestError model) {
		dao.update(model);
	}

	@Override
	public JsonSheet<RequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetitionId, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("request", "request");
		alias.put("request.user", "user");
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
					Restrictions.like("request.numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()
					);
		}
		if (errorId == 0) {
			errorId = null;
		}
		if (errorId != null) {
			
			columns.put("error", Restrictions.eq("error.id", errorId));
		}
		if (typePetitionId == 0) {
			typePetitionId = null;
		}
		
		if (typePetitionId != null) {
			alias.put("typePetition", "typePetition");
			columns.put("typePetition", Restrictions.eq("typePetition.id", typePetitionId));
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