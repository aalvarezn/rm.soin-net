package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE_HISTORIAL")
public class PReleaseTrackingToError implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RELEASE_ID\"", nullable = true)
	private PReleaseReferenceToError release;

	@Column(name = "ACTUALIZACION")
	private Date trackingDate;

	@Column(name = "ESTADO")
	private String status;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "MOTIVO")
	private String motive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PReleaseReferenceToError getRelease() {
		return release;
	}

	public void setRelease(PReleaseReferenceToError release) {
		this.release = release;
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
