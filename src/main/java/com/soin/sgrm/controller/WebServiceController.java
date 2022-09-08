package com.soin.sgrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.service.RFCService;

@RestController
@RequestMapping("/webService")
public class WebServiceController {
	
		@Autowired 
		RFCService rfcService;
		@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
		@ResponseStatus(HttpStatus.OK)
		public List<RFC> getRFCS(){
			return rfcService.findAll();
		}
	
	
	
}
