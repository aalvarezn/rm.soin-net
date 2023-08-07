package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SOLICITUD_ESTIMACIONES")
public class PRequest_Estimate implements Serializable  {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHAESTIFINALIZACION")
	private Timestamp requestDateEstimate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHAFINALIZACION")
	private Timestamp requestDateFinal;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUD\"", nullable = true)
	private PRequestBaseReference requestBase;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getRequestDateEstimate() {
		return requestDateEstimate;
	}

	public void setRequestDateEstimate(Timestamp requestDateEstimate) {
		this.requestDateEstimate = requestDateEstimate;
	}

	public Timestamp getRequestDateFinal() {
		return requestDateFinal;
	}

	public void setRequestDateFinal(Timestamp requestDateFinal) {
		this.requestDateFinal = requestDateFinal;
	}

	public PRequestBaseReference getRequestBase() {
		return requestBase;
	}

	public void setRequestBase(PRequestBaseReference requestBase) {
		this.requestBase = requestBase;
	}
	

	
	

}
