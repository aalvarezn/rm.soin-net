package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.WFIncidenceDao;
import com.soin.sgrm.dao.wf.WFReleaseDao;
import com.soin.sgrm.dao.wf.WFUserDao;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManager")
@Service("WFIncidenceService")
public class WFIncidenceServiceImpl implements WFIncidenceService {

	@Autowired
	private WFIncidenceDao dao;

	@Override
	public List<WFIncidence> list() {
		return dao.list();
	}

	@Override
	public WFIncidence findWFIncidenceById(Long id) {
		return dao.findWFIncidenceById(id);
	}

	@Override
	public JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId)
			throws SQLException, ParseException {
		return dao.listWorkFlowIncidence(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId);
	}

	@Override
	public void wfStatusIncidence(WFIncidence incidence) {
		dao.wfStatusIncidence(incidence);
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


}
