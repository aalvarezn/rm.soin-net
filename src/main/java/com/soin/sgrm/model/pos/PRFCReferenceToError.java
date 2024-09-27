package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RFC")
public class PRFCReferenceToError  implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfc_seq")
	@SequenceGenerator(name = "rfc_seq", sequenceName = "rfc_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUM_SOLICITUD")
	private String numRequest;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
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
