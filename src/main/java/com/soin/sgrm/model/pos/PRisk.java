package com.soin.sgrm.model.pos;

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

@Entity
@Table(name = "RELEASES_RIESGO")
public class PRisk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "releases_riesgo_seq")
	@SequenceGenerator(name = "releases_riesgo_seq", sequenceName = "releases_riesgo_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String name;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
