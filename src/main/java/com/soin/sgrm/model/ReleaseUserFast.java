package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class ReleaseUserFast implements Serializable {

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
	private Date createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	@Column(name = "REINTENTOS", nullable = true)
	private Integer retries;



	@Column(name = "OPERADOR")
	private String operator;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "NUMEROVERSION")
	private String versionNumber;
	@Transient
	private Timestamp timeNew;
	
	@Transient
	private String dateChange;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
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

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public String getDateChange() {
		return dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	public Timestamp getTimeNew() {
		return timeNew;
	}

	public void setTimeNew(Timestamp timeNew) {
		this.timeNew = timeNew;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	
}
