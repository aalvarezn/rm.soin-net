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
@Table(name = "SOLICITUD_RM_P1_R5")
public class PRequestRM_P1_R5 implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_rm_p1_r5_seq")
	@SequenceGenerator(name = "solicitud_rm_p1_r5_seq", sequenceName = "solicitud_rm_p1_r5_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "AMBIENTES")
	private String ambient;

	@Column(name = "TIPO_CAMBIO")
	private String typeChange;

	@Column(name = "CAMBIOS_SERVICIO")
	private String changeService;

	@Column(name = "JUSTIFICACION_CAMBIO")
	private String justify;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUD\"", nullable = false)
	private PRequestBase requestBase;
	
	@Transient
	private Long requestBaseId;
	
	@Transient
	private String message;
	
	@Transient
	private String senders;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAmbient() {
		return ambient;
	}

	public void setAmbient(String ambient) {
		this.ambient = ambient;
	}

	public String getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(String typeChange) {
		this.typeChange = typeChange;
	}

	public String getChangeService() {
		return changeService;
	}

	public void setChangeService(String changeService) {
		this.changeService = changeService;
	}

	public String getJustify() {
		return justify;
	}

	public void setJustify(String justify) {
		this.justify = justify;
	}

	public PRequestBase getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(PRequestBase requestBase) {
		this.requestBase = requestBase;
	}

	public Long getRequestBaseId() {
		return requestBaseId;
	}

	public void setRequestBaseId(Long requestBaseId) {
		this.requestBaseId = requestBaseId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenders() {
		return senders;
	}

	public void setSenders(String senders) {
		this.senders = senders;
	}
	
	
	
}
