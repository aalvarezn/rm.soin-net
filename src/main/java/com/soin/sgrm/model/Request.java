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
@Table(name = "REQUERIMIENTOS_REQUERIMIENTO")
public class Request implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO_SOIN")
	private String code_soin;

	@Column(name = "CODIGO_ICE")
	private String code_ice;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "GESTOR_SOIN")
	private String soinManagement;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode_soin() {
		return code_soin;
	}

	public void setCode_soin(String code_soin) {
		this.code_soin = code_soin;
	}

	public String getCode_ice() {
		return code_ice;
	}

	public void setCode_ice(String code_ice) {
		this.code_ice = code_ice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSoinManagement() {
		return soinManagement;
	}

	public void setSoinManagement(String soinManagement) {
		this.soinManagement = soinManagement;
	}

}
