package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.AttentionGroupDao;
import com.soin.sgrm.dao.StatusRequestDao;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.StatusRequest;

@Transactional("transactionManager")
@Service("AttentionGroupService")
public class AttentionGroupServiceImpl implements AttentionGroupService{

	@Autowired
	AttentionGroupDao dao;
	
	@Override
	public AttentionGroup findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public AttentionGroup findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<AttentionGroup> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(AttentionGroup model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		AttentionGroup model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(AttentionGroup model) {
		dao.update(model);
	}

	@Override
	public  List<AttentionGroup> findGroupByUserId(Integer id) {
		return dao.findGroupByUserId(id);
	}

}
