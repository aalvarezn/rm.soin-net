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

import java.util.Date;


@Entity
@Table(name = "RFC_HISTORIAL")
public class PRFCTrackingToError implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RFC_ID", nullable = true)
	private PRFCReferenceToError rfc;

	@Column(name = "ACTUALIZACION")
	private Date trackingDate;

	@Column(name = "ESTADO")
	private String status;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "MOTIVO")
	private String motive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public PRFCReferenceToError getRfc() {
		return rfc;
	}

	public void setRfc(PRFCReferenceToError rfc) {
		this.rfc = rfc;
	}

	public Date getTrackingDate() {
		return trackingDate;
	}

	public void setTrackingDate(Date trackingDate) {
		this.trackingDate = trackingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}
	
	
}
