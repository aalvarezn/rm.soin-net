package com.soin.sgrm.model;

import java.util.List;
import javax.persistence.Transient;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportTest {

	@Transient
	private System system;
	@Transient
	private Project project;
	@Transient
	private Errors_Release typeError;
	@Transient
	private String systemNameNew;
	
	@Transient
	private String projectNameNew;
	
	@Transient
	private String typeErrorNameNew;
	
	@Transient
	private String dateNew;
	
	@Transient
	private JRBeanCollectionDataSource releaseDataSource;
	
	@Transient
	private JRBeanCollectionDataSource rfcDataSource;
	
	@Transient
	private JRBeanCollectionDataSource requestDataSource;
	@Transient
	private JRBeanCollectionDataSource countDataSource;

	
	public void setReleaseDataSource(List<ReleaseReportFast> releaseDataSource) {
		JRBeanCollectionDataSource releaseDataSource1 = new JRBeanCollectionDataSource(releaseDataSource, false);
		this.releaseDataSource = releaseDataSource1;
	}
	
	public JRBeanCollectionDataSource getReleaseDataSource() {
		
		return releaseDataSource;
	}
	
	public void setRfcDataSource(List<RFCReport> rfcDataSource) {
		JRBeanCollectionDataSource rfcDataSource1 = new JRBeanCollectionDataSource(rfcDataSource, false);
		this.rfcDataSource = rfcDataSource1;
	}
	
	public JRBeanCollectionDataSource getRfcDataSource() {
		
		return rfcDataSource;
	}
	
	public void setRequestDataSource(List<RequestReport> requests) {
		JRBeanCollectionDataSource requestDataSource1 = new JRBeanCollectionDataSource(requests, false);
		this.requestDataSource = requestDataSource1;
	}
	
	
	
	public JRBeanCollectionDataSource getCountDataSource() {
		return countDataSource;
	}

	public void setCountDataSource(List<?> count) {
		JRBeanCollectionDataSource countDataSource = new JRBeanCollectionDataSource(count, false);
		this.countDataSource = countDataSource;
	}

	public JRBeanCollectionDataSource getRequestDataSource() {
		return requestDataSource;
	}
	

	public void setSystem(System system) {
		this.system=system;
	}

	public void setProject(Project project) {
		this.project=project;
	}

	public void setTypeError(Errors_Release typeError) {
		this.typeError=typeError;
	}

	public System getSystem() {
		return system;
	}

	public Project getProject() {
		return project;
	}

	public Errors_Release getTypeError() {
		return typeError;
	}

	public String getSystemNameNew() {
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "Sin sistema seleccionado";
		}	
	}

	public void setSystemNameNew(String systemNameNew) {
		this.systemNameNew = systemNameNew;
	}

	public String getProjectNameNew() {
		if(getProject()!=null) {
			return getProject().getCode();
		}else {
			return "Sin proyecto seleccionado";
		}	
	}

	public void setProjectNameNew(String projectNameNew) {
		this.projectNameNew = projectNameNew;
	}

	public String getTypeErrorNameNew() {
		if(getTypeError()!=null) {
			return getTypeError().getName();
		}else {
			return "Sin error seleccionado";
		}	
	}

	public void setTypeErrorNameNew(String typeErrorNameNew) {
		this.typeErrorNameNew = typeErrorNameNew;
	}

	public String getDateNew() {
		return dateNew;
	}

	public void setDateNew(String dateNew) {
		if(dateNew!="") {
			this.dateNew = dateNew;
		}else {
			this.dateNew="Sin un rango de fecha establecido";
		}
	}





	
}