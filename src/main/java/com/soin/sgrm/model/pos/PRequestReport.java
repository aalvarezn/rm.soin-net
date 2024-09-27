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
@Table(name = "SOLICITUD")
public class PRequestReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitud_seq")
	@SequenceGenerator(name = "solicitud_seq", sequenceName = "solicitud_seq", allocationSize = 1)
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
	@JoinColumn(name = "\"ID_USUARIO\"", nullable = false)
	private PUser user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SOLICITUDTIPO\"", nullable = false)
	private PTypePetition typePetition;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_SISTEMA\"", nullable = false)
	private PSystemInfo system;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHASOLICITUD")
	private Timestamp requestDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_ESTADO\"", nullable = false)
	private PStatusRequest status;
	
	@OrderBy("trackingDate ASC")
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rfc")
	private Set<PRequestBaseTracking> tracking = new HashSet<PRequestBaseTracking>();
	@Transient 
	private JRBeanCollectionDataSource trackingDataSource;
	
	@Transient 
	private JRBeanCollectionDataSource r4DataSource;
	

	@Transient
	private Long typePetitionId;

	@Transient
	private Integer systemId;

	@Transient
	private String codeOpportunity;
	
	@Transient
	private Integer typePetitionNum;

	@Transient
	private String systemName;

	@Transient
	private String typePetitionName;

	@Transient
	private String codeProyectName;
	
	@Transient
	private String statusName;

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

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
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

	public String getUserName() {

		if (getUser() != null) {
			return getUser().getFullName();
		} else {
			return "No hay un usuario seleccionado";
		}

	}
	
	public PStatusRequest getStatus() {
		return status;
	}

	public void setStatus(PStatusRequest status) {
		this.status = status;
	}

	public String getStatusName() {

		if (getStatus() != null) {
			return getStatus().getName();
		} else {
			return "No hay un estado seleccionado";
		}

	}


	public String getTypePetitionName() {
		if (getTypePetition() != null) {
			return getTypePetition().getCode();
		} else {
			return "No hay un tipo seleccionado";
		}
	}

	public String getCodeProyectName() {

		if (getCodeProyect() != null) {
			return getCodeProyect();
		} else {
			return "No hay una codigo seleccionado ";
		}
	}

	public String getSystemName() {

		if (getSystem() != null) {
			return getSystem().getName();
		} else {
			return "No hay un sistema seleccionado";
		}
	}

	public Integer getTypePetitionNum() {
		return typePetitionNum;
	}

	public void setTypePetitionNum(Integer typePetitionNum) {
		this.typePetitionNum = typePetitionNum;
	}

	public JRBeanCollectionDataSource getTrackingDataSource() {
		
			List<PRequestBaseTracking> listTracking=new ArrayList<>();
			
			Set<PRequestBaseTracking> tracking =getTracking(); 
			if(tracking.size()>0) {
			for(PRequestBaseTracking trackinRequest: tracking) {
					listTracking.add(trackinRequest);
			}
			}else {
				PRequestBaseTracking withOutTracking=new PRequestBaseTracking();
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

	public void setTrackingDataSource(JRBeanCollectionDataSource trackingDataSource) {
		this.trackingDataSource = trackingDataSource;
	}

	public JRBeanCollectionDataSource getR4DataSource() {
		return r4DataSource;
	}

	public void setR4DataSource(List<?> userList) {
		JRBeanCollectionDataSource r4DataSource = new JRBeanCollectionDataSource(userList, false);
		this.r4DataSource = r4DataSource;

	}

	public Set<PRequestBaseTracking> getTracking() {
		return tracking;
	}

	public void setTracking(Set<PRequestBaseTracking> tracking) {
		this.tracking = tracking;
	}
	
	
}
