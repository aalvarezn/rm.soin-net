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
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.soin.sgrm.model.pos.wf.PNodeIncidence;
import com.soin.sgrm.model.wf.NodeIncidence;

@Entity
@Table(name = "INCIDENCIA")
public class PIncidence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incidencia_seq")
	@SequenceGenerator(name = "incidencia_seq", sequenceName = "incidencia_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUM_TICKET")
	private String numTicket;

	@Column(name = "TITULO")
	private String title;

	@Column(name = "DETALLE")
	private String detail;

	@Column(name = "RESULTADO")
	private String result;

	@Column(name = "NOTA_ADICIONAL")
	private String note;

	@Column(name = "GENERADO_POR")
	private String createFor;

	@Column(name = "ASIGNADO")
	private String assigned;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "MOTIVO")
	private String motive;


	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE")
	private String message;


	@Column(name = "ERROR")
	private String errorNew;
	
	@Column(name = "SOLUCION")
	private String solution;
	
	@Column(name = "CAUSA")
	private String cause;

	
	@Column(name = "SLA_ACTIVO")
	private Integer slaActive;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHAACTU")
	private Timestamp updateDate;


	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "SALIDA_OPTIMA")
	private Timestamp exitOptimalDate;

	@Column(name = "TIEMPO_MILI")
	private Integer timeMili;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = true)
	private PUser user;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_TIPO\"", nullable = false)
	private PSystemTypeIncidence typeIncidence;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PSystem_StatusIn status;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_PRIORIDAD\"", nullable = false)
	private PSystem_Priority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
	private PSystemInfo system;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "INCIDENCIA_ARCHIVOINCIDENCIA", joinColumns = {
			@JoinColumn(name = "\"ID_INCIDENCIA\"") }, inverseJoinColumns = { @JoinColumn(name = "\"ARCHIVO_ID\"") })

	private Set<PIncidenceFile> files = new HashSet<>();

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "incidence")
	private Set<PIncidenceTracking> tracking = new HashSet<PIncidenceTracking>();

	@OrderBy("logDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "incidence")
	private Set<PIncidenceLog> log = new HashSet<PIncidenceLog>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_NODO\"", nullable = true)
	private PNodeIncidence node;


	@Transient
	private Long typeIncidenceId;
	@Transient
	private Integer systemId;
	@Transient
	private Long priorityId;

	@Transient
	private Integer userNewId;
	@Transient
	private Long ticketId;

	@Transient
	private String email;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumTicket() {
		return numTicket;
	}

	public void setNumTicket(String numTicket) {
		this.numTicket = numTicket;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getExitOptimalDate() {
		return exitOptimalDate;
	}

	public void setExitOptimalDate(Timestamp exitOptimalDate) {
		this.exitOptimalDate = exitOptimalDate;
	}

	public Integer getTimeMili() {
		return timeMili;
	}

	public void setTimeMili(Integer timeMili) {
		this.timeMili = timeMili;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}


	public PSystemTypeIncidence getTypeIncidence() {
		return typeIncidence;
	}

	public void setTypeIncidence(PSystemTypeIncidence typeIncidence) {
		this.typeIncidence = typeIncidence;
	}

	public PSystem_Priority getPriority() {
		return priority;
	}

	public void setPriority(PSystem_Priority priority) {
		this.priority = priority;
	}

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public PSystem_StatusIn getStatus() {
		return status;
	}

	public void setStatus(PSystem_StatusIn status) {
		this.status = status;
	}

	public String getCreateFor() {
		return createFor;
	}

	public void setCreateFor(String createFor) {
		this.createFor = createFor;
	}

	public Set<PIncidenceFile> getFiles() {
		return files;
	}

	public void setFiles(Set<PIncidenceFile> files) {
		this.files = files;
	}

	public Set<PIncidenceTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PIncidenceTracking> tracking) {
		this.tracking = tracking;
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

	public Long getTypeIncidenceId() {
		return typeIncidenceId;
	}

	public void setTypeIncidenceId(Long typeIncidenceId) {
		this.typeIncidenceId = typeIncidenceId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PNodeIncidence getNode() {
		return node;
	}

	public void setNode(PNodeIncidence node) {
		this.node = node;
	}

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public Set<PIncidenceLog> getLog() {
		return log;
	}

	public void setLog(Set<PIncidenceLog> log) {
		this.log = log;
	}

	public Integer getUserNewId() {
		return userNewId;
	}

	public void setUserNewId(Integer userNewId) {
		this.userNewId = userNewId;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getSlaActive() {
		return slaActive;
	}

	public void setSlaActive(Integer slaActive) {
		this.slaActive = slaActive;
	}


	public String getErrorNew() {
		return errorNew;
	}

	public void setErrorNew(String errorNew) {
		this.errorNew = errorNew;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	
}