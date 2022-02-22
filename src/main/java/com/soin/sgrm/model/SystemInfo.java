package com.soin.sgrm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_SISTEMA")
public class SystemInfo implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "NOMECLATURA_VIEJA")
	private int nomenclature;

	@Column(name = "PROYECTO_ID")
	private int proyectId;

	@Column(name = "IMPORTAR_OBJETOS_BASE_DATOS")
	private Boolean importObjects;

	@Column(name = "REQUIERE_COMANDOS_PERSONALEB6A")
	private Boolean customCommands;

	@Column(name = "ES_AIA")
	private Boolean isAIA;

	@Column(name = "ES_BO")
	private Boolean isBO;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIDER_TECNICO_ID", nullable = true)
	private User leader;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_EQUIPO_TR2580", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> userTeam = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@OrderBy("fullName ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_GESTORES", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> managers = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMA_CORREO", joinColumns = { @JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CORREO_ID") })
	private Set<EmailTemplate> emailTemplate = new HashSet<EmailTemplate>();

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

	public int getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(int nomenclature) {
		this.nomenclature = nomenclature;
	}

	public int getProyectId() {
		return proyectId;
	}

	public void setProyectId(int proyectId) {
		this.proyectId = proyectId;
	}

	public Boolean getImportObjects() {
		return importObjects;
	}

	public void setImportObjects(Boolean importObjects) {
		this.importObjects = importObjects;
	}

	public Boolean getCustomCommands() {
		return customCommands;
	}

	public void setCustomCommands(Boolean customCommands) {
		this.customCommands = customCommands;
	}

	public Boolean getIsAIA() {
		return isAIA;
	}

	public void setIsAIA(Boolean isAIA) {
		this.isAIA = isAIA;
	}

	public Boolean getIsBO() {
		return isBO;
	}

	public void setIsBO(Boolean isBO) {
		this.isBO = isBO;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public Set<User> getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(Set<User> userTeam) {
		this.userTeam = userTeam;
	}

	public Set<EmailTemplate> getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(Set<EmailTemplate> emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Set<User> getManagers() {
		return managers;
	}

	public void setManagers(Set<User> managers) {
		this.managers = managers;
	}
	
	public String getManager() {
		for (User user : this.managers) {
			return user.getFullName();
		}
		return null;
	}

}
