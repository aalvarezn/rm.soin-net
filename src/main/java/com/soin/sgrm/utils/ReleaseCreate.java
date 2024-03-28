package com.soin.sgrm.utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReleaseObject;

public class ReleaseCreate {

	private String release_id;
	private String releaseNumber;
	private String systemCode;
	private String impactId;
	private String riskId;
	private String priorityId;
	private String description;
	private String senders;
	private String message;
	private String bugs;

	// Tipos de reportes
	private Boolean reportHaveArt;
	private Boolean reportfixedTelephony;
	private Boolean reportHistoryTables;
	private Boolean reportNotHaveArt;
	private Boolean reportMobileTelephony;
	private Boolean reportTemporaryTables;
	private Boolean billedCalls;
	private Boolean notBilledCalls;

	private String functionalSolution;
	private String technicalSolution;
	private String notInstalling;
	private String observations;
	private List<Integer> dependenciesFunctionals;
	private List<Integer> dependenciesTechnical;
	private String functionalDescription;
	private List<Integer> ambient;
	private List<Integer> modifiedComponent;
	private String preConditions;
	private String postConditions;
	private String installationInstructions;
	private String verificationInstructions;
	private String rollbackPlan;
	private String certInstallationInstructions;
	private String certVerificationInstructions;
	private String certRollbackPlan;
	private String prodInstallationInstructions;
	private String prodVerificationInstructions;
	private String prodRollbackPlan;
	private String downRequired;
	private List<Integer> actions;
	private String observationsProd;
	private String observationsPreQa;
	private String observationsQa;
	private String minimalEvidence;
	private List<ItemObject> objectItemConfiguration;
	private String versionNumber;

	public String getRelease_id() {
		return release_id;
	}

	public void setRelease_id(String release_id) {
		this.release_id = release_id;
	}

	public String getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getImpactId() {
		return impactId;
	}

	public void setImpactId(String impactId) {
		this.impactId = impactId;
	}

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getPriorityId() {
		return priorityId;
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

	public void setPriorityId(String priorityId) {
		this.priorityId = priorityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public List<Integer> getDependenciesFunctionals() {
		return dependenciesFunctionals;
	}

	public void setDependenciesFunctionals(String dependenciesFunctionals) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(dependenciesFunctionals, new TypeReference<List<Integer>>() {
			});
			this.dependenciesFunctionals = jsonList;
		} catch (Exception e) {
			this.dependenciesFunctionals = null;
			Sentry.capture(e, "releaseCreate");
		}
	}

	public List<Integer> getDependenciesTechnical() {
		return dependenciesTechnical;
	}

	public void setDependenciesTechnical(String dependenciesTechnical) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(dependenciesTechnical, new TypeReference<List<Integer>>() {
			});
			this.dependenciesTechnical = jsonList;
		} catch (Exception e) {
			this.dependenciesTechnical = null;
			Sentry.capture(e, "releaseCreate");
		}
	}

	public String getFunctionalDescription() {
		return functionalDescription;
	}

	public void setFunctionalDescription(String functionalDescription) {
		this.functionalDescription = functionalDescription;
	}

	public List<Integer> getAmbient() {
		return ambient;
	}

	public void setAmbient(String ambient) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(ambient, new TypeReference<List<Integer>>() {
			});
			this.ambient = jsonList;
		} catch (Exception e) {
			this.ambient = null;
			Sentry.capture(e, "releaseCreate");
		}
	}

	public List<Integer> getModifiedComponent() {
		return modifiedComponent;
	}

	public void setModifiedComponent(String modifiedComponent) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(modifiedComponent, new TypeReference<List<Integer>>() {
			});
			this.modifiedComponent = jsonList;
		} catch (Exception e) {
			this.modifiedComponent = null;
			Sentry.capture(e, "releaseCreate");
		}
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

	public String getInstallationInstructions() {
		return installationInstructions;
	}

	public void setInstallationInstructions(String installationInstructions) {
		this.installationInstructions = installationInstructions;
	}

	public String getVerificationInstructions() {
		return verificationInstructions;
	}

	public void setVerificationInstructions(String verificationInstructions) {
		this.verificationInstructions = verificationInstructions;
	}

	public String getRollbackPlan() {
		return rollbackPlan;
	}

	public void setRollbackPlan(String rollbackPlan) {
		this.rollbackPlan = rollbackPlan;
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

	public String getDownRequired() {
		return downRequired;
	}

	public void setDownRequired(String downRequired) {
		this.downRequired = downRequired;
	}

	public List<Integer> getActions() {
		return actions;
	}

	public void setActions(String actions) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> jsonList = mapper.readValue(actions, new TypeReference<List<Integer>>() {
			});
			this.actions = jsonList;
		} catch (Exception e) {
			this.actions = null;
			Sentry.capture(e, "releaseCreate");
		}
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

	public String getMinimalEvidence() {
		return minimalEvidence;
	}

	public void setMinimalEvidence(String minimalEvidence) {
		this.minimalEvidence = minimalEvidence;
	}

	public List<ItemObject> getObjectItemConfiguration() {
		return objectItemConfiguration;
	}

	public void setObjectItemConfiguration(String objectItemConfiguration) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<ItemObject> jsonList = mapper.readValue(objectItemConfiguration,
					new TypeReference<List<ItemObject>>() {
					});
			this.objectItemConfiguration = jsonList;
		} catch (Exception e) {
			this.objectItemConfiguration = null;
			Sentry.capture(e, "releaseCreate");
		}
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	// Se valida Impacto, Riesgo, Prioridad y Descripcion
	public ArrayList<MyError> validEmailInformation(ReleaseCreate rc, ArrayList<MyError> errors) {
		if (rc.getSenders() != null) {
			if (rc.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(rc.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (rc.getMessage() != null) {
			if (rc.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}
		return errors;
	}

	public MyError getErrorSenders(String senders) {

		String[] listSenders = senders.split(",");
		String to_invalid = "";
		for (int i = 0; i < listSenders.length; i++) {
			if (!CommonUtils.isValidEmailAddress(listSenders[i])) {
				if (to_invalid.equals("")) {
					to_invalid += listSenders[i];
				} else {
					to_invalid += "," + listSenders[i];
				}

			}
		}
		if (!to_invalid.equals("")) {
			return new MyError("senders", "dirección(es) inválida(s) " + to_invalid);
		}
		return null;
	}

	// Se valida Impacto, Riesgo, Prioridad y Descripcion
	public ArrayList<MyError> validGeneralInformation(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getImpactId().equals(""))
			errors.add(new MyError("impactId", "Valor requerido."));

		if (rc.getRiskId().equals(""))
			errors.add(new MyError("riskId", "Valor requerido."));

		if (rc.getPriorityId().equals(""))
			errors.add(new MyError("priorityId", "Valor requerido."));

		if (rc.getDescription().equals(""))
			errors.add(new MyError("description", "Valor requerido."));

		return errors;
	}

	// Se valida observations
	public ArrayList<MyError> validObservations(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getObservations().equals(""))
			errors.add(new MyError("observations", "Valor requerido."));

		return errors;
	}

	// Se valida technicalSolution, functionalSolution, notInstalling
	public ArrayList<MyError> validInformationSolution(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getTechnicalSolution().equals(""))
			errors.add(new MyError("technicalSolution", "Valor requerido."));

		if (rc.getFunctionalSolution().equals(""))
			errors.add(new MyError("functionalSolution", "Valor requerido."));

		if (rc.getNotInstalling().equals(""))
			errors.add(new MyError("notInstalling", "Valor requerido."));

		return errors;
	}

	// Se valida ambient, preConditions y postConditions
	public ArrayList<MyError> validAmbientDefinition(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getAmbient().size() == 0)
			errors.add(new MyError("ambient", "Valor requerido."));

		if (rc.getPreConditions().equals(""))
			errors.add(new MyError("preConditions", "Valor requerido."));

		if (rc.getPostConditions().equals(""))
			errors.add(new MyError("postConditions", "Valor requerido."));

		return errors;
	}

	// Se valida ambient, preConditions y postConditions
	public ArrayList<MyError> validModifiedComponentDefinition(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getModifiedComponent().size() == 0)
			errors.add(new MyError("modifiedComponent", "Valor requerido."));
		return errors;
	}

	// Se valida installationInstructions, verificationInstructions y rollbackPlan
	public ArrayList<MyError> validInstalationData(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getInstallationInstructions().equals(""))
			errors.add(new MyError("installationInstructions", "Valor requerido."));

		if (rc.getVerificationInstructions().equals(""))
			errors.add(new MyError("verificationInstructions", "Valor requerido."));

		if (rc.getRollbackPlan().equals(""))
			errors.add(new MyError("rollbackPlan", "Valor requerido."));

		return errors;
	}

	// Se valida certInstallationInstructions, certVerificationInstructions,
	// certRollbackPlan, prodInstallationInstructions, prodVerificationInstructions
	// y prodRollbackPlan
	public ArrayList<MyError> validDataBaseInstalation(ReleaseCreate rc, ArrayList<MyError> errors) {

		// Certificacion
		if (rc.getCertInstallationInstructions().equals(""))
			errors.add(new MyError("certInstallationInstructions", "Valor requerido."));

		if (rc.getCertVerificationInstructions().equals(""))
			errors.add(new MyError("certVerificationInstructions", "Valor requerido."));

		if (rc.getCertRollbackPlan().equals(""))
			errors.add(new MyError("certRollbackPlan", "Valor requerido."));

		// Produccion
		if (rc.getProdInstallationInstructions().equals(""))
			errors.add(new MyError("prodInstallationInstructions", "Valor requerido."));

		if (rc.getProdVerificationInstructions().equals(""))
			errors.add(new MyError("prodVerificationInstructions", "Valor requerido."));

		if (rc.getProdRollbackPlan().equals(""))
			errors.add(new MyError("prodRollbackPlan", "Valor requerido."));

		return errors;
	}

	// Se valida minimalEvidence
	public ArrayList<MyError> validMinimalEvidence(ReleaseCreate rc, ArrayList<MyError> errors) {
		if (rc.getMinimalEvidence().equals(""))
			errors.add(new MyError("minimalEvidence", "Valor requerido."));

		return errors;
	}

	// Se valida objetos SQL
	public ArrayList<MyError> validSqlObject(ReleaseCreate rc, ArrayList<MyError> errors) {

		for (ItemObject object : rc.getObjectItemConfiguration()) {

			if (object.getIsSql().equals("1")) {
				if(object.getExecute().equals("1")) {
					if (object.getDbScheme().equals("")) {
						errors.add(new MyError("form-tags-" + object.getId(), "Valor requerido."));
					}
				}

			}

		}
		return errors;
	}

	// Se valida actions
	public ArrayList<MyError> validActions(ReleaseCreate rc, ArrayList<MyError> errors) {

		if (rc.getDownRequired().equals("1")) {
			if (rc.getActions().size() == 0) {
				errors.add(new MyError("actionsTable", "Valor requerido."));
			}
		}
		return errors;
	}

	// Se valida observationsProd, observationsPreQa, observationsQa
	public ArrayList<MyError> validEnvironmentObservations(ReleaseCreate rc, ArrayList<MyError> errors) {
		if (rc.observationsProd.equals(""))
			errors.add(new MyError("observationsProd", "Valor requerido."));
		if (rc.observationsPreQa.equals(""))
			errors.add(new MyError("observationsPreQa", "Valor requerido."));
		if (rc.observationsQa.equals(""))
			errors.add(new MyError("observationsQa", "Valor requerido."));

		return errors;
	}

	public boolean haveSql() {
		if (this.objectItemConfiguration != null) {
			for (ItemObject object : this.objectItemConfiguration) {
				if (object.getIsSql() != null) {
					if (object.getIsSql().equals("1")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean haveExecPlan() {
		if (this.objectItemConfiguration != null) {
			for (ItemObject object : this.objectItemConfiguration) {
				if (object.getExecutePlan() != null) {
					if (object.getExecutePlan().equals("1")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getBugs() {
		return bugs;
	}

	public void setBugs(String bugs) {
		this.bugs = bugs;
	}
	
}
