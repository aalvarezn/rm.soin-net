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
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">RFC N�</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.numRequest}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de creaci�n</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${rfc.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Proyecto</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.codeProyect}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.user.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Impacto</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.impact.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Tipo de cambio</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.typeChange.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Prioridad</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.priority.name}</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de creaci�n</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${rfc.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-sm-12">
					<h5 class="titulares">Fecha y hora propuesta para ejecutar el
						cambio</h5>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha y hora Inicio </label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${rfc.requestDateBegin }</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha y hora Fin </label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${rfc.requestDateFinish }</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Raz&oacute;n del cambio.</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.reasonChange }</textarea>
				</div>


				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Efecto si no se implementa el cambio.</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.effect }</textarea>
				</div>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci�n de Cambio</h5>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Sistemas impactados</label>

					<div id="listSystems">
						<ul class="nav nav-pills">


							<c:forEach items="${systemsImplicated}" var="system">

								<li class="nav-item dependency m-r-10">${system}</li>

							</c:forEach>

						</ul>
					</div>
				</div>

				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Releases a Instalar</label>

					<div id="listSystems">
						<ul class="nav nav-pills">


							<c:forEach items="${rfc.releases}" var="release">

								<li class="nav-item dependency m-r-10">${release.numRelease}</li>

							</c:forEach>

						</ul>
					</div>
				</div>
			</div>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Detalles de la implementaci&oacute;n</h5>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Detalles</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.detail }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Plan de retorno</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.returnPlan }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Evidencias</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.evidence }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Requisitos especiales</label>
					<textarea class="areaWidth" rows="" cols="">${rfc.requestEsp }</textarea>
				</div>
				<div class="col-md-6 col-lg-6 col-xs-6 col-sm-6 m-b-5">
					<label for="">Requiere Base de datos</label>
					<div class="switch">
						<c:choose>
							<c:when test="${rfc.requiredBD}">
								<label>No<input type="checkbox" checked="checked"
									disabled="disabled"><span class="lever"></span>S�
								</label>
							</c:when>
							<c:otherwise>
								<label>No<input type="checkbox" disabled="disabled"><span
									class="lever"></span>S�
								</label>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Archivos Adjuntos</h5>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
					<div class="clearfix">
						<div class="body table-responsive">
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-8 col-lg-8 col-xs-12 col-sm-12">Nombre</th>
										<th class="col-md-4 col-lg-4 col-xs-12 col-sm-12">Fecha
											de Carga</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${rfc.files}" var="fileRFC">
										<tr id="${fileRFC.id}">
											<td>${fileRFC.name}</td>
											<td><fmt:formatDate value="${fileRFC.revisionDate}"
													type="both" /></td>
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