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
@Table(name = "SOLICITUD_RM_P1_R4")
public class PRequestRM_P1_R4 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_rm_p1_r4_seq")
	@SequenceGenerator(name = "solicitud_rm_p1_r4_seq", sequenceName = "solicitud_rm_p1_r4_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "CORREO")
	private String email;
	
	@Column(name = "USUARIO_GIT")
	private String userGit;

	@Column(name = "PERMISOS")
	private String permissions;
	
	@Column(name = "ESPECIFICACION")
	private String espec;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_AMBIENTE\"", nullable = false)
	private PAmbient ambient;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_TIPO_R4\"", nullable = false)
	private PTypePetitionR4 type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUD\"", nullable = false)
	private PRequestBase requestBase;

	
	@Transient
	private Integer ambientId;
	
	@Transient
	private Long typeId;

	@Transient
	private Long requestBaseId;
	
	@Transient
	private String typeName;
	
	@Transient
	private String ambientName;
	
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

	public PAmbient getAmbient() {
		return ambient;
	}

	public void setAmbient(PAmbient ambient) {
		this.ambient = ambient;
	}

	public PRequestBase getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(PRequestBase requestBase) {
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

	public PTypePetitionR4 getType() {
		return type;
	}

	public void setType(PTypePetitionR4 type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getUserGit() {
		return userGit;
	}

	public void setUserGit(String userGit) {
		this.userGit = userGit;
	}

	public String getTypeName() {
		if(getType()==null) {
			return "Sin tipo seleccionado";
		}else {
			return getType().getCode();
		}
		
		
	}

	public String getAmbientName() {
		if(getAmbient()==null) {
			return "Sin ambiente seleccionado";
		}else {
			return getAmbient().getName();
		}
	}
	
	
}
