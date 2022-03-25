package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SISTEMA")
public class PSystem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"LIDER_TECNICO_ID\"", nullable = false)
	private PUser leader;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PROYECTO_ID\"", nullable = false)
	private PProject project;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"SISTEMA_GESTOR\"", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"USUARIO_ID\"") })
	private Set<PUser> managers = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"SISTEMA_EQUIPO_TRABAJO\"", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"USUARIO_ID\"") })
	private Set<PUser> team = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "system")
	private Set<PSystemProject> projectCodes = new HashSet<>();

	// PLANTILLA_ID

	@Column(name = "IMPORTAR_OBJETO_BD")
	private Boolean importObjects;

	@Column(name = "COMANDO_PERSONALIZADO")
	private Boolean customCommands;

	@Transient
	private String[] strManagers;

	@Transient
	private String[] strTeam;

	@Transient
	private String leaderUserName;

	@Transient
	private String projectCode;

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

	public PUser getLeader() {
		return leader;
	}

	public void setLeader(PUser leader) {
		this.leader = leader;
	}

	public PProject getProject() {
		return project;
	}

	public void setProject(PProject project) {
		this.project = project;
	}

	public Set<PUser> getManagers() {
		return managers;
	}

	public void setManagers(Set<PUser> managers) {
		this.managers = managers;
	}

	public Set<PUser> getTeam() {
		return team;
	}

	public void setTeam(Set<PUser> team) {
		this.team = team;
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

	public String[] getStrManagers() {
		return strManagers;
	}

	public void setStrManagers(String[] strManagers) {
		this.strManagers = strManagers;
	}

	public String[] getStrTeam() {
		return strTeam;
	}

	public void setStrTeam(String[] strTeam) {
		this.strTeam = strTeam;
	}

	public String getLeaderUserName() {
		return leaderUserName;
	}

	public void setLeaderUserName(String leaderUserName) {
		this.leaderUserName = leaderUserName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Set<PSystemProject> getProjectCodes() {
		return projectCodes;
	}

	public void setProjectCodes(Set<PSystemProject> projectCodes) {
		this.projectCodes = projectCodes;
	}


}


