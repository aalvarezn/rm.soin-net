package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SigesDao;
import com.soin.sgrm.dao.pos.PButtonInfraDao;
import com.soin.sgrm.dao.pos.PSigesDao;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PSiges;

@Service("PsigesService")
@Transactional("transactionManagerPos")
public class PButtonInfraServiceImpl implements PButtonInfraService{

	@Autowired
	PButtonInfraDao dao;
	
	@Override
	public PButtonInfra findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PButtonInfra findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PButtonInfra> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PButtonInfra model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PButtonInfra model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PButtonInfra model) {
		dao.update(model);
	}

	@Override
	public List<PButtonInfra> listCodeSiges(Integer id) {
		return dao.listCodeSiges(id);
	}

	@Override
	public boolean checkUniqueCode(String codeSiges) {
		return dao.checkUniqueCode(codeSiges);
	}
	@Override
	public boolean veryUpdateSigesCode(Long id, String codeSiges) {
		
		return dao.veryUpdateSigesCode(id,codeSiges);
	}

	@Override
	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges) {
		return dao.veryUpdateSigesCodeDif(id,codeSiges);
	}


}
