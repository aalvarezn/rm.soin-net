package com.soin.sgrm.model;


import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;

@Entity
@Table(name = "INCIDENCIA")
public class IncidenceResume implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SLA_ACTIVO")
	private Integer slaActive;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHACREACION")
	private Timestamp createDate;
	
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "SALIDA_OPTIMA")
	private Timestamp exitOptimalDate;
	
	@Column(name = "TIEMPO_MILI")
	private Integer timeMili;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO", nullable = false)
	private System_StatusIn status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSlaActive() {
		return slaActive;
	}

	public void setSlaActive(Integer slaActive) {
		this.slaActive = slaActive;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getExitOptimalDate() {
		return exitOptimalDate;
	}

	public void setExitOptimalDate(Timestamp exitOptimalDate) {
		this.exitOptimalDate = exitOptimalDate;
	}

	public Integer getTimeMili() {
		return timeMili;
	}

	public void setTimeMili(Integer timeMili) {
		this.timeMili = timeMili;
	}

	public System_StatusIn getStatus() {
		return status;
	}

	public void setStatus(System_StatusIn status) {
		this.status = status;
	}
	
}