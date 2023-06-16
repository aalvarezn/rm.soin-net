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
import com.soin.sgrm.dao.RequestNewDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("RequestNewService")
@Transactional("transactionManager")
public class RequestNewServiceImpl implements RequestNewService {

	@Autowired
	RequestNewDao dao;

	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(RequestNewServiceImpl.class);

	@Override
	public Request findById(Integer id) {
		
		return dao.getById(id);
	}

	@Override
	public Request findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<Request> findAll() {
	
		return dao.findAll();
	}

	@Override
	public void save(Request model) {
		dao.save(model);
	}

	@Override
	public void delete(Integer id) {
		Request model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Request model) {
		dao.update(model);
	}



	@Override
	public com.soin.sgrm.utils.JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, int proyectId, int typeId, Integer userLogin) throws ParseException {
		
		return dao.findAll( sEcho,  iDisplayStart,  iDisplayLength,
				 sSearch,  proyectId,  typeId,  userLogin) ;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	

}