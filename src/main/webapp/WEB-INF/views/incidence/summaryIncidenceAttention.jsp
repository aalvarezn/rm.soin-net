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
<link rel="stylesheet"
	href="<c:url value='/static/plugins/jquery-file-upload/css/jquery.fileupload.css'/>" />
<link rel="stylesheet"
	href="<c:url value='/static/plugins/jquery-file-upload/css/jquery.fileupload-ui.css'/>" />
</head>


<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<input type="hidden" id="rfcId" name="postMSG" value="${incidence.id}">
	<!-- Page Loader -->
	<%@include file="../plantilla/pageLoader.jsp"%>
	<!-- #END# Page Loader -->

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

				<!-- addFileModal -->
				<%@include file="../release/addFileModal.jsp"%>
				<!-- #END# addFileModal -->
			</div>
			<span class="topArrow"> <i class="material-icons pointer">keyboard_arrow_up</i>
			</span>
			<div class="row clearfix">
				<div class="button-demo flr">
					<a href="<c:url value='/homeIncidenceAttention'/> " class="btn btn-default">IR
						A INICIO</a> <input type="hidden" id="dateInitial"
						value="${incidence.updateDate}"> <input type="hidden"
						id="dateFinal" value="${incidence.exitOptimalDate }"> <input
						type="hidden" id="attetionTime" value="	${incidence.timeMili}">


				</div>
			</div>
			<div class="block-header">
				<p class="font-20">
					<span class="col-blue-grey">TRAMITAR TICKET</span> <span
						class="flr font-bold col-cyan ">${incidence.numTicket}</span>
				</p>
			</div>
			<hr>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n general</h5>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">N° Ticket</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.numTicket}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Sistema</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.system.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de actualizaci&oacute;n</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${incidence.updateDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado actual</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.status.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.createFor}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Tipo de ticket</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.typeIncidence.typeIncidence.code}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de solicitud del ticket</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${incidence.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha limite de atencion </label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${incidence.exitOptimalDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Tiempo de atenci&oacute;n</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.priority.time}</p>
						</div>
					</div>
				</div>


				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-t-10 m-b-10">
					<label for="">Detalles</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.detail }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Resultado esperado</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.result }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Notas adicionales</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.note }</textarea>
				</div>

				<c:if test="${incidence.cause != null}">
								<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-t-10 m-b-10">
					<label for="">Error presentado</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.errorNew }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Causa de la incidencia</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.cause }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Solución aplicada</label>
					<textarea class="areaWidth" disabled="disabled" rows="" cols="">${incidence.solution }</textarea>
				</div>
				</c:if>

			</div>

			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Bit&aacute;cora</h5>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-2 col-lg-2 col-xs-2 col-sm-2">Fecha</th>
										<th class="col-md-4 col-lg-4 col-xs-4 col-sm-4">Nombre
											Solicitante</th>
										<th class="col-md-6 col-lg-6 col-xs-6 col-sm-6">Motivo</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${incidence.log}" var="logs">
										<tr id="${logs.id}">
											<td><fmt:formatDate value="${logs.logDate}" type="both" /></td>
											<td>${logs.operator}</td>
											<td>${logs.motive}</td>
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
					<h5 class="titulares">Archivos adjuntos</h5>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table id="attachedFilesTable"
								class="table table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-8 col-lg-8 col-xs-8 col-sm-8">Nombre</th>
										<th class="col-md-4 col-lg-4 col-xs-4 col-sm-4">Fecha de
											carga</th>
										<th class="col-md-1 col-lg-1 col-xs-12 col-sm-12">Acciones</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${incidence.files}" var="fileIncidence">
										<tr id="${fileIncidence.id}">
											<td>${fileIncidence.name}</td>
											<td><fmt:formatDate
													value="${fileIncidence.revisionDate}" type="both" /></td>
											<td>
												<div style="text-align: center">
													<div class="iconLine">
														<a
															href="<c:url value='/file/singleDownloadIncidence-${fileIncidence.id}'/>"
															download class=""> <i class="material-icons col-cyan"
															style="font-size: 30px;">cloud_download</i>
														</a>
													</div>
												</div>
											</td>
										</tr>
									</c:forEach>

								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@include file="../plantilla/footer.jsp"%>

	<script
		src="<c:url value='/static/js/incidence/incidenceAttention.js'/>"></script>
	<script
		src="<c:url value='/static/js/incidence/incidenceFileUpload.js'/>"></script>

	<!-- Validate Core Js -->

	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
			autosize($('textarea'));
		});
	</script>

</body>

</html>