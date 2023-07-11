package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.soin.sgrm.model.pos.PAttentionGroup;

@Transactional("transactionManagerPos")
@Service("PPAttentionGroupService")
public class PAttentionGroupServiceImpl implements PAttentionGroupService{

	@Autowired
	com.soin.sgrm.dao.pos.PAttentionGroupDao dao;
	
	@Override
	public PAttentionGroup findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PAttentionGroup findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PAttentionGroup> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PAttentionGroup model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PAttentionGroup model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PAttentionGroup model) {
		dao.update(model);
	}

	@Override
	public  List<PAttentionGroup> findGroupByUserId(Integer id) {
		return dao.findGroupByUserId(id);
	}

}
