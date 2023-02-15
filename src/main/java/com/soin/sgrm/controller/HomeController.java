package com.soin.sgrm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Parameter;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.corp.RMReleaseFileService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.EnviromentConfig;

import com.soin.sgrm.exception.Sentry;

import org.springframework.util.FileCopyUtils;

@Controller
@RequestMapping("/")

public class HomeController extends BaseController {

	@Autowired
	private Environment env;
	private String nd = "No definido";

	@Autowired
	@Qualifier("dataSource")
	private DataSource dsSGRM;

	@Autowired
	UserInfoService userService;

	@Autowired
	RMReleaseFileService rmReleaseSerivce;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private EmailTemplateService emailService;

	@Autowired
	private ParameterService paramService;

	EnviromentConfig envConfig = new EnviromentConfig();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		if (request.isUserInRole("ROLE_Release Manager")) {
			return "redirect:/management/release/";
		}
		if (request.isUserInRole("ROLE_QA")) {
			return "redirect:/release/qa";
		}
		if (request.isUserInRole("ROLE_Gestor Incidencias")) {
			return "redirect:/baseKnowledge/";
		}
		return "redirect:/release/";
	}
	
	@RequestMapping(value = "/homeRFC", method = RequestMethod.GET)
	public String indexRFC(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		if (request.isUserInRole("ROLE_Release Manager")) {
			return "redirect:/management/rfc/";
		}

		return "redirect:/rfc/";
	}
	@RequestMapping(value = "/homeRequest", method = RequestMethod.GET)
	public String indexRequest(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		if (request.isUserInRole("ROLE_Release Manager")) {
			return "redirect:/management/request/";
		}

		return "redirect:/request/";
	}
	
	@RequestMapping(value = "/homeIncidence", method = RequestMethod.GET)
	public String indexIncidence(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "redirect:/incidence/";
	}
	
	@RequestMapping(value = "/homeIncidenceAttention", method = RequestMethod.GET)
	public String indexIncidenceAttention(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "redirect:/incidenceManagement/";
	}
	
	@RequestMapping(value = "/homeBaseKnowledge", method = RequestMethod.GET)
	public String indexBaseKnow(HttpServletRequest request, Locale locale, Model model, HttpSession session) {



		return "redirect:/baseKnowledge/";
	}


	@RequestMapping(value = "/successLogin", method = RequestMethod.GET)
	public String successLogin(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		if (request.isUserInRole("ROLE_Admin")) {
			return "redirect:/admin/";
		}
		if (request.isUserInRole("ROLE_Gestor Incidencias")) {
			return "redirect:/baseKnowledge/";
		}
		

		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes,
			@RequestParam(value = "error", required = false) String error) {
		String referrer = request.getHeader("Referer");
		if (referrer != null) {
			request.getSession().setAttribute("url_prior_login", referrer);
		}

		if (error != null) {
			model.addAttribute("errorMessge", "Usuario o Contraseña es incorrecto!");
		}
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
	public String forgetPassword(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/forgetPassword";
	}

	@RequestMapping(value = "/recoverPassword", method = RequestMethod.POST)
	public String recoverPassword(HttpServletRequest request, @ModelAttribute("UserInfo") UserInfo user, ModelMap model,
			Locale locale, HttpSession session) {
		try {
			if (CommonUtils.isValidEmailAddress(user.getEmailAddress())) {
				UserInfo userInfo = userService.getUserByEmail(user.getEmailAddress());
				if (userInfo != null) {
					String code = "soin" + CommonUtils.getRandom();
					String newPassword = encoder.encode(code);
					userInfo.setPassword(newPassword);
					Parameter param = paramService.findByCode(2);
					if (param != null) {
						EmailTemplate email = emailService.findById(Integer.parseInt(param.getParamValue()));
						if (email != null) {
							userService.changePassword(userInfo);
							emailService.sendMail(userInfo, code, email);
							model.addAttribute("successMessge", "Correo de restablecimiento enviado!");
						} else {
							model.addAttribute("errorMessge", "Correo definido no existe!");
						}
					} else {
						model.addAttribute("errorMessge", "Parámetro de correo no definido!");
					}
				} else {
					model.addAttribute("errorMessge", "Correo ingresado no existe!");
				}
			} else {
				model.addAttribute("errorMessge", "Correo ingresado invalido!");
			}

		} catch (Exception e) {
			Sentry.capture(e, "home");
			model.addAttribute("errorMessge", "Error: " + e.toString());
		}

		return "/forgetPassword";
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		String ds_sgrm = "Error en conexión";
		try {
			userService.getUserByUsername("");
			ds_sgrm = "OK";
		} catch (Exception e) {
			ds_sgrm = e.getMessage();
		}
		model.addAttribute("ds_sgrm", ds_sgrm);

		String ds_username = "";
		try {
			Connection conn = dsSGRM.getConnection();
			DatabaseMetaData mtdt = conn.getMetaData();
			ds_username = mtdt.getUserName();
		} catch (Exception e1) {
			ds_username = e1.getMessage();
		}
		ds_username = (ds_username != null) ? ds_username : nd;
		model.addAttribute("ds_username", ds_username);

		String ds_corp = "Error en conexión";
		try {
			rmReleaseSerivce.findByRelease("");
			ds_corp = "OK";
		} catch (Exception e) {
			ds_corp = e.getMessage();
		}
		model.addAttribute("ds_corp", ds_corp);

		String ds_corp_username = env.getProperty("ds.corp.username");
		ds_corp_username = (ds_corp_username != null) ? ds_corp_username : nd;
		model.addAttribute("ds_corp_username", ds_corp_username);

		String ds_corp_password = env.getProperty("ds.corp.password");
		ds_corp_password = (ds_corp_password != null) ? "xxxx" : nd;
		model.addAttribute("ds_corp_password", ds_corp_password);

		String fileStore_path = env.getProperty("fileStore.path");
		model.addAttribute("fileStore_path", fileStore_path);

		String systemErrorLog = "";
		try {
			systemErrorLog = getLog4jProperty("log4j.appender.SYSTEM_ERROR.File");
		} catch (Exception e) {
			systemErrorLog = e.getMessage();
		}
		model.addAttribute("systemErrorLog", systemErrorLog);

		String webserviceLog = "";
		try {
			webserviceLog = getLog4jProperty("log4j.appender.WEBSERVICE.File");
		} catch (Exception e) {
			webserviceLog = e.getMessage();
		}
		model.addAttribute("webserviceLog", webserviceLog);

		String releaseErrorLog = "";
		try {
			releaseErrorLog = getLog4jProperty("log4j.appender.RELEASE_ERROR.File");
		} catch (Exception e) {
			releaseErrorLog = e.getMessage();
		}
		model.addAttribute("releaseErrorLog", releaseErrorLog);

		String fileReadLog = "";
		try {
			fileReadLog = getLog4jProperty("log4j.appender.FILE_READ.File");
		} catch (Exception e) {
			fileReadLog = e.getMessage();
		}
		model.addAttribute("fileReadLog", fileReadLog);
		String fileAdminLog = "";
		try {
			fileAdminLog = getLog4jProperty("log4j.appender.ADMIN_ERROR.File");
		} catch (Exception e) {
			fileAdminLog = e.getMessage();
		}
		model.addAttribute("fileAdminLog", fileAdminLog);

		Map<String, String> emailProperties = envConfig.getEntryProperties("mailProperties");
		emailProperties.put("mail.user", envConfig.getEntry("mailUser"));
		model.addAttribute("emailProperties", emailProperties);

		return "/plantilla/info";
	}

	@SuppressWarnings("resource")
	public String getLog4jProperty(String property) {
		java.io.InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream("log4j.properties");
		java.util.Scanner s = new java.util.Scanner(propertiesStream).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		String[] list = result.split("\\r?\\n");

		for (String string : list) {
			if (string.contains(property + "=")) {
				return string.replaceAll(property + "=", "");
			}
		}
		return "";
	}

	public Map<String, String> getEmailProperties() {
		Map<String, String> emailProperties = new HashMap<>();
		String[] properties = { "mail.smtp.starttls.enable", "mail.smtp.auth", "mail.transport.protocol", "mail.debug",
				"mail.smtp.ssl.trust", "mail.user", "mail.password", "mail.port" };

		for (String property : properties) {
			String value = env.getProperty(property);
			if (property.equals("mail.password")) {
				value = "xxxx";
			}
			emailProperties.put(property, (value != null) ? value : nd);
		}
		return emailProperties;
	}

	@RequestMapping(value = "/logDownload-{name}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable String name) throws IOException {

		String logPath = getLog4jProperty("log4j.appender." + name + ".File");
		File file = new File(logPath);

		// Se modifica la respuesta para descargar el archivo
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

}
