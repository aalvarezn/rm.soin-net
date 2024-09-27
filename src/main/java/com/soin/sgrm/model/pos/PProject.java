package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;


@Entity
@Table(name = "PROYECTOS_PROYECTO")
public class PProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proyectos_proyecto_seq")
	@SequenceGenerator(name = "proyectos_proyecto_seq", sequenceName = "proyectos_proyecto_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "CODIGO")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String code;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	private String description;

	@Column(name = "USER_EMAIL")
	private String userEmail;

	@Column(name = "PASSWORD_EMAIL")
	private String passwordEmail;

	@Column(name = "RELEASE_MANAGER_EMAIL")
	private String releaseManagerEmail;

	@Column(name = "NOTIFICAR")
	private Boolean notify;

	@Column(name = "NOTIFICAR_GESTOR")
	private Boolean notifyManager;

	@Column(name = "NOTIFICAR_PMO")
	private Boolean notifyPMO;

	@Column(name = "PMO_EMAIL")
	private String pmoEmail;

	@Column(name = "NOTIFICAR_LIDER_TECNICO")
	private Boolean notifyTechLead;
	
	@Column(name = "PERMITIR_REPETIR")
	private Boolean allowRepeat;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPasswordEmail() {
		return passwordEmail;
	}

	public void setPasswordEmail(String passwordEmail) {
		this.passwordEmail = passwordEmail;
	}

	public String getReleaseManagerEmail() {
		return releaseManagerEmail;
	}

	public void setReleaseManagerEmail(String releaseManagerEmail) {
		this.releaseManagerEmail = releaseManagerEmail;
	}

	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public Boolean getNotifyManager() {
		return notifyManager;
	}

	public void setNotifyManager(Boolean notifyManager) {
		this.notifyManager = notifyManager;
	}

	public Boolean getNotifyPMO() {
		return notifyPMO;
	}

	public void setNotifyPMO(Boolean notifyPMO) {
		this.notifyPMO = notifyPMO;
	}

	public String getPmoEmail() {
		return pmoEmail;
	}

	public void setPmoEmail(String pmoEmail) {
		this.pmoEmail = pmoEmail;
	}

	public Boolean getNotifyTechLead() {
		return notifyTechLead;
	}

	public void setNotifyTechLead(Boolean notifyTechLead) {
		this.notifyTechLead = notifyTechLead;
	}

	public Boolean getAllowRepeat() {
		return allowRepeat;
	}

	public void setAllowRepeat(Boolean allowRepeat) {
		this.allowRepeat = allowRepeat;
	}
	
	
}
