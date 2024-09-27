package com.soin.sgrm.model.pos;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RFCS_ESTADO")
public class PStatusRFC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfc_estado_seq")
	@SequenceGenerator(name = "rfc_estado_seq", sequenceName = "rfc_estado_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "MOTIVO")
	private String reason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
