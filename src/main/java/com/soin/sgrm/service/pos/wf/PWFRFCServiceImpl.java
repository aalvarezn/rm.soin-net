package com.soin.sgrm.service.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PWFRFCDao;
import com.soin.sgrm.model.pos.wf.PWFRFC;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManagerPos")
@Service("PWFRFCService")
public class PWFRFCServiceImpl implements PWFRFCService {

	@Autowired
	private PWFRFCDao dao;

	@Override
	public List<PWFRFC> list() {
		return dao.list();
	}

	@Override
	public PWFRFC findWFRFCById(Long id) {
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
	public void wfStatusRFC(PWFRFC rfc) {
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
	public void wfStatusRFCWithOutMin(PWFRFC rfc) {
		dao.wfStatusRFCWithOutMin(rfc);
		
	}



}