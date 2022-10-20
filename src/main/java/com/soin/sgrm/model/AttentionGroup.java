package com.soin.sgrm.model;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "GRUPOATENCION")
public class AttentionGroup implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "CODIGO")
	private String code;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "GRUPOATENCION_USUARIO", joinColumns = { @JoinColumn(name = "ID_GRUPO") }, inverseJoinColumns = {
			@JoinColumn(name = "ID_USUARIO") })
	private Set<User> userAttention= new HashSet<>();
	

	@Transient
	List<Integer> usersAttentionId;
	

	@Transient
	private String listNames;
	
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<User> getUserAttention() {
		return userAttention;
	}

	public void setUserAttention(Set<User> userAttention) {
		this.userAttention = userAttention;
	}

	public List<Integer> getUsersAttentionId() {
		return usersAttentionId;
	}

	public void setUsersAttentionId(List<Integer> usersAttentionId) {
		this.usersAttentionId = usersAttentionId;
	}


	public void checkUserExists(Set<User> usersNews) {
		this.userAttention.retainAll(usersNews);
		// Agrego los nuevos ambients
		for (User user : usersNews) {
			if (!existUser(user.getId())) {
				this.userAttention.add(user);
			}
		}
	}

	public boolean existUser(Integer id) {
		for (User user : this.userAttention) {
			if (user.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public String getListNames() {
		String listNames = "<ul>";
		for (User user : this.userAttention) {
			listNames += "<li> " + user.getFullName() + "</li>";
			
		}
		listNames += "</ul>";
		return listNames;
	}

	public void setListNames(String listNames) {
		this.listNames = listNames;
	}
	
}
