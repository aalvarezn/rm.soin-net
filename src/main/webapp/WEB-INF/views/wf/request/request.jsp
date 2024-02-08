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

<!-- TagInput Js -->
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">

<link
	href="<c:url value='/static/plugins/multi-select/css/multi-select.css'/>"
	rel="stylesheet" type="text/css">

<!-- Custom Css -->
<link
	href="<c:url value='/static/plugins/summernote/css/summernote.min.css'/>"
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
	<section>
		<!-- 		workFlowModal -->
		<%@include file="../../wf/request/nodeModal.jsp"%>
		<!-- 		#END# workFlowModal -->
	</section>
	<!-- Top Bar -->
	<%@include file="../../plantilla/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>
	<section class="content m-t-90I">
		<div class="container-fluid">
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="" style="padding-top: -5pc;">
						<h2 class="title-Adm m-t-0">REQUERIMIENTOS</h2>
					</div>
					<hr>
				</div>
			</div>
			<div class="row clearfix">

				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Proyecto</label>
					<div class="form-group">
						<select id="proyectFilter"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${projects}" var="project">
								<option value="${project.id }" >${project.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Tipo Requerimiento</label>
					<div class="form-group">
						<select id="typeRequestFilter"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${typeRequests}" var="typeRequest">
								<option value="${typeRequest.id }" >${typeRequest.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
					<div id="tableSection" class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="body ">
								<div class="body table-responsive">
									<table id="requestTable"
										class="table table-bordered table-striped table-hover dataTable">
										<thead>
											<tr>
												<th></th>
												<th>Código Soin</th>
												<th>Código ICE</th>
												<th style="width: 33%;">Descripción</th>
												<th>Proyecto</th>
												<th>Tipo requerimiento</th>
												<th class="actCol" style="text-align: center;">Aprobaci&oacute;n
													Automatica</th>
													<th>Tr&aacute;mite automatico</th>

											</tr>
										</thead>

									</table>
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>
		</div>

	</section>

	<!-- Script Section -->
	<%@include file="../../plantilla/scriptSection.jsp"%>
	<!-- #END# Script Section -->
	<script src="<c:url value='/static/js/wf/tpo/request.js'/>"></script>
	<script
		src="<c:url value='/static/js/request/excelSync.js?v=${jsVersion}'/>"></script>

</body>

</html>