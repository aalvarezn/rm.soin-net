package com.soin.sgrm.model.pos;

import java.util.List;
import javax.persistence.Transient;

import com.soin.sgrm.model.ErrorTypeGraph;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PErrorRFCReport {

	@Transient
	private List<PRFCError> listErrorRFC;
	@Transient
	private PSystem system;
	@Transient
	private PSiges siges;
	@Transient
	private PErrors_RFC typeError;
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
	private JRBeanCollectionDataSource systemXGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemTableXGraphSource;


	public List<PRFCError> getListErrorRelease() {
		return listErrorRFC;
	}

	public void setListErrorRelease(List<PRFCError> listErrorRFC) {
		this.listErrorRFC = listErrorRFC;
	}

	
	public void setErrordataSource(List<PRFCError> errordataSource) {
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

	public void setSiges(PSiges siges) {
		this.siges=siges;
	}

	public void setTypeError(PErrors_RFC typeError) {
		this.typeError=typeError;
	}

	public PSystem getSystem() {
		return system;
	}

	public PSiges getSiges() {
		return siges;
	}

	public PErrors_RFC getTypeError() {
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



	public void setSystemXGraphSource(List<ErrorTypeGraph> sistemXGraphSource) {
		JRBeanCollectionDataSource systemXGraphSource1 = new JRBeanCollectionDataSource(sistemXGraphSource, false);
		this.systemXGraphSource = systemXGraphSource1;
	}

	public JRBeanCollectionDataSource getSystemXGraphSource() {
		return systemXGraphSource;
	}

	public void setSystemTableXGraphSource(List<ErrorTypeGraph> systemXGraphList) {
		JRBeanCollectionDataSource systemTableXGraphSource1 = new JRBeanCollectionDataSource(systemXGraphList, false);
		this.systemTableXGraphSource = systemTableXGraphSource1 ;
	}

	public JRBeanCollectionDataSource getSystemTableXGraphSource() {
		return systemTableXGraphSource;
	}
	
	
}