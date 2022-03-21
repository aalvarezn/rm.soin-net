package com.soin.sgrm.model.pos;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@Entity
@Table(name = "GDOC_CONFIGURACION")
public class PGDocConfiguration implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	private String name;

	@Column(name = "CREDENTIAL_JSON")
	@NotEmpty(message = Constant.EMPTY)
	private String credentials;

	@Column(name = "HOJAID")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String spreadSheet;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PROYECTO_ID\"")
	private PProject proyect;


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


	public String getCredentials() {
		return credentials;
	}


	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}


	public String getSpreadSheet() {
		return spreadSheet;
	}


	public void setSpreadSheet(String spreadSheet) {
		this.spreadSheet = spreadSheet;
	}


	public PProject getProyect() {
		return proyect;
	}


	public void setProyect(PProject proyect) {
		this.proyect = proyect;
	}
}
