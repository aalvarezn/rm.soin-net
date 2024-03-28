package com.soin.sgrm.service.pos.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PWFUserDao;
import com.soin.sgrm.model.pos.wf.PWFUser;

@Transactional("transactionManagerPos")
@Service("PWFUserService")
public class PWFUserServiceImpl implements PWFUserService {

	@Autowired
	private PWFUserDao dao;

	@Override
	public List<PWFUser> list() {
		return dao.list();
	}

	@Override
	public PWFUser findWFUserById(Integer id) {
		return dao.findWFUserById(id);
	}

}
