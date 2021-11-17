package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_TIPOOBJECTO")
public class TypeObject implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SISTEMAS_TIPOOBJECTO_SQ")
	@SequenceGenerator(name = "SISTEMAS_TIPOOBJECTO_SQ", sequenceName = "SISTEMAS_TIPOOBJECTO_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "EXTENSION")
	private String extension;

	@Column(name = "SISTEMA_ID")
	private int system_id;

	@Column(name = "DESCRIPCION")
	private String description;

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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public int getSystem_id() {
		return system_id;
	}

	public void setSystem_id(int system_id) {
		this.system_id = system_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
