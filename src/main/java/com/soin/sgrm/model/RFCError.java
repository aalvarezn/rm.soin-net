package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RFC_ERROR")
public class RFCError implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_ERROR")
	private Timestamp errorDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SIGES_ID", nullable = true)
	private Siges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RFC_ID", nullable = true)
	private RFC_WithoutRelease rfc;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ERROR_ID", nullable = true)
	private Errors_RFC error;
	
	@Column(name = "OBSERVACIONES")
	private String observations;

	@Transient 
	private String errorName;
	
	@Transient 
	private String rfcName;
	
	@Transient 
	private String systemName;
	
	@Transient 
	private String sigesName;
	
	@Transient 
	private String userName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RFC_WithoutRelease getRfc() {
		return rfc;
	}

	public void setRfc(RFC_WithoutRelease rfc) {
		this.rfc = rfc;
	}

	public Errors_RFC getError() {
		return error;
	}

	public void setError(Errors_RFC error) {
		this.error = error;
	}

	public Timestamp getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Timestamp errorDate) {
		this.errorDate = errorDate;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Siges getSiges() {
		return siges;
	}

	public void setSiges(Siges siges) {
		this.siges = siges;
	}

	public String getErrorName() {
		
		if(getError()!=null) {
			return getError().getName();
		}else {
			return "No hay un error relacionado";
		}	
	}

	public String getRfcName() {
		if(getRfc()!=null) {
			return getRfc().getNumRequest();
		}else {
			return "No hay un RFC relacionado";
		}	
	}

	public String getSystemName() {
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema relacionado";
		}	
	}

	public String getSigesName() {
		if(getSiges()!=null) {
			return getSiges().getCodeSiges();
		}else {
			return "No hay un proyecto siges relacionado";
		}	
	}

	public String getUserName() {
		if(getRfc()!=null) {
			return getRfc().getUser().getFullName();
		}else {
			return "No hay un usuario relacionado";
		}
	}
}