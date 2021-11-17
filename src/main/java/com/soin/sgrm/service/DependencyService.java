package com.soin.sgrm.service;

import java.util.ArrayList;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;

public interface DependencyService {
	
	ArrayList<Dependency> save(Release release, ArrayList<Dependency> dependencies);

	Dependency save(Release release, Dependency dependency);
	
	void delete(Dependency dependency);

	Dependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional);
}
