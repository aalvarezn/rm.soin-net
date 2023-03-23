package com.soin.sgrm.model;

import java.util.List;
import javax.persistence.Transient;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ErrorRFCReport {

	@Transient
	private List<RFCError> listErrorRFC;
	@Transient
	private System system;
	@Transient
	private Siges siges;
	@Transient
	private Errors_RFC typeError;
	@Transient
	private String systemNameNew;
	
	@Transient
	private String sigesNameNew;
	
	@Transient
	private String typeErrorNameNew;
	
	@Transient
	private String dateNew;
	
	@Transient
	private JRBeanCollectionDataSource errordataSource;
	
	@Transient
	private JRBeanCollectionDataSource errorTypeGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource sigesGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemGraphSource;

	public List<RFCError> getListErrorRelease() {
		return listErrorRFC;
	}

	public void setListErrorRelease(List<RFCError> listErrorRFC) {
		this.listErrorRFC = listErrorRFC;
	}

	
	public void setErrordataSource(List<RFCError> errordataSource) {
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
	
	public void setSigesGraphSource(List<ErrorTypeGraph> sigesGraphSource) {
		JRBeanCollectionDataSource sigesGraphSource1 = new JRBeanCollectionDataSource(sigesGraphSource, false);
		this.sigesGraphSource = sigesGraphSource1;
	}
	public JRBeanCollectionDataSource getSigesGraphSource() {
		return sigesGraphSource;
	}
	
	public void setSystemGraphSource(List<ErrorTypeGraph> systemGraphSource) {
		JRBeanCollectionDataSource systemGraphSource1 = new JRBeanCollectionDataSource(systemGraphSource, false);
		this.systemGraphSource = systemGraphSource1;
	}
	
	public JRBeanCollectionDataSource getSystemGraphSource() {
		return systemGraphSource;
	}
	
	public void setSystem(System system) {
		this.system=system;
	}

	public void setSiges(Siges siges) {
		this.siges=siges;
	}

	public void setTypeError(Errors_RFC typeError) {
		this.typeError=typeError;
	}

	public System getSystem() {
		return system;
	}

	public Siges getSiges() {
		return siges;
	}

	public Errors_RFC getTypeError() {
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

	public String getSigesNameNew() {
		if(getSiges()!=null) {
			return getSiges().getCodeSiges();
		}else {
			return "Sin codigo siges seleccionado";
		}	
	}

	public void setSigesNameNew(String sigesNameNew) {
		this.sigesNameNew = sigesNameNew;
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

	
}