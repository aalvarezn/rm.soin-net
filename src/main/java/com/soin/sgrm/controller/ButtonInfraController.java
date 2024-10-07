package com.soin.sgrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.functiongraph.v2.FunctionGraphClient;
import com.huaweicloud.sdk.functiongraph.v2.model.InvokeFunctionRequest;
import com.huaweicloud.sdk.functiongraph.v2.model.InvokeFunctionResponse;
import com.huaweicloud.sdk.functiongraph.v2.region.FunctionGraphRegion;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingShow;
import com.soin.sgrm.model.pos.PRequestBaseTrackingShow;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.PButtonInfraService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/buttonInfra")
public class ButtonInfraController extends BaseController {

	@Autowired
	PSystemService psystemService;

	@Autowired
	PButtonInfraService pbuttonInfraService;

	private final Environment environment;

	@Autowired
	public ButtonInfraController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	public static final Logger logger = Logger.getLogger(RFCController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			List<PSystem> systems = psystemService.listProjectsall(getUserLogin().getId());
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "buttonInfra");
			e.printStackTrace();
		}
		return "/buttons/buttonInfra";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {

		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			Integer systemId;
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}

			String dateRange = request.getParameter("dateRange");
			JsonSheet<?> buttons = new JsonSheet<>();
			List<PSystem> systems = psystemService.listProjectsall(getUserLogin().getId());
			buttons = pbuttonInfraService.findAllButton(sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
					systemId, systems);
			return buttons;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/action/{id}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse actionButton(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			// curl basico
			/*
			 * RestTemplate restTemplate = new RestTemplate(); String url =
			 * "https://api.agify.io?name=michael"; ResponseEntity<String> response =
			 * restTemplate.getForEntity(url, String.class);
			 * res.setData(response.getBody());
			 */
			// Invocacion si es Huaweii
			String id_cuenta = "b7b7717355c742b28c5ffcad2e4ceba1";
			String function = "default:Function-Prueba:latest";
			String region = "la-north-2";
			String ak = "IDT0DQN8LBYZ6RPFOPUB";
			String sk = "uSgOOeDa6jF56m85wZNEPQUWES3ISCS4iWAvgJHc";
			/*
			 * Map<String, Object> body = new HashMap<>(); BasicCredentials basicCredentials
			 * = new BasicCredentials() .withAk(ak) .withSk(sk); FunctionGraphClient client
			 * = FunctionGraphClient.newBuilder() .withCredential(basicCredentials)
			 * .withRegion(FunctionGraphRegion.LA_NORTH_2) .build();
			 * 
			 * String functionUrn =
			 * "urn:fss:la-north-2:b7b7717355c742b28c5ffcad2e4ceba1:function:default:Function-Prueba:latest";
			 * InvokeFunctionRequest request2 = new InvokeFunctionRequest()
			 * .withFunctionUrn(functionUrn) .withBody(body); InvokeFunctionResponse
			 * response = client.invokeFunction(request2); String result =
			 * response.getResult();
			 */
			ICredential auth = new BasicCredentials().withAk(ak).withSk(sk);

			FunctionGraphClient client = FunctionGraphClient.newBuilder().withCredential(auth)
					.withRegion(FunctionGraphRegion.LA_NORTH_2).build();
			InvokeFunctionRequest request2 = new InvokeFunctionRequest();
			Map<String, Object> listbodyInvokeFunctionRequestBody = new HashMap<>();
		
			request2.withBody(listbodyInvokeFunctionRequestBody);
			request2.withFunctionUrn("urn:fss:la-north-2:b7b7717355c742b28c5ffcad2e4ceba1:function:default:Function-Prueba:latest");
			request2.withXCffLogType("tail");
			request2.withXCFFRequestVersion("v1");

			InvokeFunctionResponse response = client.invokeFunction(request2);
			System.out.println(response.toString());
			res.setStatus("success");
			res.setData(response.getResult());
			return res;

		} catch (Exception e) {
			Sentry.capture(e, "buttonAction");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}