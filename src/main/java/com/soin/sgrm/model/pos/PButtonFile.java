package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.utils.Constant;


@Entity
@Table(name = "RELEASES_BOTONEDITARARCHIVO")
public class PButtonFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "releases_botonarchivo_seq")
	@SequenceGenerator(name = "releases_botonarchivo_seq", sequenceName = "releases_botonarchivo_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RELEASE_ID\"", nullable = false)
	private PReleaseUser release;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String description;
	
	@Column(name = "MODULO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String module;


	@Column(name = "HTML_DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String descriptionHtml;

	@Column(name = "ARCHIVO_EDITAR")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String fileEdit;

	@Column(name = "PROPIETARIO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String owner;

	@Column(name = "PERMISOS")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String permissions;

	@Column(name = "SUSTITUIR_VARIABLES")
	private Boolean replaceVariables;

	@Column(name = "DISPONIBILIDAD_USERMIN")
	private Boolean userminAvailability;

	@Column(name = "COMANDO_ANTES_EDITAR")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String commandBeforeEditing;

	@Column(name = "COMANDO_ANTES_SALVAR")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String commandBeforeSaving;

	@Column(name = "COMANDO_DESPUES_EJECUTAR")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String commandBeforeExecuting;

	@Fetch(value = FetchMode.SUBSELECT)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToMany(mappedBy = "button", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<PDetailButtonFile> detailsButtonFiles = new ArrayList<PDetailButtonFile>();

	@Transient
	private String detailFileName;
	@Transient
	private String detailFileDescription;
	@Transient
	private String detailFileType;
	@Transient
	private String detailFileTypeText;
	@Transient
	private String detailFileQuotationMarks;
	@Transient
	private String detailFileRequired;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PReleaseUser getRelease() {
		return release;
	}

	public void setRelease(PReleaseUser release) {
		this.release = release;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	public String getDescriptionHtml() {
		return descriptionHtml;
	}

	public void setDescriptionHtml(String descriptionHtml) {
		this.descriptionHtml = descriptionHtml;
	}

	public String getFileEdit() {
		return fileEdit;
	}

	public void setFileEdit(String fileEdit) {
		this.fileEdit = fileEdit;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public boolean isReplaceVariables() {
		return replaceVariables;
	}

	public void setReplaceVariables(boolean replaceVariables) {
		this.replaceVariables = replaceVariables;
	}

	public boolean isUserminAvailability() {
		return userminAvailability;
	}

	public void setUserminAvailability(boolean userminAvailability) {
		this.userminAvailability = userminAvailability;
	}

	public String getCommandBeforeEditing() {
		return commandBeforeEditing;
	}

	public void setCommandBeforeEditing(String commandBeforeEditing) {
		this.commandBeforeEditing = commandBeforeEditing;
	}

	public String getCommandBeforeSaving() {
		return commandBeforeSaving;
	}

	public void setCommandBeforeSaving(String commandBeforeSaving) {
		this.commandBeforeSaving = commandBeforeSaving;
	}

	public String getCommandBeforeExecuting() {
		return commandBeforeExecuting;
	}

	public void setCommandBeforeExecuting(String commandBeforeExecuting) {
		this.commandBeforeExecuting = commandBeforeExecuting;
	}

	public List<PDetailButtonFile> getDetailsButtonFiles() {
		return detailsButtonFiles;
	}

	public void setDetailsButtonFiles(String detailsButtonFiles) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<PDetailButtonFile> jsonList = Arrays
					.asList(mapper.readValue(detailsButtonFiles, PDetailButtonFile[].class));
			this.detailsButtonFiles = jsonList;
		} catch (Exception e) {
			this.detailsButtonFiles = null;
			Sentry.capture(e, "buttonFile");
		}
	}

	public void setDetailsButtonFiles(List<PDetailButtonFile> detailsButtonFiles) {
		this.detailsButtonFiles = detailsButtonFiles;
	}

	public void updateDetailsButtonFiles(List<PDetailButtonFile> detailsButtonFiles) {
		this.detailsButtonFiles = detailsButtonFiles;
	}

	public boolean existDetail(int id) {
		for (PDetailButtonFile detail : getDetailsButtonFiles()) {
			if (detail.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public String getDetailFileName() {
		return detailFileName;
	}

	public void setDetailFileName(String detailFileName) {
		this.detailFileName = detailFileName;
	}

	public String getDetailFileDescription() {
		return detailFileDescription;
	}

	public void setDetailFileDescription(String detailFileDescription) {
		this.detailFileDescription = detailFileDescription;
	}

	public String getDetailFileType() {
		return detailFileType;
	}

	public void setDetailFileType(String detailFileType) {
		this.detailFileType = detailFileType;
	}

	public String getDetailFileTypeText() {
		return detailFileTypeText;
	}

	public void setDetailFileTypeText(String detailFileTypeText) {
		this.detailFileTypeText = detailFileTypeText;
	}

	public String getDetailFileQuotationMarks() {
		return detailFileQuotationMarks;
	}

	public void setDetailFileQuotationMarks(String detailFileQuotationMarks) {
		this.detailFileQuotationMarks = detailFileQuotationMarks;
	}

	public String getDetailFileRequired() {
		return detailFileRequired;
	}

	public void setDetailFileRequired(String detailFileRequired) {
		this.detailFileRequired = detailFileRequired;
	}
}
