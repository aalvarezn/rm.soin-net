package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PComponent;

public interface PComponentService extends BaseService<Long, PComponent>{

	List<PComponent> findBySystem(List<Integer> systemIds);

	List<PComponent> findBySystem(Integer id);

}
