package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "GDOCS_CONFIGURACIONGDOCS")
public class GDoc implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GDOCS_CONFIGURACIONGDOCS_SQ")
	@SequenceGenerator(name = "GDOCS_CONFIGURACIONGDOCS_SQ", sequenceName = "GDOCS_CONFIGURACIONGDOCS_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	private String description;

	@Column(name = "GOOGLE_JSON_CREDENTIALS")
	@NotEmpty(message = Constant.EMPTY)
	private String credentials;

	@Column(name = "SPREADSHEET_ID")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String spreadSheet;

	@Column(name = "SIGUIENTE_SINCRONIZACION")
	private Date nextSincronization;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROYECTO_ID", nullable = true)
	private Project proyect;

	@Transient
	private Integer proyectId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getNextSincronization() {
		return nextSincronization;
	}

	public void setNextSincronization(Date nextSincronization) {
		this.nextSincronization = nextSincronization;
	}

	public Project getProyect() {
		return proyect;
	}

	public void setProyect(Project proyect) {
		this.proyect = proyect;
	}

	public Integer getProyectId() {
		return proyectId;
	}

	public void setProyectId(Integer proyectId) {
		this.proyectId = proyectId;
	}

}
