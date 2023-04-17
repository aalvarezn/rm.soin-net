package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RFC")
public class RFCReport implements Serializable {

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

	@Column(name = "RAZON_CAMBIO")
	private String reasonChange;

	@Column(name = "EFECTO")
	private String effect;

	@Column(name = "DETALLE_IMPLEMEN")
	private String detail;

	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "SCHEMADB")
	private String schemaDB;

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
	private SystemInfo system;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

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


	public String getSchemaDB() {
		return schemaDB;
	}

	public void setSchemaDB(String schemaDB) {
		this.schemaDB = schemaDB;
	}

	public Siges getSiges() {
		return siges;
	}

	public void setSiges(Siges siges) {
		this.siges = siges;
	}


	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

}
