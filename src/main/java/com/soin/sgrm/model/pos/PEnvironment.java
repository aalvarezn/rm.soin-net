package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

public class PEnvironment implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "EXTERNO")
	private String extern;
}
