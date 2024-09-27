package com.soin.sgrm.model.pos;

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
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "GENERADOR_DOCS_TEMPLATEGEN54D5")
public class PDocTemplate implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generador_docs_seq")
	@SequenceGenerator(name = "generador_docs_seq", sequenceName = "generador_docs_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String name;

	@Column(name = "NOMBRE_TEMPLATE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String templateName;

	@Column(name = "COMPONENTE_GENERADOR_WORD")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String componentGenerator;

	@Column(name = "SUFIJO_EN_NOMBRE")
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String sufix;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;

	@Transient
	private Integer systemId;

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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getComponentGenerator() {
		return componentGenerator;
	}

	public void setComponentGenerator(String componentGenerator) {
		this.componentGenerator = componentGenerator;
	}

	public String getSufix() {
		return sufix;
	}

	public void setSufix(String sufix) {
		this.sufix = sufix;
	}

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

}
