package com.soin.sgrm.model.wf;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.sql.Timestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Value;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseTracking;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class WFRelease implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NODO_ID", nullable = true)
	private Node node;

	@Column(name = "OPERADOR")
	private String operator;
	
	@Value("${retries:0}")
	@Column(name = "REINTENTOS", nullable = false)
	private Integer retries;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Transient
	private Node nodeFinish;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public void convertReleaseToWFRelease(Release release) {
		this.node = release.getNode();
		this.createDate=release.getCreateDate();
		this.releaseNumber = release.getReleaseNumber();
		this.system = release.getSystem();
		this.status = release.getStatus();
		this.user = release.getUser();
	}

	public Node getNodeFinish() {
		return nodeFinish;
	}

	public void setNodeFinish(Node nodeFinish) {
		this.nodeFinish = nodeFinish;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}
	
	
}