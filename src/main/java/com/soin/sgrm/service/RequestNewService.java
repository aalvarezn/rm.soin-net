package com.soin.sgrm.service;

import java.text.ParseException;
import com.soin.sgrm.model.Request;

public interface RequestNewService extends BaseService<Integer, Request>{
	
	public com.soin.sgrm.utils.JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, int proyectId, int typeId,Integer userLogin) throws ParseException;

	void delete(Integer id);



}