package com.soin.sgrm.model.wf;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "SEGURIDAD_CUSTOMUSER")
public class WFUser implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "USERNAME")
	@Size(max = 10, message = "Máximo 30 caracteres.")
	private String username;

	@Column(name = "FULL_NAME")
	@Size(max = 10, message = "Máximo 254 caracteres.")
	private String fullName;

	@Column(name = "SHORT_NAME")
	@Size(max = 10, message = "Máximo 30 caracteres.")
	private String shortName;

	@Column(name = "EMAIL")
	@Size(max = 10, message = "Máximo 254 caracteres.")
	private String email;

	@Column(name = "IS_ACTIVE")
	private int isActive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
