package com.soin.sgrm.service.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Type;

public interface TypeService {

	List<Type> list();

	Type findById(Integer id);

	void save(Type type);

	void update(Type type);

	void delete(Integer id);

}
