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
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;


@Entity
@Table(name = "REQUERIMIENTOS_REQUERIMIENTO")
public class PRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
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
	@JoinColumn(name = "\"PROYECTO_ID\"", nullable = true)
	private PProject proyect;

	@Column(name = "ESTADO")
	private String status;
	
	@Column(name = "NOMBRE_NODO")
	private String nodeName;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "LIDER_TECNICO_SOIN")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String leaderSoin;

	@Column(name = "LIDER_TECNICO_ICE")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String liderIce;

	@Column(name = "GESTOR_ICE")
	@Size(max = 200, message = "Máximo 200 caracteres.")
	private String iceManagement;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TIPO_REQUERIMIENTO_ID\"", nullable = true)
	private PTypeRequest typeRequest;

	@Value("${active:true}")
	@Column(name = "ACTIVO")
	private boolean active;
	
	@Column(name = "AUTO")
	private Integer auto;

	@Column(name = "GESTOR_ID")
	private Integer userManager;

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

	public PProject getProyect() {
		return proyect;
	}

	public void setProyect(PProject proyect) {
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

	public PTypeRequest getTypeRequest() {
		return typeRequest;
	}

	public void setTypeRequest(PTypeRequest typeRequest) {
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

	public Integer getUserManager() {
		return userManager;
	}

	public void setUserManager(Integer userManager) {
		this.userManager = userManager;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}
	
	
}
