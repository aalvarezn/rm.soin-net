package com.soin.sgrm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "GENERADOR_DOCS_TEMPLATEGEN54D5")
public class DocsTemplate {
	
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="NOMBRE")
	private String name;
	
	@Column(name="NOMBRE_TEMPLATE")
	private String templateName;
	
	@Column(name="COMPONENTE_GENERADOR_WORD")
	private String componentGenerator;
	
	@Column(name="SUFIJO_EN_NOMBRE")
	private String sufix;
	
	@Column(name="SISTEMA_ID")
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

	public int getSystem_id() {
		return system_id;
	}

	public void setSystem_id(int system_id) {
		this.system_id = system_id;
	}

}
