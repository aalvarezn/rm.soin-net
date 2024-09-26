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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SOLICITUD_RM_P1_R3")
public class PRequestRM_P1_R3 implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_rm_p1_r3_seq")
	@SequenceGenerator(name = "solicitud_rm_p1_r3_seq", sequenceName = "solicitud_rm_p1_r3_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "METODO_CONEXION")
	private String connectionMethod;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUD\"", nullable = false)
	private PRequestBase requestBase;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "R3_USUARIO", joinColumns = { @JoinColumn(name = "\"ID_SOLICITUDR3\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ID_USER\"") })
	private Set<PUser> userRM= new HashSet<>();

	@Transient
	List<Integer> usersRMId;
	
	@Transient
	private String message;
	
	@Transient
	private String senders;
	
	@Transient
	private String listNames;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConnectionMethod() {
		return connectionMethod;
	}

	public void setConnectionMethod(String connectionMethod) {
		this.connectionMethod = connectionMethod;
	}

	public PRequestBase getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(PRequestBase requestBase) {
		this.requestBase = requestBase;
	}

	public Set<PUser> getUserRM() {
		return userRM;
	}

	public void setUserRM(Set<PUser> userRM) {
		this.userRM = userRM;
	}

	public List<Integer> getUsersRMId() {
		return usersRMId;
	}

	public void setUsersRMId(List<Integer> usersRMId) {
		this.usersRMId = usersRMId;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenders() {
		return senders;
	}

	public void setSenders(String senders) {
		this.senders = senders;
	}

	public void checkUserRmExists(Set<PUser> usersNews) {
		this.userRM.retainAll(usersNews);
		// Agrego los nuevos ambients
		for (PUser user : usersNews) {
			if (!existUserRm(user.getId())) {
				this.userRM.add(user);
			}
		}
	}

	public boolean existUserRm(Integer id) {
		for (PUser user : this.userRM) {
			if (user.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public String getListNames() {
		String listNames = "<ul>";
		for (PUser user : this.userRM) {
			listNames += "<li> " + user.getFullName() + "</li>";
			
		}
		listNames += "</ul>";
		return listNames;
	}

	public void setListNames(String listNames) {
		this.listNames = listNames;
	}
	
}
