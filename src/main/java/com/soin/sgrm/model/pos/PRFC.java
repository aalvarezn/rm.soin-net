package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RFC")
public class PRFC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO_PROYECTO")
	private String codeProyect;

	@Column(name = "NUM_SOLICITUD")
	private String numRequest;

	@Column(name = "REQUIERE_BD")
	private Boolean requiredBD;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_PRIORIDAD\"", nullable = true)
	private PPriority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatus status;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeProyect() {
		return codeProyect;
	}

	public void setCodeProyect(String codeProyect) {
		this.codeProyect = codeProyect;
	}

	public String getNumRequest() {
		return numRequest;
	}

	public void setNumRequest(String numRequest) {
		this.numRequest = numRequest;
	}

	public Boolean getRequiredBD() {
		return requiredBD;
	}

	public void setRequiredBD(Boolean requiredBD) {
		this.requiredBD = requiredBD;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public PPriority getPriority() {
		return priority;
	}

	public void setPriority(PPriority priority) {
		this.priority = priority;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

}
