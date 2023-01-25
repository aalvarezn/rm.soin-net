package com.soin.sgrm.model.wf;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;


import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCTracking;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.User;

@Entity
@Table(name = "RFC")
public class WFRFC implements Serializable {

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

	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "SCHEMADB")
	private String schemaDB;

	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE_GESTOR")
	private String message;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRIORIDAD", nullable = true)
	private Priority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_IMPACTO", nullable = true)
	private Impact impact;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CAMBIO", nullable = true)
	private TypeChange typeChange;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO", nullable = false)
	private StatusRFC status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SIGES", nullable = false)
	private Siges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SISTEMA", nullable = false)
	private SystemInfo systemInfo;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

	@Column(name = "FECHA_EJECUCION_INICIO")
	private String requestDateBegin;

	@Column(name = "FECHA_EJECUCION_FINAL")
	private String requestDateFinish;


	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<RFCTracking> tracking = new HashSet<RFCTracking>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NODO_ID", nullable = true)
	private NodeRFC node;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "RFC_RELEASE", joinColumns = { @JoinColumn(name = "ID_RFC") }, inverseJoinColumns = {
			@JoinColumn(name = "ID_RELEASE") })
	private Set<Release_RFC> releases = new HashSet<>();
	
	// Agregar Motivo
	// Agregar Usuario que lo cambio
	@Transient
	private String[] strReleases;

	@Transient
	private int impactId;

	@Transient
	private int priorityId;

	@Transient
	private Integer systemId;
	
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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Impact getImpact() {
		return impact;
	}

	public void setImpact(Impact impact) {
		this.impact = impact;
	}

	public TypeChange getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(TypeChange typeChange) {
		this.typeChange = typeChange;
	}

	public StatusRFC getStatus() {
		return status;
	}

	public void setStatus(StatusRFC status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	

	public String[] getStrReleases() {
		return strReleases;
	}

	public void setStrReleases(String[] strReleases) {
		this.strReleases = strReleases;
	}

	public int getImpactId() {
		return impactId;
	}

	public void setImpactId(int impactId) {
		this.impactId = impactId;
	}

	public int getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(int priorityId) {
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

	public NodeRFC getNode() {
		return node;
	}

	public void setNode(NodeRFC node) {
		this.node = node;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Set<RFCTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<RFCTracking> tracking) {
		this.tracking = tracking;
	}

	public String getSchemaDB() {
		return schemaDB;
	}

	public void setSchemaDB(String schemaDB) {
		this.schemaDB = schemaDB;
	}

	public String getSenders() {
		return senders;
	}

	public void setSenders(String senders) {
		this.senders = senders;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Siges getSiges() {
		return siges;
	}

	public void setSiges(Siges siges) {
		this.siges = siges;
	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public void convertRFCToWFRFC(RFC rfc) {
		this.node = rfc.getNode();
		this.requestDate=rfc.getRequestDate();
		this.numRequest = rfc.getNumRequest();
		this.systemInfo = rfc.getSystemInfo();
		this.status = rfc.getStatus();
		this.user = rfc.getUser();
	}

	public Set<Release_RFC> getReleases() {
		return releases;
	}

	public void setReleases(Set<Release_RFC> releases) {
		this.releases = releases;
	}
	
	
	
	
}
