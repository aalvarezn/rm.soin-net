package com.soin.sgrm.controller.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/docFile")
public class DocFileController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(DocFileController.class);

	@Autowired
	private Environment env;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		HashMap<String, String> docFiles = new HashMap<String, String>();
		String basePath = env.getProperty("fileStore.templates");
		File folder = new File(basePath);
		File[] folderList = folder.listFiles();
		if (folderList != null && folderList.length > 0) {
			for (int i=0; i< folderList.length; i++) {
				if(!folderList[i].isDirectory())
					docFiles.put(FilenameUtils.removeExtension(folderList[i].getName()), FilenameUtils.getExtension(folderList[i].getPath()));
			}
		}
		model.addAttribute("docFiles", docFiles);
		return "/admin/docFile/docFile";	
	}

	@RequestMapping(value = "/download/{name}/{ext}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable String name, @PathVariable String ext) throws IOException {
		String basePath = env.getProperty("fileStore.templates");
		File file = new File(basePath+name+"."+ext);
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


	@RequestMapping(value = "/uploadDocFile", method = RequestMethod.POST)
	public @ResponseBody JsonResponse singleFileUpload(
			@RequestParam("file") MultipartFile file) {
		JsonResponse json = new JsonResponse();
		try {
			if (file.getName().equals("") || file.isEmpty()) {
				json.setStatus("fail");
				json.setException("Archivo no seleccionado");
				return json;
			}
			String basePath = env.getProperty("fileStore.templates");
			File oldFile = new File(basePath+ file.getOriginalFilename());
			//se verifica que el archivo exista en el folder
			if(!oldFile.exists()) {
				FileCopyUtils.copy(file.getBytes(), new File(basePath+ file.getOriginalFilename()));
			}else {
				//si existe se mueve a la carpeta de viejos con un consecutivo de fecha
				SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String date = sformat.format(new Date());
				File rename = new File(basePath+ "oldVersionFiles/"+date+"_"+file.getOriginalFilename());
				oldFile.renameTo(rename);
				oldFile.delete();
				FileCopyUtils.copy(file.getBytes(), new File(basePath+ file.getOriginalFilename()));
			}
			json.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e,"docFile");
			json.setStatus("exception");
			json.setException(e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			if (e instanceof MaxUploadSizeExceededException) {
				json.setException("Tamaño máximo de" + Constant.MAXFILEUPLOADSIZE + "MB.");
			}
		}
		return json;
	}

	@RequestMapping(value = "/delete/{name}/{ext}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteDocTemplate(HttpServletResponse response, @PathVariable String name, @PathVariable String ext, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String basePath = env.getProperty("fileStore.templates");
			File file = new File(basePath+name+"."+ext);

			if(file.exists()) {
				SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String date = sformat.format(new Date());
				File rename = new File(basePath+ "removedFiles/"+date+"_"+ file.getName());
				file.renameTo(rename);
				file.delete();
			}else {
				res.setStatus("exception");
				res.setException("El archivo seleccionado no existe.");
			}

			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e,"docFile");
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

}
