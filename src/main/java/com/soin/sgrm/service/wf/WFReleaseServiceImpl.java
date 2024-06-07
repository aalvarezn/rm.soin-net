package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.WFReleaseDao;
import com.soin.sgrm.dao.wf.WFUserDao;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManager")
@Service("WFReleaseService")
public class WFReleaseServiceImpl implements WFReleaseService {

	@Autowired
	private WFReleaseDao dao;

	@Override
	public List<WFRelease> list() {
		return dao.list();
	}

	@Override
	public WFRelease findWFReleaseById(Integer id) {
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
	public void wfStatusRelease(WFRelease release) {
		dao.wfStatusRelease(release);
	}

	@Override
	public JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId,
			List<Integer> listIdRelease,Integer userId) throws SQLException, ParseException {
		return dao.listWorkFlowManager(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId, statusId, listIdRelease,userId);
	}

	@Override
	public Integer countByType(String group, List<Integer> listIdRelease,Integer userId) {
		return dao.countByType(group, listIdRelease,userId);
	}

	@Override
	public void wfStatusReleaseWithOutMin(WFRelease release) {
		dao.wfStatusReleaseWithOutMin(release);
		
	}

}