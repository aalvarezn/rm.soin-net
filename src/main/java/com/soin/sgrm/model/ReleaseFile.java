package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_ARCHIVO")
public class ReleaseFile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SISTEMAS_ARCHIVO_SQ")
	@SequenceGenerator(name = "SISTEMAS_ARCHIVO_SQ", sequenceName = "SISTEMAS_ARCHIVO_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "PATH")
	private String path;

	@DateTimeFormat(pattern = "dd-MM-yyy")
	@Column(name = "FECHA_REVISION")
	private Timestamp revisionDate;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Timestamp getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(Timestamp revisionDate) {
		this.revisionDate = revisionDate;
	}
}
