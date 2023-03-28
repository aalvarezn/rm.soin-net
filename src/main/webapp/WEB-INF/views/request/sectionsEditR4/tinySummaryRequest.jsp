<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>

<head>
<%@include file="../../plantilla/header.jsp"%>

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

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">N° Solicitud</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${request.numRequest}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de creación</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${request.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Proceso al que pertenece</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${request.systemInfo.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${request.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${request.user.fullName}</p>
						</div>
					</div>
				</div>
			</div>
			<c:choose>
				<c:when test="${verifySos}">

					<div class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
							<div class="row clearfix">
								<div class="col-sm-12">
									<h5 class="titulares">Usuarios Solicitados</h5>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="table-responsive m-b-20">
										<table
											class="table tableIni table-bordered table-striped table-hover dataTable"
											id="userTable">
											<thead>
												<tr>
													<th>Nombre</th>
													<th>Correo</th>
													<th>Tipo</th>
													<th>Permisos</th>
													<th>Ambiente</th>
													<th>Especificacion</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listUsers}" var="user">
													<tr>
														<td>${user.name}</td>
														<td>${user.email}</td>
														<td>${user.type.code}</td>
														<td>${user.permissions}</td>
														<td>${user.ambient.name}</td>
														<td>${user.espec}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
							<div class="row clearfix">
								<div class="col-sm-12">
									<h5 class="titulares">Usuarios Solicitados</h5>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="table-responsive m-b-20">
										<table
											class="table tableIni table-bordered table-striped table-hover dataTable"
											id="userTable">
											<thead>
												<tr>
													<th>Nombre</th>
													<th>Correo</th>
													<th>Usuario git</th>
													<th>Tipo</th>
													<th>Permisos</th>
													<th>Ambiente</th>
													<th>Especificaci&oacute;n</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listUsers}" var="user">
													<tr>
														<td>${user.name}</td>
														<td>${user.email}</td>
														<td>${user.gitUser}</td>
														<td>${user.type.code}</td>
														<td>${user.permissions}</td>
														<td>${user.ambient.name}</td>
														<td>${user.espec}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>

						<c:forEach items="${userInfo.authorities}" var="authority">
							<c:if test="${authority.name == 'Release Manager'}">
								<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
									<div class="m-b-20">
										<c:if test="${request.status.name ne 'Anulado'}">
											<button type="button" class="btn btn-default setIcon"
												onclick="confirmCancelRequest(${request.id})" title="Anular"
												style="background-color: #00294c !important; color: #fff; border: none !important;">
												<span>ANULAR</span><span style="margin-left: 10px;"><i
													class="material-icons m-t--2">highlight_off</i></span>
											</button>
										</c:if>
										<button type="button" class="btn btn-default setIcon"
											onclick="changeStatusRequest(${request.id}, '${request.numRequest}' )"
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
				</c:otherwise>
			</c:choose>
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