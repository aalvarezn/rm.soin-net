package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Tree;

public interface TreeDao {
	
	List<Tree> findTree(String releaseNumber, Integer depth);
}
