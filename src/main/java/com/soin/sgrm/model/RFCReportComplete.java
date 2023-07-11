package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Entity
@Table(name = "RFC")
public class RFCReportComplete implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO_PROYECTO")
	private String codeProyect;

	@Column(name = "NUM_SOLICITUD")
	private String rfcNumber;
	
	@Column(name = "RAZON_CAMBIO")
	private String reasonChange;
	
	@Column(name = "FECHA_EJECUCION_INICIO")
	private String requestDateBegin;

	@Column(name = "FECHA_EJECUCION_FINAL")
	private String requestDateFinish;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRIORIDAD", nullable = true)
	private Priority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_IMPACTO", nullable = true)
	private Impact impact;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CAMBIO", nullable = true)
	private TypeChange typeChange;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO", nullable = false)
	private StatusRFC status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SIGES", nullable = false)
	private Siges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SISTEMA", nullable = false)
	private SystemInfo system;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<RFCTracking> tracking = new HashSet<RFCTracking>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "RFC_RELEASE", joinColumns = { @JoinColumn(name = "ID_RFC") }, inverseJoinColumns = {
			@JoinColumn(name = "ID_RELEASE") })
	private Set<Release_RFCFast> releases = new HashSet<>();
	
	// Agregar Motivo
	// Agregar Usuario que lo cambio
	@Transient
	private String systemName;
	
	@Transient
	private String statusName;

	@Transient
	private String priorityName;
	
	@Transient
	private String impactName;
	
	@Transient
	private String sigesName;
	
	@Transient
	private Integer totalObjectsToInstall;
	
	@Transient
	private List<RFCTracking> listTracking;
	
	@Transient 
	private JRBeanCollectionDataSource trackingDataSource;
	
	@Transient
	private JRBeanCollectionDataSource releasesDataSource;
	
	@Transient
	private JRBeanCollectionDataSource objectsDataSource;
	
	
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

	public Integer getTotalObjectsToInstall() {
		return totalObjectsToInstall;
	}

	public void setTotalObjectsToInstall(Integer totalObjectsToInstall) {
		this.totalObjectsToInstall = totalObjectsToInstall;
	}

	public String getUserName() {
		
		if(getUser()!=null) {
			return getUser().getFullName();
		}else {
			return "No hay un usuario seleccionado";
		}
		
	}
	
	public String getStatusName() {
		 
		if(getStatus()!=null) {
			return getStatus().getName();
		}else {
			return "No hay un estado seleccionado";
		}
	}

	public String getImpactName() {
			if(getImpact()!=null) {
			return getImpact().getName();
		}else {
			return "No hay un impacto seleccionado";
		}
	}

	public String getPriorityName() {
	
		if(getPriority()!=null) {
			return getPriority().getName();
		}else {
			return "No hay una prioridad seleccionada ";
		}
	}

	public String getSystemName() {
		
		if(getSystem()!=null) {
			return getSystem().getName();
		}else {
			return "No hay un sistema seleccionado";
		}
	}
	
	public String getRfcNumber() {
		return rfcNumber;
	}

	public void setRfcNumber(String rfcNumber) {
		this.rfcNumber = rfcNumber;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Impact getImpact() {
		return impact;
	}

	public void setImpact(Impact impact) {
		this.impact = impact;
	}

	public TypeChange getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(TypeChange typeChange) {
		this.typeChange = typeChange;
	}

	public StatusRFC getStatus() {
		return status;
	}

	public void setStatus(StatusRFC status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}


	public Siges getSiges() {
		return siges;
	}

	public void setSiges(Siges siges) {
		this.siges = siges;
	}


	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Set<RFCTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<RFCTracking> tracking) {
		this.tracking = tracking;
	}

	public List<RFCTracking> getListTracking() {
		return listTracking;
	}

	public void setListTracking(List<RFCTracking> listTracking) {
		this.listTracking = listTracking;
	}
	
	public JRBeanCollectionDataSource getTrackingDataSource() {
		List<RFCTracking> listTracking=new ArrayList<>();
		
		Set<RFCTracking> tracking =getTracking(); 
		if(tracking.size()>0) {
		for(RFCTracking trackinRFC: tracking) {
				listTracking.add(trackinRFC);
		}
		}else {
			RFCTracking withOutTracking=new RFCTracking();
				withOutTracking.setStatus("Sin tracking previo");
				withOutTracking.setOperator("Sin tracking previo");
				withOutTracking.setMotive("Sin tracking previo");
				withOutTracking.setTrackingDate(null);
				withOutTracking.setStatus("Sin tracking previo");
				listTracking.add(withOutTracking);
			
		}
			JRBeanCollectionDataSource trackingDataSource = new JRBeanCollectionDataSource(listTracking,false);
	       return trackingDataSource;
	   }

	public void setTrackingdataSource(JRBeanCollectionDataSource trackingDataSource) {
		this.trackingDataSource = trackingDataSource;
	}

	public Set<Release_RFCFast> getReleases() {
		return releases;
	}

	public void setReleases(Set<Release_RFCFast> releases) {
		this.releases = releases;
	}

	public JRBeanCollectionDataSource getReleasesDataSource() {
		return releasesDataSource;
	}

	public void setReleasesDataSource(List<?> releasesList) {
		JRBeanCollectionDataSource releasesDataSource = new JRBeanCollectionDataSource(releasesList, false);
		this.releasesDataSource = releasesDataSource;
	}

	public JRBeanCollectionDataSource getObjectsDataSource() {
		return objectsDataSource;
	}

	public void setObjectsDataSource(List<?> objectsList) {
		JRBeanCollectionDataSource objectsDataSource = new JRBeanCollectionDataSource(objectsList, false);
		this.objectsDataSource = objectsDataSource;
	}

	public String getSigesName() {
		return sigesName;
	}

	public void setSigesName(String sigesName) {
		this.sigesName = sigesName;
	}

	public String getRequestDateBegin() {
		return requestDateBegin;
	}

	public void setRequestDateBegin(String requestDateBegin) {
		this.requestDateBegin = requestDateBegin;
	}

	public String getRequestDateFinish() {
		return requestDateFinish;
	}

	public void setRequestDateFinish(String requestDateFinish) {
		this.requestDateFinish = requestDateFinish;
	}

	

}
