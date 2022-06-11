package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.HashSet;
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

@Entity
@Table(name = "ENTORNO")
public class PEnvironment implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "EXTERNO")
	private String extern;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"SISTEMA_ENTORNO\"", joinColumns = { @JoinColumn(name = "\"ENTORNO_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"SISTEMA_ID\"") })
	private Set<PSystem> systems = new HashSet<>();
	
	@Transient
	private String[] strRoles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtern() {
		return extern;
	}

	public void setExtern(String extern) {
		this.extern = extern;
	}

	public Set<PSystem> getSystems() {
		return systems;
	}

	public void setSystems(Set<PSystem> systems) {
		this.systems = systems;
	}

	public String[] getStrRoles() {
		return strRoles;
	}

	public void setStrRoles(String[] strRoles) {
		this.strRoles = strRoles;
	}
	
	
	
}
