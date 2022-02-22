package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.dao.TreeDao;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Tree;

@Transactional("transactionManager")
@Service("TreeService")
public class TreeServiceImpl implements TreeService {

	@Autowired
	TreeDao dao;

	@Override
	public List<Tree> findTree(String releaseNumber, Integer depth) {
		return dao.findTree(releaseNumber, depth);
	}


}
