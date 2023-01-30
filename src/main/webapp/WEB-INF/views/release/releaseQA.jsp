<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.soin.sgrm.model.ReleaseUser"%>
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
	<input type="text" id="postMSG" name="postMSG" value="${data}">
	<c:forEach items="${userInfo.authorities}" var="authority">
		<c:if test="${authority.name == 'QA'}">
			<input type="text" id="isQA" name="isQA" value="true">
		</c:if>
	</c:forEach>
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
	<section class="content m-t-70I">
		<div class="container-fluid">
			<div class="row">
				<%@include file="../release/trackingReleaseModal.jsp"%>
				<!-- #tableSection#-->
				<div id="tableSection">
					<div class="block-header">
						<h2>RELEASES</h2>
						<input type="hidden" id="listType" name="${list}" value="${list}" />
					</div>

					<!-- Tab PANELS -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane  active" id="releases">
							<div class="row clearfix">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons default">dashboard</i>
										</div>
										<div class="content">
											<div class="text">TODOS</div>
											<div class="number count-to" data-from="0"
												data-to="${systemC['all']}" data-speed="1000"
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
												data-to="${systemC['draft']}" data-speed="1000"
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
												data-to="${systemC['requested']}" data-speed="1000"
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
												data-to="${systemC['completed']}" data-speed="15"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane " id="equipos">
							<div class="row clearfix">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons default">dashboard</i>
										</div>
										<div class="content">
											<div class="text">TODOS</div>
											<div class="number count-to" data-from="0"
												data-to="${systemC['all']}" data-speed="1000"
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
												data-to="${systemC['draft']}" data-speed="1000"
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
												data-to="${systemC['requested']}" data-speed="1000"
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
												data-to="${systemC['completed']}" data-speed="15"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane " id="sistemas">
							<div class="row clearfix">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons default">dashboard</i>
										</div>
										<div class="content">
											<div class="text">TODOS</div>
											<div class="number count-to" data-from="0"
												data-to="${systemC['all']}" data-speed="1000"
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
												data-to="${systemC['draft']}" data-speed="1000"
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
												data-to="${systemC['requested']}" data-speed="1000"
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
												data-to="${systemC['completed']}" data-speed="15"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
							</div>
						</div>

					</div>
					<!-- #END# TAB PANELS -->
					<div class="row clearfix">
						<!-- TAB COUNTS -->
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div id="releaseTabs">
								<!-- Nav tabs -->
								<ul class="nav nav-tabs tab-nav-right" role="tablist">
									<li onclick="changeSelectView(3)" class="active"
										role="presentation"><a href="#sistemas" data-toggle="tab">TODOS
											LOS SISTEMAS</a></li>
								</ul>
							</div>
						</div>
					</div>
					<!-- #END# TAB COUNTS -->

					<!-- tableFilters -->
					<div id="tableFilters" class="row clearfix m-t-20">
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label>Rango de Fechas</label>
							<div class="input-group">
								<span class="input-group-addon"> <i
									class="material-icons">date_range</i>
								</span>
								<div class="form-line">
									<input type="text" class="form-control" name="daterange"
										value="" />
								</div>
							</div>
						</div>
						<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
							<label>Sistema</label>
							<div class="form-group m-b-0">
								<select id="systemId"
									class="form-control show-tick selectpicker"
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
								<select id="statusId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="0">-- Todos --</option>
									<c:forEach items="${statuses}" var="status">
										<c:if test="${status.name ne 'Anulado'}">
											<option value="${status.id }">${status.name }</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="button-demo-refresh flr">
							<button title="Refrescar tabla con filtros!" type="button" class="btn btn-primary setIcon"
								onclick="refreshTable()">
								<span><i
									class="material-icons m-t--2 ">update</i></span>
							</button>
						</div>
					</div>
					<!-- #tableFilters# -->
					<div id="tableSection" class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="body ">
								<div class="body table-responsive">
									<table id="dtReleases"
										class="table table-bordered table-striped table-hover dataTable">
										<thead>
											<tr>
												<th>ID</th>
												<th>N�mero</th>
												<th>Sistema</th>
												<th>Descripci�n</th>
												<th>Observaci�n</th>
												<th>Release</th>
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
				<!-- #tableSection# -->
			</div>
		</div>
	</section>

	<%@include file="../plantilla/footer.jsp"%>

	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script
		src="<c:url value='/static/js/release/releaseQA.js?v=${jsVersion}'/>"></script>
</body>

</html>