package com.soin.sgrm.dao.pos;

import java.text.ParseException;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.utils.JsonSheet;

public interface PRequestNewDao extends BaseDao<Integer, PRequest>{


	public JsonSheet<?> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			int proyectId, int typeId, Integer userLogin) throws ParseException;

}
