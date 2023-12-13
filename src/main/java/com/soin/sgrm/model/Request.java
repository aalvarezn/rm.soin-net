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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "REQUERIMIENTOS_REQUERIMIENTO")
public class Request implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUERIMIENTOS_REQUERIM278D_SQ")
	@SequenceGenerator(name = "REQUERIMIENTOS_REQUERIM278D_SQ", sequenceName = "REQUERIMIENTOS_REQUERIM278D_SQ", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CODIGO_SOIN")
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String code_soin;

	@Column(name = "CODIGO_ICE")
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String code_ice;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "GESTOR_SOIN")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String soinManagement;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROYECTO_ID", nullable = true)
	private Project proyect;

	@Column(name = "ESTADO")
	private String status;

	@Column(name = "LIDER_TECNICO_SOIN")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String leaderSoin;

	@Column(name = "LIDER_TECNICO_ICE")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String liderIce;

	@Column(name = "GESTOR_ICE")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String iceManagement;

	@Column(name = "GESTOR_ID")
	private Integer userManager;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPO_REQUERIMIENTO_ID", nullable = true)
	private TypeRequest typeRequest;

	@Value("${active:true}")
	@Column(name = "ACTIVO")
	private boolean active;
	
	@Column(name = "AUTO")
	private Integer auto;

	@Transient
	Integer typeRequestId;

	@Transient
	Integer proyectId;

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

	public String getCode_ice() {
		return code_ice;
	}

	public void setCode_ice(String code_ice) {
		this.code_ice = code_ice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSoinManagement() {
		return soinManagement;
	}

	public void setSoinManagement(String soinManagement) {
		this.soinManagement = soinManagement;
	}

	public Project getProyect() {
		return proyect;
	}

	public void setProyect(Project proyect) {
		this.proyect = proyect;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLeaderSoin() {
		return leaderSoin;
	}

	public void setLeaderSoin(String leaderSoin) {
		this.leaderSoin = leaderSoin;
	}

	public String getLiderIce() {
		return liderIce;
	}

	public void setLiderIce(String liderIce) {
		this.liderIce = liderIce;
	}

	public String getIceManagement() {
		return iceManagement;
	}

	public void setIceManagement(String iceManagement) {
		this.iceManagement = iceManagement;
	}

	public TypeRequest getTypeRequest() {
		return typeRequest;
	}

	public void setTypeRequest(TypeRequest typeRequest) {
		this.typeRequest = typeRequest;
	}

	public Integer getTypeRequestId() {
		return typeRequestId;
	}

	public void setTypeRequestId(Integer typeRequestId) {
		this.typeRequestId = typeRequestId;
	}

	public Integer getProyectId() {
		return proyectId;
	}

	public void setProyectId(Integer proyectId) {
		this.proyectId = proyectId;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getAuto() {
		return auto;
	}

	public void setAuto(Integer auto) {
		this.auto = auto;
	}

	public Integer  getUserManager() {
		return userManager;
	}

	public void setUserManager(Integer userManager) {
		this.userManager = userManager;
	}

	
	

}
