package com.soin.sgrm.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReportReleaseDTO;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {
	ReportReleaseDTO obtenerReportReleases(Map<String,Object> params) throws IOException, JRException;

	HttpServletResponse getReportReleases(ReleaseReport release,String imageBase64,HttpServletResponse response) throws JRException, IOException;
	
}
