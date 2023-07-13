package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PStatus;

public interface PStatusDao {

	List<PStatus> list();
	
	PStatus findByName(String name);
	
	PStatus findById(Integer id);

	void save(PStatus status);

	void update(PStatus status);

	void delete(Integer id);

	List<PStatus> listWithOutAnul();
}
