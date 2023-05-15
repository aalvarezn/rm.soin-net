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
@Table(name = "SOLICITUD_ERROR")
public class RequestError implements Serializable {
	
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
	@JoinColumn(name = "TIPO_SOLI_ID", nullable = true)
	private TypePetition typePetition;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITUD_ID", nullable = true)
	private RequestBase request;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ERROR_ID", nullable = true)
	private Errors_Requests error;
	
	@Column(name = "OBSERVACIONES")
	private String observations;

	@Transient 
	private String errorName;
	
	@Transient 
	private String requestName;
	
	@Transient 
	private String systemName;
	
	@Transient 
	private String typePetitionName;
	
	@Transient 
	private String userName;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequestBase getRequest() {
		return request;
	}

	public void setRequest(RequestBase request) {
		this.request = request;
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

	public TypePetition getTypePetition() {
		return typePetition;
	}

	public void setTypePetition(TypePetition typePetition) {
		this.typePetition = typePetition;
	}

	public Errors_Requests getError() {
		return error;
	}

	public void setError(Errors_Requests error) {
		this.error = error;
	}
	public String getErrorName() {
		
		if(getError()!=null) {
			return getError().getName();
		}else {
			return "No hay un error relacionado";
		}	
	}

	public String getRequestName() {
		if(getRequest()!=null) {
			return getRequest().getNumRequest();
		}else {
			return "No hay una solicitud relacionado";
		}	
	}

	public String getSystemName() {
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema relacionado";
		}	
	}

	public String getTypePetitionName() {
		if(getTypePetition()!=null) {
			return getTypePetition().getCode();
		}else {
			return "No hay un tipo relacionado";
		}	
	}

	public String getUserName() {
		if(getRequest()!=null) {
			return getRequest().getUser().getFullName();
		}else {
			return "No hay un usuario relacionado";
		}
	}

}