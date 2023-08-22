package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Tree;

public interface PTreeService {
	
	List<Tree> findTree(String releaseNumber, Integer depth);
}
