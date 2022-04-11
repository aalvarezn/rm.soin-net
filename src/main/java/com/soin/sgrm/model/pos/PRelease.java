package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RELEASE")
public class PRelease implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO_RELEASE")
	private String numRelease;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"")
	private PSystem system;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"")
	private PStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumRelease() {
		return numRelease;
	}

	public void setNumRelease(String numRelease) {
		this.numRelease = numRelease;
	}

	public PSystem getSystem() {
		return system;
	}

	public void setSystem(PSystem system) {
		this.system = system;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}
	
	
}
