package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.EmailTemplateDao;
import com.soin.sgrm.model.pos.PEmailTemplate;

@Service("emailTemplateService")
@Transactional("transactionManagerPos")
public class EmailTemplateServiceImpl implements EmailTemplateService {
	
	@Autowired
	EmailTemplateDao dao;
	@Override
	public PEmailTemplate findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PEmailTemplate findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PEmailTemplate> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PEmailTemplate model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PEmailTemplate model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PEmailTemplate model) {
		dao.update(model);
	}

}
