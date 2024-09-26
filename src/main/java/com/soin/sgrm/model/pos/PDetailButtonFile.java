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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.soin.sgrm.model.ButtonFile;


@Entity
@Table(name = "RELEASES_DETALLEEDITARARCHIVO")
public class PDetailButtonFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "releases_detallearchivo_seq")
	@SequenceGenerator(name = "releases_detallearchivo_seq", sequenceName = "releases_detallearchivo_id_seq", allocationSize = 1)
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
	@JoinColumn(name = "\"BOTON_ID\"", nullable = false)
	private PButtonFile button;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TIPO_ID\"", nullable = true)
	private PTypeDetail typeDetail;

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

	public PButtonFile getButton() {
		return button;
	}

	public void setButton(PButtonFile button) {
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

	public PTypeDetail getTypeDetail() {
		return typeDetail;
	}

	public void setTypeDetail(PTypeDetail typeDetail) {
		this.typeDetail = typeDetail;
	}

}
