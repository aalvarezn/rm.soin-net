package com.soin.sgrm.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.response.JsonSheet;

public abstract class AbstractDao<PK extends Serializable, T> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
	}

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Connection getConnection() {

		Session session = sessionFactory.getCurrentSession();
		Connection connection = ((SessionImpl) session).connection();

		return connection;
	}

	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(persistentClass, "t");
	}

	@SuppressWarnings("unchecked")
	public T getByKey(String key, String value) {
		return (T) getSession().createCriteria(persistentClass).add(Restrictions.eq(key, value)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T getById(Long id) {
		return (T) getSession().createCriteria(persistentClass).add(Restrictions.eq("id", id)).uniqueResult();
	}

	public void persist(T entity) {
		getSession().persist(entity);
	}

	public void save(T entity) {
		persist(entity);
	}

	public void merge(T entity) {
		getSession().merge(entity);
	}

	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public void update(T entity) {
		getSession().update(entity);
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public void deleteByKey(String key, String value) {
		T entity = getByKey(key, value);
		delete(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		return findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, null);
	}

	@SuppressWarnings("unused")
	public JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {

		return findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, null,0);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public JsonSheet<T> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias,Integer veri) {
		JsonSheet<T> sheet = new JsonSheet<T>();
		
		Criteria criteria = createEntityCriteria();
		if(veri==1) {
			criteria.addOrder(Order.desc("requestDate"));
		}if(veri==2) {
			criteria.addOrder(Order.desc("errorDate"));
		}
		else {
			criteria.addOrder(Order.desc("id"));
		}
		
		if (alias != null)
			for (Map.Entry<String, String> aliasName : alias.entrySet())
				criteria.createAlias(aliasName.getKey(), (String) aliasName.getValue());

		for (Map.Entry<String, Object> column : columns.entrySet())
			criteria = addFilters(criteria, columns);
		if (iDisplayStart != null)
			criteria.setFirstResult(iDisplayStart);
		if (iDisplayLength != null)
			criteria.setMaxResults(iDisplayLength);

		Criteria criteriaCount = createEntityCriteria();
		if (alias != null)
			for (Map.Entry<String, String> aliasName : alias.entrySet())
				criteriaCount.createAlias(aliasName.getKey(), (String) aliasName.getValue());
		for (Map.Entry<String, Object> column : columns.entrySet())
			criteriaCount = addFilters(criteriaCount, columns);

		if (qSrch != null) {
			criteria.add(qSrch);
			criteriaCount.add(qSrch);
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (fetchs != null)
			for (String itemModel : fetchs)
				criteria.setFetchMode(itemModel, FetchMode.SELECT);

		criteriaCount.setProjection(Projections.countDistinct("id"));

		List<T> list = (List<T>) criteria.list();

		Long count = (Long) criteriaCount.uniqueResult();
		int recordsTotal = count.intValue();

		if (sEcho != null)
			sheet.setDraw(sEcho);
		sheet.setRecordsTotal(recordsTotal);
		sheet.setRecordsFiltered(recordsTotal);
		sheet.setData(list);
		return sheet;
	}

	@SuppressWarnings("unchecked")
	public Criteria addFilters(Criteria criteria, Map<String, Object> columns) {
		if (columns == null)
			return criteria;
		for (Map.Entry<String, Object> column : columns.entrySet()) {
			if (column.getValue().getClass().isArray())
				criteria.add(Restrictions.in(column.getKey(), (Object[]) column.getValue()));
			else if (column.getValue() instanceof List)
				criteria.add(Restrictions.in(column.getKey(), ((List<Object>) column.getValue()).toArray()));
			else if (column.getValue() instanceof Criterion)
				criteria.add((Criterion) column.getValue());
			else
				criteria.add(Restrictions.eq(column.getKey(), column.getValue()));
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getSession().createCriteria(persistentClass).list();
	}

}
