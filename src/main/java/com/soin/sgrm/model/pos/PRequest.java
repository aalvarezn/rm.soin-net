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
	private String code;
	
	@Column(name = "CODIGO_ICE")
	private String wordGenerator;
	
	@Column(name = "LIDER_TECNICO_SOIN")
	private String leaderTecSoin;
	
	@Column(name = "LIDER_TECNICO_ICE")
	private String leaderTecIce;

	@Column(name = "GESTOR_SOIN")
	private String managerSoin;
	
	@Column(name = "GESTOR_ICE")
	private String managerIce;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TIPO_REQUERIMIENTO_ID\"")
	private PTypeRequest typeRequest;
	
	@Column(name = "ACTIVO")
	private String active;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PROYECTO_ID\"")
	private PProject project;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWordGenerator() {
		return wordGenerator;
	}

	public void setWordGenerator(String wordGenerator) {
		this.wordGenerator = wordGenerator;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public PProject getProject() {
		return project;
	}

	public void setProject(PProject project) {
		this.project = project;
	}

}
