package com.soin.sgrm.controller.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PGDoc;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PTypeRequest;
import com.soin.sgrm.service.GDocService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.TypeRequestService;
import com.soin.sgrm.service.pos.PGDocService;
import com.soin.sgrm.service.pos.PProjectService;
import com.soin.sgrm.service.pos.PRequestService;
import com.soin.sgrm.service.pos.PTypeRequestService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/admin/request")
public class RequestController extends BaseController {

	public static final Logger logger = Logger.getLogger(RequestController.class);

	@Autowired
	RequestService requestService;

	@Autowired
	ProjectService projectService;

	@Autowired
	TypeRequestService typeRequestService;
	
	@Autowired
	PRequestService prequestService;

	@Autowired
	PProjectService pprojectService;

	@Autowired
	PTypeRequestService ptypeRequestService;

	@Autowired
	GDocService gDocService;
	
	@Autowired
	PGDocService pgDocService;
	private final Environment environment;
	private static final String APPLICATION_NAME = "sgrm";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

	
	@Autowired
	public RequestController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest req, Locale locale, Model model, HttpSession session) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("requests", requestService.list());
			model.addAttribute("request", new Request());
			model.addAttribute("projects", projectService.listAll());
			model.addAttribute("project", new Project());
			model.addAttribute("typeRequests", typeRequestService.list());
			model.addAttribute("typeRequest", new TypeRequest());
		} else if (profile.equals("postgres")) {
			model.addAttribute("requests", prequestService.list());
			model.addAttribute("request", new PRequest());
			model.addAttribute("projects", pprojectService.listAll());
			model.addAttribute("project", new PProject());
			model.addAttribute("typeRequests", ptypeRequestService.list());
			model.addAttribute("typeRequest", new PTypeRequest());
		}
		
		
		return "/admin/request/request";
	}

	@RequestMapping(value = "/findRequest/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findRequest(@PathVariable Integer id, HttpServletRequest req, Locale locale,
			Model model, HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Request request = requestService.findById(id);
				return request;
			} else if (profile.equals("postgres")) {
				PRequest request = prequestService.findById(id);
				return request;
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveRequest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRequest(HttpServletRequest req,

			@Valid @ModelAttribute("Request") Request request, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (request.getTypeRequestId() == null) {
				res.setStatus("fail");
				res.addError("typeRequestId", "Seleccione una opción");
			}

			if (request.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					request.setActive(true);
					request.setProyect(projectService.findById(request.getProyectId()));
					request.setTypeRequest(typeRequestService.findById(request.getTypeRequestId()));
					requestService.save(request);
					res.setObj(request);
				} else if (profile.equals("postgres")) {
					PRequest prequest=new PRequest();
					prequest.setActive(true);
					prequest.setProyect(pprojectService.findById(request.getProyectId()));
					prequest.setCode_ice(request.getCode_ice());
					prequest.setCode_soin(request.getCode_soin());
					prequest.setDescription(request.getDescription());
					prequest.setLiderIce(request.getLiderIce());
					prequest.setLeaderSoin(request.getLeaderSoin());
					prequest.setIceManagement(request.getIceManagement());
					prequest.setSoinManagement(request.getSoinManagement());
					prequest.setStatus(request.getStatus());
					prequest.setTypeRequest(ptypeRequestService.findById(request.getTypeRequestId()));
					prequestService.save(prequest);
					res.setObj(prequest);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al crear requerimiento: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateRequest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateRequest(HttpServletRequest req,
			@Valid @ModelAttribute("Request") Request request, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (request.getTypeRequestId() == null) {
				res.setStatus("fail");
				res.addError("typeRequestId", "Seleccione una opción");
			}

			if (request.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Request requestOrigin = requestService.findById(request.getId());
					requestOrigin.setStatus(request.getStatus());
					requestOrigin.setCode_soin(request.getCode_soin());
					requestOrigin.setCode_ice(request.getCode_ice());
					requestOrigin.setDescription(request.getDescription());
					requestOrigin.setLeaderSoin(request.getLeaderSoin());
					requestOrigin.setLiderIce(request.getLiderIce());
					requestOrigin.setSoinManagement(request.getSoinManagement());
					requestOrigin.setIceManagement(request.getIceManagement());

					request.setProyect(projectService.findById(request.getProyectId()));
					request.setTypeRequest(typeRequestService.findById(request.getTypeRequestId()));

					requestService.update(requestOrigin);
					res.setObj(request);
				} else if (profile.equals("postgres")) {
					PRequest prequestOrigin = prequestService.findById(request.getId());
					prequestOrigin.setStatus(request.getStatus());
					prequestOrigin.setCode_soin(request.getCode_soin());
					prequestOrigin.setCode_ice(request.getCode_ice());
					prequestOrigin.setDescription(request.getDescription());
					prequestOrigin.setLeaderSoin(request.getLeaderSoin());
					prequestOrigin.setLiderIce(request.getLiderIce());
					prequestOrigin.setSoinManagement(request.getSoinManagement());
					prequestOrigin.setIceManagement(request.getIceManagement());

					prequestOrigin.setProyect(pprojectService.findById(request.getProyectId()));
					prequestOrigin.setTypeRequest(ptypeRequestService.findById(request.getTypeRequestId()));

					prequestService.update(prequestOrigin);
					res.setObj(prequestOrigin);
				}
				
			
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al modificar requerimiento: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteRequest/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRequest(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				requestService.delete(id);
			} else if (profile.equals("postgres")) {
				prequestService.delete(id);
			}
			res.setStatus("success");
			res.setObj(id);
			
		} catch (Exception e) {

			res.setStatus("exception");
			res.setException("Error al eliminar requerimiento: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar requerimiento: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "request");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/syncExcel", method = RequestMethod.POST)
	public @ResponseBody JsonResponse syncExcel(HttpServletRequest req, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			
			

			String profile = profileActive();
			if (profile.equals("oracle")) {
				List<GDoc> configs = gDocService.list();
				List<TypeRequest> typeRequests = typeRequestService.list();

				Thread syncRequest = new Thread(() -> {
					syncRequest(configs, typeRequests, requestService.list());
				});
				Thread syncTPOs = new Thread(() -> {
					TypeRequest type = null;
					for (TypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					syncTPOs(configs, type, requestService.listByType(type));
				});
				Thread syncTPOsSupport = new Thread(() -> {
					TypeRequest type = null;
					for (TypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					syncTPOsSupport(configs, type, requestService.listByType(type));
				});
				Thread syncTPOsMonthly = new Thread(() -> {
					TypeRequest type = null;
					for (TypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					syncTPOsMonthly(configs, type, requestService.listByType(type));
				});
				Thread syncBTs = new Thread(() -> {
					TypeRequest type = null;
					for (TypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("BT"))
							type = typeRequest;
					}
					syncBTs(configs, type, requestService.listByType(type));
				});
				syncTPOs.start();
				syncRequest.start();
				syncTPOsSupport.start();
				syncTPOsMonthly.start();
				syncBTs.start();

			} else if (profile.equals("postgres")) {
				List<PGDoc> configs = pgDocService.list();
				List<PTypeRequest> typeRequests = ptypeRequestService.list();

				Thread psyncRequest = new Thread(() -> {
					psyncRequest(configs, typeRequests, prequestService.list());
				});
				Thread psyncTPOs = new Thread(() -> {
					PTypeRequest type = null;
					for (PTypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					psyncTPOs(configs, type, prequestService.listByType(type));
				});
				Thread psyncTPOsSupport = new Thread(() -> {
					PTypeRequest type = null;
					for (PTypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					psyncTPOsSupport(configs, type, prequestService.listByType(type));
				});
				Thread psyncTPOsMonthly = new Thread(() -> {
					PTypeRequest type = null;
					for (PTypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("TPO"))
							type = typeRequest;
					}
					psyncTPOsMonthly(configs, type, prequestService.listByType(type));
				});
				Thread psyncBTs = new Thread(() -> {
					PTypeRequest type = null;
					for (PTypeRequest typeRequest : typeRequests) {
						if (typeRequest.getCode().equals("BT"))
							type = typeRequest;
					}
					psyncBTs(configs, type, prequestService.listByType(type));
				});
				psyncTPOs.start();
				psyncRequest.start();
				psyncTPOsSupport.start();
				psyncTPOsMonthly.start();
				psyncBTs.start();

			}
			res.setStatus("success");
			res.setData("Inicia proceso de sincronización.");
		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al sincronizar requerimientos: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	public void psyncRequest(List<PGDoc> configs, List<PTypeRequest> typeRequests, List<PRequest> requests) {
		try {
			// Se define los indices de las columnas
			Integer leaderSoinIndex = null, idIceIndex = null, descriptionIndex = null, iceManagementIndex = null,
					typeIndex = null, tpoIndex = null, count = 0;
			PProject proyect = null;
			PRequest request = null;
			PTypeRequest typeRequest = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (PGDoc config : configs) {
				List<List<Object>> values = pgetSheets(config, "MATRIZ");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						tpoIndex = row.indexOf("CODIGO");
						idIceIndex = row.indexOf("RQ");
						descriptionIndex = row.indexOf("DESCRIPCION");
						leaderSoinIndex = row.indexOf("LIDER TECNICO");
						iceManagementIndex = row.indexOf("REFERENTE");
						typeIndex = row.indexOf("TIPO");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (PRequest req : requests) {
								if (CommonUtils.equalsWithNulls(row.get(tpoIndex), req.getCode_soin())
										&& CommonUtils.equalsWithNulls(row.get(idIceIndex), req.getCode_ice())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								typeRequest = null;
								// Se obtiene el tipo de requerimiento
								for (PTypeRequest type : typeRequests) {
									if (type.getCode().equals((String) row.get(typeIndex))) {
										typeRequest = type;
									}
								}
								// Si no existe, se crea y se agrega a la lista
								if (typeRequest == null) {
									typeRequest = new PTypeRequest();
									typeRequest.setCode((String) row.get(typeIndex));
									typeRequest.setDescription((String) row.get(typeIndex));
									typeRequest = ptypeRequestService.save(typeRequest);
									typeRequests.add(typeRequest);
								}

								// Se crea en caso de que no exista
								request = (!existRequest) ? new PRequest() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setLiderIce((String) row.get(leaderSoinIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setProyect(proyect);
								request.setTypeRequest(typeRequest);
								request.setActive(true);
								if (existRequest)
									prequestService.update(request);
								else
									prequestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void psyncTPOs(List<PGDoc> configs, PTypeRequest type, List<PRequest> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			PProject proyect = null;
			PRequest request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (PGDoc config : configs) {
				List<List<Object>> values = pgetSheets(config, "TPO´s");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("ESTADO");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("ID ICE");
						descriptionIndex = row.indexOf("Nombre Requerimiento");
						soinManagementIndex = row.indexOf("Gestores de Procesos de Negocio SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							PRequest req = prequestService.listByTypeAndCodeSoin(type,
									((String) row.get(tpoIndex)).trim());

							if (req != null) {
								request = req;
								existRequest = true;
							}

							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new PRequest() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								if (existRequest)
									prequestService.update(request);
								else
									prequestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void psyncTPOsSupport(List<PGDoc> configs, PTypeRequest type, List<PRequest> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			PProject proyect = null;
			PRequest request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (PGDoc config : configs) {
				List<List<Object>> values = pgetSheets(config, "TPOsSoporte");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("ESTADO");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("ID ICE");
						descriptionIndex = row.indexOf("Nombre Requerimiento");
						soinManagementIndex = row.indexOf("Gestores de Procesos de Negocio SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (PRequest req : requests) {
								if ((((String) row.get(tpoIndex)).trim()).equals(req.getCode_soin().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new PRequest() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								if (existRequest)
									prequestService.update(request);
								else
									prequestService.save(request);
							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void psyncTPOsMonthly(List<PGDoc> configs, PTypeRequest type, List<PRequest> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			PProject proyect = null;
			PRequest request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (PGDoc config : configs) {
				List<List<Object>> values = pgetSheets(config, "Mensual AIA");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("Estado");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("RQ");
						descriptionIndex = row.indexOf("Descripción");
						soinManagementIndex = row.indexOf("Gestor SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (PRequest req : requests) {
								if ((((String) row.get(tpoIndex)).trim()).equals(req.getCode_soin().trim())
										&& (((String) row.get(idIceIndex)).trim()).equals(req.getCode_ice().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new PRequest() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								if (existRequest)
									prequestService.update(request);
								else
									prequestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void psyncBTs(List<PGDoc> configs, PTypeRequest type, List<PRequest> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, descriptionIndex = null, soinManagementIndex = null, iceManagementIndex = null,
					btIndex = null, codeIce = null, count = 0;
			PProject proyect = null;
			PRequest request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (PGDoc config : configs) {
				List<List<Object>> values = pgetSheets(config, "BT´s");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento

					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("Estado");
						btIndex = row.indexOf("Boleta");
						codeIce = row.indexOf("RQ");
						descriptionIndex = row.indexOf("Nombre");
						soinManagementIndex = row.indexOf("Gestor SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (btIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (PRequest req : requests) {
								if ((((String) row.get(btIndex)).trim()).equals(req.getCode_soin().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new PRequest() : request;
								request.setCode_soin((String) row.get(btIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(codeIce) + " " + row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setCode_ice((String) row.get(codeIce));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								request.setActive(true);
								if (existRequest) {
									prequestService.update(request);
								} else {
									request.setActive(true);
									prequestService.save(request);
								}

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public List<List<Object>> pgetSheets(PGDoc config, String range) {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			final String spreadsheetId = config.getSpreadSheet();
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, pgetCredentials(HTTP_TRANSPORT, config))
					.setApplicationName(APPLICATION_NAME).build();
			ValueRange values = service.spreadsheets().values().get(spreadsheetId, range).execute();
			List<List<Object>> sheets = values.getValues();
			return sheets;
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return new ArrayList<List<Object>>();
	}

	/**
	 * Se crea un objeto de credenciales con la informacion del excel.
	 */
	@SuppressWarnings("deprecation")
	private Credential pgetCredentials(final NetHttpTransport HTTP_TRANSPORT, PGDoc config)
			throws IOException, GeneralSecurityException {
		String key = config.getCredentials();

		InputStream inputStream = new ByteArrayInputStream(key.getBytes(Charset.forName("UTF-8")));

		Credential credential = GoogleCredential.fromStream(inputStream, HTTP_TRANSPORT, JSON_FACTORY)
				.createScoped(SCOPES);
		credential.refreshToken();
		return credential;

	}
	public void syncRequest(List<GDoc> configs, List<TypeRequest> typeRequests, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer leaderSoinIndex = null, idIceIndex = null, descriptionIndex = null, iceManagementIndex = null,
					typeIndex = null, tpoIndex = null, count = 0;
			Project proyect = null;
			Request request = null;
			TypeRequest typeRequest = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (GDoc config : configs) {
				List<List<Object>> values = getSheets(config, "MATRIZ");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						tpoIndex = row.indexOf("CODIGO");
						idIceIndex = row.indexOf("RQ");
						descriptionIndex = row.indexOf("DESCRIPCION");
						leaderSoinIndex = row.indexOf("LIDER TECNICO");
						iceManagementIndex = row.indexOf("REFERENTE");
						typeIndex = row.indexOf("TIPO");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (Request req : requests) {
								if (CommonUtils.equalsWithNulls(row.get(tpoIndex), req.getCode_soin())
										&& CommonUtils.equalsWithNulls(row.get(idIceIndex), req.getCode_ice())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								typeRequest = null;
								// Se obtiene el tipo de requerimiento
								for (TypeRequest type : typeRequests) {
									if (type.getCode().equals((String) row.get(typeIndex))) {
										typeRequest = type;
									}
								}
								// Si no existe, se crea y se agrega a la lista
								if (typeRequest == null) {
									typeRequest = new TypeRequest();
									typeRequest.setCode((String) row.get(typeIndex));
									typeRequest.setDescription((String) row.get(typeIndex));
									typeRequest = typeRequestService.save(typeRequest);
									typeRequests.add(typeRequest);
								}

								// Se crea en caso de que no exista
								request = (!existRequest) ? new Request() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setLiderIce((String) row.get(leaderSoinIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setProyect(proyect);
								request.setTypeRequest(typeRequest);
								request.setActive(true);
								if (existRequest)
									requestService.update(request);
								else
									requestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void syncTPOs(List<GDoc> configs, TypeRequest type, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			Project proyect = null;
			Request request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (GDoc config : configs) {
				List<List<Object>> values = getSheets(config, "TPO´s");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("ESTADO");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("ID ICE");
						descriptionIndex = row.indexOf("Nombre Requerimiento");
						soinManagementIndex = row.indexOf("Gestores de Procesos de Negocio SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							Request req = requestService.listByTypeAndCodeSoin(type,
									((String) row.get(tpoIndex)).trim());

							if (req != null) {
								request = req;
								existRequest = true;
							}

							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new Request() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								

								if (existRequest)
									requestService.update(request);
								else
									requestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void syncTPOsSupport(List<GDoc> configs, TypeRequest type, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			Project proyect = null;
			Request request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (GDoc config : configs) {
				List<List<Object>> values = getSheets(config, "TPOsSoporte");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("ESTADO");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("ID ICE");
						descriptionIndex = row.indexOf("Nombre Requerimiento");
						soinManagementIndex = row.indexOf("Gestores de Procesos de Negocio SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (Request req : requests) {
								if ((((String) row.get(tpoIndex)).trim()).equals(req.getCode_soin().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new Request() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								if (existRequest)
									requestService.update(request);
								else
									requestService.save(request);
							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void syncTPOsMonthly(List<GDoc> configs, TypeRequest type, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, idIceIndex = null, descriptionIndex = null, soinManagementIndex = null,
					iceManagementIndex = null, tpoIndex = null, count = 0;
			Project proyect = null;
			Request request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (GDoc config : configs) {
				List<List<Object>> values = getSheets(config, "Mensual AIA");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento
					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("Estado");
						tpoIndex = row.indexOf("TPO");
						idIceIndex = row.indexOf("RQ");
						descriptionIndex = row.indexOf("Descripción");
						soinManagementIndex = row.indexOf("Gestor SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (tpoIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (Request req : requests) {
								if ((((String) row.get(tpoIndex)).trim()).equals(req.getCode_soin().trim())
										&& (((String) row.get(idIceIndex)).trim()).equals(req.getCode_ice().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new Request() : request;
								request.setCode_soin((String) row.get(tpoIndex));
								request.setCode_ice((String) row.get(idIceIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								if(request.getStatus().trim().equals("COMPLETADA")) {
									request.setActive(false);
								}else {
									request.setActive(true);
								}
								if (existRequest)
									requestService.update(request);
								else
									requestService.save(request);

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public void syncBTs(List<GDoc> configs, TypeRequest type, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, descriptionIndex = null, soinManagementIndex = null, iceManagementIndex = null,
					btIndex = null, codeIce = null, count = 0;
			Project proyect = null;
			Request request = null;
			Boolean existRequest = false;
			// Se recorren las configuracion para acceder a la hoja de TPOs
			for (GDoc config : configs) {
				List<List<Object>> values = getSheets(config, "BT´s");
				count = 0;
				// Se recorre los registros con un contador para definir los indices del inicio
				// con la primera fila
				for (List<Object> row : values) {
					// Se inicia de nuevo el requerimiento

					request = null;
					if (count == 0) {
						for (int i = 0; i < row.size(); i++)
							row.set(i, ((String) row.get(i)).trim());
						statusIndex = row.indexOf("Estado");
						btIndex = row.indexOf("Boleta");
						codeIce = row.indexOf("RQ");
						descriptionIndex = row.indexOf("Nombre");
						soinManagementIndex = row.indexOf("Gestor SOIN");
						iceManagementIndex = row.indexOf("Referente ICE");
						proyect = config.getProyect();
					} else {
						// Se verifica que la hoja tenga la columna de TPO
						if (btIndex != -1) {
							// Se verfica si el requerimiento ya existe
							existRequest = false;
							for (Request req : requests) {
								if ((((String) row.get(btIndex)).trim()).equals(req.getCode_soin().trim())) {
									request = req;
									existRequest = true;
								}
							}
							try {
								// Se crea en caso de que no exista
								request = (!existRequest) ? new Request() : request;
								request.setCode_soin((String) row.get(btIndex));
								request.setStatus((String) row.get(statusIndex));
								request.setDescription((String) row.get(codeIce) + " " + row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
								request.setCode_ice((String) row.get(codeIce));
								request.setTypeRequest(type);
								request.setProyect(proyect);
								request.setActive(true);
								if (existRequest) {
									requestService.update(request);
								} else {
									request.setActive(true);
									requestService.save(request);
								}

							} catch (Exception e) {
								Sentry.capture(e, "request");
								logger.log(MyLevel.RELEASE_ERROR, e.toString());
							}
						}
					}
					count++;
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	public List<List<Object>> getSheets(GDoc config, String range) {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			final String spreadsheetId = config.getSpreadSheet();
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, config))
					.setApplicationName(APPLICATION_NAME).build();
			ValueRange values = service.spreadsheets().values().get(spreadsheetId, range).execute();
			List<List<Object>> sheets = values.getValues();
			return sheets;
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return new ArrayList<List<Object>>();
	}

	/**
	 * Se crea un objeto de credenciales con la informacion del excel.
	 */
	@SuppressWarnings("deprecation")
	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GDoc config)
			throws IOException, GeneralSecurityException {
		String key = config.getCredentials();

		InputStream inputStream = new ByteArrayInputStream(key.getBytes(Charset.forName("UTF-8")));

		Credential credential = GoogleCredential.fromStream(inputStream, HTTP_TRANSPORT, JSON_FACTORY)
				.createScoped(SCOPES);
		credential.refreshToken();
		return credential;

	}

	@RequestMapping(value = "/softDelete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse softDelete(HttpServletRequest req, HttpServletResponse response, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Request request;
				Integer requestId = Integer.parseInt(req.getParameter("requestId"));
				request = requestService.findById(requestId);
				request.setActive(!request.getActive());
				requestService.softDelete(request);
				res.setObj(request.getActive());
			} else if (profile.equals("postgres")) {
				PRequest prequest;
				Integer prequestId = Integer.parseInt(req.getParameter("requestId"));
				prequest = prequestService.findById(prequestId);
				prequest.setActive(!prequest.getActive());
				prequestService.softDelete(prequest);
				res.setObj(prequest.getActive());
			}
			
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

}
