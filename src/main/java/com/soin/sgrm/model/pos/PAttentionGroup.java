package com.soin.sgrm.model.pos;

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
public class PAttentionGroup implements Serializable{

	
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_LEAD\"")
	private PUser lead;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "\"GRUPOATENCION_USUARIO\"", joinColumns = { @JoinColumn(name = "\"ID_GRUPO\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ID_USUARIO\"") })
	private Set<PUser> userAttention= new HashSet<>();
	

	@Transient
	List<Integer> usersAttentionId;
	
	@Transient
	 int leaderId;
	

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
	
	public PUser getLead() {
		return lead;
	}

	public void setLead(PUser lead) {
		this.lead = lead;
	}

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leadId) {
		this.leaderId = leadId;
	}

	public Set<PUser> getUserAttention() {
		return userAttention;
	}

	public void setUserAttention(Set<PUser> userAttention) {
		this.userAttention = userAttention;
	}

	public List<Integer> getUsersAttentionId() {
		return usersAttentionId;
	}

	public void setUsersAttentionId(List<Integer> usersAttentionId) {
		this.usersAttentionId = usersAttentionId;
	}


	public void checkUserExists(Set<PUser> usersNews) {
		this.userAttention.retainAll(usersNews);
		// Agrego los nuevos ambients
		for (PUser user : usersNews) {
			if (!existUser(user.getId())) {
				this.userAttention.add(user);
			}
		}
	}

	public boolean existUser(Integer id) {
		for (PUser user : this.userAttention) {
			if (user.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public String getListNames() {
		String listNames = "<ul>";
		for (PUser user : this.userAttention) {
			listNames += "<li> " + user.getFullName() + "</li>";
			
		}
		listNames += "</ul>";
		return listNames;
	}

	public void setListNames(String listNames) {
		this.listNames = listNames;
	}
	
}