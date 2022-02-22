package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.WFUserDao;
import com.soin.sgrm.model.wf.WFUser;

@Transactional("transactionManager")
@Service("WFUserService")
public class WFUserServiceImpl implements WFUserService {

	@Autowired
	private WFUserDao dao;

	@Override
	public List<WFUser> list() {
		return dao.list();
	}

	@Override
	public WFUser findWFUserById(Integer id) {
		return dao.findWFUserById(id);
	}

}
