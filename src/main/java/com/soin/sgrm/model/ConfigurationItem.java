package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "AMBIENTES_CONFIGURATIONITEM")
public class ConfigurationItem implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMBIENTES_CONFIGURATIONITEM_SQ")
	@SequenceGenerator(name = "AMBIENTES_CONFIGURATIONITEM_SQ", sequenceName = "AMBIENTES_CONFIGURATIONITEM_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "SISTEMA_ID")
	private int system_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSystem_id() {
		return system_id;
	}

	public void setSystem_id(int system_id) {
		this.system_id = system_id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
