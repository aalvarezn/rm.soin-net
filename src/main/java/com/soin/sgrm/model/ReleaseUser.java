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
@Table(name = "RELEASES_RELEASE")
public class ReleaseUser implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "OBSERVACIONES")
	private String observations;

	@Column(name = "FECHA_CREACION")
	private String createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

}
