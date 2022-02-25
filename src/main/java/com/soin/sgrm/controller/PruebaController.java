package com.soin.sgrm.controller;



import java.util.List;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.soin.sgrm.model.migrate.TipoCambio;
import com.soin.sgrm.service.PruebaService;






@Controller
@RequestMapping("/prueba")
public class PruebaController {
	
	private PruebaService prueba;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public List<TipoCambio> getTipoCambio(){
		return prueba.getCambios();
		
	}
}
