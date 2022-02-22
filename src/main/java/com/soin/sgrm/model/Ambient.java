package com.soin.sgrm.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "AMBIENTES_AMBIENTE")
public class Ambient implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMBIENTES_AMBIENTE_SQ")
	@SequenceGenerator(name = "AMBIENTES_AMBIENTE_SQ", sequenceName = "AMBIENTES_AMBIENTE_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String code;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String name;

	@Column(name = "DETALLES")
	@NotEmpty(message = Constant.EMPTY)
	private String details;

	@Column(name = "NOMBRE_SERVIDOR")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String serverName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemUser system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPO_AMBIENTE_ID", nullable = true)
	private TypeAmbient typeAmbient;

	@Transient
	private Integer typeAmbientId;

	@Transient
	private Integer systemId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public SystemUser getSystem() {
		return system;
	}

	public void setSystem(SystemUser system) {
		this.system = system;
	}

	public TypeAmbient getTypeAmbient() {
		return typeAmbient;
	}

	public void setTypeAmbient(TypeAmbient typeAmbient) {
		this.typeAmbient = typeAmbient;
	}

	public Integer getTypeAmbientId() {
		return typeAmbientId;
	}

	public void setTypeAmbientId(Integer typeAmbientId) {
		this.typeAmbientId = typeAmbientId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

}
