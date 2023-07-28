package com.soin.sgrm.dao.pos;

import java.util.ArrayList;

import com.soin.sgrm.model.pos.PDependency;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;

public interface PDependencyDao {

	ArrayList<PDependency> save(PReleaseEdit release, ArrayList<PDependency> dependencies);

	PDependency save(PRelease release, PDependency dependency);

	void delete(PDependency dependency);

	PDependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional);
}
