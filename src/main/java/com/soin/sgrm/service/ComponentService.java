package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Component;

public interface ComponentService extends BaseService<Long, Component>{

	List<Component> findBySystem(List<Integer> systemIds);

	List<Component> findBySystem(Integer id);

}
