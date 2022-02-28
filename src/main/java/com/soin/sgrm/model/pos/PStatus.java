package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESTADO")
public class PStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "EN_PROGRESO")
	private int in_progress;
	
	@Column(name = "FINALIZADO")
	private int finished;
	
	@Column(name = "MOTIVO")
	private String reasen;
	
	@Column(name = "CODE")
	private String code;
	
	public int getIn_progress() {
		return in_progress;
	}

	public void setIn_progress(int in_progress) {
		this.in_progress = in_progress;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public String getReasen() {
		return reasen;
	}

	public void setReasen(String reasen) {
		this.reasen = reasen;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
