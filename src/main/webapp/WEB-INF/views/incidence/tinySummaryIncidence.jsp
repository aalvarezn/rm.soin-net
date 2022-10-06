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
					<h5 class="titulares">Informaci&oacute;n general</h5>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">N° Ticket</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.numTicket}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Fecha de creación</label>
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
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Sistema</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.system.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.createFor}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Tipo de ticket</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.typeIncidence.typeIncidence.code}</p>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Prioridad</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.priority.priority.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 p-t-10">
					<label for="email_address">Tiempo de atenci&oacute;n</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${incidence.priority.time}</p>
						</div>
					</div>
				</div>
			</div>

			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Detalles del ticket</h5>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Detalles</label>
					<textarea class="areaWidth" rows="" cols="">${incidence.detail }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Resultado esperado</label>
					<textarea class="areaWidth" rows="" cols="">${incidence.result }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Notas adicionales</label>
					<textarea class="areaWidth" rows="" cols="">${incidence.note }</textarea>
				</div>

			</div>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Archivos adjuntos</h5>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-6 col-lg-6 col-xs-6 col-sm-6">Nombre</th>
										<th class="col-md-5 col-lg-5 col-xs-5 col-sm-5">Fecha de
											carga</th>
										<th class="col-md-1 col-lg-1 col-xs-1 col-sm-1">Acciones</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${incidence.files}" var="fileIncidence">
										<tr id="${fileIncidence.id}">
											<td>${fileIncidence.name}</td>
											<td><fmt:formatDate value="${fileIncidence.revisionDate}"
													type="both" /></td>
											<td><a
												href="<c:url value='/file/singleDownloadIncidence-${fileIncidence.id }'/>"
												download class=""> <i class="material-icons col-cyan"
													style="font-size: 30px;">cloud_download</i>
											</a></td>
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
	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<!-- Bootstrap Core Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

	<!-- Select Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-select/js/bootstrap-select.js'/>"></script>

	<!-- Slimscroll Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-slimscroll/jquery.slimscroll.js'/>"></script>

	<!-- Jquery CountTo Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-countto/jquery.countTo.js'/>"></script>

	<!-- Autosize Plugin Js -->
	<script src="<c:url value='/static/plugins/autosize/autosize.js'/>"></script>

	<!-- Jquery DataTable Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>

	<!-- Bootstrap Notify Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-notify/bootstrap-notify.js'/>"></script>

	<!-- SweetAlert Plugin Js -->
	<script
		src="<c:url value='/static/plugins/sweetalert/sweetalert.min.js'/>"></script>

	<!-- TagInput Js -->
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js'/>"></script>
	<%-- 	<script src="<c:url value='/static/js/release/release.js'/>"></script> --%>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#tinyContentSummary textarea").parent().removeClass('focused');
			$("#tinyContentSummary input").parent().removeClass('focused');
			$("#tinyContentSummary input").attr("disabled", true);
			$("#tinyContentSummary textarea").attr("disabled", true);
			autosize($('textarea'));

			$('table').DataTable({
				"language" : {
					"emptyTable" : "No existen registros",
					"zeroRecords" : "No existen registros"
				},
				"searching" : false,
				"paging" : false
			});
		});
	</script>

</body>

</html>