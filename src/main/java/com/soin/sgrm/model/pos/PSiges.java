package com.soin.sgrm.model.pos;


import java.io.Serializable;

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

@Entity
@Table(name = "SISTEMA_SIGES")
public class PSiges implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistema_siges_seq")
	@SequenceGenerator(name = "sistema_siges_seq", sequenceName = "sistema_siges_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGOSIGES")
	private String codeSiges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"")
	private PSystemInfo system;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"EMAIL_ID\"")
	private PEmailTemplate emailTemplate;
	
	@Transient
	private int systemId;
	
	@Transient
	private int emailTemplateId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeSiges() {
		return codeSiges;
	}

	public void setCodeSiges(String codeSiges) {
		this.codeSiges = codeSiges;
	}

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public PEmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(PEmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public int getEmailTemplateId() {
		return emailTemplateId;
	}

	public void setEmailTemplateId(int emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}
	
	
	
}
