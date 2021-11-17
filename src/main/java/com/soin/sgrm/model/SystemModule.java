package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_MODULO")
public class SystemModule  implements Serializable{
	
	@Id
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "SISTEMA_ID")
	private int system_id;

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

	public int getSystem_id() {
		return system_id;
	}

	public void setSystem_id(int system_id) {
		this.system_id = system_id;
	}
}
