package com.soin.sgrm.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.ReleaseAction;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.SystemService;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.*;

import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.DetailButtonFile;
import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.ButtonFile;

public class DocxVariables {

	Variables variables = null;

	public DocxVariables() {
		variables = new Variables();
	}

	public Variables getVariables() {
		return variables;
	}

	public void setVariables(Variables variables) {
		this.variables = variables;
	}

	public void addVariable(String pattern, String data) {
		variables.addTextVariable(new TextVariable(pattern, data));
	}

	/**
	 * @description: Completa variables generales de un documento de release.
	 * @author: Esteban Bogantes H.
	 * @return: Variables generales de un release.
	 **/
	@SuppressWarnings("rawtypes")
	public void generalInfo(ReleaseSummary release, SystemService systemService) throws Exception {
		SystemModule module = systemService.findModuleBySystem(release.getSystem().getId());
		String user = release.getUser().getFullName();
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(release.getCreateDate());

		addVariable("{{fecha_documento}}", timeStamp);
		addVariable("{{numero_release}}", release.getReleaseNumber());
		addVariable("{{sistema}}", release.getSystem().getName());
		addVariable("{{modulo}}", module.getName());

		if (release.getRequestList().size() > 0) {
			String manager = release.getManager();
			addVariable("{{gestor}}", (manager != null ? manager : ""));
		} else {
			if (release.getSystem().getManager() != null) {
				addVariable("{{gestor}}", release.getSystem().getManager());
			} else {
				if (release.getIncidents() != null) {
					addVariable("{{gestor}}", "Randall Sanchez");
				} else {
					if (release.getOperativeSupport() != null) {
						addVariable("{{gestor}}", "Soporte Operativo");
					} else {
						addVariable("{{gestor}}", "");
					}
				}
			}
		}

		addVariable("{{responsable_release}}", user);
		addVariable("{{lider_tecnico}}", (release.getSystem().getLeader().getFullName()));
		addVariable("{{requiere_bajar}}", ((release.isDown_required()) ? "X" : ""));

		if (release.getImpact() != null) {
			addVariable("{{impacto_Bajo}}", (release.getImpact().getName().equals("Bajo") ? "X" : ""));
			addVariable("{{impacto_Medio}}", (release.getImpact().getName().equals("Medio") ? "X" : ""));
			addVariable("{{impacto_Alto}}", (release.getImpact().getName().equals("Alto") ? "X" : ""));
		} else {
			addVariable("{{impacto_Bajo}}", "");
			addVariable("{{impacto_Medio}}", "");
			addVariable("{{impacto_Alto}}", "");
		}

		if (release.getRisk() != null) {
			addVariable("{{riesgo_Bajo}}", (release.getRisk().getName().equals("Bajo") ? "X" : ""));
			addVariable("{{riesgo_Medio}}", (release.getRisk().getName().equals("Medio") ? "X" : ""));
			addVariable("{{riesgo_Alto}}", (release.getRisk().getName().equals("Alto") ? "X" : ""));
		} else {
			addVariable("{{riesgo_Bajo}}", "");
			addVariable("{{riesgo_Medio}}", "");
			addVariable("{{riesgo_Alto}}", "");
		}

		if (release.getPriority() != null) {
			addVariable("{{prioridad}}", release.getPriority().getName());
		} else {
			addVariable("{{prioridad}}", "");
		}

		String typeRequest = "TPO:";
		String requestName = "";
		Iterator iter = null;

		if (!release.getRequestList().isEmpty()) {
			iter = release.getRequestList().iterator();
			typeRequest = ((((Request) iter.next()).getCode_soin().contains("TPO")) ? "TPO:" : "BT:");
			requestName = release.getTPO_BT();
		}

		if (release.getIncidents() != null) {
			typeRequest = "Incidente (IN):";
			requestName = release.getIncidents();
		}

		if (release.getProblems() != null) {
			typeRequest = "Problema (PR):";
			requestName = release.getProblems();
		}

		if (release.getServiceRequests() != null) {
			typeRequest = "Solicitud de servicio (SS):";
			requestName = release.getServiceRequests();
		}
		addVariable("{{typeRequest}}", typeRequest);
		addVariable("{{requestName}}", requestName);
		addVariable("{{TPOs}}", (release.getTPO_BT() != null ? release.getTPO_BT() : ""));
		addVariable("{{incidentes}}", (release.getIncidents() != null ? release.getIncidents() : ""));
		addVariable("{{problemas}}", (release.getProblems() != null ? release.getProblems() : ""));
		addVariable("{{boletas_de_trabajo}}", "");
		addVariable("{{solicitudes_de_servicio}}",
				(release.getServiceRequests() != null ? release.getServiceRequests() : ""));
		addVariable("{{r indicadores}}", release.getIdentificator());
		addVariable("{{elaborado_por}}", user);
		addVariable("{{r descripcion}}", release.getDescription());
		addVariable("{{tiene_sql}}", (release.getHas_changes_in_bd()) ? "Si tiene" : "No tiene");
		addVariable("{{dependencias}}", release.getDependenciesList());
	}

	public void releaseSolutionInformation(ReleaseSummary release) {
		addVariable("{{r solucion_tecnica}}",
				(release.getTechnicalSolution() != null) ? release.getTechnicalSolution() : "");
		addVariable("{{r solucion_funcional}}",
				(release.getFunctionalSolution() != null) ? release.getFunctionalSolution() : "");
		addVariable("{{no_instalacion}}", (release.getNotInstalling() != null) ? release.getNotInstalling() : "");
		addVariable("{{r observaciones}}", (release.getObservations() != null) ? release.getObservations() : "");
	}

	public void releaseAmbientInformation(ReleaseSummary release) {
		addVariable("{{r pre_condiciones}}",
				(release.getPreConditions() != null ? release.getPreConditions() : Constant.EMPTYVARDOC));
		addVariable("{{r post_condiciones}}",
				(release.getPostConditions() != null ? release.getPostConditions() : Constant.EMPTYVARDOC));
	}

	public void releaseInstalationInstructions(ReleaseSummary release) {
		variables.addTextVariable(new TextVariable("{{r instrucciones_de_instalacion}}",
				(release.getInstallation_instructions() != null ? release.getInstallation_instructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r instrucciones_de_verificacion}}",
				(release.getVerification_instructions() != null ? release.getVerification_instructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r plan_de_rollback}}",
				(release.getRollback_plan() != null ? release.getRollback_plan() : Constant.EMPTYVARDOC)));
	}

	/**
	 * @description: Completa variables de instrucciones de base de datos.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de instrucciones de base de datos.
	 **/
	public void releaseDataBaseInstructions(ReleaseSummary release) {
		variables.addTextVariable(new TextVariable("{{r instrucciones_de_instalacion_base_datos_prd}}",
				(release.getProdInstallationInstructions() != null ? release.getProdInstallationInstructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r instrucciones_de_verificacion_base_datos_prd}}",
				(release.getProdVerificationInstructions() != null ? release.getProdVerificationInstructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r plan_de_rollback_base_datos_prd}}",
				(release.getProdVerificationInstructions() != null ? release.getProdVerificationInstructions()
						: Constant.EMPTYVARDOC)));

		variables.addTextVariable(new TextVariable("{{r instrucciones_de_instalacion_base_datos_cert}}",
				(release.getCertInstallationInstructions() != null ? release.getCertInstallationInstructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r instrucciones_de_verificacion_base_datos_cert}}",
				(release.getCertVerificationInstructions() != null ? release.getCertVerificationInstructions()
						: Constant.EMPTYVARDOC)));
		variables.addTextVariable(new TextVariable("{{r plan_de_rollback_base_datos_cert}}",
				(release.getCertRollbackPlan() != null ? release.getCertRollbackPlan() : Constant.EMPTYVARDOC)));
	}

	

	/**
	 * @description: Completa tabla de acciones.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de tabla de acciones.
	 **/
	public void releaseActions(ReleaseSummary release) {
		if (release.getActions().size() == 0) {
			variables.addTextVariable(new TextVariable("{{r amb.observacion}}", ""));
			variables.addTextVariable(new TextVariable("{{r amb.tiempo}}", ""));
			variables.addTextVariable(new TextVariable("{{r amb.entorno}}", ""));
			variables.addTextVariable(new TextVariable("{{r amb.accion}}", ""));
		} else {
			// Se agrega la tabla de acciones
			TableVariable tableAmbient = new TableVariable();
			List<Variable> ambObservation = new ArrayList<Variable>();
			List<Variable> ambTime = new ArrayList<Variable>();
			List<Variable> ambEnvironment = new ArrayList<Variable>();
			List<Variable> ambAction = new ArrayList<Variable>();
			for (ReleaseAction action : release.getActions()) {
				ambObservation
						.add(new TextVariable("{{r amb.observacion}}", getValueDefaultEmpty(action.getObservation())));
				ambTime.add(new TextVariable("{{r amb.tiempo}}", getValueDefaultEmpty(action.getTime())));
				ambEnvironment.add(
						new TextVariable("{{r amb.entorno}}", getValueDefaultEmpty(action.getEnvironment().getName())));
				ambAction.add(new TextVariable("{{r amb.accion}}", getValueDefaultEmpty(action.getAction().getName())));
			}
			tableAmbient.addVariable(ambObservation);
			tableAmbient.addVariable(ambTime);
			tableAmbient.addVariable(ambEnvironment);
			tableAmbient.addVariable(ambAction);
			variables.addTableVariable(tableAmbient);
		}
	}

	/**
	 * @description: Completa tabla de entornos.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de tabla de entornos.
	 **/
	public void releaseEnvironment(ReleaseSummary release) {
		TableVariable tableEnv = new TableVariable();
		List<Variable> envName = new ArrayList<Variable>();
		List<Variable> envObs = new ArrayList<Variable>();
		List<Variable> envCheck = new ArrayList<Variable>();

		TableVariable tableEnvExternal = new TableVariable();
		List<Variable> envExternalName = new ArrayList<Variable>();
		List<Variable> envExternalObs = new ArrayList<Variable>();
		List<Variable> envExternalCheck = new ArrayList<Variable>();

		for (ReleaseAction action : release.getActions()) {
			if (action.getEnvironment().getExternal()) {
				envExternalName.add(new TextVariable("{{r bajar_entornos_externos}}",
						getValueDefaultEmpty(action.getEnvironment().getName())));

				envExternalObs.add(new TextVariable("{{r bajar_entornos_observaciones}}",
						getValueDefaultEmpty(action.getObservation())));

				envExternalCheck.add(new TextVariable("{{e fila_equis}}", "X"));
			} else {
				envName.add(new TextVariable("{{r bajar_entornos}}",
						getValueDefaultEmpty(action.getEnvironment().getName())));

				envObs.add(new TextVariable("{{r bajar_observaciones}}",
						getValueDefaultEmpty(action.getEnvironment().getName())));

				envCheck.add(new TextVariable("{{i fila_equis}}", "X"));
			}

		}
		tableEnv.addVariable(envName);
		tableEnv.addVariable(envObs);
		tableEnv.addVariable(envCheck);

		tableEnvExternal.addVariable(envExternalName);
		tableEnvExternal.addVariable(envExternalObs);
		tableEnvExternal.addVariable(envExternalCheck);

		variables.addTableVariable(tableEnv);
		variables.addTableVariable(tableEnvExternal);

		if (tableEnv.getNumberOfRows() == 0) {
			variables.addTextVariable(new TextVariable("{{r bajar_entornos}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_acciones}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_tiempos}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_observaciones}}", ""));
			variables.addTextVariable(new TextVariable("{{i fila_equis}}", ""));
		}
		if (tableEnvExternal.getNumberOfRows() == 0) {
			variables.addTextVariable(new TextVariable("{{r bajar_entornos_externos}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_entornos_acciones}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_entornos_tiempos}}", ""));
			variables.addTextVariable(new TextVariable("{{r bajar_entornos_observaciones}}", ""));
			variables.addTextVariable(new TextVariable("{{e fila_equis}}", ""));
		}

	}

	/**
	 * @description: Completa tabla de ambientes.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de tabla de ambientes.
	 **/
	public void releaseAmbients(ReleaseSummary release) {
		if (release.getAmbients().size() == 0) {
			variables.addTextVariable(new TextVariable("{{r fila_ambientes}}", ""));
			variables.addTextVariable(new TextVariable("{{a fila_equis}}", ""));
		} else {
			// Se agrega la tabla de acciones
			TableVariable tableAmbients = new TableVariable();
			List<Variable> ambName = new ArrayList<Variable>();
			List<Variable> ambCheck = new ArrayList<Variable>();
			for (Ambient ambient : release.getAmbients()) {
				ambName.add(new TextVariable("{{r fila_ambientes}}", getValueDefaultEmpty(ambient.getName())));
				ambCheck.add(new TextVariable("{{a fila_equis}}", "X"));
			}
			tableAmbients.addVariable(ambName);
			tableAmbients.addVariable(ambCheck);
			variables.addTableVariable(tableAmbients);
		}
	}

	/**
	 * @description: Componentes modificados.
	 * @author: Esteban Bogantes H.
	 * @return: variables de componentes modificados
	 **/
	public void releaseModifiedComponents(ReleaseSummary release) {

		variables.addTextVariable(
				new TextVariable("{{comp_modif_composites}}", ((release.existComponent("COMPOSITES")) ? "X" : " ")));

		variables.addTextVariable(
				new TextVariable("{{comp_modif_jdbc}}", ((release.existComponent("JDBC")) ? "X" : " ")));

		variables.addTextVariable(
				new TextVariable("{{comp_modif_oracle}}", ((release.existComponent("ORACLE")) ? "X" : " ")));

		variables
				.addTextVariable(new TextVariable("{{comp_modif_mds}}", ((release.existComponent("MDS")) ? "X" : " ")));

		variables
				.addTextVariable(new TextVariable("{{comp_modif_jms}}", ((release.existComponent("JMS")) ? "X" : " ")));

		variables.addTextVariable(new TextVariable("{{comp_modif_mq}}", ((release.existComponent("MQ")) ? "X" : " ")));

		variables.addTextVariable(new TextVariable("{{comp_modif_aq}}", ((release.existComponent("AQ")) ? "X" : " ")));
	}

	/**
	 * @description: Completa tabla de detalles de botones de comando.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de tabla de detalles de botones de comando.
	 **/
	public void releaseButtonCommandDetails(ReleaseSummary release) {
		if (release.getButtons().size() > 0) {
			// Se agrega la tabla de detalles
			TableVariable tableDetails = null;
			List<Variable> bdName = null;
			List<Variable> bdDescription = null;
			List<Variable> bdType = null;
			List<Variable> bdTypeText = null;
			List<Variable> bdQuotationMarks = null;
			List<Variable> bdRequired = null;
			for (ButtonCommand button : release.getButtons()) {

				tableDetails = new TableVariable();
				bdName = new ArrayList<Variable>();
				bdDescription = new ArrayList<Variable>();
				bdType = new ArrayList<Variable>();
				bdTypeText = new ArrayList<Variable>();
				bdQuotationMarks = new ArrayList<Variable>();
				bdRequired = new ArrayList<Variable>();

				if (button.getDetailsButtonCommands().size() == 0) {
					variables.addTextVariable(new TextVariable("{{detailName_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailDescription_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailType_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailTypeText_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailQuotationMarks_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailRequired_" + button.getId() + "}}", ""));
				}

				for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
					bdName.add(new TextVariable("{{detailName_" + button.getId() + "}}", detail.getName()));
					bdDescription.add(
							new TextVariable("{{detailDescription_" + button.getId() + "}}", detail.getDescription()));
					bdType.add(new TextVariable("{{detailType_" + button.getId() + "}}",
							detail.getTypeDetail().getName()));
					bdTypeText.add(new TextVariable("{{detailTypeText_" + button.getId() + "}}",
							(detail.getTypeText() != null) ? detail.getTypeText() : ""));
					bdQuotationMarks.add(new TextVariable("{{detailQuotationMarks_" + button.getId() + "}}",
							(detail.getQuotationMarks()) ? "Si" : "No"));
					bdRequired.add(new TextVariable("{{detailRequired_" + button.getId() + "}}",
							(detail.getIsRequired()) ? "Si" : "No"));
				}
				tableDetails.addVariable(bdName);
				tableDetails.addVariable(bdDescription);
				tableDetails.addVariable(bdType);
				tableDetails.addVariable(bdTypeText);
				tableDetails.addVariable(bdQuotationMarks);
				tableDetails.addVariable(bdRequired);
				variables.addTableVariable(tableDetails);
			}

		}
	}

	/**
	 * @description: Completa tabla de detalles de botones de archivo.
	 * @author: Esteban Bogantes H.
	 * @return: Variables de tabla de detalles de botones de archivo.
	 **/
	public void releaseButtonFileDetails(ReleaseSummary release) {
		if (release.getButtonsFile().size() > 0) {
			// Se agrega la tabla de detalles
			TableVariable tableFileDetails = null;
			List<Variable> bdFileName = null;
			List<Variable> bdFileDescription = null;
			List<Variable> bdFileType = null;
			List<Variable> bdFileTypeText = null;
			List<Variable> bdFileQuotationMarks = null;
			List<Variable> bdFileRequired = null;
			for (ButtonFile button : release.getButtonsFile()) {

				tableFileDetails = new TableVariable();
				bdFileName = new ArrayList<Variable>();
				bdFileDescription = new ArrayList<Variable>();
				bdFileType = new ArrayList<Variable>();
				bdFileTypeText = new ArrayList<Variable>();
				bdFileQuotationMarks = new ArrayList<Variable>();
				bdFileRequired = new ArrayList<Variable>();

				if (button.getDetailsButtonFiles().size() == 0) {
					variables.addTextVariable(new TextVariable("{{detailFileName_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailFileDescription_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailFileType_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailFileTypeText_" + button.getId() + "}}", ""));
					variables.addTextVariable(
							new TextVariable("{{detailFileQuotationMarks_" + button.getId() + "}}", ""));
					variables.addTextVariable(new TextVariable("{{detailFileRequired_" + button.getId() + "}}", ""));
				}

				for (DetailButtonFile detail : button.getDetailsButtonFiles()) {
					bdFileName.add(new TextVariable("{{detailFileName_" + button.getId() + "}}", detail.getName()));
					bdFileDescription.add(new TextVariable("{{detailFileDescription_" + button.getId() + "}}",
							detail.getDescription()));
					bdFileType.add(new TextVariable("{{detailFileType_" + button.getId() + "}}",
							detail.getTypeDetail().getName()));
					bdFileTypeText.add(new TextVariable("{{detailFileTypeText_" + button.getId() + "}}",
							(detail.getTypeText() != null) ? detail.getTypeText() : ""));
					bdFileQuotationMarks.add(new TextVariable("{{detailFileQuotationMarks_" + button.getId() + "}}",
							(detail.getQuotationMarks()) ? "Si" : "No"));
					bdFileRequired.add(new TextVariable("{{detailFileRequired_" + button.getId() + "}}",
							(detail.getIsRequired()) ? "Si" : "No"));
				}
				tableFileDetails.addVariable(bdFileName);
				tableFileDetails.addVariable(bdFileDescription);
				tableFileDetails.addVariable(bdFileType);
				tableFileDetails.addVariable(bdFileTypeText);
				tableFileDetails.addVariable(bdFileQuotationMarks);
				tableFileDetails.addVariable(bdFileRequired);
				variables.addTableVariable(tableFileDetails);
			}
		}
	}

	public String getBoolean(boolean value) {
		return (value) ? "Si" : "No";
	}

	public String getValueDefaultEmpty(String value) {
		if (value == null)
			return "";
		else
			return value;
	}

}
