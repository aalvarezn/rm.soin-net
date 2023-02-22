package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Component;

public interface ComponentDao extends BaseDao<Long, Component> {

	List<Component> findBySystem(List<Integer> systemIds);

	List<Component> findBySystem(Integer id);

}
