package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BASECONOCIMIENTO")
public class BaseKnowledgeReference  implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUM_ERROR")
	private String numError;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumError() {
		return numError;
	}

	public void setNumError(String numError) {
		this.numError = numError;
	}

}
