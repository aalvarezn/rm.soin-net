package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.soin.sgrm.model.migrate.TipoCambio;

public class PruebaDaoImp implements PruebaDao {
    @Autowired 
    private SessionFactory sessionFactory;
    
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoCambio> getCambios() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TipoCambio.class);
		return crit.list();
	}

}
