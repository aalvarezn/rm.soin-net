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

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SISTEMAS_CONFIGURATION")
public class PSystemConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private PSystemInfo system;

	@Column(name = "INFORMACION_GENERAL")
	private boolean generalInfo;

	@Column(name = "INFORMACION_SOLUCION")
	private boolean solutionInfo;

	@Column(name = "DEFINICION_AMBIENTES")
	private boolean definitionEnvironment;

	@Column(name = "DATOS_INSTALACION")
	private boolean instalationData;

	@Column(name = "INSTRUCCIONES_BASEDATOS")
	private boolean dataBaseInstructions;

	@Column(name = "AMBIENTES_BAJAR")
	private boolean downEnvironment;

	@Column(name = "OBSERVACIONES_AMBIENTES")
	private boolean environmentObservations;

	@Column(name = "PRUEBAS_SUGERIDAS")
	private boolean suggestedTests;

	@Column(name = "ITEMS_CONFIGURACION")
	private boolean configurationItems;

	@Column(name = "DEPENDENCIAS")
	private boolean dependencies;

	@Column(name = "ARCHIVOS_ADJUNTOS")
	private boolean attachmentFiles;

	@Column(name = "VERSIONAPLICACION")
	private boolean applicationVersion;
	
	@Column(name = "OBSERVACIONES")
	private boolean observations;

	@Transient
	private Integer systemId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PSystemInfo getSystem() {
		return system;
	}

	public void setSystem(PSystemInfo system) {
		this.system = system;
	}

	public boolean getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(boolean generalInfo) {
		this.generalInfo = generalInfo;
	}

	public boolean getSolutionInfo() {
		return solutionInfo;
	}

	public void setSolutionInfo(boolean solutionInfo) {
		this.solutionInfo = solutionInfo;
	}

	public boolean getDefinitionEnvironment() {
		return definitionEnvironment;
	}

	public void setDefinitionEnvironment(boolean definitionEnvironment) {
		this.definitionEnvironment = definitionEnvironment;
	}

	public boolean getInstalationData() {
		return instalationData;
	}

	public void setInstalationData(boolean instalationData) {
		this.instalationData = instalationData;
	}

	public boolean getDataBaseInstructions() {
		return dataBaseInstructions;
	}

	public void setDataBaseInstructions(boolean dataBaseInstructions) {
		this.dataBaseInstructions = dataBaseInstructions;
	}

	public boolean getDownEnvironment() {
		return downEnvironment;
	}

	public void setDownEnvironment(boolean downEnvironment) {
		this.downEnvironment = downEnvironment;
	}

	public boolean getEnvironmentObservations() {
		return environmentObservations;
	}

	public void setEnvironmentObservations(boolean environmentObservations) {
		this.environmentObservations = environmentObservations;
	}

	public boolean getSuggestedTests() {
		return suggestedTests;
	}

	public void setSuggestedTests(boolean suggestedTests) {
		this.suggestedTests = suggestedTests;
	}

	public boolean getConfigurationItems() {
		return configurationItems;
	}

	public void setConfigurationItems(boolean configurationItems) {
		this.configurationItems = configurationItems;
	}

	public boolean getDependencies() {
		return dependencies;
	}

	public void setDependencies(boolean dependencies) {
		this.dependencies = dependencies;
	}

	public boolean getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(boolean attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public boolean isApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(boolean applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public boolean getObservations() {
		return observations;
	}

	public void setObservations(boolean observations) {
		this.observations = observations;
	}
	
	

}
