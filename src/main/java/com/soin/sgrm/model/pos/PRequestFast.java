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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

import com.soin.sgrm.utils.Constant;

@Entity
@Table(name = "REQUERIMIENTOS_REQUERIMIENTO")
public class PRequestFast implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requerimientos_requerimiento_seq")
	@SequenceGenerator(name = "requerimientos_requerimiento_seq", sequenceName = "requerimientos_requerimiento_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CODIGO_SOIN")
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String code_soin;

	@Column(name = "GESTOR_ID")
	private Integer userManager;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode_soin() {
		return code_soin;
	}

	public void setCode_soin(String code_soin) {
		this.code_soin = code_soin;
	}
	
	public Integer  getUserManager() {
		return userManager;
	}

	public void setUserManager(Integer userManager) {
		this.userManager = userManager;
	}

	
	

}
