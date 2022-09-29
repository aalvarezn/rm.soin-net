package com.soin.sgrm.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.soin.sgrm.response.JsonSheet;

public interface BaseDao<PK, T> {

	T getByKey(String key, String value);
	
	T getById(Long id);

	void save(T model);

	void merge(T model);

	void saveOrUpdate(T model);

	void update(T model);

	void flush();

	void clear();

	void deleteByKey(String key, String value);

	void delete(T model);

	JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, Map<String, Object> columns,
			Criterion qSrch);

	JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, Map<String, Object> columns,
			Criterion qSrch, List<String> fetchs);

	JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, Map<String, Object> columns,
			Criterion qSrch, List<String> fetchs, Map<String, String> alias,Integer veri);

	List<T> findAll();

}
