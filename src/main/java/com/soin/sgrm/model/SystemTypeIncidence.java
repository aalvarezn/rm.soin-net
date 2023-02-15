package com.soin.sgrm.model;

import java.io.Serializable;

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

@Entity
@Table(name = "SISTEMA_TIPOINCIDENCIA")
public class SystemTypeIncidence implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPOINCIDENCIA_ID", nullable = true)
	private TypeIncidence typeIncidence;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private System system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMAIL_ID")
	private EmailTemplate emailTemplate;
	
	@Transient
	Integer systemId;

	@Transient
	Long typeIncidenceId;
	
	@Transient
	Integer emailId;

	public TypeIncidence getTypeIncidence() {
		return typeIncidence;
	}

	public void setTypeIncidence(TypeIncidence typeIncidence) {
		this.typeIncidence = typeIncidence;
	}

	public Long getTypeIncidenceId() {
		return typeIncidenceId;
	}

	public void setTypeIncidenceId(Long typeIncidenceId) {
		this.typeIncidenceId = typeIncidenceId;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
	
	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}
	
	
	
	
}
