<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_2" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>
<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Informaci&oacute;n de Cambio</h5>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
			<b>Sistema</b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<select class="form-control show-tick" id="systemId" name="systemId">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${systems}" var="systems">
						<option value="${systems.id }">${systems.name }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="riskId_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="table-responsive"
					style="margin-top: 20px; margin-bottom: 20px;">
					<table
						class="table table-bordered table-striped table-hover dataTable"
						id="crontabTable">
						<thead>
							<tr>
								<th>Comando</th>
								<th>Usuario</th>
								<th>Descripci&oacute;n</th>
								<th>Entrada del comando</th>
								<th class="actCol" style="text-align: center;">Acciones</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${release.crontabs}" var="crontab">
								<tr id="${crontab.id}">
									<td>${crontab.commandCron}</td>
									<td>${crontab.user}</td>
									<td>${crontab.descriptionCron}</td>
									<td>${crontab.commandEntry}</td>
									<td><div style="text-align: center">
											<i onclick="editCrontab(${crontab.id})"
												class="material-icons gris" style="font-size: 30px;">mode_edit</i>
											<i onclick="deleteCrontab(${crontab.id})"
												class="material-icons gris" style="font-size: 30px;">delete</i>
										</div></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
		<div class="row clearfix">
			<div class="alig_btn" style="margin-top: 10px;">
				<button type="button" class="btn btn-primary setIcon"
					onclick="openCrontabForm()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
	</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="table-responsive"
					style="margin-top: 20px; margin-bottom: 20px;">
					<div class="help-info">
						<b>Releases a instalar Prod </b>
					</div>
					<table
						class="table table-bordered table-striped table-hover dataTable"
						id="crontabTable">
						<thead>
							<tr>
								<th>Comando</th>
								<th>Usuario</th>
								<th>Descripci&oacute;n</th>
								<th>Entrada del comando</th>
								<th class="actCol" style="text-align: center;">Acciones</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${release.crontabs}" var="crontab">
								<tr id="${crontab.id}">
									<td>${crontab.commandCron}</td>
									<td>${crontab.user}</td>
									<td>${crontab.descriptionCron}</td>
									<td>${crontab.commandEntry}</td>
									<td><div style="text-align: center">
											<i onclick="editCrontab(${crontab.id})"
												class="material-icons gris" style="font-size: 30px;">mode_edit</i>
											<i onclick="deleteCrontab(${crontab.id})"
												class="material-icons gris" style="font-size: 30px;">delete</i>
										</div></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-40">
		<c:if test="${empty  release.functionalDescription}">
			<input type="checkbox" id="requiredFunctionalDes" class="filled-in">
		</c:if>

		<c:if test="${not empty  release.functionalDescription}">
			<input type="checkbox" id="requiredFunctionalDes" class="filled-in"
				checked="checked">
		</c:if>
		<label for="requiredFunctionalDes">&iquest;Requiere BD?</label>
	</div>
</div>
<c:if test="${systemConfiguration.definitionEnvironment}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Definici&oacute;n de Ambientes</h5>
		</div>
		<div class="col-sm-12">

			<div class="form-group m-b-0i">
				<div class="form-line">
					<input type="text" id="ambient" name="ambient" maxlength="30"
						class="form-control" placeholder="Ingrese una b&uacute;squeda..."
						style="height: 60px;">
					<div class="help-info">M&aacute;x. 30 caracteres</div>
				</div>
				<label id="ambient_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-11 col-md-11 col-sm-12 col-xs-12 m-b-15 p-l-0">
			<div id="listAmbients" class="col-sm-12">
				<ul class="nav nav-pills">
					<c:forEach items="${release.ambients}" var="ambient">
						<li id="${ambient.id}" class="nav-item dependency">
							${ambient.name} ${ambient.details} <span class="flr m-b--10"
							style="margin-top: -3px;"> <a
								onclick="deleteAmbient(${ambient.id})" title="Borrar"><i
									class="gris"><span class="lnr lnr-cross-circle p-l-5"></span></i></a>
						</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Pre condiciones</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="preConditions" name="preConditions"
						class="form-control"
						placeholder="Ingrese las concidiciones de pre instalaci&oacute;n..."
						style="">${release.preConditions}</textarea>
				</div>
				<label id="preConditions_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Post condiciones</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="postConditions"
						name="postConditions" class="form-control"
						placeholder="Ingrese las condiciones de post instalaci&oacute;n..."
						style="">${release.postConditions}</textarea>
				</div>
				<label id="postConditions_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</c:if>
<c:if test="${release.system.isBO}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Tipo de reporte</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">ICE Facturaci&oacute;n</label>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportHaveArt}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte tiene arte<input id="reportHaveArt"
									type="checkbox" value="1" checked="checked"><span
									class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte tiene arte<input id="reportHaveArt"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportfixedTelephony}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de telefon&iacute;a fija<input
									id="reportfixedTelephony" type="checkbox" value="1"
									checked="checked"><span class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de telefon&iacute;a fija<input
									id="reportfixedTelephony" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportHistoryTables}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de tablas hist&oacute;ricas<input
									id="reportHistoryTables" type="checkbox" value="1"
									checked="checked"><span class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de tablas hist&oacute;ricas<input
									id="reportHistoryTables" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportNotHaveArt}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte sin arte<input id="reportNotHaveArt"
									type="checkbox" value="1" checked="checked"><span
									class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte sin arte<input id="reportNotHaveArt"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportMobileTelephony}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de telefon&iacute;a movil<input
									id="reportMobileTelephony" type="checkbox" value="1"
									checked="checked"><span class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de telefon&iacute;a movil<input
									id="reportMobileTelephony" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.reportTemporaryTables}">

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de tablas temporales<input
									id="reportTemporaryTables" type="checkbox" value="1"
									checked="checked"><span class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Reporte de tablas temporales<input
									id="reportTemporaryTables" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">ICE Reporte BRM (SIEBEL)</label>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.billedCalls}">

							<div class="switch" style="margin-top: 20px;">
								<label>Llamadas facturadas<input id="billedCalls"
									type="checkbox" value="1" checked="checked"><span
									class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Llamadas facturadas<input id="billedCalls"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<c:choose>
						<c:when test="${release.notBilledCalls}">

							<div class="switch" style="margin-top: 20px;">
								<label>Llamadas por facturar<input id="notBilledCalls"
									type="checkbox" value="1" checked="checked"><span
									class="lever"></span>
								</label>
							</div>
						</c:when>
						<c:otherwise>

							<div class="switch" style="margin-top: 20px;">
								<label>Llamadas por facturar<input id="notBilledCalls"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</c:if>
<c:if test="${systemConfiguration.instalationData}">
	<div class="row clearfix activeSection">

		<div class="col-sm-12">
			<h5 class="titulares">Datos de Instalaci&oacute;n</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Instrucciones de
				Instalaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="installationInstructions"
						class="form-control"
						placeholder="Ingrese las instrucciones para instalaci&oacute;del release..."
						style="">${release.installation_instructions}</textarea>
				</div>
				<label id="installationInstructions_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Instrucciones de
				Verificaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="verificationInstructions"
						name="verificationInstructions" class="form-control"
						placeholder="Ingrese las instrucciones de verificaci&oacute;n..."
						style="">${release.verification_instructions}</textarea>
				</div>
				<label id="verificationInstructions_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Plan de Rollback</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="rollbackPlan" name="rollbackPlan"
						class="form-control"
						placeholder="Ingrese las instrucciones en caso de error..."
						style="">${release.rollback_plan}</textarea>
				</div>
				<label id="rollbackPlan_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${systemConfiguration.dataBaseInstructions}">
	<div class="row clearfix activeSection">

		<div class="col-sm-12">
			<h5 class="titulares">Instrucciones de Instalaci&oacute;n de
				Base de Datos</h5>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<h2 style="font-size: 20px;">Certificaci&oacute;n</h2>
			<hr>
			<label for="email_address">Instrucciones de
				Instalaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="certInstallationInstructions"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.certInstallationInstructions}</textarea>
				</div>
				<label id="certInstallationInstructions_error"
					class="error fieldError" for="name" style="visibility: hidden;">Campo
					Requerido.</label>
			</div>
			<label for="email_address">Instrucciones de
				Verificaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="certVerificationInstructions"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.certVerificationInstructions}</textarea>
				</div>
				<label id="certVerificationInstructions_error"
					class="error fieldError" for="name" style="visibility: hidden;">Campo
					Requerido.</label>
			</div>
			<label for="email_address">Plan de Rollback</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="certRollbackPlan"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.certRollbackPlan}</textarea>
				</div>
				<label id="certRollbackPlan_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<h2 style="font-size: 20px;">Producci&oacute;n</h2>
			<hr>
			<label for="email_address">Instrucciones de
				Instalaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="prodInstallationInstructions"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.prodInstallationInstructions}</textarea>
				</div>
				<label id="prodInstallationInstructions_error"
					class="error fieldError" for="name" style="visibility: hidden;">Campo
					Requerido.</label>
			</div>
			<label for="email_address">Instrucciones de
				Verificaci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="prodVerificationInstructions"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.prodVerificationInstructions}</textarea>
				</div>
				<label id="prodVerificationInstructions_error"
					class="error fieldError" for="name" style="visibility: hidden;">Campo
					Requerido.</label>
			</div>
			<label for="email_address">Plan de Rollback</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="prodRollbackPlan"
						class="form-control" placeholder="Ingrese las instrucciones"
						style="">${release.prodRollbackPlan}</textarea>
				</div>
				<label id="prodRollbackPlan_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</c:if>
<c:if test="${systemConfiguration.downEnvironment}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Ambientes a Bajar</h5>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
			<label for="email_address">Requiere Bajar Ambientes</label>
			<c:choose>
				<c:when test="${release.down_required == '1'}">
					<div class="switch" style="margin-top: 20px;">
						<label>NO<input id="downRequired" type="checkbox"
							value="1" checked="checked"><span class="lever"></span>S&Iacute;
						</label>
					</div>
				</c:when>
				<c:otherwise>
					<div class="switch" style="margin-top: 20px;">
						<label>NO<input id="downRequired" type="checkbox"
							value="0"><span class="lever"></span>S&Iacute;
						</label>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
			<div id="environmentsActions" class="disn">
				<button type="button" class="btn btn-primary setIcon"
					onclick="openEnvironmentActionModal()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
			<div id="environmentsActions" class="disn">
				<div class="table-responsive" style="margin-top: 20px;">
					<table
						class="table table-bordered table-striped table-hover dataTable"
						id="environmentActionTable" id="ordenamiento">
						<thead>
							<tr>
								<th>Observaci&oacute;n</th>
								<th>Tiempo</th>
								<th>Entorno</th>
								<th>Acci&oacute;n</th>
								<th
									style="text-align: center; padding-left: 0px; padding-right: 0px;">Acciones</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${release.actions}" var="action">
								<tr id="${action.id}">
									<td>${action.observation}</td>
									<td>${action.time}</td>
									<td>${action.environment.name}</td>
									<td>${action.action.name}</td>
									<td>
										<div style="text-align: center">
											<i onClick="deleteaEnvironmentAction( ${action.id})"
												class="material-icons gris" style="font-size: 30px">delete</i>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div style="margin-top: -15px;" class="form-group m-t-10 m-b-0i">
					<label id="actionsTable_error" class="error fieldError" for="name"
						style="visibility: hidden;">Campo Requerido.</label>
				</div>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${systemConfiguration.environmentObservations}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Observaciones</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Observaci&oacute;n Ambiente
				Producci&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="observationsProd"
						name="observationsProd" class="form-control"
						placeholder="Ingrese una observaci&oacute;n..." style="">${release.observationsProd}</textarea>
				</div>
				<label id="observationsProd_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Observaciones Ambiente Pre QA</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="observationsPreQa"
						name="observationsPreQa" class="form-control"
						placeholder="Ingrese una observaci&oacute;n..." style="">${release.observationsPreQa}</textarea>
				</div>
				<label id="observationsPreQa_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
			<label for="email_address">Observaci&oacute;n Ambiente QA</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" id="observationsQa" name="observationsQa"
						class="form-control"
						placeholder="Ingrese una observaci&oacute;n..." style="">${release.observationsQa}</textarea>
				</div>
				<label id="observationsQa_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</c:if>
<c:if test="${release.system.isAIA}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Consideraciones Exclusivas de AIA</h5>
		</div>
	</div>
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<label for="email_address">Componentes Modificados</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<input type="text" id="modifiedComponent" name="modifiedComponent"
						maxlength="30" class="form-control"
						placeholder="Ingrese una b&uacute;squeda..." style="height: 60px;">
					<div class="help-info">M&aacute;x. 30 caracteres</div>
				</div>
				<label id="modifiedComponent_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-11 col-md-11 col-sm-12 col-xs-12 m-b-15 p-l-0">
			<div id="listComponents" class="col-sm-12">
				<ul class="nav nav-pills">
					<c:forEach items="${release.modifiedComponents}"
						var="modifiedComponent">
						<li id="${modifiedComponent.id}" class="nav-item dependency">
							${modifiedComponent.name} <span class="flr m-b--10"
							style="margin-top: -3px;"> <a
								onclick="deleteModifiedComponent(${modifiedComponent.id})"
								title="Borrar"><i class="gris"><span
										class="lnr lnr-cross-circle p-l-5"></span></i></a>
						</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${systemConfiguration.suggestedTests}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Pruebas Sugeridas</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<label for="email_address">Pruebas m&iacute;nimas sugeridas
				en QA</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="minimalEvidence"
						name="minimalEvidence" class="form-control"
						placeholder="Ingrese una prueba..." style="">${release.minimal_evidence}</textarea>
				</div>
				<label id="minimalEvidence_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</c:if>