package com.soin.sgrm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.BaseKnowledgeFile;
import com.soin.sgrm.model.DocTemplate;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseFile;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseFile;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.BaseKnowledgeFileService;
import com.soin.sgrm.service.BaseKnowledgeService;
import com.soin.sgrm.service.DocTemplateService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCFileService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseFileService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestFileService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.DocxContext;
import com.soin.sgrm.utils.DocxVariables;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

	public static final Logger logger = Logger.getLogger(FileController.class);

	@Autowired
	private DocTemplateService docsTemplateService;
	@Autowired
	ReleaseService releaseService;
	@Autowired
	RequestService requestService;
	@Autowired
	ProjectService projectService;
	@Autowired
	SystemService systemService;
	@Autowired
	ReleaseFileService releaseFileService;
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	RFCService rfcService;
	
	@Autowired
	BaseKnowledgeService baseKnowledgeService;
	
	@Autowired
	RequestBaseService requestBaseService;

	@Autowired 
	RFCFileService rfcFileService;
	
	@Autowired 
	BaseKnowledgeFileService baseKnowFileService;
	
	@Autowired 
	RequestFileService requestFileService;
	
	@Autowired
	SigesService sigesService;
	
	@Autowired
	private Environment env;

	DocxVariables docxVariables = null;

	DocxContext context = null;

	Map<String, String> replacedElementsMap;

	/**
	 * @description: Descarga de un archivo particular del release.
	 * @author: Esteban Bogantes H.
	 * @return: archivo a descargar.
	 * @throws SQLException
	 **/
	@RequestMapping(value = "/impactObject-{id}", method = RequestMethod.GET)
	public void impactObject(HttpServletResponse response, @PathVariable Integer id) throws IOException, SQLException {
		String basePath = env.getProperty("fileStore.path");
		ReleaseSummary release = releaseService.findById(id); // se obtiene el release
		String path = basePath + createPath(release, basePath) + "documentos/";
		new File(path).mkdirs();
		File temp = new File(path + release.getReleaseNumber() + ".csv");
		List<String> list = releaseFileService.ImpactObjects(id); // lista de objetos
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(temp);
			for (String string : list) {
				fileWriter.append(
						((string == null || string.length() == 0) ? "" : (string.substring(0, string.length() - 1))));
				fileWriter.append("\n");
			}
		} catch (Exception e) {
			Sentry.capture(e, "files");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				Sentry.capture(e, "files");
				logger.log(MyLevel.RELEASE_ERROR, e.toString());
			}
		}

		prepareFile(response, temp, temp.getName());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(temp));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	/**
	 * @description: Adjunta el archivo al release.
	 * @author: Esteban Bogantes H.
	 * @return: estado de la carga del archivo.
	 * @throws SQLException
	 **/
	@RequestMapping(value = "/singleUpload-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse singleFileUpload(@PathVariable Integer id,
			@RequestParam("file") MultipartFile file) throws SQLException {
		JsonResponse json = new JsonResponse();

		// valida que se selecciono un archivo
		if (file.getName().equals("") || file.isEmpty()) {
			json.setStatus("fail");
			json.setException("Archivo no seleccionado");
			return json;
		}

		// Direccion del archivo a guardar
		String basePath = env.getProperty("fileStore.path");
		ReleaseSummary release=releaseService.findById(id);
		String path = createPath(release, basePath);
		String fileName = file.getOriginalFilename().replaceAll("\\s", "_");

		// Referencia del archivo
		ReleaseFile releaseFile = new ReleaseFile();
		releaseFile.setName(fileName);
		releaseFile.setPath(basePath + path + fileName);
		long time = System.currentTimeMillis();
		java.sql.Timestamp revisionDate = new java.sql.Timestamp(time);
		releaseFile.setRevisionDate(revisionDate);
		try {
			// Se carga el archivo y se guarda la referencia
			FileCopyUtils.copy(file.getBytes(), new File(basePath + path + fileName));
			releaseFileService.save(id, releaseFile);
			releaseFile = releaseFileService.findReleaseFile(releaseFile.getPath());
			json.setStatus("success");
			json.setObj(releaseFile);
		} catch (SQLException ex) {
			Sentry.capture(ex, "files");
			json.setStatus("exception");
			json.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			json.setStatus("exception");
			json.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			if (e instanceof MaxUploadSizeExceededException) {
				json.setException("Tamaño máximo de" + Constant.MAXFILEUPLOADSIZE + "MB.");
			}
		}
		return json;
	}

	/**
	 * @description: Descarga de un archivo particular del release.
	 * @author: Esteban Bogantes H.
	 * @return: archivo a descargar.
	 **/
	@RequestMapping(value = "/singleDownload-{id}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable Integer id) throws IOException {

		ReleaseFile releaseFile = releaseFileService.findReleaseFileById(id);
		File file = new File(releaseFile.getPath());

		// Se modifica la respuesta para descargar el archivo
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + releaseFile.getName());
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

	/**
	 * @description: borra un archivo adjuntado de un release.
	 * @author: Esteban Bogantes H.
	 * @return: response de la eliminacion.
	 **/
	@RequestMapping(value = "/deleteFileUpload-{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteFileUpload(@PathVariable Integer id) {
		JsonResponse res = new JsonResponse();
		res.setStatus("success");
		try {
			ReleaseFile releaseFile = releaseFileService.findReleaseFileById(id);
			releaseFileService.deleteReleaseFile(releaseFile);
			File file = new File(releaseFile.getPath());
			if (file.exists()) {
				file.delete();
			}
			res.setData(releaseFile.getId() + "");
		} catch (SQLException ex) {
			Sentry.capture(ex, "files");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	/**
	 * @description: Se crea la direccion donde se guardan los archivos del release.
	 * @author: Esteban Bogantes H.
	 * @return: Base path del release.
	 * @throws SQLException
	 **/
	public String createPath(ReleaseSummary release, String basePath) throws SQLException {

			Project project = projectService.findById(release.getSystem().getProyectId());
			String path = project.getCode() + "/" + release.getSystem().getName() + "/";
			if (release.getRequestList().size() != 0) {
				Request request = release.getRequestList().iterator().next();
				if (request.getCode_ice() != null) {
					path += request.getCode_soin().replace(" ","") + "_" + request.getCode_ice().replace(" ","") + "/";
				} else {
					path += request.getCode_soin().replace(" ","") + "/";
				}
			}
			path += release.getReleaseNumber() + "/";
			path=path.trim();
			new File(basePath + path).mkdirs();
			return path;
		

	}

	/**
	 * @description: Crea la asociacion para descargar el archivo en la respuesta.
	 * @author: Esteban Bogantes H.
	 * @return: response modificado.
	 **/
	public HttpServletResponse prepareFile(HttpServletResponse response, File outFile, String name) {
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + name);

		String mimeType = URLConnection.guessContentTypeFromName(name);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name));

		response.setContentLength((int) outFile.length());
		return response;
	}

	/**
	 * @description: Se genera el documento del release para descargar.
	 * @author: Esteban Bogantes H.
	 * @return: archivo a descargar.
	 **/
	@SuppressWarnings({ "unused" })
	@RequestMapping(value = "/download/{releaseId}/{docId}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable Integer releaseId, @PathVariable Integer docId)
			throws FileNotFoundException, Docx4JException, IOException {

		String basePath = env.getProperty("fileStore.path");
		try {
			DocTemplate docFile = docsTemplateService.findById(docId);
			ReleaseSummary release = releaseService.findById(releaseId);

			if (docFile != null) {
				// Se obtiene el archivo plantilla
				File fileTemplate = new File(env.getProperty("fileStore.templates") + docFile.getTemplateName());
				String path = basePath + createPath(release, basePath) + "documentos/";
				new File(path).mkdirs();
				String sufix = (docFile.getSufix() == null) ? "" : docFile.getSufix();
				// Se genera el archivo de salida
				File outFile = new File(path + release.getReleaseNumber() + sufix + ".docx");

				// TODO Descomentear esta linea
				// if (!outFile.exists()) {
				FileUtils.copyFile(fileTemplate, outFile);

				if (docFile.getComponentGenerator().equals("GenerarDocumentoBRM_IFW_BO")) {
					preloadCommands(release, fileTemplate, outFile);
				}

				Docx docx = new Docx(outFile.getPath());
				docx.setVariablePattern(new VariablePattern("{{", "}}"));
				// preparing variables
				docxVariables = new DocxVariables();

				if (docFile.getComponentGenerator().equals("GenerarDocumentoAIA")) {
					generateAIADocument(release, docFile.getTemplateName());
				}

				if (docFile.getComponentGenerator().equals("GenerarDocumentoSBLRFT")) {
					generateSBLRFTDocument(release, docFile.getTemplateName());
				}

				if (docFile.getComponentGenerator().equals("GenerarDocumentoCorpCBO")) {
					generateCorpCBO(release, docFile.getTemplateName());
				}

				if (docFile.getComponentGenerator().equals("GenerarDocumentoKomercialICE")) {
					generateKMIDocument(release, docFile.getTemplateName());
				}
				if (docFile.getComponentGenerator().equals("GenerarDocumentoATV")) {
					generateATVDocument(release, docFile.getTemplateName());
				}
				if (docFile.getComponentGenerator().equals("GenerarDocumentoAGILE")) {
					generateAGILEDocument(release, docFile.getTemplateName());
				}
				if (docFile.getComponentGenerator().equals("GenerarDocumentoBRM_IFW_BO")) {
					generateBRM_IFW_BODocument(release, docFile.getTemplateName());
				}

				docx.fillTemplate(docxVariables.getVariables());
				docx.save(outFile.getPath());
				XWPFDocument document = docx.getXWPFDocument();
				lineBreak(document, outFile);
				response = prepareFile(response, outFile, outFile.getName());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(outFile));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			}

		} catch (Exception e) {
			Sentry.capture(e, "files");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
	}

	private void lineBreak(XWPFDocument document, File outFile) throws FileNotFoundException, IOException {
		List<XWPFTable> xwpftables = document.getTables();
		// se recorren las tablas
		for (XWPFTable xwpftable : xwpftables) {
			List<XWPFTableRow> xwpfrows = xwpftable.getRows();
			// se recorren las filas de la tabla
			for (XWPFTableRow xwpfrow : xwpfrows) {
				List<XWPFTableCell> xwpfcells = xwpfrow.getTableCells();
				// se recorren las celdas de la fila
				for (XWPFTableCell xwpfcell : xwpfcells) {
					for (XWPFParagraph p : xwpfcell.getParagraphs()) {
						for (XWPFRun xwpfRun : p.getRuns()) {
							String xwpfRunText = xwpfRun.getText(xwpfRun.getTextPosition());
							if (xwpfRunText != null && xwpfRunText.contains("\n")) {
								String[] lines = xwpfRunText.split("\n");
								if (lines.length > 0) {
									xwpfRun.setText(lines[0], 0); // set first line into XWPFRun
									for (int i = 1; i < lines.length; i++) {
										// add break and insert new text
										xwpfRun.addBreak();
										xwpfRun.setText(lines[i]);
									}
								}

							} else {
								if (xwpfRunText != null)
									xwpfRun.setText(xwpfRunText, 0);
							}
						}
					}
				}
			}
			document.write(new FileOutputStream(outFile.getPath()));
		}
	}

	/**
	 * @description: Completa variables para el documento de AIA.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de AIA.
	 **/
	public void generateAIADocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.releaseDataBaseInstructions(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
		docxVariables.releaseEnvironment(release);
		docxVariables.releaseActions(release);
		docxVariables.releaseModifiedComponents(release);
	}

	/**
	 * @description: Completa variables para el documento de ATV.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de ATV.
	 **/
	public void generateKMIDocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseActions(release);
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
	}

	/**
	 * @description: Completa variables para el documento de ATV.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de ATV.
	 **/
	public void generateATVDocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.releaseActions(release);
		docxVariables.releaseDataBaseInstructions(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
	}

	/**
	 * @description: Completa variables para el documento de AGILE.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de AGILE.
	 **/
	public void generateAGILEDocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseActions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
	}

	/**
	 * @description: Completa variables para el documento de CorpCBO.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de CorpCBO.
	 **/
	public void generateCorpCBO(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.releaseActions(release);
		docxVariables.releaseDataBaseInstructions(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
	}

	/**
	 * @description: Completa variables para el documento de SBLRFT.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de SBLRFT
	 **/
	public void generateSBLRFTDocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.releaseActions(release);
		docxVariables.releaseDataBaseInstructions(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
	}

	/**
	 * @description: Completa variables para el documento de BRM_IFW_BO.
	 * @author: Esteban Bogantes H.
	 * @return: Variables para el documento de BRM_IFW_BO
	 **/
	public void generateBRM_IFW_BODocument(ReleaseSummary release, String fileNameDoc) throws Exception {
		docxVariables.generalInfo(release, systemService);
		docxVariables.releaseSolutionInformation(release);
		docxVariables.releaseObjects(release, (fileNameDoc.contains("BD") || fileNameDoc.contains("DB")));
		docxVariables.releaseAmbientInformation(release);
		docxVariables.releaseAmbients(release);
		docxVariables.releaseDataBaseInstructions(release);
		docxVariables.releaseInstalationInstructions(release);
		docxVariables.addVariable("{{r pruebas_minimas_sugeridas_en_qa}}",
				(release.getMinimal_evidence() != null ? release.getMinimal_evidence() : Constant.EMPTYVARDOC));
		docxVariables.releaseButtonCommandDetails(release);
		docxVariables.releaseButtonFileDetails(release);
		docxVariables.releaseEnvironment(release);
		docxVariables.releaseActions(release);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void preloadCommands(ReleaseSummary release, File fileTemplate, File outFile) throws Exception {
		context = new DocxContext();
		context.loadCrontabs(release, systemService);
		context.loadButtonCommands(release, systemService);
		context.loadButtonFiles(release, systemService);
		context.definedReports(release);
		DocxStamper stamper = new DocxStamperConfiguration().build();
		InputStream inputstream = new FileInputStream(fileTemplate);
		OutputStream os = new FileOutputStream(outFile);
		stamper.stamp(inputstream, context, os);
		os.close();
	}
	

	/**
	 * @description: Se crea la direccion donde se guardan los archivos del rfc.
	 * @author: Anthony Alvarez N.
	 * @return: Base path del release.
	 * @throws SQLException
	 **/
	public String createPathRFC(Long id, String basePath) throws SQLException {
		RFC rfc;
		try {
			rfc = rfcService.findById(id);
			Siges siges = sigesService.findByKey("codeSiges", rfc.getCodeProyect());
			SystemInfo system= siges.getSystem();
			String path = system.getName() + "/" +siges.getCodeSiges()  + "/";

			path += rfc.getNumRequest() + "/";
			new File(basePath + path).mkdirs();
			return path;
		} catch (Exception e) {
			Sentry.capture(e, "files");
			throw e;
		}

	}
	
	/**
	 * @description: Adjunta el archivo al rfc.
	 * @author: Anthony Alvarez N.
	 * @return: estado de la carga del archivo.
	 * @throws SQLException
	 **/
	@RequestMapping(value = "/singleUploadRFC-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse singleFileUploadRFC(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) throws SQLException {
		JsonResponse json = new JsonResponse();
		
		
		// valida que se selecciono un archivo
		if (file.getName().equals("") || file.isEmpty()) {
			json.setStatus("fail");
			json.setException("Archivo no seleccionado");
			return json;
		}
		
		// Direccion del archivo a guardar
		String basePath = env.getProperty("fileStore.path");
		String path = createPathRFC(id, basePath);
		String fileName = file.getOriginalFilename().replaceAll("\\s", "_");

		// Referencia del archivo
		RFCFile rfcFile = new RFCFile();
		rfcFile.setName(fileName);
		rfcFile.setPath(basePath + path + fileName);
		long time = System.currentTimeMillis();
		java.sql.Timestamp revisionDate = new java.sql.Timestamp(time);
		rfcFile.setRevisionDate(revisionDate);
		try {
			// Se carga el archivo y se guarda la referencia
			FileCopyUtils.copy(file.getBytes(), new File(basePath + path + fileName));
			rfcFileService.saveRFCFile(id, rfcFile);
			rfcFile = rfcFileService.findByKey("path", rfcFile.getPath());
			json.setStatus("success");
			json.setObj(rfcFile);
		} catch (SQLException ex) {
			Sentry.capture(ex, "files");
			json.setStatus("exception");
			json.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			json.setStatus("exception");
			json.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			if (e instanceof MaxUploadSizeExceededException) {
				json.setException("Tamaño máximo de" + Constant.MAXFILEUPLOADSIZE + "MB.");
			}
		}
		return json;
	}
	
	/**
	 * @description: Descarga de un archivo particular del rfc.
	 * @author: Anthony Alvarez N.
	 * @return: archivo a descargar.
	 **/
	@RequestMapping(value = "/singleDownloadRFC-{id}", method = RequestMethod.GET)
	public void downloadFileRFC(HttpServletResponse response, @PathVariable Long id) throws IOException {

		RFCFile rfcFile = rfcFileService.findById(id);
		File file = new File(rfcFile.getPath());

		// Se modifica la respuesta para descargar el archivo
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + rfcFile.getName());
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
	
	/**
	 * @description: borra un archivo adjuntado de un rfc.
	 * @author: Anthony Alvarez N.
	 * @return: response de la eliminacion.
	 **/
	@RequestMapping(value = "/deleteFileUploadRFC-{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteFileUploadRFC(@PathVariable Long id) {
		JsonResponse res = new JsonResponse();
		res.setStatus("success");
		try {
			RFCFile rfcFile = rfcFileService.findById(id);
			rfcFileService.deleteRFC(rfcFile);
			File file = new File(rfcFile.getPath());
			if (file.exists()) {
				file.delete();
			}
			res.setData(rfcFile.getId() + "");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	/**
	 * @description: Se crea la direccion donde se guardan los archivos del request.
	 * @author: Anthony Alvarez N.
	 * @return: Base path del request.
	 * @throws SQLException
	 **/
	public String createPathRequest(Long id, String basePath) throws SQLException {
		RequestBaseR1  requestBase;
		try {
			String path="";
			requestBase = requestBaseService.findByR1(id);
			if(!requestBase.getTypePetition().getCode().equals("RM-P1-R1")) {
				RequestBase newRequestBase =requestBaseService.findById(id);
				Siges siges = newRequestBase.getSiges();
				SystemInfo system= siges.getSystem();
				 path = system.getName() + "/" +siges.getCodeSiges()  + "/"+requestBase.getTypePetition().getCode()+"/";
			}else {
				SystemInfo system= requestBase.getSystemInfo();
				path = system.getName() + "/"+requestBase.getTypePetition().getCode()+"/";
			}
			

			path += requestBase.getNumRequest() + "/";
			new File(basePath + path).mkdirs();
			return path;
		} catch (Exception e) {
			Sentry.capture(e, "files");
			throw e;
		}

	}
	
	/**
	 * @description: Adjunta el archivo al request.
	 * @author: Anthony Alvarez N.
	 * @return: estado de la carga del archivo.
	 * @throws SQLException
	 **/
	@RequestMapping(value = "/singleUploadRequest-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse singleFileUploadRequest(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) throws SQLException {
		JsonResponse json = new JsonResponse();
		
		
		// valida que se selecciono un archivo
		if (file.getName().equals("") || file.isEmpty()) {
			json.setStatus("fail");
			json.setException("Archivo no seleccionado");
			return json;
		}
		
		// Direccion del archivo a guardar
		String basePath = env.getProperty("fileStore.path");
		String path = createPathRequest(id, basePath);
		String fileName = file.getOriginalFilename().replaceAll("\\s", "_");

		// Referencia del archivo
		RequestBaseFile requestFile = new RequestBaseFile();
		requestFile.setName(fileName);
		requestFile.setPath(basePath + path + fileName);
		long time = System.currentTimeMillis();
		java.sql.Timestamp revisionDate = new java.sql.Timestamp(time);
		requestFile.setRevisionDate(revisionDate);
		try {
			// Se carga el archivo y se guarda la referencia
			FileCopyUtils.copy(file.getBytes(), new File(basePath + path + fileName));
			requestFileService.saveRequestFile(id, requestFile);
			requestFile = requestFileService.findByKey("path", requestFile.getPath());
			json.setStatus("success");
			json.setObj(requestFile);
		} catch (SQLException ex) {
			Sentry.capture(ex, "files");
			json.setStatus("exception");
			json.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			json.setStatus("exception");
			json.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			if (e instanceof MaxUploadSizeExceededException) {
				json.setException("Tamaño máximo de" + Constant.MAXFILEUPLOADSIZE + "MB.");
			}
		}
		return json;
	}
	
	/**
	 * @description: Descarga de un archivo particular del rfc.
	 * @author: Anthony Alvarez N.
	 * @return: archivo a descargar.
	 **/
	@RequestMapping(value = "/singleDownloadRequest-{id}", method = RequestMethod.GET)
	public void downloadFileRequest(HttpServletResponse response, @PathVariable Long id) throws IOException {

		RequestBaseFile requestFile = requestFileService.findById(id);
		File file = new File(requestFile.getPath());

		// Se modifica la respuesta para descargar el archivo
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + requestFile.getName());
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
	
	/**
	 * @description: borra un archivo adjuntado de un rfc.
	 * @author: Anthony Alvarez N.
	 * @return: response de la eliminacion.
	 **/
	@RequestMapping(value = "/deleteFileUploadRequest-{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteFileUploadRequest(@PathVariable Long id) {
		JsonResponse res = new JsonResponse();
		res.setStatus("success");
		try {
			RequestBaseFile requestFile = requestFileService.findById(id);
			requestFileService.deleteRequest(requestFile);
			File file = new File(requestFile.getPath());
			if (file.exists()) {
				file.delete();
			}
			res.setData(requestFile.getId() + "");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	
	
	/**
	 * @description: Se crea la direccion donde se guardan los archivos del error know.
	 * @author: Anthony Alvarez N.
	 * @return: Base path del error know.
	 * @throws SQLException
	 **/
	public String createPathBaseKnow(BaseKnowledge baseKnowledge, String basePath) throws SQLException {
		try {
			String path = "/"+baseKnowledge.getComponent().getName()  + "/";

			path += baseKnowledge.getNumError() + "/";
			new File(basePath + path).mkdirs();
			return path;
		} catch (Exception e) {
			Sentry.capture(e, "files");
			throw e;
		}

	}
	
	/**
	 * @description: Adjunta el archivo al error know.
	 * @author: Anthony Alvarez N.
	 * @return: estado de la carga del archivo.
	 * @throws SQLException
	 **/
	@RequestMapping(value = "/singleUploadBaseKnow-{id}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse singleFileUploadBaseKnowledge(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) throws SQLException {
		JsonResponse json = new JsonResponse();
		BaseKnowledge baseKnowledge = baseKnowledgeService.findById(id);
		
		// valida que se selecciono un archivo
		if (file.getName().equals("") || file.isEmpty()) {
			json.setStatus("fail");
			json.setException("Archivo no seleccionado");
			return json;
		}
		
		// Direccion del archivo a guardar
		String basePath = baseKnowledge.getUrl();
		String path = createPathBaseKnow(baseKnowledge, basePath);
		String fileName = file.getOriginalFilename().replaceAll("\\s", "_");

		// Referencia del archivo
		BaseKnowledgeFile baseKnowFile = new BaseKnowledgeFile();
		baseKnowFile.setName(fileName);
		baseKnowFile.setPath(basePath + path + fileName);
		long time = System.currentTimeMillis();
		java.sql.Timestamp revisionDate = new java.sql.Timestamp(time);
		baseKnowFile.setRevisionDate(revisionDate);
		try {
			// Se carga el archivo y se guarda la referencia
			FileCopyUtils.copy(file.getBytes(), new File(basePath + path + fileName));
			baseKnowFileService.saveBaseKnowledgeFile(id, baseKnowFile);
			baseKnowFile = baseKnowFileService.findByKey("path", baseKnowFile.getPath());
			json.setStatus("success");
			json.setObj(baseKnowFile);
		} catch (SQLException ex) {
			Sentry.capture(ex, "files");
			json.setStatus("exception");
			json.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			json.setStatus("exception");
			json.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			if (e instanceof MaxUploadSizeExceededException) {
				json.setException("Tamaño máximo de" + Constant.MAXFILEUPLOADSIZE + "MB.");
			}
		}
		return json;
	}
	
	/**
	 * @description: Descarga de un archivo particular del error baseKnow.
	 * @author: Anthony Alvarez N.
	 * @return: archivo a descargar.
	 **/
	@RequestMapping(value = "/singleDownloadBaseKnowledge-{id}", method = RequestMethod.GET)
	public void downloadFileBaseKnowledge(HttpServletResponse response, @PathVariable Long id) throws IOException {

		BaseKnowledgeFile baseKnowFile = baseKnowFileService.findById(id);
		File file = new File(baseKnowFile.getPath());

		// Se modifica la respuesta para descargar el archivo
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + baseKnowFile.getName());
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
	
	/**
	 * @description: borra un archivo adjuntado de un base knowledge.
	 * @author: Anthony Alvarez N.
	 * @return: response de la eliminacion.
	 **/
	@RequestMapping(value = "/deleteFileUploadBaseKnowledge-{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteFileUploadBaseKnowledge(@PathVariable Long id) {
		JsonResponse res = new JsonResponse();
		res.setStatus("success");
		try {
			BaseKnowledgeFile baseKnowledgeFile = baseKnowFileService.findById(id);
			baseKnowFileService.deleteBaseKnowLedgeFile(baseKnowledgeFile);
			File file = new File(baseKnowledgeFile.getPath());
			if (file.exists()) {
				file.delete();
			}
			res.setData(baseKnowledgeFile.getId() + "");
		} catch (Exception e) {
			Sentry.capture(e, "files");
			res.setStatus("exception");
			res.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

}
