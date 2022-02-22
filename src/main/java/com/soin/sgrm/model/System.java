package com.soin.sgrm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_SISTEMA")
public class System implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SISTEMAS_SISTEMA_SQ")
	@SequenceGenerator(name = "SISTEMAS_SISTEMA_SQ", sequenceName = "SISTEMAS_SISTEMA_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 10, message = "Máximo 10 caracteres.")
	private String code;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String name;

	@Column(name = "NOMECLATURA_VIEJA")
	private Boolean nomenclature;

	@Column(name = "IMPORTAR_OBJETOS_BASE_DATOS")
	private Boolean importObjects;

	@Column(name = "REQUIERE_COMANDOS_PERSONALEB6A")
	private Boolean customCommands;

	@Column(name = "ES_AIA")
	private Boolean isAIA;

	@Column(name = "ES_BO")
	private Boolean isBO;

	@Column(name = "OBSERVACIONES_ADICIONALES_1441")
	private Boolean additionalObservations;

	@Column(name = "INSTRUCCIONES_INSTALACION_665F")
	private Boolean installationInstructions;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIDER_TECNICO_ID", nullable = true)
	private User leader;

	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade({ CascadeType.EVICT })
	@JoinColumn(name = "PROYECTO_ID", nullable = true)
	private Project proyect;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_EQUIPO_TR2580", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> userTeam = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_GESTORES", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> managers = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMA_CORREO", joinColumns = { @JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CORREO_ID") })
	private Set<EmailTemplate> emailTemplate = new HashSet<EmailTemplate>();

	@Transient
	List<Integer> managersId;

	@Transient
	List<Integer> userTeamId;

	@Transient
	Integer leaderId;

	@Transient
	Integer proyectId;

	@Transient
	Integer emailId;

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

	public Boolean getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(Boolean nomenclature) {
		this.nomenclature = nomenclature;
	}

	public Project getProyect() {
		return proyect;
	}

	public void setProyect(Project proyect) {
		this.proyect = proyect;
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

	public Boolean getAdditionalObservations() {
		return additionalObservations;
	}

	public void setAdditionalObservations(Boolean additionalObservations) {
		this.additionalObservations = additionalObservations;
	}

	public Boolean getInstallationInstructions() {
		return installationInstructions;
	}

	public void setInstallationInstructions(Boolean installationInstructions) {
		this.installationInstructions = installationInstructions;
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

	public Set<User> getManagers() {
		return managers;
	}

	public void setManagers(Set<User> managers) {
		this.managers = managers;
	}

	public Set<EmailTemplate> getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(Set<EmailTemplate> emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public List<Integer> getManagersId() {
		return managersId;
	}

	public void setManagersId(String managersId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(managersId, new TypeReference<List<Integer>>() {
			});
			this.managersId = jsonList;
		} catch (Exception e) {
			this.managersId = null;
			Sentry.capture(e, "managers");
		}
	}

	public List<Integer> getUserTeamId() {
		return userTeamId;
	}

	public void setUserTeamId(String userTeamId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(userTeamId, new TypeReference<List<Integer>>() {
			});
			this.userTeamId = jsonList;
		} catch (Exception e) {
			this.userTeamId = null;
			Sentry.capture(e, "userTeam");
		}
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	public Integer getProyectId() {
		return proyectId;
	}

	public void setProyectId(Integer proyectId) {
		this.proyectId = proyectId;
	}

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}

	public void checkTeamsExists(Set<User> usersNews) {
		this.userTeam.retainAll(usersNews);
		// Agrego los nuevos usuarios
		for (User user : usersNews) {
			if (!existUserTeam(user.getId())) {
				this.userTeam.add(user);
			}
		}
	}

	public boolean existUserTeam(Integer id) {
		for (User user : this.userTeam) {
			if (user.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public void checkManagersExists(Set<User> managersNews) {
		this.managers.retainAll(managersNews);
		// Agrego los nuevos usuarios
		for (User user : managersNews) {
			if (!existUserManager(user.getId())) {
				this.managers.add(user);
			}
		}
	}

	public boolean existUserManager(Integer id) {
		for (User user : this.managers) {
			if (user.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public void changeEmail(EmailTemplate email) {
		this.emailTemplate.retainAll(new HashSet<EmailTemplate>());
		if(email != null) {
			this.emailTemplate.add(email);
		}
	}

}
