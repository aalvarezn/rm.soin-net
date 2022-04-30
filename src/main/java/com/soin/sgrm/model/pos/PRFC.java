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
	
	@Column(name = "RAZON_CAMBIO")
	private String reasonChange;

	@Column(name = "EFECTO")
	private String effect;

	@Column(name = "DETALLE_IMPLEMEN")
	private String detail;

	@Column(name = "EVIDENCIA")
	private String evidence;
	

	@Column(name = "REQUISITOS_ESP")
	private String requestEsp;
	
	@Column(name = "PLAN_RETORNO")
	private String returnPlan;
	
	
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
	
	
	@Column(name = "FECHA_EJECUCION_INICIO")
	private String requestDateBegin;
	

	@Column(name = "FECHA_EJECUCION_FINAL")
	private String requestDateFinish;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"RFC_RELEASE\"", joinColumns = { @JoinColumn(name = "\"ID_RFC\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ID_RELEASE\"") })
	private Set<PRelease> releases = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"RFC_ARCHIVORFC\"", joinColumns = { @JoinColumn(name = "\"RFC_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ARCHIVORFC_ID\"") })
	private Set<PRFCFile> files = new HashSet<>();
	
	//Agregar Motivo
	//Agregar Usuario que lo cambio
	@Transient
	private String[] strReleases;
	
	@Transient
	private Long impactId;
	
	@Transient
	private Long priorityId;
	
	@Transient
	private Long typeChangeId;
	
	@Transient
	private String releasesList;
	
	
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

	public String getReasonChange() {
		return reasonChange;
	}

	public void setReasonChange(String reasonChange) {
		this.reasonChange = reasonChange;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getEvidence() {
		return evidence;
	}

	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}

	public String getRequestEsp() {
		return requestEsp;
	}

	public void setRequestEsp(String requestEsp) {
		this.requestEsp = requestEsp;
	}

	public String getReturnPlan() {
		return returnPlan;
	}

	public void setReturnPlan(String returnPlan) {
		this.returnPlan = returnPlan;
	}

	public PPriority getPriority() {
		return priority;
	}

	public void setPriority(PPriority priority) {
		this.priority = priority;
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

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
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

	public void setStrReleases(String[] strReleases) {
		this.strReleases = strReleases;
	}

	public Long getImpactId() {
		return impactId;
	}

	public void setImpactId(Long impactId) {
		this.impactId = impactId;
	}

	public Long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}

	public Long getTypeChangeId() {
		return typeChangeId;
	}

	public void setTypeChangeId(Long typeChangeId) {
		this.typeChangeId = typeChangeId;
	}

	public String getReleasesList() {
		return releasesList;
	}

	public void setReleasesList(String releasesList) {
		this.releasesList = releasesList;
	}

	public String getRequestDateBegin() {
		return requestDateBegin;
	}

	public void setRequestDateBegin(String requestDateBegin) {
		this.requestDateBegin = requestDateBegin;
	}

	public String getRequestDateFinish() {
		return requestDateFinish;
	}

	public void setRequestDateFinish(String requestDateFinish) {
		this.requestDateFinish = requestDateFinish;
	}

	public Set<PRFCFile> getFiles() {
		return files;
	}

	public void setFiles(Set<PRFCFile> files) {
		this.files = files;
	}
	
	
	

}
