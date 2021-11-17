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

<!-- Waves Effect Css -->
<link href="<c:url value='/static/plugins/node-waves/waves.css'/>"
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

</head>
<body class="theme-grey">
	<input type="text" id="postMSG" name="postMSG" value="${data}">
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

				<!-- #addReleaseSection#  -->
				<%@include file="../release/addRelease.jsp"%>
				<%@include file="../release/changeUserModal.jsp"%>
				<!-- #addReleaseSection#-->

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
									<div class="info-box bg-cyan hover-expand-effect">
										<div class="icon">
											<i class="material-icons">school</i>
										</div>
										<div class="content">
											<div class="text">CERTIFICACI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${userC['certification']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-orange hover-expand-effect">
										<div class="icon">
											<i class="material-icons">priority_high</i>
										</div>
										<div class="content">
											<div class="text">BORRADOR</div>
											<div class="number count-to" data-from="0"
												data-to="${userC['draft']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons">check_box</i>
										</div>
										<div class="content">
											<div class="text">EN REVISI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${userC['review']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-pink hover-expand-effect">
										<div class="icon">
											<i class="material-icons">flag</i>
										</div>
										<div class="content">
											<div class="text">COMPLETADOS</div>
											<div class="number count-to" data-from="0"
												data-to="${userC['completed']}" data-speed="15"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane " id="equipos">
							<div class="row clearfix">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-cyan hover-expand-effect">
										<div class="icon">
											<i class="material-icons">school</i>
										</div>
										<div class="content">
											<div class="text">CERTIFICACI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${teamC['certification']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-orange hover-expand-effect">
										<div class="icon">
											<i class="material-icons">priority_high</i>
										</div>
										<div class="content">
											<div class="text">BORRADOR</div>
											<div class="number count-to" data-from="0"
												data-to="${teamC['draft']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons">check_box</i>
										</div>
										<div class="content">
											<div class="text">EN REVISI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${teamC['review']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-pink hover-expand-effect">
										<div class="icon">
											<i class="material-icons">flag</i>
										</div>
										<div class="content">
											<div class="text">COMPLETADOS</div>
											<div class="number count-to" data-from="0"
												data-to="${teamC['completed']}" data-speed="15"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane " id="sistemas">
							<div class="row clearfix">
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-cyan hover-expand-effect">
										<div class="icon">
											<i class="material-icons">school</i>
										</div>
										<div class="content">
											<div class="text">CERTIFICACI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${systemC['certification']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-orange hover-expand-effect">
										<div class="icon">
											<i class="material-icons">priority_high</i>
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
									<div class="info-box bg-light-green hover-expand-effect">
										<div class="icon">
											<i class="material-icons">check_box</i>
										</div>
										<div class="content">
											<div class="text">EN REVISI&Oacute;N</div>
											<div class="number count-to" data-from="0"
												data-to="${systemC['review']}" data-speed="1000"
												data-fresh-interval="20"></div>
										</div>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
									<div class="info-box bg-pink hover-expand-effect">
										<div class="icon">
											<i class="material-icons">flag</i>
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
									<li role="presentation" class="active"><a href="#releases"
										onclick="changeSelectView(1)" data-toggle="tab">MIS
											RELEASES</a></li>
									<li onclick="changeSelectView(2)" role="presentation"><a
										href="#equipos" data-toggle="tab">MIS EQUIPOS</a></li>
									<li onclick="changeSelectView(3)" role="presentation"><a
										href="#sistemas" data-toggle="tab">TODOS LOS SISTEMAS</a></li>
								</ul>
							</div>
						</div>
						<!-- #END# TAB COUNTS -->

						<!-- Basic Examples -->

						<div id="tableSection"
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

							<div class="body">
								<div class="body table-responsive" style="margin-top: 20px;">
									<table id="dtReleases"
										class="table table-bordered table-striped table-hover dataTable">
										<thead>
											<tr>
												<th>ID</th>
												<th>Sistema</th>
												<th>Número</th>
												<th >Descripción</th>
												<th>Observación</th>
												<th>Release</th>
												<th>Solicitante</th>
												<th>Creado</th>
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
		<a id="buttonAddRelease" type="button"
			class="btn btn-primary btn-fab waves-effect fixedDown"
			<%-- href="<c:url value='/release/release-generate'/>"> --%>
 			onclick="openAddReleaseSection()">
			<i class="material-icons lh-1-8">add</i>
		</a>

	</section>

	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<script type="text/javascript"
		src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>

	<!-- Bootstrap Core Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

	<!-- Select Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-select/js/bootstrap-select.js'/>"></script>

	<!-- Slimscroll Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-slimscroll/jquery.slimscroll.js'/>"></script>

	<!-- Waves Effect Plugin Js -->
	<script src="<c:url value='/static/plugins/node-waves/waves.js'/>"></script>

	<!-- Jquery CountTo Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-countto/jquery.countTo.js'/>"></script>

	<!-- Autosize Plugin Js -->
	<script src="<c:url value='/static/plugins/autosize/autosize.js'/>"></script>

	<!-- Jquery DataTable Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/fnFindCellRowIndexes.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>

	<!-- Bootstrap Notify Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-notify/bootstrap-notify.js'/>"></script>

	<!-- SweetAlert Plugin Js -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>

	<!-- Moment Plugin Js -->
	<script src="<c:url value='/static/plugins/momentjs/moment.js'/>"></script>

	<!-- BlockUI Plugin Js -->
	<script src="<c:url value='/static/plugins/blockPage/blockUI.js'/>"></script>

	<!-- Bootstrap Material Datetime Picker Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js'/>"></script>

	<!-- Custom Js -->

	<script src="<c:url value='/static/js/admin.js?v=${jsVersion}'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script
		src="<c:url value='/static/js/release/release.js?v=${jsVersion}'/>"></script>
</body>

</html>