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
@Table(name = "SOLICITUD")
public class PRequestBaseReferenceToError implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUM_SOLICITUD")
	private String numRequest;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SISTEMA", nullable = false)
	private PSystemInfo system;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumRequest() {
		return numRequest;
	}
	public void setNumRequest(String numRequest) {
		this.numRequest = numRequest;
	}
	public PSystemInfo getSystem() {
		return system;
	}
	public void setSystem(PSystemInfo system) {
		this.system = system;
	}
	
}
