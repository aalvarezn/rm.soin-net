package com.soin.sgrm.model;

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
public class RequestBaseReport implements Serializable  {
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

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SOLICITUDTIPO", nullable = false)
	private TypePetition typePetition;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO", nullable = false)
	private StatusRequest status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SISTEMA", nullable = false)
	private SystemInfo systemInfo;

	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	
	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<RequestBaseTracking> tracking = new HashSet<RequestBaseTracking>();
	
	
	@Transient
	private Long typePetitionId;

	@Transient
	private Integer systemId;
	
	@Transient
	private Integer typePetitionNum;
	
	@Transient
	private String codeOpportunity;
	
	@Transient
	private String userName;
	
	@Transient
	private String typePetitionName;
	
	@Transient
	private String systemName;


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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TypePetition getTypePetition() {
		return typePetition;
	}

	public void setTypePetition(TypePetition typePetition) {
		this.typePetition = typePetition;
	}

	public StatusRequest getStatus() {
		return status;
	}

	public void setStatus(StatusRequest status) {
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
	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
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

	public Set<RequestBaseTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<RequestBaseTracking> tracking) {
		this.tracking = tracking;
	}

	public String getCodeOpportunity() {
		return codeOpportunity;
	}

	public void setCodeOpportunity(String codeOpportunity) {
		this.codeOpportunity = codeOpportunity;
	}

	public String getStatusName() {
		 
		if(getStatus()!=null) {
			return getStatus().getName();
		}else {
			return "No hay un estado seleccionado";
		}
	}
	
	

	public Integer getTypePetitionNum() {
		return typePetitionNum;
	}

	public void setTypePetitionNum(Integer typePetitionNum) {
		this.typePetitionNum = typePetitionNum;
	}

	public String getUserName() {
		if(getUser()!=null) {
		return getUser().getFullName();
	}else {
		return "No hay un usuario asignado";
	}
	}

	public String getTypePetitionName() {
		
		if(getTypePetition()!=null) {
		return getTypePetition().getCode();
	}else {
		return "No hay un tipo de peticion seleccionado";
	}
		
	}

	public String getSystemName() {
		if(getSystemInfo()!=null) {
		return getSystemInfo().getName();
	}else {
		return "No hay un sistema seleccionado";
	}
	}

	


	

}