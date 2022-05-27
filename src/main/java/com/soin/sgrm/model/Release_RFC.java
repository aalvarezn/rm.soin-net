

package com.soin.sgrm.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class Release_RFC implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_RELEASE_SQ")
	@SequenceGenerator(name = "RELEASES_RELEASE_SQ", sequenceName = "RELEASES_RELEASE_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	// Informacion General
	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;	

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_OBJETOS", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "OBJETO_ID") })
	private Set<ReleaseObject> objects = new HashSet<ReleaseObject>();



	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	
	@Column(name = "MOTIVO")
	private String motive;

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

	

	public Set<ReleaseObject> getObjects() {
		return objects;
	}

	public void setObjects(Set<ReleaseObject> objects) {
		this.objects = objects;
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

	

	

	public ReleaseObject getObjectById(Integer id) {
		for (ReleaseObject object : this.objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}

	public boolean haveSql() {
		for (ReleaseObject object : this.objects) {
			if (object.getIsSql() == 1) {
				return true;
			}
		}
		return false;
	}

	public boolean haveExecPlan() {
		for (ReleaseObject object : this.objects) {
			if (object.getIsSql() == 1 && object.getExecutePlan() == 1) {
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

}
