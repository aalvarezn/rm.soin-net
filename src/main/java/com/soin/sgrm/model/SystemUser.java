package com.soin.sgrm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import com.soin.sgrm.model.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_SISTEMA")
public class SystemUser implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO")
	private String code;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "NOMECLATURA_VIEJA")
	private int nomenclature;

	@Column(name = "INSTRUCCIONES_INSTALACION_665F")
	private int installation_instructions_665f;

	@Column(name = "OBSERVACIONES_ADICIONALES_1441")
	private String additional_remarks_1441;

	@Column(name = "LIDER_TECNICO_ID")
	private int tec_leader_id;

	@Column(name = "ES_BO")
	private int is_bo;

	@Column(name = "IMPORTAR_OBJETOS_BASE_DATOS")
	private int import_objects_db;

	@Column(name = "REQUIERE_COMANDOS_PERSONALEB6A")
	private int required_commands;

	@Column(name = "ES_AIA")
	private String is_aia;

	@Column(name = "PROYECTO_ID")
	private String proyectId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SISTEMAS_SISTEMA_EQUIPO_TR2580", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> user = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SISTEMAS_SISTEMA_GESTORES", joinColumns = {
			@JoinColumn(name = "SISTEMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<User> managers = new HashSet<User>();

//    @ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
//	private User zsd;

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

	public int getInstallation_instructions_665f() {
		return installation_instructions_665f;
	}

	public void setInstallation_instructions_665f(int installation_instructions_665f) {
		this.installation_instructions_665f = installation_instructions_665f;
	}

	public String getAdditional_remarks_1441() {
		return additional_remarks_1441;
	}

	public void setAdditional_remarks_1441(String additional_remarks_1441) {
		this.additional_remarks_1441 = additional_remarks_1441;
	}

	public int getTec_leader_id() {
		return tec_leader_id;
	}

	public void setTec_leader_id(int tec_leader_id) {
		this.tec_leader_id = tec_leader_id;
	}

	public int getIs_bo() {
		return is_bo;
	}

	public void setIs_bo(int is_bo) {
		this.is_bo = is_bo;
	}

	public int getImport_objects_db() {
		return import_objects_db;
	}

	public void setImport_objects_db(int import_objects_db) {
		this.import_objects_db = import_objects_db;
	}

	public int getRequired_commands() {
		return required_commands;
	}

	public void setRequired_commands(int required_commands) {
		this.required_commands = required_commands;
	}

	public String getIs_aia() {
		return is_aia;
	}

	public void setIs_aia(String is_aia) {
		this.is_aia = is_aia;
	}

	public String getProyectId() {
		return proyectId;
	}

	public void setProyectId(String proyectId) {
		this.proyectId = proyectId;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public Set<User> getManagers() {
		return managers;
	}

	public void setManagers(Set<User> managers) {
		this.managers = managers;
	}

	public String getManagerList() {
		String x = "";
		for (User user : managers) {
			x += user.getFullName() + ",";
		}
		return x;
	}

}
