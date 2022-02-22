<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	<%@include file="../../plantilla/admin/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/admin/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>
	<section class="content m-t-80I">
		<div class="container-fluid">
			<!-- 			<div class="row clearfix"> -->
			<!-- 				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"> -->
			<!-- 					<div class="" style="padding-top: -5pc;"> -->
			<!-- 						<h2 class="title-Adm m-t-0">ARBOL DE DEPENDENCIAS</h2> -->
			<!-- 					</div> -->
			<!-- 					<hr> -->
			<!-- 				</div> -->
			<!-- 			</div> -->
			<div class="row clearfix">
				<form action="" id="treeForm">
					<div class="form-group ">
						<label for="to" class="col-sm-2 col-form-label lbtxt m-t-10">Numero
							Release:</label>
						<div class="col-sm-4">
							<div class="form-line">
								<input type="text" id="releaseNumber" name="releaseNumber"
									value="${not empty release.releaseNumber ? release.releaseNumber: ''}" class="form-control" placeholder="#####">
							</div>
						</div>
						<label for="to" class="col-sm-1 col-form-label lbtxt m-t-10">Profundidad:</label>
						<div class="col-sm-1">
							<div class="form-line">
								<input type="number" id="depth" name="depth" value="${not empty release.releaseNumber ? 2 : 0}" min="0" max="10"
									class="form-control" placeholder="0">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="button-demo flr">
								<button type="button" class="btn btn-primary next-step"
									onclick="searchTree()">
									<i class="material-icons iconbtn">search</i> BUSCAR
								</button>
							</div>
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
			<ul class='node-menu'>
				<li id="summary" data-id="" data-release="">Resumen</li>
				<li id="clipboard" data-id="" data-release="">Copiar número de release</li>
				<li id="tree" data-id="" data-release="">Ver árbol</li>
			</ul>
		</div>
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
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>

	<!-- Moment Plugin Js -->
	<script src="<c:url value='/static/plugins/momentjs/moment.js'/>"></script>

	<!-- Vis Plugin Js -->
	<script src="<c:url value='/static/plugins/vis/vis-network.js'/>"></script>

	<!-- 	<script type="text/javascript" -->
	<!-- 		src="https://unpkg.com/alea@1.0.0/alea.js"></script> -->
	<%-- 	<script src="<c:url value='/static/plugins/vis/exampleUtil.js'/>"></script> --%>

	<!-- Bootstrap Material Datetime Picker Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js'/>"></script>

	<!-- Custom Js -->

	<script src="<c:url value='/static/js/admin.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>

	<script src="<c:url value='/static/js/admin/tree.js'/>"></script>
</body>

</html>