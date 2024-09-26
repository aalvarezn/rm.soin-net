package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.utils.ReleaseCreate;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class PRelease implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "releases_release_seq")
	@SequenceGenerator(name = "releases_release_seq", sequenceName = "releases_release_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	// Informacion General
	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PSystemInfo system;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"PRIORIDAD_ID\"", nullable = true)
	private PPriority priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RIESGO_ID\"", nullable = true)
	private PRisk risk;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"IMPACTO_ID\"", nullable = true)
	private PImpact impact;

	@Column(name = "DESCRIPCION")
	private String description;

	// Informacion de la solucion
	@Column(name = "OBSERVACIONES")
	private String observations;

	@Column(name = "SOLUCION_FUNCIONAL")
	private String functionalSolution;

	@Column(name = "SOLUCION_TECNICA")
	private String technicalSolution;

	@Column(name = "NO_INSTALACION")
	private String notInstalling;

	// Definición de Ambientes
	@Column(name = "PRE_CONDICIONES")
	private String preConditions;

	@Column(name = "POST_CONDICIONES")
	private String postConditions;

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
	
	@Column(name = "REMITENTES")
	private String senders;

	@Column(name = "MENSAJE_DESARROLLADOR")
	private String message;


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

	// Observaciones
	@Column(name = "OBSERVACIONES_AMBIENTE_PRO6612")
	private String observationsProd;

	@Column(name = "OBSERVACIONES_AMBIENTE_PREQA")
	private String observationsPreQa;

	@Column(name = "OBSERVACIONES_AMBIENTE_QA")
	private String observationsQa;

	// Pruebas sugeridas
	@Column(name = "PRUEBAS_MINIMAS_SUGERIDAS_CF17")
	private String minimal_evidence;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_OBJETOS", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"OBJETO_ID\"") })
	private Set<PReleaseObject> objects = new HashSet<PReleaseObject>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_AMBIENTES", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"AMBIENTE_ID\"") })
	private Set<PAmbient> ambients = new HashSet<PAmbient>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_COMPONENTE", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"COMPONENTEMODIFICADO_ID\"") })
	private Set<PModifiedComponent> modifiedComponents = new HashSet<PModifiedComponent>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToMany(mappedBy = "release", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<PDependency> dependencies = new HashSet<PDependency>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToMany(mappedBy = "release", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<PCrontab> crontabs = new HashSet<PCrontab>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToMany(mappedBy = "release", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<PButtonCommand> buttons = new HashSet<PButtonCommand>();

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_REQUERIMIENTO", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"REQUERIMIENTO_ID\"") })
	private Set<PRequest> requirements = new HashSet<PRequest>();

	@Column(name = "DESCRIPCION_FUNCIONAL")
	private String functionalDescription;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SOLICITADO_POR_ID\"", nullable = true)
	private PUser user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ESTADO_ID\"", nullable = true)
	private PStatus status;

	@Column(name = "INCIDENTES")
	private String incident;

	@Column(name = "PROBLEMAS")
	private String problem;

	@Column(name = "SOLICITUDES_DE_SERVICIO")
	private String service_requests;

	@Column(name = "SOPORTE_OPERATIVO_ICE")
	private String operative_support;

	@Value("${is_subRelease:0}")
	@Column(name = "ES_SUBRELEASE")
	private int is_subRelease;

	@Value("${old_nomenclature:0}")
	@Column(name = "NOMENCLATURA_VIEJA")
	private int old_nomenclature;

	@Value("${plans_exject_list:0}")
	@Column(name = "PLANES_EJECUCION_LISTOS")
	private int plans_exject_list;

	@Value("${down_required:0}")
	@Column(name = "REQUIERE_BAJAR")
	private int down_required;

	@Value("${fixed_telephone_report:0}")
	@Column(name = "SECUENCIA")
	private int sequence;

	@Value("${has_changes_in_bd:0}")
	@Column(name = "TIENE_CAMBIOS_EN_BASE_DE_DATOS")
	private int has_changes_in_bd;

	@Cascade(CascadeType.SAVE_UPDATE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"MODULO_ID\"", nullable = false)
	private PModule module;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "FECHA_CREACION")
	private Timestamp createDate;

	@Column(name = "NUMEROVERSION")
	private String versionNumber;

	@Column(name = "OPERADOR")
	private String operator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"NODO_ID\"", nullable = true)
	private PNode node;
	
	@Column(name = "MOTIVO")
	private String motive;

	@Column(name = "BUGS")
	private String bugs;
	
	@Transient
	private PNode nodeFinish;
	
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

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public PPriority getPriority() {
		return priority;
	}

	public void setPriority(PPriority priority) {
		this.priority = priority;
	}

	public PRisk getRisk() {
		return risk;
	}

	public void setRisk(PRisk risk) {
		this.risk = risk;
	}

	public PImpact getImpact() {
		return impact;
	}

	public void setImpact(PImpact impact) {
		this.impact = impact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
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

	public Set<PReleaseObject> getObjects() {
		return objects;
	}

	public void setObjects(Set<PReleaseObject> objects) {
		this.objects = objects;
	}

	public void addReleaseObject(PReleaseObject obj) {
		objects.add(obj);
	}

	public Set<PAmbient> getAmbients() {
		return ambients;
	}

	public void setAmbients(Set<PAmbient> ambients) {
		this.ambients = ambients;
	}

	public Set<PModifiedComponent> getModifiedComponents() {
		return modifiedComponents;
	}

	public void setModifiedComponents(Set<PModifiedComponent> modifiedComponents) {
		this.modifiedComponents = modifiedComponents;
	}

	public Set<PDependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<PDependency> dependencies) {
		this.dependencies = dependencies;
	}

	public String getFunctionalDescription() {
		return functionalDescription;
	}

	public void setFunctionalDescription(String functionalDescription) {
		this.functionalDescription = functionalDescription;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}

	public PStatus getStatus() {
		return status;
	}

	public void setStatus(PStatus status) {
		this.status = status;
	}

	public String getIncident() {
		return incident;
	}

	public void setIncident(String incident) {
		this.incident = incident;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getService_requests() {
		return service_requests;
	}

	public void setService_requests(String service_requests) {
		this.service_requests = service_requests;
	}

	public String getOperative_support() {
		return operative_support;
	}

	public void setOperative_support(String operative_support) {
		this.operative_support = operative_support;
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

	public int getDown_required() {
		return down_required;
	}

	public void setDown_required(int down_required) {
		this.down_required = down_required;
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

	public PModule getModule() {
		return module;
	}

	public void setModule(PModule module) {
		this.module = module;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
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

	public String getMinimal_evidence() {
		return minimal_evidence;
	}

	public void setMinimal_evidence(String minimal_evidence) {
		this.minimal_evidence = minimal_evidence;
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

	public Set<PCrontab> getCrontabs() {
		return crontabs;
	}

	public void setCrontabs(Set<PCrontab> crontabs) {
		this.crontabs = crontabs;
	}

	public Set<PButtonCommand> getButtons() {
		return buttons;
	}

	public void setButtons(Set<PButtonCommand> buttons) {
		this.buttons = buttons;
	}

	public Set<PRequest> getRequirements() {
		return requirements;
	}

	public void setRequirements(Set<PRequest> requirements) {
		this.requirements = requirements;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public PReleaseObject getObjectById(Integer id) {
		for (PReleaseObject object : this.objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}

	public boolean haveSql() {
		for (PReleaseObject object : this.objects) {
			if (object.getIsSql() == 1) {
				return true;
			}
		}
		return false;
	}

	public boolean haveExecPlan() {
		for (PReleaseObject object : this.objects) {
			if (object.getIsSql() == 1 && object.getExecutePlan() == 1) {
				return true;
			}
		}
		return false;
	}

	public void checkModifiedComponents(List<PModifiedComponent> news) {
		this.modifiedComponents.retainAll(news);
		// Agrego los nuevos componentes
		for (PModifiedComponent component : news) {
			if (!existModifiedComponent(component.getId())) {
				this.modifiedComponents.add(component);
			}
		}
	}

	public void checkAmbientsExists(List<PAmbient> ambientsNews) {
		this.ambients.retainAll(ambientsNews);
		// Agrego los nuevos ambients
		for (PAmbient ambient : ambientsNews) {
			if (!existAmbient(ambient.getId())) {
				this.ambients.add(ambient);
			}
		}
	}

	public void checkDependenciesExists(Set<PDependency> external) {
		Set<PDependency> toRemove = new HashSet<PDependency>();
		boolean existIn = false;
		for (PDependency in : this.dependencies) {
			existIn = false;
			for (PDependency out : external) {
				if (in.getTo_release().getId() == out.getTo_release().getId()
						&& in.getRelease().getId() == out.getRelease().getId()) {
					in.setIsFunctional(out.getIsFunctional());
					existIn = true;
				}
			}
			if (!existIn) {
				toRemove.add(in);
			}
		}
		this.dependencies.removeAll(toRemove);

		boolean existOut = false;
		for (PDependency out : external) {
			existOut = false;
			for (PDependency in : this.dependencies) {
				if (in.getTo_release().getId() == out.getTo_release().getId()
						&& in.getRelease().getId() == out.getRelease().getId()) {
					existOut = true;
				}
			}
			if (!existOut) {
				this.dependencies.add(out);
			}
		}
	}

	public void retainAllDependencies(Set<PDependency> external) {
		boolean existIn = false;
		PDependency in = null;
		PDependency out = null;
		Iterator<PDependency> itr = this.dependencies.iterator();
		Iterator<PDependency> itr2 = external.iterator();
		while (itr.hasNext()) {
			in = itr.next();
			existIn = false;
			while (itr2.hasNext()) {
				out = itr2.next();
				if (in.getTo_release().getId() == out.getTo_release().getId()
						&& in.getRelease().getId() == out.getRelease().getId()) {
					existIn = true;
				}

			}
			if (!existIn) {
				this.dependencies.remove(in);
			}

		}

	}

	public boolean existAmbient(Integer id) {
		for (PAmbient ambient : this.ambients) {
			if (ambient.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean existModifiedComponent(Integer id) {
		for (PModifiedComponent component : this.modifiedComponents) {
			if (component.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean existObject(String name, Integer type, Integer itemConfId) {
		for (PReleaseObject item : this.objects) {
			if (item.getName().equals(name) && item.getTypeObject().getId() == type
					&& item.getConfigurationItem().getId() == itemConfId) {
				return true;
			}
		}
		return false;
	}

	public boolean existDependency(PDependency dependency, Set<PDependency> origin) {
		for (PDependency item : origin) {
			if (item.getTo_release().getId() == dependency.getTo_release().getId()
					&& item.getRelease().getId() == dependency.getRelease().getId()) {
				return true;
			}
		}
		return false;
	}

	public Boolean equals(PRelease obj) {
		if (this.id == obj.getId() && this.releaseNumber.equals(obj.getReleaseNumber())) {
			return true;
		}
		return false;
	}

	public void addDependency(PDependency dep) {
		this.dependencies.add(dep);
	}

	public Integer getExistDependencyId(PDependency dep) {
		for (PDependency item : this.dependencies) {
			if (item.getTo_release().getReleaseNumber().equals(dep.getTo_release().getReleaseNumber())) {
				return item.getId();
			}
		}
		return 0;
	}

	public boolean isMandatory(PDependency item) {
		for (PDependency dep : this.dependencies) {
			if (dep.getRelease().getId() == item.getRelease().getId()
					&& dep.getTo_release().getId() == item.getTo_release().getId()) {
				return dep.getMandatory();
			}
		}
		return false;
	}

	public void setAttributes(ReleaseCreate rc) throws Exception {

		try {
			// Informacion General
			if (!rc.getImpactId().equals("")) {
				PImpact impact = new PImpact();
				impact.setId(Integer.parseInt(rc.getImpactId()));
				this.impact = impact;
			}

			if (!rc.getRiskId().equals("")) {
				PRisk risk = new PRisk();
				risk.setId(Integer.parseInt(rc.getRiskId()));
				this.risk = risk;
			}

			if (!rc.getPriorityId().equals("")) {
				PPriority priority = new PPriority();
				priority.setId(Integer.parseInt(rc.getPriorityId()));
				this.priority = priority;
			}
			if(rc.getSenders()!=null) {
			if(!rc.getSenders().equals("")){
				if (rc.getSenders().length() < 256) {
					rc.setSenders(rc.getSenders());
				} 
				
				this.senders=rc.getSenders();
			}
			}
			if(rc.getMessage()!=null) {
				if(!rc.getMessage().equals("")){
					if (rc.getMessage().length() < 256) {
						this.message=rc.getMessage();
					} 
					
				}
			}
		
			this.description = rc.getDescription();

			// Tipos de reporte
			this.reportHaveArt = rc.getReportHaveArt();
			this.reportfixedTelephony = rc.getReportfixedTelephony();
			this.reportHistoryTables = rc.getReportHistoryTables();
			this.reportNotHaveArt = rc.getReportNotHaveArt();
			this.reportMobileTelephony = rc.getReportMobileTelephony();
			this.reportTemporaryTables = rc.getReportTemporaryTables();

			this.billedCalls = rc.getBilledCalls();
			this.notBilledCalls = rc.getNotBilledCalls();

			// Informacion de la solucion
			this.functionalSolution = rc.getFunctionalSolution();
			this.technicalSolution = rc.getTechnicalSolution();
			this.notInstalling = rc.getNotInstalling();
			this.observations = rc.getObservations();

			// Definición de Ambiente
			this.preConditions = rc.getPreConditions();
			this.postConditions = rc.getPostConditions();

			// Datos de Instalación
			this.installation_instructions = rc.getInstallationInstructions();
			this.verification_instructions = rc.getVerificationInstructions();
			this.rollback_plan = rc.getRollbackPlan();

			// Instrucciones de Instalación de Base de Datos
			this.certInstallationInstructions = rc.getCertInstallationInstructions();
			this.certVerificationInstructions = rc.getCertVerificationInstructions();
			this.certRollbackPlan = rc.getCertRollbackPlan();
			this.prodInstallationInstructions = rc.getProdInstallationInstructions();
			this.prodVerificationInstructions = rc.getProdVerificationInstructions();
			this.prodRollbackPlan = rc.getProdRollbackPlan();

			// Pruebas sugeridas
			this.minimal_evidence = rc.getMinimalEvidence();

			// Requerimientos
			this.functionalDescription = rc.getFunctionalDescription();

			// Requiere bajar ambientes
			if (rc.getDownRequired() != null) {
				this.down_required = Integer.parseInt(rc.getDownRequired());
			}

			// Observaciones
			this.observationsProd = rc.getObservationsProd();
			this.observationsPreQa = rc.getObservationsPreQa();
			this.observationsQa = rc.getObservationsQa();

			this.plans_exject_list = (rc.haveExecPlan()) ? 1 : 0;
			this.has_changes_in_bd = (rc.haveSql()) ? 1 : 0;

			this.versionNumber = rc.getVersionNumber();

		} catch (Exception e) {
			Sentry.capture(e, "release");
			throw e;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public PNode getNode() {
		return node;
	}

	public void setNode(PNode node) {
		this.node = node;
	}
	
	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public PNode getNodeFinish() {
		return nodeFinish;
	}

	public void setNodeFinish(PNode nodeFinish) {
		this.nodeFinish = nodeFinish;
	}

	public String getBugs() {
		return bugs;
	}

	public void setBugs(String bugs) {
		this.bugs = bugs;
	}

	
}
