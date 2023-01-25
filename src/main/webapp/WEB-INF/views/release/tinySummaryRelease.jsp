<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>

<head>
<%@include file="../plantilla/header.jsp"%>

<!-- Bootstrap Core Css -->
<link
	href="<c:url value='/static/plugins/bootstrap/css/bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Animation Css -->
<link href="<c:url value='/static/plugins/animate-css/animate.css'/>"
	rel="stylesheet" type="text/css">

<!-- Morris Chart Css-->
<link href="<c:url value='/static/plugins/morrisjs/morris.css'/>"
	rel="stylesheet" type="text/css">

<!-- JQuery DataTable Css -->
<link
	href="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Bootstrap Select Css -->
<link
	href="<c:url value='/static/bootstrap-select/css/bootstrap-select.css'/>"
	type="text/css">

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" />

<!-- TagInput Js -->
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">
</head>

<body class="theme-grey">
	<section id="tinyContentSummary" style="padding-right: 20px;">
		<div class="">
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n General</h5>
					<input type="hidden" id="release_id" value="${release.id}">
					<input type="hidden" id="releaseNumber"
						value="${release.releaseNumber}">
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Release N°</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.releaseNumber}</p>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de creación</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${release.createDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Sistema</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.system.code}</p>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.status.name}</p>
						</div>
					</div>
				</div>
			</div>

			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.user.fullName}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Impacto</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.impact.name}</p>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Riesgo</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.risk.name}</p>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Prioridad</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${release.priority.name}</p>
						</div>
					</div>
				</div>
				<c:if test="${systemConfiguration.applicationVersion}">
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
						<label for="email_address">Versi&oacute;n</label>
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${release.versionNumber}</p>
							</div>
						</div>
					</div>
				</c:if>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-sm-12">
					<h5 class="titulares">Detalles de la Solución</h5>
				</div>

				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Descripción</label>
					<textarea class="areaWidth" rows="" cols="">${release.description }</textarea>
				</div>

				<c:if test="${systemConfiguration.observations}">
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Observaciones</label>
						<textarea class="areaWidth" rows="" cols="">${release.observations }</textarea>
					</div>
				</c:if>

				<c:if test="${systemConfiguration.solutionInfo}">
					<div class="col-md-12 col-lg-12 col-xs-6 col-sm-6 m-b-10">
						<label for="">Solución Funcional</label>
						<c:choose>
							<c:when test="${not empty release.functionalSolution}">
								<textarea class="areaWidth" rows="" cols="">${release.functionalSolution }</textarea>
							</c:when>
							<c:otherwise>
								<p>N/A</p>
							</c:otherwise>
						</c:choose>
					</div>

					<div class="col-md-12 col-lg-12 col-xs-6 col-sm-6 m-b-10">
						<label for="">Solución Técnica</label>
						<c:choose>
							<c:when test="${not empty release.technicalSolution}">
								<textarea class="areaWidth" rows="" cols="">${release.technicalSolution }</textarea>
							</c:when>
							<c:otherwise>
								<p>N/A</p>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Consecuencias si no se instala</label>
						<c:choose>
							<c:when test="${not empty release.notInstalling}">
								<textarea class="areaWidth" rows="" cols="">${release.notInstalling }</textarea>
							</c:when>
							<c:otherwise>
								<p>N/A</p>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
				<div class="col-md-6 col-lg-6 col-xs-6 col-sm-6 m-b-5">
					<label for="">Requiere Plan de ejecución</label>
					<div class="switch">
						<c:choose>
							<c:when test="${release.plan_required}">
								<label>No<input type="checkbox" checked="checked"
									disabled="disabled"><span class="lever"></span>Sí
								</label>
							</c:when>
							<c:otherwise>
								<label>No<input type="checkbox" disabled="disabled"><span
									class="lever"></span>Sí
								</label>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="col-md-6 col-lg-6 col-xs-6 col-sm-6 m-b-5">
					<label for="">Tiene Objetos SQL</label>
					<div class="switch">
						<c:choose>
							<c:when test="${release.has_changes_in_bd}">
								<label>No<input type="checkbox" checked="checked"
									disabled="disabled"><span class="lever"></span>Sí
								</label>
							</c:when>
							<c:otherwise>
								<label>No<input type="checkbox" disabled="disabled"><span
									class="lever"></span>Sí
								</label>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<c:if test="${systemConfiguration.definitionEnvironment}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Definición de Ambientes</h5>
					</div>

					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Ambientes</label>

						<div id="listAmbients">
							<ul class="nav nav-pills">
								<c:forEach items="${release.ambients}" var="ambient">
									<li class="nav-item dependency m-r-10">${ambient.name}
										${ambient.details}</li>
								</c:forEach>
							</ul>
						</div>

					</div>

					<div class="col-md-12 col-lg-12 col-xs-6 col-sm-6">
						<label for="">Pre Condiciones</label>
						<textarea class="areaWidth" rows="" cols="">${release.preConditions }</textarea>
					</div>

					<div class="col-md-12 col-lg-12 col-xs-6 col-sm-6">
						<label for="">Post Condiciones</label>
						<textarea class="areaWidth" rows="" cols="">${release.postConditions }</textarea>
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
											<label>Llamadas por facturar<input
												id="notBilledCalls" type="checkbox" value="1"
												checked="checked"><span class="lever"></span>
											</label>
										</div>
									</c:when>
									<c:otherwise>

										<div class="switch" style="margin-top: 20px;">
											<label>Llamadas por facturar<input
												id="notBilledCalls" type="checkbox" value="0"><span
												class="lever"></span>
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
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Datos de Instalación</h5>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.installation_instructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.verification_instructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Plan de Rollback</label>
						<textarea class="areaWidth" rows="" cols="">${release.rollback_plan }</textarea>
					</div>
				</div>
			</c:if>

			<c:if test="${systemConfiguration.dataBaseInstructions}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Instrucciones de Instalación de Base de
							Datos</h5>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<h2 class="m-t-10" style="font-size: 20px;">Certificaci&oacute;n</h2>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.certInstallationInstructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.certVerificationInstructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Plan de Rollback</label>
						<textarea class="areaWidth" rows="" cols="">${release.certRollbackPlan }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<h2 class="m-t-10" style="font-size: 20px;">Producci&oacute;n</h2>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.prodInstallationInstructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.prodVerificationInstructions }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
						<label for="email_address">Plan de Rollback</label>
						<textarea class="areaWidth" rows="" cols="">${release.prodRollbackPlan }</textarea>
					</div>
				</div>
			</c:if>

			<c:if test="${systemConfiguration.downEnvironment}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Ambientes a Bajar</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="table-responsive">
							<table
								class="table tableInit tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th>Observación</th>
										<th>Tiempo</th>
										<th>Entorno</th>
										<th>Acción</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${release.actions}" var="action">
										<tr>
											<td>${action.observation}</td>
											<td>${action.time}</td>
											<td>${action.environment.name}</td>
											<td>${action.action.name}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${systemConfiguration.environmentObservations}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Observaciones</h5>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 m-b-10">
						<label for="email_address">Observaci&oacute;n Ambiente
							Producci&oacute;n</label>
						<textarea class="areaWidth" rows="" cols="">${release.observationsProd }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 m-b-10">
						<label for="email_address">Observaciones Ambiente Pre QA</label>
						<textarea class="areaWidth" rows="" cols="">${release.observationsPreQa }</textarea>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 m-b-10">
						<label for="email_address">Observaci&oacute;n Ambiente QA</label>
						<textarea class="areaWidth" rows="" cols="">${release.observationsQa }</textarea>
					</div>
				</div>
			</c:if>
			<c:if test="${release.system.isAIA}">
				<div class="row clearfix activeSection">
					<div class="col-sm-12">
						<h5 class="titulares">Componentes Modificados</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-b-10">
						<div id="listComponents" class="">
							<ul class="nav nav-pills">
								<c:forEach items="${release.modifiedComponents}"
									var="modifiedComponent">
									<li class="nav-item dependency m-r-10">
										${modifiedComponent.name}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${systemConfiguration.suggestedTests}">
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Pruebas Sugeridas</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-b-10">
						<div class="clearfix">
							<textarea class="areaWidth" rows="" cols="">${release.minimal_evidence }</textarea>
						</div>
					</div>
				</div>
			</c:if>

			<c:if test="${release.system.customCommands}">
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Crontabs</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">
								<table
									class="table tableInit tableIni table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Comando</th>
											<th>Usuario</th>
											<th>Descripci&oacute;n</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${release.crontabs}" var="crontab">
											<tr>
												<td>${crontab.commandCron}</td>
												<td>${crontab.user}</td>
												<td>${crontab.descriptionCron}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Botones de comandos</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">
								<table
									class="table tableInit tableIni table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Nombre</th>
											<th>Descripci&oacute;n</th>
											<th>Comando</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${release.buttons}" var="button">
											<tr>
												<td>${button.name}</td>
												<td>${button.description}</td>
												<td>${button.command}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Botones de edici&oacute;n de archivos</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">
								<table
									class="table tableInit tableIni table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Descripci&oacute;n</th>
											<th>Descripci&oacute;n html</th>
											<th>Archivo</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${release.buttonsFile}" var="buttonFile">
											<tr>
												<td>${buttonFile.description}</td>
												<td>${buttonFile.descriptionHtml}</td>
												<td>${buttonFile.fileEdit}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</c:if>

			<c:if test="${systemConfiguration.configurationItems}">
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Items de Configuraci&oacute;n y Objetos</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">

								<table id="tableTest6"
									class="table table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Nombre</th>
											<th>Fecha Revisión</th>
											<th>Número Revisión</th>
											<th>Tipo</th>
											<th>Item Configuración</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<h5 class="titulares">Objetos SQL</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">

								<table id="tableTest7"
									class="table table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Nombre del Objeto</th>
											<th>Ocupa Ejecutar</th>
											<th>Esquema</th>
											<th>Requiere Plan de ejecución</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${systemConfiguration.dependencies}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Dependencias del Release</h5>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<label>Funcionales</label>
						<div id="listAmbients">
							<ul class="nav">
								<c:forEach items="${release.dependencies}" var="dependency">
									<c:if test="${dependency.isFunctional}">
										<li class="nav-item dependency m-t-10"><span
											class="font-bold col-blue-grey">
												${dependency.to_release.releaseNumber}: </span> <span>${dependency.to_release.description}</span></li>

									</c:if>
								</c:forEach>
							</ul>
						</div>
						<c:if test="${not empty release.functionalDescription}">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-t-30">
									<label for="email_address">Descripci&oacute;n de la
										dependencia</label>
									<div class="form-group m-b-0i">
										<div class="form-line disabled">
											<p>${release.functionalDescription}</p>
										</div>
									</div>
								</div>
							</div>
						</c:if>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
						<label>Técnicas</label>
						<div id="listAmbients">
							<ul class="nav">
								<c:forEach items="${release.dependencies}" var="dependency">
									<c:if test="${not dependency.isFunctional}">
										<li class="nav-item dependency m-t-10"><span
											class="font-bold col-blue-grey">
												${dependency.to_release.releaseNumber}: </span> <span>${dependency.to_release.description}</span></li>

									</c:if>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</c:if>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Archivos Adjuntos</h5>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table
								class="table tableInit tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-8 col-lg-8 col-xs-12 col-sm-12">Nombre</th>
										<th class="col-md-3 col-lg-3 col-xs-12 col-sm-12">Fecha
											de Carga</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${docs}" var="doc">
										<tr>
											<td>${doc.templateName}</td>
											<td></td>
										</tr>
									</c:forEach>
									<c:forEach items="${release.files}" var="fileRelease">
										<tr id="${fileRelease.id}">
											<td>${fileRelease.name}</td>
											<td><fmt:formatDate value="${fileRelease.revisionDate}"
													type="both" /></td>
										</tr>
									</c:forEach>
									<tr>
										<td>OBJETOS IMPACTADOS</td>
										<td></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@include file="../plantilla/footer.jsp"%>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#tinyContentSummary textarea").parent().removeClass('focused');
			$("#tinyContentSummary input").parent().removeClass('focused');
			$("#tinyContentSummary input").attr("disabled", true);
			$("#tinyContentSummary textarea").attr("disabled", true);
			autosize($('textarea'));
			$(".tableInit").DataTable({
				"language" : {
					"emptyTable" : "No existen registros",
					"zeroRecords" : "No existen registros"
				},
				"searching" : false,
				"paging" : false
			});
		});
	</script>

	<script src="<c:url value='/static/js/release/releaseTinySummary.js'/>"></script>
</body>

</html>