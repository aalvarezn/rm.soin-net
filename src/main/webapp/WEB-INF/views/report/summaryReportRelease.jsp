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

	<input type="hidden" id="releaseId" value="${release.id}">
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
						<span class="col-blue-grey">REPORTE RELEASE</span> <span
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
						<label for="email_address">Fecha de creación</label>
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
									value="${release.risk.name}" class="form-control"
									placeholder="">
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

				</div>
				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Seguimiento del release</h5>
					</div>
					<form id="trackingReleaseForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="idRelease"
							value="" />
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 table-responsive">
							<table id="trackingTable"
								class="table table-bordered table-hover">
								<thead>
									<tr>
										<th colspan="2" style="text-align: center;">Estado</th>
										<th>Fecha del estado</th>
										<th>Responsable</th>
										<th>Motivo</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>

					</form>
				</div>

				<div class="row clearfix">
					<div class="col-sm-12">
						<h5 class="titulares">Árbol de dependencias</h5>
					</div>
					<div id="pdf">
					<div class="row clearfix">
						<form action="" id="treeForm">
							<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<div class="form-group ">
								<label for="to" class="col-sm-2">Profundidad:2</label>
							</div>
							</div>
						</form>
					</div>
					<div class="row clearfix">
						<div class="col-sm-12 m-t-15 m-l-10">
							<button type="button" class="btn bg-light-green btn-xs">Producción</button>
							<button type="button" class="btn bg-orange btn-xs">Certificación</button>
							<button type="button" class="btn bg-green btn-xs">Solicitado</button>
							<button type="button" class="btn bg-normal-blue btn-xs">Borrador</button>
							<button type="button" class="btn bg-pink btn-xs">Anulado</button>
						</div>
						<div id="mynetwork"
							style="width: 97%; height: 450px; border: 1px solid lightgray; margin-left: 15px;">
							<div class="vis-network"
								style="position: relative; overflow: hidden; touch-action: pan-y; user-select: none; width: 100%; height: 100%;"
								tabindex="900">
								<canvas
									style="position: relative; touch-action: none; user-select: none; width: 100%; height: 100%;"
									width="600" height="600"></canvas>
							</div>
						</div>
					</div>
					</div>
				</div>
			
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
					<button type="button" class="btn btn-default setIcon"
						onclick="exportPDF(	 '${release.releaseNumber}' )" title="Exportar"
						style="background-color: #00294c !important; color: #fff; border: none !important; padding-left: 20px;">
						<span>Exportar PDF</span><span style="margin-left: 25px;"><i
							class="material-icons m-t--2">offline_pin</i></span>
					</button>
				</div>

				<!--
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
				-->
			</div>
		</div>
	</section>
	<%@include file="../plantilla/footer.jsp"%>
	<!-- Vis Plugin Js -->
	<script src="<c:url value='/static/plugins/vis/vis-network.js'/>"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.2.13/jspdf.plugin.autotable.min.js"></script>
	<script
		src="<c:url value='/static/plugins/html2canvas/html2canvas.min.js'/>"></script>
	

	<script
		src="<c:url value='/static/js/report/releaseSummaryActions.js'/>"></script>

</body>

</html>