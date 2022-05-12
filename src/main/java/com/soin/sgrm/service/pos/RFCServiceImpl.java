package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.RFCDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("rfcService")
@Transactional("transactionManagerPos")
public class RFCServiceImpl implements RFCService {

	@Autowired
	RFCDao dao;

	public static final Logger logger = Logger.getLogger(RFCServiceImpl.class);

	@Override
	public PRFC findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PRFC> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRFC model) {
		dao.save(model);

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PRFC model) {
		dao.update(model);

	}

	@Override
	public PRFC findById(Long id) {

		return dao.getById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<PRFC> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long sStatus, String dateRange, Long sPriority, Long sImpact) {
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
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.name", sSearch, MatchMode.ANYWHERE).ignoreCase()
					
					);
		}
		if (sStatus == 0) {
			sStatus = null;
		}
		if (sPriority == 0) {
			sPriority = null;
		}
		if (sImpact == 0) {
			sImpact = null;
		}
		if (sStatus != null) {
			
			columns.put("status", Restrictions.eq("status.id", sStatus));
		}

		if (sPriority != null) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (sImpact != null) {
			alias.put("impact", "impact");
			columns.put("impact", Restrictions.or(Restrictions.eq("impact.id", sImpact)));

		}
		 

		List<String> fetchs = new ArrayList<String>();
		
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias);
	}

	public String verifySecuence(String partCode) {
		String numRFC = "";
		try {
			int amount = existNumRelease(partCode);

			if (amount == 0) {
				numRFC = partCode + "_01_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRFC;
			} else {
				if (amount < 10) {
					numRFC = partCode + "_0" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
					return numRFC;
				}
				numRFC = partCode + "_" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRFC;
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";

	}

	public String generateRFCNumber(String codeProject) {
		String numRFC = "";
		String partCode = "";
		try {

			partCode = "RFC_" + codeProject + "_SC";

			numRFC = verifySecuence(partCode);

		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			numRFC = "Sin Asignar";
		}
		return numRFC;
	}

	@Override
	public Integer existNumRelease(String number_release) throws SQLException {
		return dao.existNumRelease(number_release);
	}

	@Override
	public Integer countByType(String name, String type, int query, Object[] ids) {
		// TODO Auto-generated method stub
		return dao.countByType(name, type, query, ids);
	}

	@Override
	public JsonSheet<PRFC> findAll(String name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long sStatus, String dateRange, Long sPriority, Long sImpact) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("status", "status");
		alias.put("user", "user");
		columns.put("user",(Restrictions.eq("user.userName", name)));
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
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.name", sSearch, MatchMode.ANYWHERE).ignoreCase()
					
					
					);
		}
		if (sStatus == 0) {
			sStatus = null;
		}
		if (sPriority == 0) {
			sPriority = null;
		}
		if (sImpact == 0) {
			sImpact = null;
		}
		if (sStatus != null) {
			
			columns.put("status", Restrictions.eq("status.id", sStatus));
		}else {
			columns.put("status",Restrictions.not(Restrictions.in("status.name",
					Constant.FILTRED)));
		}

		if (sPriority != null) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (sImpact != null) {
			alias.put("impact", "impact");
			columns.put("impact", Restrictions.or(Restrictions.eq("impact.id", sImpact)));

		}
	
		
		 

		List<String> fetchs = new ArrayList<String>();
		
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias);
	}

}
