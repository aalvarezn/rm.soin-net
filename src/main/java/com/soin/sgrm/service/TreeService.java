package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Tree;

public interface TreeService {
	
	List<Tree> findTree(String releaseNumber, Integer depth);
}
