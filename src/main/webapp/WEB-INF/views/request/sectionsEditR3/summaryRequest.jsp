<!DOCTYPE html>
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
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">
<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page Loader -->
	<%@include file="../../plantilla/pageLoader.jsp"%>
	<!-- #END# Page Loader -->

	<!-- Overlay For Sidebars -->
	<div class="overlay"></div>
	<!-- #END# Overlay For Sidebars -->

	<!-- Top Bar -->
	<%@include file="../../plantilla/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>

	<section id="contentSummary" class="content">
		<div class="container-fluid">
			<div class="row clearfix">
				<%@include file="../../request/changeStatusModal.jsp"%>
			</div>
			<span class="topArrow"> <i class="material-icons pointer">keyboard_arrow_up</i>
			</span>
			<div class="row clearfix">
				<div class="button-demo flr">
					<a href="<c:url value='/homeRequest'/> " class="btn btn-default">IR
						A INICIO</a>
				</div>
			</div>
			<div class="block-header">
				<p class="font-20">
					<span class="col-blue-grey">RESUMEN SOLICITUD</span> <span
						class="flr font-bold col-cyan ">${request.numRequest}</span>
				</p>
			</div>
			<hr>
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
					<label for="email_address">Fecha de creaci&oacute;n</label>
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
					<label for="email_address">Proyecto</label>
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
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Lista de colaboradores con acceso a la
						herramienta</h5>
				</div>

				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Usuarios RM asignados</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">${requestR3.listNames}</div>
					</div>
				</div>
				<div class="col-lg-8 col-md-8 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">M&eacute;todo de conexi&oacute;n</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${requestR3.connectionMethod}</p>
							</div>
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
							<table
								class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
								<thead>
									<tr>
										<th class="col-md-8 col-lg-8 col-xs-8 col-sm-8">Nombre</th>
										<th class="col-md-4 col-lg-4 col-xs-4 col-sm-4">Fecha de
											carga</th>
										<th class="col-md-1 col-lg-1 col-xs-12 col-sm-12">Acciones</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${request.files}" var="fileRequest">
										<tr id="${fileRequest.id}">
											<td>${fileRequest.name}</td>
											<td><fmt:formatDate value="${fileRequest.revisionDate}"
													type="both" /></td>
											<td class="iconLineC"><a
												href="<c:url value='/file/singleDownloadRequest-${fileRequest.id }'/>"
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
		</div>
	</section>

	<%@include file="../../plantilla/footer.jsp"%>

	<script
		src="<c:url value='/static/js/newRequest/requestSummaryActions.js'/>"></script>
	<!-- Validate Core Js -->
	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

</body>

</html>