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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.BaseKnowledgeDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.MyLevel;

@Service("BaseKnowledgeService")
@Transactional("transactionManager")
public class BaseKnowledgeServiceImpl implements BaseKnowledgeService {

	@Autowired
	BaseKnowledgeDao dao;

	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(BaseKnowledgeServiceImpl.class);

	@Override
	public BaseKnowledge findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseKnowledge> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(BaseKnowledge model) {
		dao.save(model);

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(BaseKnowledge model) {
		dao.update(model);

	}

	@Override
	public BaseKnowledge findById(Long id) {

		return dao.getById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<BaseKnowledge> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
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
			qSrch = Restrictions.or(Restrictions.like("numError", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
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

	public String verifySecuence(String partCode) {
		String numError = "";
		try {
			int amount = existNumError(partCode);

			if (amount == 0) {
				numError = partCode + "_01";
				return numError;
			} else {
				if (amount < 10) {
					numError = partCode + "_0" + (amount + 1);
					return numError;
				}
				numError = partCode + "_" + (amount + 1);
				return numError;
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";
	}

	public String generateErrorNumber(String component) {
		String numError = "";
		String partCode = "";
		try {

			partCode =  component ;
			numError = verifySecuence(partCode);

		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			numError = "Sin Asignar";
		}
		return numError;
	}

	@Override
	public Integer existNumError(String numberError) throws SQLException {
		return dao.existNumError(numberError);
	}

	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		// TODO Auto-generated method stub
		return dao.countByType(id, type, query, ids);
	}

	@Override
	public Integer countByManager(Integer id, Long idBaseKnowledge) {
		return dao.countByManager(id, idBaseKnowledge);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<BaseKnowledge> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long sStatus, String dateRange, Long component) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

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

					Restrictions.like("numError", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (sStatus == 0) {
			sStatus = null;
		}

		if (component == 0) {
			component = null;
		}
		
		if (sStatus != null) {

			columns.put("status", Restrictions.eq("status.id", sStatus));
		} 

	
		if (component != null) {
			columns.put("component", Restrictions.eq("component.id", component));

		} 

		List<String> fetchs = new ArrayList<String>();
		//fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}

}
