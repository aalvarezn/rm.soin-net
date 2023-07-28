package com.soin.sgrm.service.pos;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PDependencyDao;
import com.soin.sgrm.model.pos.PDependency;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;

@Transactional("transactionManagerPos")
@Service("PDependencyService")
public class PDependencyServiceImpl implements PDependencyService {

	@Autowired
	PDependencyDao dao;

	@Override
	public PDependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional) {
		return dao.findDependencyById(from_id, to_id, isFunctional);
	}

	@Override
	public PDependency save(PRelease release, PDependency dependency) {
		return dao.save(release, dependency);
	}

	@Override
	public void delete(PDependency dependency) {
		dao.delete(dependency);
	}

	@Override
	public ArrayList<PDependency> save(PReleaseEdit release, ArrayList<PDependency> dependencies) {
		return dao.save(release, dependencies);
	}

}
