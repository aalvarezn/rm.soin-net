package com.soin.sgrm.model.pos.wf;

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

import java.sql.Timestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Value;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseTracking;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PUser;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class PWFRelease implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SOLICITADO_POR_ID\"", nullable = true)
	private PUser user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"", nullable = true)
	private PStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"NODO_ID\"", nullable = true)
	private PNode node;

	@Column(name = "OPERADOR")
	private String operator;
	
	@Value("${retries:0}")
	@Column(name = "REINTENTOS", nullable = false)
	private Integer retries;


	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<PReleaseTracking> tracking = new HashSet<PReleaseTracking>();

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

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public PNode getNode() {
		return node;
	}

	public void setNode(PNode node) {
		this.node = node;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Set<PReleaseTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PReleaseTracking> tracking) {
		this.tracking = tracking;
	}
	
	

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public void convertReleaseToWFRelease(PRelease release) {
		this.node = release.getNode();
		this.createDate=release.getCreateDate();
		this.releaseNumber = release.getReleaseNumber();
		this.system = release.getSystem();
		this.status = release.getStatus();
		this.user = release.getUser();
	}
}