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
@Table(name = "SOLICITUD_RM_P1_R2")
public class PRequestRM_P1_R2 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "AMBIENTES")
	private String ambient;

	@Column(name = "TIPO_SERVICIO")
	private String typeService;

	@Column(name = "JERARQUIA")
	private String hierarchy;

	@Column(name = "REQUERIMIENTOS")
	private String requeriments;
	
	
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

	public String getTypeService() {
		return typeService;
	}

	public void setTypeService(String typeService) {
		this.typeService = typeService;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getRequeriments() {
		return requeriments;
	}

	public void setRequeriments(String requeriments) {
		this.requeriments = requeriments;
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
