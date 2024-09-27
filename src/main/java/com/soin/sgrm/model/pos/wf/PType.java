package com.soin.sgrm.model.pos.wf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAMITES_TIPO")
public class PType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tramites_tipo_seq")
	@SequenceGenerator(name = "tramites_tipo_seq", sequenceName = "tramites_tipo_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String name;

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

}
