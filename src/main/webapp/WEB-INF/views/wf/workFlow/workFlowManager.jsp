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

</head>
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

	<section class="content m-t-80I">
		<div class="container-fluid">

			<div class="row clearfix">
				<%@include file="../../wf/workFlow/workFlowModal.jsp"%>
			</div>

			<div class="row clearfix">
				<%@include file="../../release/trackingReleaseModal.jsp"%>
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
								data-to="${wfCount['all']}" data-speed="1000"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-cyan hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">engineering</i>
						</div>
						<div class="content">
							<div class="text">INICIADOS</div>
							<div class="number count-to" data-from="0"
								data-to="${wfCount['start']}" data-speed="1000"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
					<div class="info-box bg-orange hover-expand-effect">
						<div class="icon">
							<i class="material-icons default">change_circle</i>
						</div>
						<div class="content">
							<div class="text">EN PROCESO</div>
							<div class="number count-to" data-from="0"
								data-to="${wfCount['action']}" data-speed="1000"
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
							<div class="text">FINALIZADOS</div>
							<div class="number count-to" data-from="0"
								data-to="${wfCount['finish']}" data-speed="15"
								data-fresh-interval="20"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- #CountSection -->

			<div class="block-header">
				<h2>MIS TRÁMITES</h2>
			</div>

			<!-- tableFilters -->
			<div id="tableFilters" class="row clearfix m-t-20">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Rango de Fechas</label>
					<div class="input-group">
						<span class="input-group-addon"> <i class="material-icons">date_range</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="daterange" value="" />
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Sistema</label>
					<div class="form-group m-b-0">
						<select id="systemId" class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="0">-- Todos --</option>
							<c:forEach items="${systems}" var="system">
								<option value="${system.id }">${system.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Estado</label>
					<div class="form-group m-b-0">
						<select id="statusId" class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="0">-- Todos --</option>
							<c:forEach items="${statuses}" var="status">
								<option value="${status.id }">${status.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<!-- #tableFilters# -->
			<div class="row clearfix">
				<div id="tableSection"
					class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="body">
						<div class="body table-responsive">
							<table id="dtReleases"
								class="table table-bordered table-striped table-hover dataTable">
								<thead>
									<tr>
										<th>ID</th>
										<th>Node</th>
										<th>Número release</th>
										<th>Solicitante</th>
										<th>Modificado</th>
										<th>Estado</th>
										<th>Acciones</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@include file="../../plantilla/footer.jsp"%>

	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script
		src="<c:url value='/static/js/wf/workFlowManager.js?v=${jsVersion}'/>"></script>
</body>

</html>