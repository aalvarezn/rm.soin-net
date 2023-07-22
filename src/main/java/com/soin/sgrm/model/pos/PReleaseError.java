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
@Table(name = "RELEASE_ERROR")
public class PReleaseError implements Serializable {
	
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
	@JoinColumn(name = "\"PROYECTO_ID\"", nullable = true)
	private PProject project;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RELEASE_ID\"", nullable = true)
	private PReleases_WithoutObj release;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ERROR_ID\"", nullable = true)
	private PErrors_Release error;
	
	@Column(name = "OBSERVACIONES")
	private String observations;

	@Transient 
	private String errorName;
	
	@Transient 
	private String releaseName;
	
	@Transient 
	private String systemName;
	
	@Transient 
	private String projectName;
	
	@Transient 
	private String userName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PReleases_WithoutObj getRelease() {
		return release;
	}

	public void setRelease(PReleases_WithoutObj release) {
		this.release = release;
	}

	public PErrors_Release getError() {
		return error;
	}

	public void setError(PErrors_Release error) {
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

	public PProject getProject() {
		return project;
	}

	public void setProject(PProject project) {
		this.project = project;
	}

	public String getErrorName() {
		
		if(getError()!=null) {
			return getError().getName();
		}else {
			return "No hay un error relacionado";
		}	
	}

	public String getReleaseName() {
		if(getRelease()!=null) {
			return getRelease().getReleaseNumber();
		}else {
			return "No hay un release relacionado";
		}	
	}

	public String getSystemName() {
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema relacionado";
		}	
	}

	public String getProjectName() {
		if(getProject()!=null) {
			return getProject().getCode();
		}else {
			return "No hay un proyecto relacionado";
		}	
	}

	public String getUserName() {
		if(getRelease()!=null) {
			return getRelease().getUser().getFullName();
		}else {
			return "No hay un usuario relacionado";
		}
	}

}