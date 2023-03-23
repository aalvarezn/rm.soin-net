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

import com.soin.sgrm.dao.RFCErrorDao;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.response.JsonSheet;

@Transactional("transactionManager")
@Service("RFCErrorService")
public class RFCErrorServiceImpl  implements RFCErrorService{
	@Autowired
	RFCErrorDao dao;
	
	@Override
	public RFCError findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public RFCError findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<RFCError> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(RFCError model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		RFCError model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(RFCError model) {
		dao.update(model);
	}

	@Override
	public JsonSheet<RFCError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long sigesId, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("rfc", "rfc");
		alias.put("rfc.user", "user");
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
					Restrictions.like("rfc.numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()
					);
		}
		if (errorId == 0) {
			errorId = null;
		}
		if (errorId != null) {
			
			columns.put("error", Restrictions.eq("error.id", errorId));
		}
		if (sigesId == 0) {
			sigesId = null;
		}
		
		if (sigesId != null) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.eq("siges.id", sigesId));
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

	@Override
	public List<RFCError> findAllList(Long errorId, String dateRange, Long sigesId, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("rfc", "rfc");
		alias.put("rfc.user", "user");
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


		if (errorId == 0) {
			errorId = null;
		}
		if (errorId != null) {
			
			columns.put("error", Restrictions.eq("error.id", errorId));
		}
		if (sigesId == 0) {
			sigesId = null;
		}
		
		if (sigesId != null) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.eq("siges.id", sigesId));
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
		return  dao.findAll( columns, fetchs, alias,2);
	}
}