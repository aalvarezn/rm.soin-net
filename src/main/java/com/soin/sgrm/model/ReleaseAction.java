package com.soin.sgrm.model;

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

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_ACCION")
public class ReleaseAction implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "TIEMPO")
	private String time;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ENTORNO_ID")
	private SystemEnvironment environment;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCION_ID")
	private ActionEnvironment action;

	@Column(name = "OBSERVACION")
	private String observation;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public SystemEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(SystemEnvironment environment) {
		this.environment = environment;
	}

	public ActionEnvironment getAction() {
		return action;
	}

	public void setAction(ActionEnvironment action) {
		this.action = action;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
}
