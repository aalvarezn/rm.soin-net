package com.soin.sgrm.model.wf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAMITES_NODO_RFC")
public class NodeRFC implements Serializable {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String label;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRAMITE_ID", nullable = false)
	private WorkFlowRFC workFlow;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "nodeFrom", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<EdgeRFC> edges = new ArrayList<EdgeRFC>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private StatusRFC status;
	
	@Column(name = "X")
	private Float x;

	@Column(name = "Y")
	private Float y;

	@Column(name = "GRUPO")
	private String group;

	@Transient
	private Integer workFlowId;
	
	@Transient
	private Long statusId;

	@Column(name = "CORREO")
	private Boolean sendEmail;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "TRAMITES_NODO_NOT_RFC", joinColumns = { @JoinColumn(name = "NODO_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<WFUser> users = new HashSet<WFUser>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "TRAMITES_NODO_ACT_RFC", joinColumns = { @JoinColumn(name = "NODO_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CUSTOMUSER_ID") })
	private Set<WFUser> actors = new HashSet<WFUser>();

	@Transient
	private List<Integer> usersIds;

	@Transient
	private List<Integer> actorsIds;

	public NodeRFC() {
		super();
	}

	public NodeRFC(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	

	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Integer getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Integer workFlowId) {
		this.workFlowId = workFlowId;
	}

	public List<EdgeRFC> getEdges() {
		return edges;
	}

	public void setEdges(List<EdgeRFC> edges) {
		this.edges = edges;
	}

	public StatusRFC getStatus() {
		return status;
	}

	public void setStatus(StatusRFC status) {
		this.status = status;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Set<WFUser> getUsers() {
		return users;
	}

	public void setUsers(Set<WFUser> users) {
		this.users = users;
	}

	public List<Integer> getUsersIds() {
		return usersIds;
	}

	public void setUsersIds(String usersIds) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(usersIds, new TypeReference<List<Integer>>() {
			});
			this.usersIds = jsonList;
		} catch (Exception e) {
			this.usersIds = null;
			Sentry.capture(e, "usersIds");
		}
	}

	public void clearUsers() {
		for (WFUser user : this.users) {
			this.users.remove(user);
		}
	}

	public void addUser(WFUser user) {
		this.users.add(user);
	}

	public Set<WFUser> getActors() {
		return actors;
	}

	public void setActors(Set<WFUser> actors) {
		this.actors = actors;
	}

	public List<Integer> getActorsIds() {
		return actorsIds;
	}

	public void setActorsIds(String actorsIds) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(actorsIds, new TypeReference<List<Integer>>() {
			});
			this.actorsIds = jsonList;
		} catch (Exception e) {
			this.actorsIds = null;
			Sentry.capture(e, "actorsIds");
		}
	}
	
	public void clearActors() {
		for (WFUser user : this.actors) {
			this.actors.remove(user);
		}
	}

	public void addActor(WFUser user) {
		this.actors.add(user);
	}

	public WorkFlowRFC getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlowRFC workFlow) {
		this.workFlow = workFlow;
	}




	
}