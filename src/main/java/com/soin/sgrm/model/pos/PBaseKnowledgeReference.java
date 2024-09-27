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
@Table(name = "BASECONOCIMIENTO")
public class PBaseKnowledgeReference  implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baseconocimiento_seq")
	@SequenceGenerator(name = "baseconocimiento_seq", sequenceName = "baseconocimiento_id_seq", allocationSize = 1)
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
