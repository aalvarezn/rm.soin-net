package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ReleaseDao;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.utils.JsonSheet;

@Service("releaseService")
@Transactional("transactionManagerPos")
public class ReleaseServiceImpl implements ReleaseService {
	
	@Autowired
	ReleaseDao dao;
	@Override
	public PRelease findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PRelease findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRelease> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PRelease model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRelease model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRelease model) {
		dao.update(model);
	}

	@Override
	public List<PRelease> listReleasesBySystem1(Long id) {
		return dao.listReleasesBySystem1(id);
	}

	@Override
	public JsonSheet<?> listReleasesBySystem( int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, Long systemId) throws SQLException, ParseException  {

		return dao.listReleasesBySystem( sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
	}

}
