package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PStatus;

public interface PStatusService {
	
	List<PStatus> list();
	
	PStatus findByName(String name);
	
	PStatus findById(Integer id);

	void save(PStatus status);

	void update(PStatus status);

	void delete(Integer id);

	List<PStatus> listWithOutAnul();

}
