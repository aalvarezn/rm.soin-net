package com.soin.sgrm.service.pos;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestNewDao;
import com.soin.sgrm.model.pos.PRequest;

@Service("PRequestNewService")
@Transactional("transactionManagerPos")
public class PRequestNewServiceImpl implements PRequestNewService {

	@Autowired
	PRequestNewDao dao;


	public static final Logger logger = Logger.getLogger(PRequestNewServiceImpl.class);

	@Override
	public PRequest findById(Integer id) {
		
		return dao.getById(id);
	}

	@Override
	public PRequest findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequest> findAll() {
	
		return dao.findAll();
	}

	@Override
	public void save(PRequest model) {
		dao.save(model);
	}

	@Override
	public void delete(Integer id) {
		PRequest model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequest model) {
		dao.update(model);
	}



	@Override
	public com.soin.sgrm.utils.JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, int proyectId, int typeId, Integer userLogin) throws ParseException {
		
		return dao.findAll( sEcho,  iDisplayStart,  iDisplayLength,
				 sSearch,  proyectId,  typeId,  userLogin) ;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	

}