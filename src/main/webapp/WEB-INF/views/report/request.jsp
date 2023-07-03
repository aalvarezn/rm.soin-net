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

<!-- <!-- Bootstrap Select Css -->
<!-- <link rel="stylesheet" -->
<!-- 	href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.9/dist/css/bootstrap-select.min.css"> -->

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" />

<!-- Materialize Css -->
<%-- <link href="<c:url value='/static/plugins/materialize-css/css/materialize.css'/>" --%>
<!-- 	rel="stylesheet" /> -->

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

<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css'/>" />
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">
</head>
<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page downloading -->
	<%@include file="../plantilla/downloading.jsp"%>
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

	<section class="content m-t-80I">
		<div class="container-fluid">

			<div class="row clearfix">
				<%@include file="../release/changeUserModal.jsp"%>
			</div>

			<div class="row clearfix">
				<%@include file="../request/changeStatusModal.jsp"%>
			</div>

			<div class="row clearfix">
				<%@include file="../request/trackingRequestModal.jsp"%>
			</div>

			<!-- CountSection -->
			<div class="row clearfix">

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-light-green hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">dashboard</i>
						</div>
						<div class="content">
							<div class="text">TODOS</div>
							<div class="number count-to" data-from="0"
								data-to="${rfcC['all']}" data-speed="1000"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-cyan hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">drafts</i>
						</div>
						<div class="content">
							<div class="text">BORRADOR</div>
							<div class="number count-to" data-from="0"
								data-to="${rfcC['draft']}" data-speed="1000"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-orange hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">priority_high</i>
						</div>
						<div class="content">
							<div class="text">SOLICITADOS</div>
							<div class="number count-to" data-from="0"
								data-to="${rfcC['requested']}" data-speed="1000"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-pink hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">flag</i>
						</div>
						<div class="content">
							<div class="text">COMPLETADOS</div>
							<div class="number count-to" data-from="0"
								data-to="${rfcC['completed']}" data-speed="15"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- #CountSection -->
			<!-- tableFilters -->
			<div id="tableFilters" class="row clearfix m-t-20">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Rango de Fechas</label>
					<div class="input-group">
						<span class="input-group-addon"> <i class="material-icons">date_range</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="daterange"
								id="daterange" value="" />
						</div>
					</div>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
					<label>Proyecto</label>
					<div class="form-group m-b-0">
						<select id="projectId" class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="0">-- Todos --</option>
							<c:forEach items="${projects}" var="project">
								<option value="${project.id }">${project.code}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
					<label>Sistemas</label>
					<div class="form-group m-b-0">
						<select id="systemId" disabled name="systemId"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="0">-- Todos --</option>
						</select>
					</div>
				</div>

				<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
					<label>Tipo de Solicitud</label>
					<div class="form-group m-b-0">
						<select id="typePetitionId"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="0">-- Todos --</option>
							<c:forEach items="${typePetitions}" var="typePetition">
								<option value="${typePetition.id }">${typePetition.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
					<div class="button-demo-refresh" style="padding-top: 25px;">
						<button title="Refrescar tabla con filtros!" type="button"
							class="btn btn-primary setIcon" onclick="refreshTable()">
							<span><i class="material-icons m-t--2 ">update</i></span>
						</button>
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="button-demo flr">
						<button type="button" class="btn btn-primary setIcon"
							onclick="downLoadReport(1)">
							<span>DESCARGAR EXCEL </span><span style="margin-left: 5px;"><i
								class="fas fa-file-excel text-success "></i></span>
						</button>
					</div>
					<div class="button-demo flr">
						<button type="button" class="btn btn-primary setIcon"
							onclick="downLoadReport(2)">
							<span>DESCARGAR PDF</span><span style="margin-left: 5px;"><i
								class="fas fa-file-pdf text-danger"></i></span>
						</button>
					</div>
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
				<div id="tableSection" class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="body ">
							<div class="body table-responsive">
								<table id="dtRequests"
									class="table table-bordered table-striped table-hover dataTable">
									<thead>
										<tr>
											<th></th>
											<th>N�mero Solicitud</th>
											<th>Sistema</th>
											<th>Tipo Solicitud</th>
											<th>Solicitante</th>
											<th>Modificado</th>
											<th>Acciones</th>
										</tr>
									</thead>

								</table>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</section>

	<%@include file="../plantilla/footer.jsp"%>
	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script src="<c:url value='/static/js/report/request.js'/>"></script>

	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>
</body>

</html>