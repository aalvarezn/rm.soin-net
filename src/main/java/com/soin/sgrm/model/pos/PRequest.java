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
@Table(name = "REQUERIMIENTO")
public class PRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "ESTADO")
	private String state;
	
	@Column(name = "CODIGO_SOIN")
	private String codeSoin;
	
	@Column(name = "CODIGO_ICE")
	private String codeIce;
	
	@Column(name= "DESCRIPCION")
	private String description;
	
	@Column(name = "LIDER_TECNICO_SOIN")
	private String leaderTecSoin;
	
	@Column(name = "LIDER_TECNICO_ICE")
	private String leaderTecIce;

	@Column(name = "GESTOR_SOIN")
	private String managerSoin;
	
	@Column(name = "GESTOR_ICE")
	private String managerIce;
	
	@Column(name="ESTADO")
	private String status;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TIPO_REQUERIMIENTO_ID\"")
	private PTypeRequest typeRequest;
	
	@Column(name = "ACTIVO")
	private boolean active;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PROYECTO_ID\"")
	private PProject project;
	
	@Transient
	private Long projectId;
	
	@Transient
	private Long typeRequestId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCodeSoin() {
		return codeSoin;
	}

	public void setCodeSoin(String codeSoin) {
		this.codeSoin = codeSoin;
	}

	public String getCodeIce() {
		return codeIce;
	}

	public void setCodeIce(String codeIce) {
		this.codeIce = codeIce;
	}

	public String getLeaderTecSoin() {
		return leaderTecSoin;
	}

	public void setLeaderTecSoin(String leaderTecSoin) {
		this.leaderTecSoin = leaderTecSoin;
	}

	public String getLeaderTecIce() {
		return leaderTecIce;
	}

	public void setLeaderTecIce(String leaderTecIce) {
		this.leaderTecIce = leaderTecIce;
	}

	public String getManagerSoin() {
		return managerSoin;
	}

	public void setManagerSoin(String managerSoin) {
		this.managerSoin = managerSoin;
	}

	public String getManagerIce() {
		return managerIce;
	}

	public void setManagerIce(String managerIce) {
		this.managerIce = managerIce;
	}

	public PTypeRequest getTypeRequest() {
		return typeRequest;
	}

	public void setTypeRequest(PTypeRequest typeRequest) {
		this.typeRequest = typeRequest;
	}

	

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PProject getProject() {
		return project;
	}

	public void setProject(PProject project) {
		this.project = project;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getTypeRequestId() {
		return typeRequestId;
	}

	public void setTypeRequestId(Long typeRequestId) {
		this.typeRequestId = typeRequestId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
