package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SISTEMAS_SISTEMA")
public class PSystemInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "NOMECLATURA_VIEJA")
	private Boolean nomenclature;

	@Column(name = "PROYECTO_ID")
	private Integer proyectId;

	@Column(name = "IMPORTAR_OBJETOS_BASE_DATOS")
	private Boolean importObjects;

	@Column(name = "REQUIERE_COMANDOS_PERSONALEB6A")
	private Boolean customCommands;

	@Column(name = "ES_AIA")
	private Boolean isAIA;

	@Column(name = "ES_BO")
	private Boolean isBO;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"LIDER_TECNICO_ID\"", nullable = true)
	private PUser leader;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_EQUIPO_TR2580", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PUser> userTeam = new HashSet<PUser>();

	@ManyToMany(fetch = FetchType.EAGER)
	@OrderBy("fullName ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_GESTORES", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PUser> managers = new HashSet<PUser>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMA_CORREO", joinColumns = { @JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"CORREO_ID\"") })
	private Set<PEmailTemplate> emailTemplate = new HashSet<PEmailTemplate>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Boolean getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(Boolean nomenclature) {
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

	public PUser getLeader() {
		return leader;
	}

	public void setLeader(PUser leader) {
		this.leader = leader;
	}

	public Set<PUser> getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(Set<PUser> userTeam) {
		this.userTeam = userTeam;
	}

	public Set<PEmailTemplate> getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(Set<PEmailTemplate> emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Set<PUser> getManagers() {
		return managers;
	}

	public void setManagers(Set<PUser> managers) {
		this.managers = managers;
	}
	
	public String getManager() {
		for (PUser user : this.managers) {
			return user.getFullName();
		}
		return null;
	}

}
