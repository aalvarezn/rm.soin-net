package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.dao.TreeDao;
import com.soin.sgrm.dao.pos.PTreeDao;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Tree;

@Transactional("transactionManagerPos")
@Service("PTreeService")
public class PTreeServiceImpl implements PTreeService {

	@Autowired
	PTreeDao dao;

	@Override
	public List<Tree> findTree(String releaseNumber, Integer depth) {
		return dao.findTree(releaseNumber, depth);
	}


}
