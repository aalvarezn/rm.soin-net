package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SISTEMAS_ACCION")
public class PReleaseActionEdit implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistemas_accion_seq")
	@SequenceGenerator(name = "sistemas_accion_seq", sequenceName = "sistemas_accion_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "TIEMPO")
	private String time;

	@Column(name = "ENTORNO_ID")
	private Integer environment;

	@Column(name = "ACCION_ID")
	private Integer action;

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

	public Integer getEnvironment() {
		return environment;
	}

	public void setEnvironment(Integer environment) {
		this.environment = environment;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

}
