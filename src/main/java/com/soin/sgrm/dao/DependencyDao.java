package com.soin.sgrm.dao;

import java.util.ArrayList;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;

public interface DependencyDao {

	ArrayList<Dependency> save(ReleaseEdit release, ArrayList<Dependency> dependencies);

	Dependency save(Release release, Dependency dependency);

	void delete(Dependency dependency);

	Dependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional);
}
