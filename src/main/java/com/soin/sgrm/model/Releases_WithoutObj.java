package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.JoinFormula;



@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class Releases_WithoutObj implements Serializable, Cloneable {

	@Id
	@Column(name = "ID")
	private int id;

	// Informacion General
	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "DESCRIPCION")
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<ReleaseTracking> tracking = new HashSet<ReleaseTracking>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private User user;
	
	@Column(name = "TIENE_CAMBIOS_EN_BASE_DE_DATOS")
	private Boolean haveSQL;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Transient
	private Integer haveDependecy;
	
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

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Set<ReleaseTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<ReleaseTracking> tracking) {
		this.tracking = tracking;
	}

	public Boolean getHaveSQL() {
		return haveSQL;
	}

	public void setHaveSQL(Boolean haveSQL) {
		this.haveSQL = haveSQL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getHaveDependecy() {
		return haveDependecy;
	}

	public void setHaveDependecy(Integer haveDependecy) {
		this.haveDependecy = haveDependecy;
	}
	
	
}