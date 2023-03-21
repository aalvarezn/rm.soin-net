package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.WFRFCDao;
import com.soin.sgrm.dao.wf.WFReleaseDao;
import com.soin.sgrm.dao.wf.WFUserDao;
import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManager")
@Service("WFRFCService")
public class WFRFCServiceImpl implements WFRFCService {

	@Autowired
	private WFRFCDao dao;

	@Override
	public List<WFRFC> list() {
		return dao.list();
	}

	@Override
	public WFRFC findWFRFCById(Long id) {
		return dao.findWFRFCById(id);
	}

	@Override
	public JsonSheet<?> listWorkFlowRFC(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId)
			throws SQLException, ParseException {
		return dao.listWorkFlowRFC(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);
	}

	@Override
	public void wfStatusRFC(WFRFC rfc) {
		dao.wfStatusRFC(rfc);
	}

	@Override
	public JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId,
			Object[] systemsId) throws SQLException, ParseException {
		return dao.listWorkFlowManager(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId, statusId, systemsId);
	}

	@Override
	public Integer countByType(String group, Object[] ids) {
		return dao.countByType(group, ids);
	}

	@Override
	public void wfStatusRFCWithOutMin(WFRFC rfc) {
		dao.wfStatusRFCWithOutMin(rfc);
		
	}



}