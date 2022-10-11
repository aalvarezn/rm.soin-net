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
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RELEASE_ERROR")
public class ReleaseError implements Serializable {
	
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
	@JoinColumn(name = "PROYECTO_ID", nullable = true)
	private Project project;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RELEASE_ID", nullable = true)
	private Releases_WithoutObj release;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ERROR_ID", nullable = true)
	private Errors error;
	
	@Column(name = "OBSERVACIONES")
	private String observations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Releases_WithoutObj getRelease() {
		return release;
	}

	public void setRelease(Releases_WithoutObj release) {
		this.release = release;
	}

	public Errors getError() {
		return error;
	}

	public void setError(Errors error) {
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
