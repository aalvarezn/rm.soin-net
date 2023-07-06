package com.soin.sgrm.model;

import java.io.Serializable;

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

@Entity
@Table(name = "SISTEMA_ESTADOINCIDENCIA")
public class SystemStatusIncidence implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPOINCIDENCIA_ID", nullable = true)
	private StatusIncidence statusIncidence;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private System system;

	@Transient
	Integer systemId;

	@Transient
	Long statusIncidenceId;

	public StatusIncidence getStatusIncidence() {
		return statusIncidence;
	}

	public void setStatusIncidence(StatusIncidence statusIncidence) {
		this.statusIncidence = statusIncidence;
	}

	public Long getStatusIncidenceId() {
		return statusIncidenceId;
	}

	public void setStatusIncidenceId(Long statusIncidenceId) {
		this.statusIncidenceId = statusIncidenceId;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
	
	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
