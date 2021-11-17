package com.soin.sgrm.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.DependencyDao;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;

@Transactional("transactionManager")
@Service("DependencyService")
public class DependencyServiceImpl implements DependencyService {

	@Autowired
	DependencyDao dao;

	@Override
	public Dependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional) {
		return dao.findDependencyById(from_id, to_id, isFunctional);
	}

	@Override
	public Dependency save(Release release, Dependency dependency) {
		return dao.save(release, dependency);
	}

	@Override
	public void delete(Dependency dependency) {
		dao.delete(dependency);
	}

	@Override
	public ArrayList<Dependency> save(Release release, ArrayList<Dependency> dependencies) {
		return dao.save(release, dependencies);
	}

}
