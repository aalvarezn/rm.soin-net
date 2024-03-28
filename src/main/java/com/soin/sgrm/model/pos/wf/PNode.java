package com.soin.sgrm.model.pos.wf;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAMITES_NODO")
public class PNode implements Serializable {

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
	@JoinColumn(name = "\"TRAMITE_ID\"", nullable = false)
	private PWorkFlow workFlow;

	@Fetch(value = FetchMode.SUBSELECT)
	//@JsonManagedReference
	@OneToMany(mappedBy = "nodeFrom", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<PEdge> edges = new ArrayList<PEdge>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"", nullable = true)
	private PStatus status;

	@Column(name = "X")
	private Float x;

	@Column(name = "Y")
	private Float y;

	@Column(name = "GRUPO")
	private String group;
	
	@Column(name = "SKIP_NODE_ID")
	private Integer skipId;
	
	@Column(name = "REQUEST_NODE_ID")
	private Integer skipByRequestId;
	
	@Column(name = "REAPPROVE_ID")
	private Integer skipReapproveId;
	
	@Column(name = "SKIPBYREQUEST")
	private Boolean skipByRequest;
	

	@Column(name = "REAPPROVE")
	private Boolean skipReapprove;
	
	@Column(name = "SKIP")
	private Boolean skipNode;
	
	@Column(name = "MOTIVE_R")
	private String motiveSkipR;
	
	@Column(name = "MOTIVE_RA")
	private String motiveSkipRA;
	
	@Column(name = "MOTIVE_S")
	private String motiveSkip;

	@Transient
	private Integer workFlowId;

	@Transient
	private Integer statusId;

	@Column(name = "CORREO")
	private Boolean sendEmail;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "TRAMITES_NODO_NOTIFY", joinColumns = { @JoinColumn(name = "\"NODO_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PWFUser> users = new HashSet<PWFUser>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "TRAMITES_NODO_ACTOR", joinColumns = { @JoinColumn(name = "\"NODO_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"CUSTOMUSER_ID\"") })
	private Set<PWFUser> actors = new HashSet<PWFUser>();

	@Transient
	private List<Integer> usersIds;

	@Transient
	private List<Integer> actorsIds;
	
	
	public PNode() {
		super();
	}

	public PNode(int id) {
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

	public PWorkFlow getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(PWorkFlow workFlow) {
		this.workFlow = workFlow;
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

	public List<PEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<PEdge> edges) {
		this.edges = edges;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Set<PWFUser> getUsers() {
		return users;
	}

	public void setUsers(Set<PWFUser> users) {
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
		for (PWFUser user : this.users) {
			this.users.remove(user);
		}
	}

	public void addUser(PWFUser user) {
		this.users.add(user);
	}

	public Set<PWFUser> getActors() {
		return actors;
	}

	public void setActors(Set<PWFUser> actors) {
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
		for (PWFUser user : this.actors) {
			this.actors.remove(user);
		}
	}

	public void addActor(PWFUser user) {
		this.actors.add(user);
	}

	public Integer getSkipId() {
		return skipId;
	}

	public void setSkipId(Integer skipId) {
		this.skipId = skipId;
	}

	public Integer getSkipByRequestId() {
		return skipByRequestId;
	}

	public void setSkipByRequestId(Integer skipByRequestId) {
		this.skipByRequestId = skipByRequestId;
	}

	public Integer getSkipReapproveId() {
		return skipReapproveId;
	}

	public void setSkipReapproveId(Integer skipReapproveId) {
		this.skipReapproveId = skipReapproveId;
	}

	public Boolean getSkipByRequest() {
		return skipByRequest;
	}

	public void setSkipByRequest(Boolean skipByRequest) {
		this.skipByRequest = skipByRequest;
	}

	public Boolean getSkipReapprove() {
		return skipReapprove;
	}

	public void setSkipReapprove(Boolean skipReapprove) {
		this.skipReapprove = skipReapprove;
	}

	public Boolean getSkipNode() {
		return skipNode;
	}

	public void setSkipNode(Boolean skipNode) {
		this.skipNode = skipNode;
	}

	public String getMotiveSkipR() {
		return motiveSkipR;
	}

	public void setMotiveSkipR(String motiveSkipR) {
		this.motiveSkipR = motiveSkipR;
	}

	public String getMotiveSkipRA() {
		return motiveSkipRA;
	}

	public void setMotiveSkipRA(String motiveSkipRA) {
		this.motiveSkipRA = motiveSkipRA;
	}

	public String getMotiveSkip() {
		return motiveSkip;
	}

	public void setMotiveSkip(String motiveSkip) {
		this.motiveSkip = motiveSkip;
	}



}
