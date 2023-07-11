package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ModuleDao;
import com.soin.sgrm.dao.pos.PModuleDao;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.pos.PModule;

@Transactional("transactionManagerPos")
@Service("PModuleService")
public class PModuleServiceImpl implements PModuleService {

	@Autowired
	PModuleDao dao;

	@Override
	public PModule findBySystemId(String code) {
		return dao.findBySystemId(code);
	}

	@Override
	public PModule findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<PModule> list() {
		return dao.list();
	}

	@Override
	public void save(PModule module) {
		dao.save(module);
	}

	@Override
	public void update(PModule module) {
		dao.update(module);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
