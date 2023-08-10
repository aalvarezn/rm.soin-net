package com.soin.sgrm.service.pos;

import java.text.ParseException;
import com.soin.sgrm.model.pos.PRequest;

public interface PRequestNewService extends BaseService<Integer, PRequest>{
	
	public com.soin.sgrm.utils.JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, int proyectId, int typeId,Integer userLogin) throws ParseException;

	void delete(Integer id);



}