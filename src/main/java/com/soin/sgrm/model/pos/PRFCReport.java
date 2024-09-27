package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "RFC")
public class PRFCReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfc_seq")
	@SequenceGenerator(name = "rfc_seq", sequenceName = "rfc_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO_PROYECTO")
	private String codeProyect;

	@Column(name = "NUM_SOLICITUD")
	private String rfcNumber;

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
	private PStatusRFC status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SIGES\"", nullable = false)
	private PSiges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
	private PSystemInfo system;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

	// Agregar Motivo
	// Agregar Usuario que lo cambio
	@Transient
	private String systemName;
	
	@Transient
	private String statusName;

	@Transient
	private String priorityName;
	
	@Transient
	private String impactName;
	
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

	
	public String getUserName() {
		
		if(getUser()!=null) {
			return getUser().getFullName();
		}else {
			return "No hay un usuario seleccionado";
		}
		
	}
	
	public String getStatusName() {
		 
		if(getStatus()!=null) {
			return getStatus().getName();
		}else {
			return "No hay un estado seleccionado";
		}
	}

	public String getImpactName() {
			if(getImpact()!=null) {
			return getImpact().getName();
		}else {
			return "No hay un impacto seleccionado";
		}
	}

	public String getPriorityName() {
	
		if(getPriority()!=null) {
			return getPriority().getName();
		}else {
			return "No hay una prioridad seleccionada ";
		}
	}

	public String getSystemName() {
		
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema seleccionado";
		}
	}
	
	public String getRfcNumber() {
		return rfcNumber;
	}

	public void setRfcNumber(String rfcNumber) {
		this.rfcNumber = rfcNumber;
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

	public PStatusRFC getStatus() {
		return status;
	}

	public void setStatus(PStatusRFC status) {
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


	public PSiges getSiges() {
		return siges;
	}

	public void setSiges(PSiges siges) {
		this.siges = siges;
	}


	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}



}
