package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "AMBIENTES_ACCIONES")
public class ActionEnvironment implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

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

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
}
