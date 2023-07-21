package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BASECONOCIMIENTO")
public class PBaseKnowledge implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUM_ERROR")
	private String numError;

	@Column(name = "PUBLICADO")
	private Boolean publicate;

	@Column(name = "URL_ADJUNTOS")
	private String url;

	@Column(name = "DESCRIPCION")
	private String description;
	
	@Column(name = "DATOS_REQUERIDOS")
	private String dataRequired;

	@Column(name = "NOTAS")
	private String note;


	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "OPERADOR")
	private String operator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_COMPONENTE\"", nullable = true)
	private PComponent component;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatusKnowlege status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;


	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "BASECONO_ARCHIVOBASE", joinColumns = { @JoinColumn(name = "\"BASE_ID\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ARCHIVOBASECONO_ID\"") })
	private Set<PBaseKnowledgeFile> files = new HashSet<>();

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "baseKnowledge")
	private Set<PBaseKnowledgeTracking> tracking = new HashSet<PBaseKnowledgeTracking>();



	@Transient
	private Long componentId;
	
	@Transient
	private Long statusId;
	
	@Transient
	private Integer systemId;
	
	@Transient
	private Long publicateValidate;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getNumError() {
		return numError;
	}

	public void setNumError(String numError) {
		this.numError = numError;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataRequired() {
		return dataRequired;
	}

	public void setDataRequired(String dataRequired) {
		this.dataRequired = dataRequired;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public PComponent getComponent() {
		return component;
	}

	public void setComponent(PComponent component) {
		this.component = component;
	}

	public PStatusKnowlege getStatus() {
		return status;
	}

	public void setStatus(PStatusKnowlege status) {
		this.status = status;
	}

	public Set<PBaseKnowledgeFile> getFiles() {
		return files;
	}

	public void setFiles(Set<PBaseKnowledgeFile> files) {
		this.files = files;
	}

	public Set<PBaseKnowledgeTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PBaseKnowledgeTracking> tracking) {
		this.tracking = tracking;
	}

	public Long getComponentId() {
		return componentId;
	}

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Boolean getPublicate() {
		return publicate;
	}

	public void setPublicate(Boolean publicate) {
		this.publicate = publicate;
	}

	public Long getPublicateValidate() {
		return publicateValidate;
	}

	public void setPublicateValidate(Long publicateValidate) {
		this.publicateValidate = publicateValidate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
