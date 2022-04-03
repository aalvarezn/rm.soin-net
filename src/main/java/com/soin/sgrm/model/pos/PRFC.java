package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RFC")
public class PRFC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
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
	@JoinColumn(name = "\"ID_IMPACTO\"", nullable = true)
	private PImpact impact;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_CAMBIO\"", nullable = true)
	private PTypeChange typeChange;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatus status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"RFC_RELEASE\"", joinColumns = { @JoinColumn(name = "\"ID_RFC\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ID_RELEASE\"") })
	private Set<PRelease> releases = new HashSet<>();
	
	@Transient
	private String[] strReleases;
	
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

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public Set<PRelease> getReleases() {
		return releases;
	}

	public void setReleases(Set<PRelease> releases) {
		this.releases = releases;
	}

	public String[] getStrReleases() {
		return strReleases;
	}

	public void setStrReleases(String[] strSystems) {
		this.strReleases = strSystems;
	}

	public PImpact getImpact() {
		return impact;
	}

	public void setImpact(PImpact impact) {
		this.impact = impact;
	}

	public PTypeChange getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(PTypeChange typeChange) {
		this.typeChange = typeChange;
	}
	
	

}
