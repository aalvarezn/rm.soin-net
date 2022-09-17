package com.soin.sgrm.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;

@RestController
@RequestMapping("/ws/webService")
public class WebServiceController extends BaseController {

	@Autowired
	RFCService rfcService;
	@Autowired
	EmailReadService emailReadService;

	@Autowired
	ReleaseService releaseService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<RFC> getRFCS() {
		return rfcService.findAll();
	}

	@RequestMapping(value = { "/readEmail" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String readEmails() {
		try {
			emailReadService.emailRead();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "se leyo correctamente";

	}

	@RequestMapping(value = "/prueba", method = RequestMethod.GET)
	public Integer findReleaseByName(@RequestParam String name, Model model) {
		Integer idRelease;
		try {
			idRelease = releaseService.findReleaseByName(name);
		} catch (Exception e) {
			idRelease = 0;
		}
		return idRelease;
	}

}
