
package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JoinFormula;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class PRelease_RFCFast implements Serializable, Cloneable {

	@Id
	@Column(name = "ID")
	private int id;

	// Informacion General
	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "DESCRIPCION")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_OBJETOS", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"OBJETO_ID\"") })
	private Set<PReleaseObjectClean> objects = new HashSet<PReleaseObjectClean>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SOLICITADO_POR_ID\"", nullable = true)
	private PUser user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"", nullable = true)
	private PStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ANTERIOR\"", nullable = true)
	private PStatus statusBefore;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@Column(name = "TIENE_CAMBIOS_EN_BASE_DE_DATOS")
	private Boolean haveSQL;

	@Column(name = "MOTIVO")
	private String motive;

	// @JoinFormula(value = "SELECT COUNT(rr.ID) FROM RELEASES_RELEASE rr WHERE
	// rr.ID IN (SELECT rrd.TO_RELEASE_ID FROM RELEASES_RELEASE_DEPENDENCIAS rrd
	// WHERE FROM_RELEASE_ID =id) AND rr.ESTADO_ID IN(SELECT re.ID FROM
	// RELEASES_ESTADO re WHERE re.NOMBRE IN('Borrador', 'Solicitado'))")
	@Transient
	private Integer haveDependecy;

	@Transient
	private Integer numObjects;

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

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public Set<PReleaseObjectClean> getObjects() {
		return objects;
	}

	public void setObjects(Set<PReleaseObjectClean> objects) {
		this.objects = objects;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public PReleaseObjectClean getObjectById(Integer id) {
		for (PReleaseObjectClean object : this.objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}

	public boolean haveSql() {
		for (PReleaseObjectClean object : this.objects) {
			if (object.getIsSql() == 1) {
				return true;
			}
		}
		return false;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
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

	public Integer getHaveDependecy() {
		return haveDependecy;
	}

	public void setHaveDependecy(Integer haveDependecy) {
		this.haveDependecy = haveDependecy;
	}

	public PStatus getStatusBefore() {
		return statusBefore;
	}

	public void setStatusBefore(PStatus statusBefore) {
		this.statusBefore = statusBefore;
	}

	public Integer getNumObjects() {
		if (this.objects != null) {
			setNumObjects(objects.size());
		}
		return numObjects;
	}

	public void setNumObjects(Integer numObjects) {

		this.numObjects = numObjects;
	}

}
