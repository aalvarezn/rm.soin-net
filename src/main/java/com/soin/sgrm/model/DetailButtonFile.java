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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.soin.sgrm.model.ButtonFile;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_DETALLEEDITARARCHIVO")
public class DetailButtonFile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_DETALLEEDITARA26F6_SQ")
	@SequenceGenerator(name = "RELEASES_DETALLEEDITARA26F6_SQ", sequenceName = "RELEASES_DETALLEEDITARA26F6_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "ES_REQUERIDO")
	private Boolean isRequired;

	@Column(name = "COMILLAS")
	private Boolean quotationMarks;

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BOTON_ID", nullable = false)
	private ButtonFile button;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TIPO_ID", nullable = true)
	private TypeDetail typeDetail;

	@Transient
	private String typeName;

	@Column(name = "TIPO_TEXTO")
	private String typeText;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public Boolean getQuotationMarks() {
		return quotationMarks;
	}

	public void setQuotationMarks(Boolean quotationMarks) {
		this.quotationMarks = quotationMarks;
	}

	public ButtonFile getButton() {
		return button;
	}

	public void setButton(ButtonFile button) {
		this.button = button;
	}

	public void setRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public TypeDetail getTypeDetail() {
		return typeDetail;
	}

	public void setTypeDetail(TypeDetail typeDetail) {
		this.typeDetail = typeDetail;
	}

}
