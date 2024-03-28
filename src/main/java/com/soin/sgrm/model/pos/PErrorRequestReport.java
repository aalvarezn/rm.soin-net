package com.soin.sgrm.model.pos;

import java.util.List;
import javax.persistence.Transient;

import com.soin.sgrm.model.ErrorTypeGraph;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PErrorRequestReport {

	@Transient
	private List<PRequestError> listErrorRequest;
	@Transient
	private PSystem system;
	@Transient
	private PTypePetition typePetition;
	@Transient
	private PErrors_Requests typeError;
	@Transient
	private String systemNameNew;
	
	@Transient
	private String typePetitionNameNew;
	
	@Transient
	private String typeErrorNameNew;
	
	@Transient
	private String dateNew;
	
	@Transient
	private JRBeanCollectionDataSource errordataSource;
	
	@Transient
	private JRBeanCollectionDataSource errorTypeGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource typePetitionGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemGraphSource;
	
	@Transient
	private JRBeanCollectionDataSource systemXGraphSource;

	@Transient
	private JRBeanCollectionDataSource systemTableXGraphSource;

	public List<PRequestError> getListErrorRelease() {
		return listErrorRequest;
	}

	public void setListErrorRelease(List<PRequestError> listErrorRequest) {
		this.listErrorRequest = listErrorRequest;
	}

	
	public void setErrordataSource(List<PRequestError> errordataSource) {
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
	
	public void setTypePetitionGraphSource(List<ErrorTypeGraph> typePetitionGraphSource) {
		JRBeanCollectionDataSource typePetitionGraphSource1 = new JRBeanCollectionDataSource(typePetitionGraphSource, false);
		this.typePetitionGraphSource = typePetitionGraphSource1;
	}
	public JRBeanCollectionDataSource getTypePetitionGraphSource() {
		return typePetitionGraphSource;
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

	public void setTypePetition(PTypePetition typePetition) {
		this.typePetition=typePetition;
	}

	public void setTypeError(PErrors_Requests typeError) {
		this.typeError=typeError;
	}

	public PSystem getSystem() {
		return system;
	}

	public PTypePetition getTypePetition() {
		return typePetition;
	}

	public PErrors_Requests getTypeError() {
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

	public String getTypePetitionNameNew() {
		if(getTypePetition()!=null) {
			return getTypePetition().getCode();
		}else {
			return "Sin tipo de solicitud seleccionada";
		}	
	}

	public void setTypePetitionNameNew(String typePetitionNameNew) {
		this.typePetitionNameNew = typePetitionNameNew;
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

	public JRBeanCollectionDataSource getSystemXGraphSource() {
		return systemXGraphSource;
	}

	public void setSystemXGraphSource(List<ErrorTypeGraph>  systemXGraphSource) {
		JRBeanCollectionDataSource systemXGraphSource1 = new JRBeanCollectionDataSource(systemXGraphSource, false);
		this.systemXGraphSource = systemXGraphSource1;
	}

	public void setSystemTableXGraphSource(List<ErrorTypeGraph> errorSystemXErrorGraphList) {
		JRBeanCollectionDataSource systemTableXGraphSource1 = new JRBeanCollectionDataSource(errorSystemXErrorGraphList, false);
		this.systemTableXGraphSource = systemTableXGraphSource1;
	}

	public JRBeanCollectionDataSource getSystemTableXGraphSource() {
		return systemTableXGraphSource;
	}
	
	
}