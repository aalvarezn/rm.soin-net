package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "AMBIENTES_AMBIENTE")
public class Ambient implements Serializable {
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "CODIGO")
	private String code;
	
	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "DETALLES")
	private String details;
	
	@Column(name = "NOMBRE_SERVIDOR")
	private String server_name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;
	
	@Column(name = "TIPO_AMBIENTE_ID")
	private int environmentType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public int getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(int environmentType) {
		this.environmentType = environmentType;
	}
	
	
	

}
