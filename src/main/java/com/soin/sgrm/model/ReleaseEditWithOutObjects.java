package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class ReleaseEditWithOutObjects implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_RELEASE_SQ")
	@SequenceGenerator(name = "RELEASES_RELEASE_SQ", sequenceName = "RELEASES_RELEASE_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@Column(name = "DESCRIPCION")
	private String description;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@Column(name = "INCIDENTES")
	private String incidents;

	@Column(name = "PROBLEMAS")
	private String problems;

	@Column(name = "SOLICITUDES_DE_SERVICIO")
	private String serviceRequests;

	@Column(name = "SOPORTE_OPERATIVO_ICE")
	private String operativeSupport;

	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE_DESARROLLADOR")
	private String message;

	// Informacion General
	@Value("${priority:0}")
	@Column(name = "PRIORIDAD_ID", nullable = true)
	private Integer priority;

	@Value("${risk:0}")
	@Column(name = "RIESGO_ID", nullable = true)
	private Integer risk;

	@Value("${impact:0}")
	@Column(name = "IMPACTO_ID", nullable = true)
	private Integer impact;

	// Informacion de la solucion
	@Column(name = "OBSERVACIONES")
	private String observations;

	@Column(name = "SOLUCION_FUNCIONAL")
	private String functionalSolution;

	@Column(name = "SOLUCION_TECNICA")
	private String technicalSolution;

	@Column(name = "NO_INSTALACION")
	private String notInstalling;

	@Column(name = "PRE_CONDICIONES")
	private String preConditions;

	@Column(name = "POST_CONDICIONES")
	private String postConditions;

	// Datos de instalacion
	@Lob
	@Column(name = "INSTRUCCIONES_DE_INSTALACION")
	private String installation_instructions;

	@Lob
	@Column(name = "INSTRUCCIONES_DE_VERIFICACION")
	private String verification_instructions;

	@Lob
	@Column(name = "PLAN_DE_ROLLBACK")
	private String rollback_plan;

	// Tipos de reportes
	@Value("${reportHaveArt:false}")
	@Column(name = "TIENE_ARTE")
	private Boolean reportHaveArt;

	@Value("${reportfixedTelephony:false}")
	@Column(name = "REPORTE_TELEFONIA_FIJA")
	private Boolean reportfixedTelephony;

	@Value("${reportHistoryTables:false}")
	@Column(name = "REPORTE_TABLAS_HISTORICAS")
	private Boolean reportHistoryTables;

	@Value("${reportNotHaveArt:false}")
	@Column(name = "SIN_ARTE")
	private Boolean reportNotHaveArt;

	@Value("${reportMobileTelephony:false}")
	@Column(name = "REPORTE_TELEFONIA_MOVIL")
	private Boolean reportMobileTelephony;

	@Value("${reportTemporaryTables:false}")
	@Column(name = "REPORTE_TABLAS_TEMPORALES")
	private Boolean reportTemporaryTables;

	@Value("${billedCalls:false}")
	@Column(name = "LLAMADAS_FACTURADAS")
	private Boolean billedCalls;

	@Value("${notBilledCalls:false}")
	@Column(name = "LLAMADAS_POR_FACTURAR")
	private Boolean notBilledCalls;

	// Instrucciones de Instalación de Base de Datos
	@Column(name = "INSTRUCCIONES_DE_INSTALACI93F7")
	private String certInstallationInstructions;

	@Column(name = "INSTRUCCIONES_DE_VERIFICACAE52")
	private String certVerificationInstructions;

	@Column(name = "PLAN_DE_ROLLBACK_BASE_DATO51A8")
	private String certRollbackPlan;

	@Column(name = "INSTRUCCIONES_DE_INSTALACI8FEF")
	private String prodInstallationInstructions;

	@Column(name = "INSTRUCCIONES_DE_VERIFICAC2149")
	private String prodVerificationInstructions;

	@Column(name = "PLAN_DE_ROLLBACK_BASE_DATOD9B1")
	private String prodRollbackPlan;

	// Pruebas sugeridas
	@Column(name = "PRUEBAS_MINIMAS_SUGERIDAS_CF17")
	private String minimal_evidence;

	// Definición de Ambientes
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_AMBIENTES", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AMBIENTE_ID") })
	private Set<Ambient> ambients = new HashSet<Ambient>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_COMPONENTD598", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMPONENTEMODIFICADO_ID") })
	private Set<ModifiedComponent> modifiedComponents = new HashSet<ModifiedComponent>();

	// Observaciones
	@Column(name = "OBSERVACIONES_AMBIENTE_PRO6612")
	private String observationsProd;

	@Column(name = "OBSERVACIONES_AMBIENTE_PREQA")
	private String observationsPreQa;

	@Column(name = "OBSERVACIONES_AMBIENTE_QA")
	private String observationsQa;

	@Cascade(CascadeType.SAVE_UPDATE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOLICITADO_POR_ID", nullable = true)
	private UserInfo user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTADO_ID", nullable = true)
	private Status status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_REQUERIMI6CEB", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "REQUERIMIENTO_ID") })
	private Set<Request> requestList = new HashSet<Request>();

	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<Dependency> dependencies;

	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<Crontab> crontabs = new HashSet<Crontab>();

	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<ButtonCommand> buttons = new HashSet<ButtonCommand>();

	@OneToMany(mappedBy = "release", fetch = FetchType.EAGER)
	private Set<ButtonFile> buttonsFile = new HashSet<ButtonFile>();

	@Column(name = "DESCRIPCION_FUNCIONAL")
	private String functionalDescription;


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_ARCHIVOS", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ARCHIVO_ID") })
	private Set<ReleaseFile> files = new HashSet<ReleaseFile>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_ACCION", joinColumns = {
			@JoinColumn(name = "RELEASE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACCION_ID") })
	private Set<ReleaseAction> actions = new HashSet<ReleaseAction>();

	@Value("${down_required:0}")
	@Column(name = "REQUIERE_BAJAR")
	private int down_required;

	@Column(name = "MODULO_ID")
	private int module_id;

	/* Valores por defecto */

	@Value("${is_subRelease:0}")
	@Column(name = "ES_SUBRELEASE")
	private int is_subRelease;

	@Value("${old_nomenclature:0}")
	@Column(name = "NOMENCLATURA_VIEJA")
	private int old_nomenclature;

	@Value("${plans_exject_list:0}")
	@Column(name = "PLANES_EJECUCION_LISTOS")
	private int plans_exject_list;

	@Value("${fixed_telephone_report:0}")
	@Column(name = "SECUENCIA")
	private int sequence;

	@Value("${has_changes_in_bd:0}")
	@Column(name = "TIENE_CAMBIOS_EN_BASE_DE_DATOS")
	private int has_changes_in_bd;

	@Column(name = "NUMEROVERSION")
	private String versionNumber;

	@Value("${retries:0}")
	@Column(name = "REINTENTOS", nullable = false)
	private Integer retries;

	@Column(name = "OPERADOR")
	private String operator;

	@Column(name = "MOTIVO")
	private String motive;

	@Transient
	private String dateChange;
	
	@Transient
	private Timestamp timeNew;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<Request> getRequestList() {
		return requestList;
	}

	public void setRequestList(Set<Request> requestList) {
		this.requestList = requestList;
	}

	public Set<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public Set<Crontab> getCrontabs() {
		return crontabs;
	}

	public void setCrontabs(Set<Crontab> crontabs) {
		this.crontabs = crontabs;
	}

	public Set<ButtonCommand> getButtons() {
		return buttons;
	}

	public void setButtons(Set<ButtonCommand> buttons) {
		this.buttons = buttons;
	}

	public Set<ButtonFile> getButtonsFile() {
		return buttonsFile;
	}

	public void setButtonsFile(Set<ButtonFile> buttonsFile) {
		this.buttonsFile = buttonsFile;
	}

	public String getFunctionalDescription() {
		return functionalDescription;
	}

	public void setFunctionalDescription(String functionalDescription) {
		this.functionalDescription = functionalDescription;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getIncidents() {
		return incidents;
	}

	public void setIncidents(String incidents) {
		this.incidents = incidents;
	}

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}

	public String getServiceRequests() {
		return serviceRequests;
	}

	public void setServiceRequests(String serviceRequests) {
		this.serviceRequests = serviceRequests;
	}

	public String getOperativeSupport() {
		return operativeSupport;
	}

	public void setOperativeSupport(String operativeSupport) {
		this.operativeSupport = operativeSupport;
	}

	public Set<ReleaseFile> getFiles() {
		return files;
	}

	public void setFiles(Set<ReleaseFile> files) {
		this.files = files;
	}


	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getRisk() {
		return risk;
	}

	public void setRisk(Integer risk) {
		this.risk = risk;
	}

	public Integer getImpact() {
		return impact;
	}

	public void setImpact(Integer impact) {
		this.impact = impact;
	}

	public String getPreConditions() {
		return preConditions;
	}

	public void setPreConditions(String preConditions) {
		this.preConditions = preConditions;
	}

	public String getPostConditions() {
		return postConditions;
	}

	public void setPostConditions(String postConditions) {
		this.postConditions = postConditions;
	}

	public String getInstallation_instructions() {
		return installation_instructions;
	}

	public void setInstallation_instructions(String installation_instructions) {
		this.installation_instructions = installation_instructions;
	}

	public String getVerification_instructions() {
		return verification_instructions;
	}

	public void setVerification_instructions(String verification_instructions) {
		this.verification_instructions = verification_instructions;
	}

	public String getRollback_plan() {
		return rollback_plan;
	}

	public void setRollback_plan(String rollback_plan) {
		this.rollback_plan = rollback_plan;
	}

	public Boolean getReportHaveArt() {
		return reportHaveArt;
	}

	public void setReportHaveArt(Boolean reportHaveArt) {
		this.reportHaveArt = reportHaveArt;
	}

	public Boolean getReportfixedTelephony() {
		return reportfixedTelephony;
	}

	public void setReportfixedTelephony(Boolean reportfixedTelephony) {
		this.reportfixedTelephony = reportfixedTelephony;
	}

	public Boolean getReportHistoryTables() {
		return reportHistoryTables;
	}

	public void setReportHistoryTables(Boolean reportHistoryTables) {
		this.reportHistoryTables = reportHistoryTables;
	}

	public Boolean getReportNotHaveArt() {
		return reportNotHaveArt;
	}

	public void setReportNotHaveArt(Boolean reportNotHaveArt) {
		this.reportNotHaveArt = reportNotHaveArt;
	}

	public Boolean getReportMobileTelephony() {
		return reportMobileTelephony;
	}

	public void setReportMobileTelephony(Boolean reportMobileTelephony) {
		this.reportMobileTelephony = reportMobileTelephony;
	}

	public Boolean getReportTemporaryTables() {
		return reportTemporaryTables;
	}

	public void setReportTemporaryTables(Boolean reportTemporaryTables) {
		this.reportTemporaryTables = reportTemporaryTables;
	}

	public Boolean getBilledCalls() {
		return billedCalls;
	}

	public void setBilledCalls(Boolean billedCalls) {
		this.billedCalls = billedCalls;
	}

	public Boolean getNotBilledCalls() {
		return notBilledCalls;
	}

	public void setNotBilledCalls(Boolean notBilledCalls) {
		this.notBilledCalls = notBilledCalls;
	}

	public String getCertInstallationInstructions() {
		return certInstallationInstructions;
	}

	public void setCertInstallationInstructions(String certInstallationInstructions) {
		this.certInstallationInstructions = certInstallationInstructions;
	}

	public String getCertVerificationInstructions() {
		return certVerificationInstructions;
	}

	public void setCertVerificationInstructions(String certVerificationInstructions) {
		this.certVerificationInstructions = certVerificationInstructions;
	}

	public String getCertRollbackPlan() {
		return certRollbackPlan;
	}

	public void setCertRollbackPlan(String certRollbackPlan) {
		this.certRollbackPlan = certRollbackPlan;
	}

	public String getProdInstallationInstructions() {
		return prodInstallationInstructions;
	}

	public void setProdInstallationInstructions(String prodInstallationInstructions) {
		this.prodInstallationInstructions = prodInstallationInstructions;
	}

	public String getProdVerificationInstructions() {
		return prodVerificationInstructions;
	}

	public void setProdVerificationInstructions(String prodVerificationInstructions) {
		this.prodVerificationInstructions = prodVerificationInstructions;
	}

	public String getProdRollbackPlan() {
		return prodRollbackPlan;
	}

	public void setProdRollbackPlan(String prodRollbackPlan) {
		this.prodRollbackPlan = prodRollbackPlan;
	}

	public Set<Ambient> getAmbients() {
		return ambients;
	}

	public void setAmbients(Set<Ambient> ambients) {
		this.ambients = ambients;
	}

	public Set<ModifiedComponent> getModifiedComponents() {
		return modifiedComponents;
	}

	public void setModifiedComponents(Set<ModifiedComponent> modifiedComponents) {
		this.modifiedComponents = modifiedComponents;
	}

	public String getFunctionalSolution() {
		return functionalSolution;
	}

	public void setFunctionalSolution(String functionalSolution) {
		this.functionalSolution = functionalSolution;
	}

	public String getTechnicalSolution() {
		return technicalSolution;
	}

	public void setTechnicalSolution(String technicalSolution) {
		this.technicalSolution = technicalSolution;
	}

	public String getNotInstalling() {
		return notInstalling;
	}

	public void setNotInstalling(String notInstalling) {
		this.notInstalling = notInstalling;
	}

	public String getMinimal_evidence() {
		return minimal_evidence;
	}

	public void setMinimal_evidence(String minimal_evidence) {
		this.minimal_evidence = minimal_evidence;
	}

	public Set<ReleaseAction> getActions() {
		return actions;
	}

	public void setActions(Set<ReleaseAction> actions) {
		this.actions = actions;
	}

	public int getDown_required() {
		return down_required;
	}

	public void setDown_required(int down_required) {
		this.down_required = down_required;
	}

	public String getObservationsProd() {
		return observationsProd;
	}

	public void setObservationsProd(String observationsProd) {
		this.observationsProd = observationsProd;
	}

	public String getObservationsPreQa() {
		return observationsPreQa;
	}

	public void setObservationsPreQa(String observationsPreQa) {
		this.observationsPreQa = observationsPreQa;
	}

	public String getObservationsQa() {
		return observationsQa;
	}

	public void setObservationsQa(String observationsQa) {
		this.observationsQa = observationsQa;
	}

	public int getModule_id() {
		return module_id;
	}

	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}

	public int getIs_subRelease() {
		return is_subRelease;
	}

	public void setIs_subRelease(int is_subRelease) {
		this.is_subRelease = is_subRelease;
	}

	public int getOld_nomenclature() {
		return old_nomenclature;
	}

	public void setOld_nomenclature(int old_nomenclature) {
		this.old_nomenclature = old_nomenclature;
	}

	public int getPlans_exject_list() {
		return plans_exject_list;
	}

	public void setPlans_exject_list(int plans_exject_list) {
		this.plans_exject_list = plans_exject_list;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getHas_changes_in_bd() {
		return has_changes_in_bd;
	}

	public void setHas_changes_in_bd(int has_changes_in_bd) {
		this.has_changes_in_bd = has_changes_in_bd;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void clearObjectsCopy() {
		this.actions = new HashSet<ReleaseAction>();
		this.ambients = new HashSet<Ambient>();
		this.dependencies = new HashSet<Dependency>();
		this.files = new HashSet<ReleaseFile>();
		
		this.requestList = new HashSet<Request>();
	}

	public boolean existDependency(Integer id) {
		Iterator<Dependency> itr = this.dependencies.iterator();
		while (itr.hasNext()) {
			if (itr.next().getId() == id)
				return true;
		}
		return false;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDateChange() {
		return dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public Timestamp getTimeNew() {
		return timeNew;
	}

	public void setTimeNew(Timestamp timeNew) {
		this.timeNew = timeNew;
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
	
}
