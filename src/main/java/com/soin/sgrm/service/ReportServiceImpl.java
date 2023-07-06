package com.soin.sgrm.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.gdata.data.analytics.DataSource;

import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReportReleaseDTO;
import com.soin.sgrm.model.ReleaseTracking;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {


	@Autowired
	private Environment env;
	
	private JRBeanCollectionDataSource trackingDataSource;
	
	/*
	 * @Override public ReportReleaseDTO obtenerReportReleases(Map<String, Object>
	 * params) throws IOException, JRException { ReportReleaseDTO dto= new
	 * ReportReleaseDTO(); String fileName="report_releases";
	 * dto.setFileName(fileName+".pdf");
	 * 
	 * ByteArrayOutputStream stream= reportManager.export(fileName,
	 * params.get("tipo").toString(), params,null); byte[] bs=stream.toByteArray();
	 * dto.setStream(new ByteArrayInputStream(bs)); dto.setLength(bs.length); return
	 * dto;
	 * 
	 * }
	 */
	@Override
	public HttpServletResponse getReportReleases(ReleaseReport release, String imageBase64,HttpServletResponse response) throws JRException, IOException {
		String fileName = "ReleaseReport";
		 byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageBase64.replaceAll("data:image/.+;base64,", ""));
		 InputStream targetStream = new ByteArrayInputStream(decodedBytes);
		ClassPathResource resource = new ClassPathResource("reports" + File.separator + "ReleaseReport" + ".jrxml");
		InputStream inputStream = resource.getInputStream();
		// Compile the Jasper report from .jrxml to .japser
		// InputStream jasperReport = reportManager.export(fileName);
		JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
		List<ReleaseReport> releases = new ArrayList<>();
	
		releases.add(release);
	

		// Get your data source
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(releases);
		

		// Add parameters
		Map<String, Object> parameters = new HashMap<>();

		if (release.getStatus() != null) {
			parameters.put("statusRelease", release.getStatus().getName());
		}
		if (release.getSystem() != null) {
			parameters.put("systemName", release.getSystem().getName());
		}
		if (release.getImpact() != null) {
			parameters.put("impact", release.getImpact().getName());
		}
		if (release.getRisk() != null) {
			parameters.put("risk", release.getRisk().getName());
		}
		if (release.getPriority() != null) {
			parameters.put("priority", release.getPriority().getName());
		}
		
		parameters.put("treeImage", targetStream);
		
	

		// Fill the report
		JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);
		String basePath = env.getProperty("fileStore.path");
		// Export the report to a PDF file
		//JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "/Emp-Rpt.pdf");
		response.setContentType("application/pdf");
		String reportName= "REPORTE-"+release.getReleaseNumber();
		response.setHeader("Content-Disposition", "inline;filename=" +reportName );

		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		System.out.println("Done");

		return response;

	}

	@Override
	public ReportReleaseDTO obtenerReportReleases(Map<String, Object> params) throws IOException, JRException {
		// TODO Auto-generated method stub
		return null;
	}

}
