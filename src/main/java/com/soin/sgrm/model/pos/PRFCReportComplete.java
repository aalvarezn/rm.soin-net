package com.soin.sgrm.model.pos;

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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Entity
@Table(name = "RFC")
public class PRFCReportComplete implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfc_seq")
	@SequenceGenerator(name = "rfc_seq", sequenceName = "rfc_id_seq", allocationSize = 1)
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
	@JoinColumn(name = "\"ID_PRIORIDAD\"", nullable = true)
	private PPriority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_IMPACTO\"", nullable = true)
	private PImpact impact;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_CAMBIO\"", nullable = true)
	private PTypeChange typeChange;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatusRFC status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SIGES\"", nullable = false)
	private PSiges siges;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
	private PSystemInfo system;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	

	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<PRFCTracking> tracking = new HashSet<PRFCTracking>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "RFC_RELEASE", joinColumns = { @JoinColumn(name = "\"ID_RFC\"") }, inverseJoinColumns = {
			@JoinColumn(name = "\"ID_RELEASE\"") })
	private Set<PRelease_RFCFast> releases = new HashSet<>();
	
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
	private List<PRFCTracking> listTracking;
	
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

	public PPriority getPriority() {
		return priority;
	}

	public void setPriority(PPriority priority) {
		this.priority = priority;
	}

	public PImpact getImpact() {
		return impact;
	}

	public void setImpact(PImpact impact) {
		this.impact = impact;
	}

	public PTypeChange getTypeChange() {
		return typeChange;
	}

	public void setTypeChange(PTypeChange typeChange) {
		this.typeChange = typeChange;
	}

	public PStatusRFC getStatus() {
		return status;
	}

	public void setStatus(PStatusRFC status) {
		this.status = status;
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


	public PSiges getSiges() {
		return siges;
	}

	public void setSiges(PSiges siges) {
		this.siges = siges;
	}


	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public Set<PRFCTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PRFCTracking> tracking) {
		this.tracking = tracking;
	}

	public List<PRFCTracking> getListTracking() {
		return listTracking;
	}

	public void setListTracking(List<PRFCTracking> listTracking) {
		this.listTracking = listTracking;
	}
	
	public JRBeanCollectionDataSource getTrackingDataSource() {
		List<PRFCTracking> listTracking=new ArrayList<>();
		
		Set<PRFCTracking> tracking =getTracking(); 
		if(tracking.size()>0) {
		for(PRFCTracking trackinRFC: tracking) {
				listTracking.add(trackinRFC);
		}
		}else {
			PRFCTracking withOutTracking=new PRFCTracking();
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

	public Set<PRelease_RFCFast> getReleases() {
		return releases;
	}

	public void setReleases(Set<PRelease_RFCFast> releases) {
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
