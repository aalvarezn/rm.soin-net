package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.utils.Constant;


@Entity
@Table(name = "SEGURIDAD_CUSTOMUSER")
public class PUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Integer id;

	@Column(name = "PASSWORD2")
	private String password;

	@Column(unique = true, name = "USERNAME")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 30, message = "Máximo 30 caracteres.")
	private String username;
	
	@Column(unique = true, name = "GITUSERNAME")
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String gitusername;

	@Column(name = "FULL_NAME")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String fullName;

	@Column(name = "IS_RELEASE_MANAGER")
	private int isReleaseManager;

	@Column(name = "IS_SUPERUSER")
	private int isSuperUser;

	@Cascade({ CascadeType.SAVE_UPDATE })
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"SEGURIDAD_CUSTOMUSER_GROUPS\"", joinColumns = {
			@JoinColumn(name = "\"CUSTOMUSER_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"GROUP_ID\"") })
	private Set<PAuthority> authorities = new HashSet<>();

	@Column(name = "SHORT_NAME")
	@Size(max = 30, message = "Máximo 30 caracteres.")
	private String shortName;

	@Column(unique = true, name = "EMAIL")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String emailAddress;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@Column(name = "IS_STAFF")
	private Boolean staff;

	@Column(name = "DATE_JOINED")
	private Timestamp dateJoined;

	@Transient
	List<Integer> rolesId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<PAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<PAuthority> authorities) {
		this.authorities = authorities;
	}

	public int getIsReleaseManager() {
		return isReleaseManager;
	}

	public void setIsReleaseManager(int isReleaseManager) {
		this.isReleaseManager = isReleaseManager;
	}

	public int getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(int isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<Integer> getRolesId() {
		return rolesId;
	}

	public String getGitusername() {
		return gitusername;
	}

	public void setGitusername(String gitusername) {
		this.gitusername = gitusername;
	}

	public void setRolesId(String rolesId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(rolesId, new TypeReference<List<Integer>>() {
			});
			this.rolesId = jsonList;
		} catch (Exception e) {
			this.rolesId = null;
			Sentry.capture(e, "roles");
		}
	}

	public void checkAuthoritiesExists(Set<PAuthority> authsNews) {
		this.authorities.retainAll(authsNews);
		// Agrego los nuevos ambients
		for (PAuthority auth : authsNews) {
			if (!existAuthority(auth.getId())) {
				this.authorities.add(auth);
			}
		}
	}

	public boolean existAuthority(Integer id) {
		for (PAuthority auth : this.authorities) {
			if (auth.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public Boolean getStaff() {
		return staff;
	}

	public void setStaff(Boolean staff) {
		this.staff = staff;
	}

	public Timestamp getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

}
