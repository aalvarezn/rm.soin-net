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
@Table(name = "SOLICITUD")
public class PRequestBase implements Serializable  {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO_PROYECTO")
	private String codeProyect;

	@Column(name = "NUM_SOLICITUD")
	private String numRequest;
	
	@Column(name = "OPERADOR")
	private String operator;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE_GESTOR")
	private String message;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUDTIPO\"", nullable = false)
	private PTypePetition typePetition;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatusRequest status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SIGES\"", nullable = false)
	private PSiges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
	private PSystemInfo systemInfo;

	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "SOLICITUD_ARCHIVOSOLICITUD", joinColumns = { @JoinColumn(name = "\"ID_SOLICITUD\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ARCHIVO_ID\"") })
	private Set<PRequestBaseFile> files = new HashSet<>();
	
	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<PRequestBaseTracking> tracking = new HashSet<PRequestBaseTracking>();
	
	
	@Transient
	private Long typePetitionId;

	@Transient
	private Integer systemId;
	
	@Transient
	private String codeOpportunity;
	
	@Transient
	private Long codeSigesId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeProyect() {
		return codeProyect;
	}

	public void setCodeProyect(String codeProyect) {
		this.codeProyect = codeProyect;
	}

	public String getNumRequest() {
		return numRequest;
	}

	public void setNumRequest(String numRequest) {
		this.numRequest = numRequest;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public PTypePetition getTypePetition() {
		return typePetition;
	}

	public void setTypePetition(PTypePetition typePetition) {
		this.typePetition = typePetition;
	}

	public PStatusRequest getStatus() {
		return status;
	}

	public void setStatus(PStatusRequest status) {
		this.status = status;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public PSiges getSiges() {
		return siges;
	}

	public void setSiges(PSiges siges) {
		this.siges = siges;
	}

	public PSystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(PSystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public Long getTypePetitionId() {
		return typePetitionId;
	}

	public void setTypePetitionId(Long typePetitionId) {
		this.typePetitionId = typePetitionId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public Set<PRequestBaseTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PRequestBaseTracking> tracking) {
		this.tracking = tracking;
	}

	public String getSenders() {
		return senders;
	}

	public void setSenders(String senders) {
		this.senders = senders;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<PRequestBaseFile> getFiles() {
		return files;
	}

	public void setFiles(Set<PRequestBaseFile> files) {
		this.files = files;
	}

	public String getCodeOpportunity() {
		return codeOpportunity;
	}

	public void setCodeOpportunity(String codeOpportunity) {
		this.codeOpportunity = codeOpportunity;
	}

	public Long getCodeSigesId() {
		return codeSigesId;
	}

	public void setCodeSigesId(Long codeSigesId) {
		this.codeSigesId = codeSigesId;
	}

	
	

}
