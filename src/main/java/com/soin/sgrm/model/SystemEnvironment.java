package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_ENTORNO")
public class SystemEnvironment implements Serializable {
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "EXTERNO")
	private Boolean external;
	
	@Column(name = "SISTEMA_ID")
	private int systemId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getExternal() {
		return external;
	}

	public void setExternal(Boolean external) {
		this.external = external;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
	
}
