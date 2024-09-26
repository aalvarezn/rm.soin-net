package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

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
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "INCIDENCIA_MENSAJE")
public class PEmailIncidence implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incidencia_mensaje_seq")
	@SequenceGenerator(name = "incidencia_mensaje_seq", sequenceName = "incidencia_mensaje_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "MENSAJE")
	private String message;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_INCIDENCIA\"", nullable = false)
	private PIncidence incidence;
	
	@DateTimeFormat(pattern = "dd-MM-yyy")
	@Column(name = "FECHA_ENVIO")
	private Timestamp sendDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public PIncidence getIncidence() {
		return incidence;
	}

	public void setIncidence(PIncidence incidence) {
		this.incidence = incidence;
	}
	
	
}
