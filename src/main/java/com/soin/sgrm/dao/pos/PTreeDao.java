package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Tree;

public interface PTreeDao {
	
	List<Tree> findTree(String releaseNumber, Integer depth);
}
