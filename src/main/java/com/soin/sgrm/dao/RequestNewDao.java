package com.soin.sgrm.dao;

import java.text.ParseException;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.utils.JsonSheet;

public interface RequestNewDao extends BaseDao<Integer, Request>{


	public JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			int proyectId, int typeId, Integer userLogin) throws ParseException;

}
