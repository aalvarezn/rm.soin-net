package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SEGURIDAD_CUSTOMUSER")
public class PUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seguridad_customuser_seq")
	@SequenceGenerator(name = "seguridad_customuser_seq", sequenceName = "seguridad_customuser_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "LAST_LOGIN")
	private Timestamp lastLogin;
	
	@Column(name = "IS_SUPERUSER")
	private int isSuperUser;
	
	@Column(name = "USERNAME")
	@Size(max=10, message = "Máximo 30 caracteres.")
	private String username;
	
	@Column(name = "FULL_NAME")
	@Size(max=10, message = "Máximo 254 caracteres.")
	private String fullName;
	
	@Column(name = "SHORT_NAME")
	@Size(max=10, message = "Máximo 30 caracteres.")
	private String shortName;
	
	@Column(name = "EMAIL")
	@Size(max=10, message = "Máximo 254 caracteres.")
	private String email;
	
	@Column(name = "IS_STAFF")
	private Boolean isStaff;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "DATE_JOINED")
	private Timestamp dateJoined;

	@Column(name = "IS_RELEASE_MANAGER")
	private int isReleaseManager;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(int isSuperUser) {
		this.isSuperUser = isSuperUser;
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

	public boolean getIsStaff() {
		return isStaff;
	}

	public void setIsStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

	public int getIsReleaseManager() {
		return isReleaseManager;
	}

	public void setIsReleaseManager(int isReleaseManager) {
		this.isReleaseManager = isReleaseManager;
	}

//	public Set<Release> getReleases() {
//		return releases;
//	}
//
//	public void setReleases(Set<Release> releases) {
//		this.releases = releases;
//	}

}
