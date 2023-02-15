package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.EmailIncidenceDao;
import com.soin.sgrm.model.EmailIncidence;


@Transactional("transactionManager")
@Service("EmailIncidenceService")
public class EmailIncidenceServiceImpl implements EmailIncidenceService  {
	
	@Autowired 
	EmailIncidenceDao dao;
	
	@Override
	public EmailIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public EmailIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<EmailIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(EmailIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		EmailIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(EmailIncidence model) {
		dao.update(model);
	}
	@Override
	public List<EmailIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<EmailIncidence> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public EmailIncidence findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

}
