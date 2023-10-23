<!DOCTYPE html>
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

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" />

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">

<!-- Bootstrap Select Css -->
<link
	href="<c:url value='/static/plugins/bootstrap-select/css/bootstrap-select.css'/>"
	rel="stylesheet" type="text/css">

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">

<link rel="stylesheet" type="text/css"
	href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
</head>

<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page Loader -->
	<%@include file="../plantilla/pageLoader.jsp"%>
	<!-- #END# Page Loader -->
	<!-- Page downloading -->
	<%@include file="../plantilla/downloading.jsp"%>
	<!-- Overlay For Sidebars -->
	<div class="overlay"></div>
	<!-- #END# Overlay For Sidebars -->

	<!-- Top Bar -->
	<%@include file="../plantilla/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="../plantilla/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>

	<section id="contentSummary" class="content">
		<div class="container-fluid">
			<div class="row clearfix">
				<%@include file="../release/changeStatusModal.jsp"%>
			</div>
			<span class="topArrow"> <i class="material-icons pointer">keyboard_arrow_up</i>
			</span>
			<div class="row clearfix">
				<div class="button-demo flr">
					<a href="<c:url value='/'/> " class="btn btn-default">IR A
						INICIO</a>
				</div>
			</div>
			<div class="block-header">
				<p class="font-20">
					<span class="col-blue-grey">RESUMEN RELEASE</span> <span
						class="flr font-bold col-cyan ">${release.releaseNumber}</span>
				</p>
			</div>
			<hr>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n General</h5>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Release N°</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="releaseNumber"
								name="releaseNumber" value="${release.releaseNumber}"
								class="form-control" placeholder="">
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Sistema</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="system" name="system"
								value="${release.system.code}" class="form-control"
								placeholder="">
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="user" name="user"
								value="${release.user.fullName}" class="form-control"
								placeholder="">
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Fecha de actualización</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="user" name="user"
								value='<fmt:formatDate
										value="${release.createDate }" pattern="dd/MM/YYYY HH:mm:ss"  />'
								class="form-control" placeholder="">
						</div>
					</div>
				</div>
			</div>

			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="status" name="status"
								value="${release.status.name}" class="form-control"
								placeholder="">
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Impacto</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="impact" name="impact"
								value="${release.impact.name}" class="form-control"
								placeholder="">
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Riesgo</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="risk" name="risk"
								value="${release.risk.name}" class="form-control" placeholder="">
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label for="email_address">Prioridad</label>
					<div class="form-group m-b-0i">
						<div class="form-line ">
							<input type="text" readonly id="priority" name="priority"
								value="${release.priority.name}" class="form-control"
								placeholder="">
						</div>
					</div>
				</div>
				<c:if test="${systemConfiguration.applicationVersion}">
					<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
						<label for="email_address">Versi&oacute;n</label>
						<div class="form-group m-b-0i">
							<div class="form-line ">
								<input type="text" readonly id="versionNumber"
									name="versionNumber" value="${release.versionNumber}"
									class="form-control" placeholder="">
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
					<div class="form-group m-b-0i">
						<div class="form-line">
							<textarea readonly rows="" cols="" class="form-control"
								placeholder="No Aplica." style="">${release.description}</textarea>
						</div>
					</div>
				</div>
				<c:if test="${systemConfiguration.observations}">
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Observaciones</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.observations}</textarea>
							</div>
						</div>
					</div>
				</c:if>

				<c:if test="${systemConfiguration.solutionInfo}">
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Solución Funcional</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.functionalSolution}</textarea>
							</div>
						</div>
					</div>

					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Solución Técnica</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.technicalSolution}</textarea>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Consecuencias si no se instala</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.notInstalling}</textarea>
							</div>
						</div>
					</div>
				</c:if>
				<div class="col-md-6 col-lg-6 col-xs-12 col-sm-12 m-b-5">
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
				<div class="col-md-6 col-lg-6 col-xs-12 col-sm-12 m-b-5">
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

					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12">
						<label for="">Pre Condiciones</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.preConditions}</textarea>
							</div>
						</div>
					</div>

					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-t-10">
						<label for="">Post Condiciones</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.postConditions}</textarea>
							</div>
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
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.installation_instructions}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.verification_instructions}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-10">
						<label for="email_address">Plan de Rollback</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" cols="" class="form-control"
									placeholder="No Aplica." style="">${release.rollback_plan}</textarea>
							</div>
						</div>
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
						<hr class="m-t-0">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<div class="form-group ">
							<div class="form-line">
								<textarea readonly rows="2" cols=""
									id="certInstallationInstructions" class="form-control"
									placeholder="No Aplica" style="">${release.certInstallationInstructions}</textarea>
							</div>
						</div>
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<div class="form-group ">
							<div class="form-line">
								<textarea readonly rows="2" cols=""
									id="certVerificationInstructions" class="form-control"
									placeholder="No Aplica" style="">${release.certVerificationInstructions}</textarea>
							</div>
						</div>
						<label for="email_address">Plan de Rollback</label>
						<div class="form-group">
							<div class="form-line">
								<textarea readonly rows="2" cols="" id="certRollbackPlan"
									class="form-control" placeholder="No Aplica" style="">${release.certRollbackPlan}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<h2 class="m-t-10" style="font-size: 20px;">Producci&oacute;n</h2>
						<hr class="m-t-0">
						<label for="email_address">Instrucciones de
							Instalaci&oacute;n</label>
						<div class="form-group">
							<div class="form-line">
								<textarea readonly rows="2" cols=""
									id="prodInstallationInstructions" class="form-control"
									placeholder="No Aplica" style="">${release.prodInstallationInstructions}</textarea>
							</div>
						</div>
						<label for="email_address">Instrucciones de
							Verificaci&oacute;n</label>
						<div class="form-group">
							<div class="form-line">
								<textarea readonly rows="2" cols=""
									id="prodVerificationInstructions" class="form-control"
									placeholder="No Aplica" style="">${release.prodVerificationInstructions}</textarea>
							</div>
						</div>
						<label for="email_address">Plan de Rollback</label>
						<div class="form-group ">
							<div class="form-line">
								<textarea readonly rows="2" cols="" id="prodRollbackPlan"
									class="form-control" placeholder="No Aplica" style="">${release.prodRollbackPlan}</textarea>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${systemConfiguration.downEnvironment}">
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Ambientes a Bajar</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-10">
						<div class="table-responsive">
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
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
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<label for="email_address">Observaci&oacute;n Ambiente
							Producci&oacute;n</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" readonly cols=""
									class="form-control" placeholder="No Aplica." style="">${release.observationsProd}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<label for="email_address">Observaciones Ambiente Pre QA</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" readonly cols=""
									class="form-control" placeholder="No Aplica." style="">${release.observationsPreQa}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 m-t-20">
						<label for="email_address">Observaci&oacute;n Ambiente QA</label>
						<div class="form-group m-b-0i">
							<div class="form-line">
								<textarea readonly rows="2" cols="" readonly
									class="form-control" placeholder="No Aplica." style="">${release.observationsQa}</textarea>
							</div>
						</div>
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
				<div class="row clearfix m-t-10">
					<div class="col-sm-12">
						<h5 class="titulares">Pruebas Sugeridas</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12">
						<div class="clearfix">
							<c:choose>
								<c:when test="${not empty release.minimal_evidence}">
									<p>${release.minimal_evidence }</p>
								</c:when>
								<c:otherwise>
									<p>N/A</p>
								</c:otherwise>
							</c:choose>

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
									class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
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
									class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
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
									class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
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
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">
							<div class="help-info">Agregados: <span id="countObject">${release.objects.size()}</span></div>
								<table id="configurationItemsTable"
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
										<c:forEach items="${release.objects}" var="object">
											<tr>
												<td>${object.name}</td>
												<td><fmt:formatDate value="${object.revision_Date}"
														pattern="dd/MM/YYYY" /></td>
												<td>${object.revision_SVN}</td>
												<td>${object.typeObject.name}</td>
												<td>${object.configurationItem.name}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<h5 class="titulares">Objetos SQL</h5>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-10">
						<div class="clearfix">
							<div class="table-responsive">
								<table
									class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
									<thead>
										<tr>
											<th>Nombre del Objeto</th>
											<th>Ocupa Ejecutar</th>
											<th>Esquema</th>
											<th>Requiere Plan de ejecución</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${release.objects}" var="releaseObject">
											<c:if test="${releaseObject.isSql == 1}">
												<tr id="${releaseObject.id}">
													<td>${releaseObject.name}</td>
													<td><c:choose>
															<c:when test="${releaseObject.execute == 1}">
																<div class="switch">
																	<label>NO<input disabled="disabled"
																		type="checkbox" checked="checked" value="1"><span
																		class="lever"></span>S&Iacute;
																	</label>
																</div>
															</c:when>
															<c:otherwise>
																<div class="switch">
																	<label>NO<input disabled="disabled"
																		type="checkbox" value="0"><span class="lever"></span>S&Iacute;
																	</label>
																</div>
															</c:otherwise>
														</c:choose></td>
													<td><c:set var="dbScheme"
															value="${fn:replace(releaseObject.dbScheme, ',', ', ')}" />
														${dbScheme}</td>
													<td><c:choose>
															<c:when test="${releaseObject.executePlan == 1}">
																<div class="switch">
																	<label>NO<input disabled="disabled"
																		type="checkbox" checked="checked" value="1"><span
																		class="lever"></span>S&Iacute;
																	</label>
																</div>
															</c:when>
															<c:otherwise>
																<div class="switch">
																	<label>NO<input disabled="disabled"
																		type="checkbox" value="0"><span class="lever"></span>S&Iacute;
																	</label>
																</div>
															</c:otherwise>
														</c:choose></td>
												</tr>
											</c:if>
										</c:forEach>
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
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-t-30">
								<label for="email_address">Descripci&oacute;n de la
									dependencia</label>
								<div class="form-group m-b-0i">
									<div class="form-line">
										<textarea readonly rows="2" cols="" class="form-control"
											placeholder="No Aplica." style="">${release.functionalDescription}</textarea>
									</div>
								</div>
							</div>
						</div>

					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
				<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-8 col-lg-8 col-xs-12 col-sm-12">Nombre</th>
										<th class="col-md-3 col-lg-3 col-xs-12 col-sm-12">Fecha
											de Carga</th>
										<th class="col-md-1 col-lg-1 col-xs-12 col-sm-12">Acciones</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${docs}" var="doc">
										<tr>
											<td>${doc.templateName}</td>
											<td></td>
											<td>
												<div style="text-align: center">
													<div class="iconLine">
														<c:if test="${release.status.name == 'Borrador'}">
															<a href="#" class=""> <i class="material-icons gris"
																style="font-size: 30px;">cloud_download</i>
															</a>
														</c:if>

														<c:if test="${release.status.name ne 'Borrador'}">
															<a
																href="<c:url value='/file/download/${release.id }/${doc.id}'/>"
																download class=""> <i
																class="material-icons col-cyan" style="font-size: 30px;">cloud_download</i>
															</a>
														</c:if>

													</div>
												</div>
											</td>
										</tr>
									</c:forEach>
									<c:forEach items="${release.files}" var="fileRelease">
										<tr id="${fileRelease.id}">
											<td>${fileRelease.name}</td>
											<td><fmt:formatDate value="${fileRelease.revisionDate}"
													type="both" /></td>
											<td>
												<div style="text-align: center">
													<div class="iconLine">
														<a
															href="<c:url value='/file/singleDownload-${fileRelease.id}'/>"
															download class=""> <i class="material-icons col-cyan"
															style="font-size: 30px;">cloud_download</i>
														</a>
													</div>
												</div>
											</td>
										</tr>
									</c:forEach>
									<tr>
										<td>OBJETOS IMPACTADOS</td>
										<td></td>
										<td>
											<div style="text-align: center">
												<div class="iconLine">
													<c:if test="${release.status.name == 'Borrador'}">
														<a href="#" class=""> <i class="material-icons gris"
															style="font-size: 30px;">cloud_download</i>
														</a>
													</c:if>

													<c:if test="${release.status.name ne 'Borrador'}">
														<a
															href="<c:url value='/file/impactObject-${release.id}'/>"
															download class=""> <i class="material-icons col-cyan"
															style="font-size: 30px;">cloud_download</i>
														</a>
													</c:if>

												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<c:forEach items="${userInfo.authorities}" var="authority">
					<c:if test="${authority.name == 'Release Manager'}">
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
							<div class="m-b-20">
								<c:if test="${release.status.name ne 'Anulado'}">
									<button type="button" class="btn btn-default setIcon"
										onclick="confirmCancelRelease(${release.id})" title="Anular"
										style="background-color: #00294c !important; color: #fff; border: none !important;">
										<span>ANULAR</span><span style="margin-left: 10px;"><i
											class="material-icons m-t--2">highlight_off</i></span>
									</button>
								</c:if>
								<button type="button" class="btn btn-default setIcon"
									onclick="changeStatusRelease(${release.id}, '${release.releaseNumber}' )"
									title="Borrador"
									style="background-color: #00294c !important; color: #fff; border: none !important;">
									<span>CAMBIAR ESTADO</span><span style="margin-left: 10px;"><i
										class="material-icons m-t--2">offline_pin</i></span>
								</button>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</section>
	<%@include file="../plantilla/footer.jsp"%>
	<script
		src="<c:url value='/static/js/release/releaseSummaryActions.js'/>"></script>

</body>

</html>