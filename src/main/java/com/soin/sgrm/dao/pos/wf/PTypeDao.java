package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import com.soin.sgrm.model.pos.wf.PType;

public interface PTypeDao {

	List<PType> list();

	PType findById(Integer id);

	void save(PType type);

	void update(PType type);

	void delete(Integer id);

}
