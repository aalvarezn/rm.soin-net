package com.soin.sgrm.controller.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.GDocService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.TypeRequestService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

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
	GDocService gDocService;

	private static final String APPLICATION_NAME = "sgrm";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest req, Locale locale, Model model, HttpSession session) {
		model.addAttribute("requests", requestService.list());
		model.addAttribute("request", new Request());
		model.addAttribute("projects", projectService.listAll());
		model.addAttribute("project", new Project());
		model.addAttribute("typeRequests", typeRequestService.list());
		model.addAttribute("typeRequest", new TypeRequest());
		return "/admin/request/request";
	}

	@RequestMapping(value = "/findRequest/{id}", method = RequestMethod.GET)
	public @ResponseBody Request findRequest(@PathVariable Integer id, HttpServletRequest req, Locale locale,
			Model model, HttpSession session) {
		try {
			Request request = requestService.findById(id);
			return request;
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
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
				request.setActive(true);
				request.setProyect(projectService.findById(request.getProyectId()));
				request.setTypeRequest(typeRequestService.findById(request.getTypeRequestId()));
				requestService.save(request);
				res.setObj(request);
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
			requestService.delete(id);
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

	public void syncBTs(List<GDoc> configs, TypeRequest type, List<Request> requests) {
		try {
			// Se define los indices de las columnas
			Integer statusIndex = null, descriptionIndex = null, soinManagementIndex = null, iceManagementIndex = null,
					btIndex = null, count = 0;
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
						btIndex = row.indexOf("ID Boleta");
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
								request.setDescription((String) row.get(descriptionIndex));
								request.setSoinManagement((String) row.get(soinManagementIndex));
								request.setIceManagement((String) row.get(iceManagementIndex));
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
		Request request;
		try {
			Integer requestId = Integer.parseInt(req.getParameter("requestId"));
			request = requestService.findById(requestId);
			request.setActive(!request.getActive());
			requestService.softDelete(request);
			res.setObj(request.getActive());
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

}
