package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.AmbientDao;
import com.soin.sgrm.dao.pos.PAmbientDao;
import com.soin.sgrm.model.pos.PAmbient;


@Transactional("transactionManagerPos")
@Service("PAmbientService")
public class PAmbientServiceImpl implements PAmbientService {

	@Autowired
	PAmbientDao dao;

	@Override
	public List<PAmbient> list(Integer id) {
		return dao.list(id);
	}

	@Override
	public List<PAmbient> list(String search, String system) {
		return dao.list(search, system);
	}

	@Override
	public void addReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException {
		dao.addReleaseAmbient(release_id, ambient_id);
	}

	@Override
	public void deleteReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException {
		dao.deleteReleaseAmbient(release_id, ambient_id);
	}

	@Override
	public PAmbient findById(Integer id, String system) {
		return dao.findById(id, system);
	}

	@Override
	public List<PAmbient> list() {
		return dao.list();
	}

	@Override
	public PAmbient findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PAmbient PAmbient) {
		dao.save(PAmbient);
	}

	@Override
	public void update(PAmbient PAmbient) {
		dao.update(PAmbient);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
