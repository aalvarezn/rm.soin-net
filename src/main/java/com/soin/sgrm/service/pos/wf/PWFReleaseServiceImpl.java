package com.soin.sgrm.service.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PWFReleaseDao;
import com.soin.sgrm.model.pos.wf.PWFRelease;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManagerPos")
@Service("PWFReleaseService")
public class PWFReleaseServiceImpl implements PWFReleaseService {

	@Autowired
	private PWFReleaseDao dao;

	@Override
	public List<PWFRelease> list() {
		return dao.list();
	}

	@Override
	public PWFRelease findWFReleaseById(Integer id) {
		return dao.findWFReleaseById(id);
	}

	@Override
	public JsonSheet<?> listWorkFlowRelease(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		return dao.listWorkFlowRelease(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);
	}

	@Override
	public void wfStatusRelease(PWFRelease release) {
		dao.wfStatusRelease(release);
	}

	@Override
	public JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId,
			Object[] systemsId) throws SQLException, ParseException {
		return dao.listWorkFlowManager(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId, statusId, systemsId);
	}

	@Override
	public Integer countByType(String group, Object[] ids) {
		return dao.countByType(group, ids);
	}

	@Override
	public void wfStatusReleaseWithOutMin(PWFRelease release) {
		dao.wfStatusReleaseWithOutMin(release);
		
	}

}