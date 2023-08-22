package com.soin.sgrm.model.pos;

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
public class PSystemTypeIncidence implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TIPOINCIDENCIA_ID\"", nullable = true)
	private PTypeIncidence typeIncidence;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystem system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"EMAIL_ID\"")
	private PEmailTemplate emailTemplate;
	
	@Transient
	Integer systemId;

	@Transient
	Long typeIncidenceId;
	
	@Transient
	Integer emailId;

	public PTypeIncidence getTypeIncidence() {
		return typeIncidence;
	}

	public void setTypeIncidence(PTypeIncidence typeIncidence) {
		this.typeIncidence = typeIncidence;
	}

	public Long getTypeIncidenceId() {
		return typeIncidenceId;
	}

	public void setTypeIncidenceId(Long typeIncidenceId) {
		this.typeIncidenceId = typeIncidenceId;
	}

	public PSystem getSystem() {
		return system;
	}

	public void setSystem(PSystem system) {
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

	public PEmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(PEmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}
	
	
	
	
}
