package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.pos.PComponent;

public interface PComponentDao extends BaseDao<Long, PComponent> {

	List<PComponent> findBySystem(List<Integer> systemIds);

	List<PComponent> findBySystem(Integer id);

}
