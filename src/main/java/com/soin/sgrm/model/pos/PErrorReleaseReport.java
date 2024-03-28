package com.soin.sgrm.model.pos;

import java.util.List;
import javax.persistence.Transient;

import com.soin.sgrm.model.ErrorTypeGraph;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PErrorReleaseReport {

	@Transient
	private List<PReleaseError> listErrorRelease;
	@Transient
	private PSystem system;
	@Transient
	private PProject project;
	@Transient
	private PErrors_Release typeError;
	@Transient
	private String systemNameNew;
	
	@Transient
	private String projectNameNew;
	
	@Transient
	private String typeErrorNameNew;
	
	@Transient
	private String dateNew;
	
	@Transient
	private JRBeanCollectionDataSource errordataSource;
	
	@Transient
	private JRBeanCollectionDataSource errorTypeGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource projectGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource projectTableGraphSource;

	public List<PReleaseError> getListErrorRelease() {
		return listErrorRelease;
	}

	public void setListErrorRelease(List<PReleaseError> listErrorRelease) {
		this.listErrorRelease = listErrorRelease;
	}

	
	public void setErrordataSource(List<PReleaseError> errordataSource) {
		JRBeanCollectionDataSource errordataSource2 = new JRBeanCollectionDataSource(errordataSource, false);
		this.errordataSource = errordataSource2;
	}
	
	public JRBeanCollectionDataSource getErrordataSource() {
		
		return errordataSource;
	}
	
	public void setErrorTypeGraphSource(List<ErrorTypeGraph> errorTypeGraphSource) {
		JRBeanCollectionDataSource errorTypeGraphSource1 = new JRBeanCollectionDataSource(errorTypeGraphSource, false);
		this.errorTypeGraphSource = errorTypeGraphSource1;
	}
	
	public JRBeanCollectionDataSource getErrorTypeGraphSource() {
		return errorTypeGraphSource;
	}
	
	public void setProjectGraphSource(List<ErrorTypeGraph> projectGraphSource) {
		JRBeanCollectionDataSource projectGraphSource1 = new JRBeanCollectionDataSource(projectGraphSource, false);
		this.projectGraphSource = projectGraphSource1;
	}
	public JRBeanCollectionDataSource getProjectGraphSource() {
		return projectGraphSource;
	}
	
	public void setSystemGraphSource(List<ErrorTypeGraph> systemGraphSource) {
		JRBeanCollectionDataSource systemGraphSource1 = new JRBeanCollectionDataSource(systemGraphSource, false);
		this.systemGraphSource = systemGraphSource1;
	}
	
	public JRBeanCollectionDataSource getSystemGraphSource() {
		return systemGraphSource;
	}
	
	public void setSystem(PSystem system) {
		this.system=system;
	}

	public void setProject(PProject project) {
		this.project=project;
	}

	public void setTypeError(PErrors_Release typeError) {
		this.typeError=typeError;
	}

	public PSystem getSystem() {
		return system;
	}

	public PProject getProject() {
		return project;
	}

	public PErrors_Release getTypeError() {
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

	public void setErrordataSource(JRBeanCollectionDataSource errordataSource) {
		this.errordataSource = errordataSource;
	}

	public void setProjectTableGraphSource(List<ErrorTypeGraph> errorProjectGraphList) {
		JRBeanCollectionDataSource projectTableGraphSource1 = new JRBeanCollectionDataSource(errorProjectGraphList, false);
		this.projectTableGraphSource = projectTableGraphSource1;
	}

	public JRBeanCollectionDataSource getProjectTableGraphSource() {
		return projectTableGraphSource;
	}


	
}