package com.soin.sgrm.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCDao;
import com.soin.sgrm.dao.RFCWithOutReleaseDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("RFCWithoutReleaseService")
@Transactional("transactionManager")
public class RFCWithoutReleaseServiceImpl implements RFCWithoutReleaseService {

	@Autowired
	RFCWithOutReleaseDao dao;

	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(RFCWithoutReleaseServiceImpl.class);

	@Override
	public RFC_WithoutRelease findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RFC_WithoutRelease> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(RFC_WithoutRelease model) {
		dao.save(model);

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(RFC_WithoutRelease model) {
		dao.update(model);

	}

	@Override
	public RFC_WithoutRelease findById(Long id) {

		return dao.getById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<RFC_WithoutRelease> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long sStatus, String dateRange, int sPriority, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("status", "status");
		alias.put("user", "user");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("requestDate", Restrictions.between("requestDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("reasonChange", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}
		if (sStatus == 0) {
			sStatus = null;
		}
		if (sStatus != null) {

			columns.put("status", Restrictions.eq("status.id", sStatus));
		}

		if (sPriority != 0) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (systemId != 0) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

		}

		List<String> fetchs = new ArrayList<String>();

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<RFC_WithoutRelease> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long sStatus, String dateRange, int sPriority, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
				.createAlias("managers", "managers").add(Restrictions.eq("managers.id", id)).list();
		alias.put("status", "status");

		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("requestDate", Restrictions.between("requestDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			alias.put("user", "user");
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("reasonChange", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (sStatus == 0) {
			sStatus = null;
		}

		if (sStatus != null) {

			columns.put("status", Restrictions.eq("status.id", sStatus));
		} else {
			columns.put("status", Restrictions.not(Restrictions.in("status.name", Constant.FILTREDRFC)));
		}

		if (sPriority != 0) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (systemId != 0) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

		} else {
			List<Integer> listaId = new ArrayList<Integer>();
			for (SystemInfo system : systems) {
				listaId.add(system.getId());
			}
			alias.put("systemInfo", "systemInfo");
			columns.put("systemInfo", (Restrictions.in("systemInfo.id", listaId)));
		}

		List<String> fetchs = new ArrayList<String>();
		fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}

}
