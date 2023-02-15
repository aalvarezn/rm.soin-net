package com.soin.sgrm.model.wf;


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

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.IncidenceTracking;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.wf.NodeIncidence;

@Entity
@Table(name = "INCIDENCIA")
public class WFIncidence   {
	
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUM_TICKET")
	private String numTicket;

	@Column(name = "GENERADO_POR")
	private String createFor;
	
	@Column(name = "OPERADOR")
	private String operator;
	
	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE")
	private String message;
	
	@Column(name = "ASIGNADO")
	private String assigned;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHAACTU")
	private Timestamp updateDate;
	
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO", nullable = false)
	private System_StatusIn status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SISTEMA", nullable = false)
	private SystemInfo  system;
	

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "incidence")
	private Set<IncidenceTracking> tracking = new HashSet<IncidenceTracking>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NODO_ID", nullable = true)
	private NodeIncidence node;
	
	@Transient
	private Long typeIncidenceId;
	@Transient
	private Integer systemId;
	@Transient
	private Long priorityId;
	
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


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public System_StatusIn getStatus() {
		return status;
	}

	public void setStatus(System_StatusIn status) {
		this.status = status;
	}

	public String getCreateFor() {
		return createFor;
	}

	public void setCreateFor(String createFor) {
		this.createFor = createFor;
	}


	public Set<IncidenceTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<IncidenceTracking> tracking) {
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

	public NodeIncidence getNode() {
		return node;
	}

	public void setNode(NodeIncidence node) {
		this.node = node;
	}
	
	
	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public void convertReleaseToWFIncidence(Incidence incidence) {
		this.node = incidence.getNode();
		this.requestDate=incidence.getRequestDate();
		this.updateDate=incidence.getUpdateDate();
		this.numTicket = incidence.getNumTicket();
		this.system = incidence.getSystem();
		this.status = incidence.getStatus();
		this.user = incidence.getUser();
		this.operator=incidence.getOperator();
	}
}
