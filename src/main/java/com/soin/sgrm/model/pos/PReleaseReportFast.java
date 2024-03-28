package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.soin.sgrm.model.SystemInfo;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class PReleaseReportFast implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "OBSERVACIONES")
	private String observations;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"", nullable = true)
	private PStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SOLICITADO_POR_ID\"", nullable = true)
	private PUser user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystem system;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PRIORIDAD_ID\"", nullable = true)
	private PPriority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RIESGO_ID\"", nullable = true)
	private PRisk risk;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"IMPACTO_ID\"", nullable = true)
	private PImpact impact;

	@Column(name = "NUMEROVERSION")
	private String versionNumber;

	@Column(name = "REINTENTOS", nullable = true)
	private Integer retries;

	@Transient 
	private String userName;
	
	@Transient 
	private String statusName;
	
	@Transient 
	private String impactName;
	
	@Transient 
	private String priorityName;
	
	@Transient 
	private String systemName;
	
	@Transient 
	private String riskName;
	
	@Transient 
	private Timestamp createDateName;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}


	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public PSystem getSystem() {
		return system;
	}

	public void setSystem(PSystem system) {
		this.system = system;
	}


	public PPriority getPriority() {
		return priority;
	}

	public void setPriority(PPriority priority) {
		this.priority = priority;
	}

	public PRisk getRisk() {
		return risk;
	}

	public void setRisk(PRisk risk) {
		this.risk = risk;
	}

	public PImpact getImpact() {
		return impact;
	}

	public void setImpact(PImpact impact) {
		this.impact = impact;
	}



	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}
	public String getUserName() {
		
		if(getUser()!=null) {
			return getUser().getFullName();
		}else {
			return "No hay un usuario seleccionado";
		}
		
	}

	public String getStatusName() {
		 
		if(getStatus()!=null) {
			return getStatus().getName();
		}else {
			return "No hay un estado seleccionado";
		}
	}

	public String getImpactName() {
			if(getImpact()!=null) {
			return getImpact().getName();
		}else {
			return "No hay un impacto seleccionado";
		}
	}

	public String getPriorityName() {
	
		if(getPriority()!=null) {
			return getPriority().getName();
		}else {
			return "No hay una prioridad seleccionada ";
		}
	}

	public String getSystemName() {
		
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema seleccionado";
		}
	}

	public String getRiskName() {
		
		if(getRisk()!=null) {
			return getRisk().getName();
		}else {
			return "No hay un riesgo seleccionado";
		}
	}

	public Timestamp getCreateDateName() {
		if(getCreateDate()!=null) {
			return getCreateDate();
		}else {
			return null;
		}
	}

	public void setCreateDateName(Timestamp createDateName) {
		this.createDateName = createDateName;
	}
	

	
	

}
