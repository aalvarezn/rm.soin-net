package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ARCHIVOREPORTE")
public class PReportFile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "archivo_reporte_seq")
	@SequenceGenerator(name = "archivo_reporte_seq", sequenceName = "archivo_reporte_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "PATH")
	private String path;
	
	@Column(name = "\"ID_RELEASE\"")
	private Integer idRelease;
	
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_REVISION")
	private Timestamp revisionDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Integer getIdRelease() {
		return idRelease;
	}

	public void setIdRelease(Integer idRelease) {
		this.idRelease = idRelease;
	}	
}

