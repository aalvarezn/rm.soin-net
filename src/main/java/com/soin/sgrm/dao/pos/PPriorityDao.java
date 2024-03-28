package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.pos.PPriority;

public interface PPriorityDao {

	List<PPriority> list();

	PPriority findById(Integer id);

	void save(PPriority priority);

	void update(PPriority priority);

	void delete(Integer id);

}
