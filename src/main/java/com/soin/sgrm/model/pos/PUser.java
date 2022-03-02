package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.soin.sgrm.model.Authority;

@SuppressWarnings("unused")
@Entity
@Table(name = "USUARIO")
public class PUser implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "USUARIO")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ACTIVO")
	private Boolean active;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "ULTIMO_LOGUEO")
	private Timestamp lastLogin;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL, CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"USUARIO_ROL\"", joinColumns = { @JoinColumn(name = "\"USUARIO_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ROL_ID\"") })
	private Set<PAuthority> roles = new HashSet<>();

	@Transient
	private String[] strRoles;

	@Transient
	private String newPassword;

	@Transient
	private String confirmPassword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Set<PAuthority> getRoles() {
		return roles;
	}

	public void setRoles(Set<PAuthority> roles) {
		this.roles = roles;
	}

	public String[] getStrRoles() {
		return strRoles;
	}

	public void setStrRoles(String[] strRoles) {
		this.strRoles = strRoles;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (PAuthority authority : this.getRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getCode()));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.active;
	}

}
