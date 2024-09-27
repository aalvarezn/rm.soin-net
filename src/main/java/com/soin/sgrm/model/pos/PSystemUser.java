package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SISTEMAS_SISTEMA")
public class PSystemUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistemas_sistema_seq")
	@SequenceGenerator(name = "sistemas_sistema_seq", sequenceName = "sistemas_sistema_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "NOMECLATURA_VIEJA")
	private Boolean nomenclature;

	@Column(name = "INSTRUCCIONES_INSTALACION_665F")
	private Boolean installation_instructions_665f;

	@Column(name = "OBSERVACIONES_ADICIONALES_1441")
	private Boolean additional_remarks_1441;

	@Column(name = "LIDER_TECNICO_ID")
	private Integer tec_leader_id;

	@Column(name = "ES_BO")
	private Boolean is_bo;

	@Column(name = "IMPORTAR_OBJETOS_BASE_DATOS")
	private Boolean import_objects_db;

	@Column(name = "REQUIERE_COMANDOS_PERSONALEB6A")
	private Boolean required_commands;

	@Column(name = "ES_AIA")
	private Boolean is_aia;

	@Column(name = "PROYECTO_ID")
	private Integer proyectId;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_EQUIPO_TR2580", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PUser> user = new HashSet<PUser>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SISTEMAS_SISTEMA_GESTORES", joinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PUser> managers = new HashSet<PUser>();

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

	public Boolean getInstallation_instructions_665f() {
		return installation_instructions_665f;
	}

	public void setInstallation_instructions_665f(Boolean installation_instructions_665f) {
		this.installation_instructions_665f = installation_instructions_665f;
	}

	public Boolean getAdditional_remarks_1441() {
		return additional_remarks_1441;
	}

	public void setAdditional_remarks_1441(Boolean additional_remarks_1441) {
		this.additional_remarks_1441 = additional_remarks_1441;
	}

	public Boolean getIs_bo() {
		return is_bo;
	}

	public void setIs_bo(Boolean is_bo) {
		this.is_bo = is_bo;
	}

	public Boolean getImport_objects_db() {
		return import_objects_db;
	}

	public void setImport_objects_db(Boolean import_objects_db) {
		this.import_objects_db = import_objects_db;
	}

	public Boolean getRequired_commands() {
		return required_commands;
	}

	public void setRequired_commands(Boolean required_commands) {
		this.required_commands = required_commands;
	}

	public Boolean getIs_aia() {
		return is_aia;
	}

	public void setIs_aia(Boolean is_aia) {
		this.is_aia = is_aia;
	}

	public void setTec_leader_id(Integer tec_leader_id) {
		this.tec_leader_id = tec_leader_id;
	}

	public Integer getProyectId() {
		return proyectId;
	}

	public void setProyectId(Integer proyectId) {
		this.proyectId = proyectId;
	}

	public Set<PUser> getUser() {
		return user;
	}

	public void setUser(Set<PUser> user) {
		this.user = user;
	}

	public Set<PUser> getManagers() {
		return managers;
	}

	public void setManagers(Set<PUser> managers) {
		this.managers = managers;
	}

	public String getManagerList() {
		String x = "";
		for (PUser user : managers) {
			x += user.getFullName() + ",";
		}
		return x;
	}

}
