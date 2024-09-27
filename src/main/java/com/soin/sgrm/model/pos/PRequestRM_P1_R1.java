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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SOLICITUD_RM_P1_R1")
public class PRequestRM_P1_R1 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_rm_p1_r1_seq")
	@SequenceGenerator(name = "solicitud_rm_p1_r1_seq", sequenceName = "solicitud_rm_p1_r1_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TIEMPO_RESPUESTA")
	private String timeAnswer;

	@Column(name = "REQUERIMIENTOS_INI")
	private String initialRequeriments;

	@Column(name = "OBSERVACIONES_GEN")
	private String observations;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUD\"", nullable = false)
	private PRequestBase requestBase;

	@Transient
	private Long requestBaseId;
	
	@Transient
	private String message;
	
	@Transient
	private String senders;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTimeAnswer() {
		return timeAnswer;
	}

	public void setTimeAnswer(String timeAnswer) {
		this.timeAnswer = timeAnswer;
	}

	public String getInitialRequeriments() {
		return initialRequeriments;
	}

	public void setInitialRequeriments(String initialRequeriments) {
		this.initialRequeriments = initialRequeriments;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public PRequestBase getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(PRequestBase requestBase) {
		this.requestBase = requestBase;
	}



	public Long getRequestBaseId() {
		return requestBaseId;
	}

	public void setRequestBaseId(Long requestBaseId) {
		this.requestBaseId = requestBaseId;
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
	
	
	
	
	
}
