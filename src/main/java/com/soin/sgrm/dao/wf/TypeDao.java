package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.wf.Type;

public interface TypeDao {

	List<Type> list();

	Type findById(Integer id);

	void save(Type type);

	void update(Type type);

	void delete(Integer id);

}
