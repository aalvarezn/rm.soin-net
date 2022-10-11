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
@Table(name = "SOLICITUD_RM_P1_R4")
public class RequestRM_P1_R4 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "CORREO")
	private String email;

	@Column(name = "PERMISOS")
	private String permissions;
	
	@Column(name = "ESPECIFICACION")
	private String espec;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_AMBIENTE", nullable = false)
	private Ambient ambient;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TIPO_R4", nullable = false)
	private TypePetitionR4 type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SOLICITUD", nullable = false)
	private RequestBase requestBase;

	
	@Transient
	private Integer ambientId;
	
	@Transient
	private Long typeId;

	@Transient
	private Long requestBaseId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Ambient getAmbient() {
		return ambient;
	}

	public void setAmbient(Ambient ambient) {
		this.ambient = ambient;
	}

	public RequestBase getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(RequestBase requestBase) {
		this.requestBase = requestBase;
	}

	public String getEspec() {
		return espec;
	}

	public void setEspec(String espec) {
		this.espec = espec;
	}

	public Integer getAmbientId() {
		return ambientId;
	}

	public void setAmbientId(Integer ambientId) {
		this.ambientId = ambientId;
	}

	public Long getRequestBaseId() {
		return requestBaseId;
	}

	public void setRequestBaseId(Long requestBaseId) {
		this.requestBaseId = requestBaseId;
	}

	public TypePetitionR4 getType() {
		return type;
	}

	public void setType(TypePetitionR4 type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	
}
