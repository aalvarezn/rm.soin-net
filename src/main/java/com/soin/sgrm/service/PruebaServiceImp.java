package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.PruebaDao;
import com.soin.sgrm.model.migrate.TipoCambio;

public class PruebaServiceImp implements PruebaService{
	
	@Autowired
	private PruebaDao pruebaDao;
	@Override
	public List<TipoCambio> getCambios() {
		return pruebaDao.getCambios();
	}

}
