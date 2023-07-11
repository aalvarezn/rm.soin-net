package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SigesDao;
import com.soin.sgrm.dao.pos.PSigesDao;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.pos.PSiges;

@Service("PsigesService")
@Transactional("transactionManagerPos")
public class PSigesServiceImpl implements PSigesService{

	@Autowired
	PSigesDao dao;
	
	@Override
	public PSiges findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PSiges findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PSiges> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PSiges model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PSiges model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PSiges model) {
		dao.update(model);
	}

	@Override
	public List<PSiges> listCodeSiges(Integer id) {
		return dao.listCodeSiges(id);
	}



}
