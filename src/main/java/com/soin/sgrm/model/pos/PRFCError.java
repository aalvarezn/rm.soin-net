package com.soin.sgrm.model.pos;

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
public class PRFCError implements Serializable {
	
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
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SIGES_ID\"", nullable = true)
	private PSiges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RFC_ID\"", nullable = true)
	private PRFC_WithoutRelease rfc;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ERROR_ID\"", nullable = true)
	private PErrors_RFC error;
	
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

	public PRFC_WithoutRelease getRfc() {
		return rfc;
	}

	public void setRfc(PRFC_WithoutRelease rfc) {
		this.rfc = rfc;
	}

	public PErrors_RFC getError() {
		return error;
	}

	public void setError(PErrors_RFC error) {
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

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public PSiges getSiges() {
		return siges;
	}

	public void setSiges(PSiges siges) {
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