package com.soin.sgrm.service.pos;

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

import com.soin.sgrm.dao.pos.RFCDao;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.Constant;

@Service("rfcService")
@Transactional("transactionManagerPos")
public class RFCServiceImpl implements RFCService {

	@Autowired
	RFCDao dao;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PRFC model) {
		// TODO Auto-generated method stub

	}

	@Override
	public PRFC findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<PRFC> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Integer sStatus, String dateRange) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

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
			qSrch = Restrictions.or(Restrictions.like("rowId", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}

		List<String> fetchs = new ArrayList<String>();

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias);
	}

}
