<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<%@include file="../../plantilla/header.jsp"%>

<!-- Style Section -->
<%@include file="../../plantilla/styleSection.jsp"%>
<!-- #END# Style Section -->

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
		<!-- EmailModal -->
		<%@include file="../../admin/request/requestModal.jsp"%>
		<!-- #END# EmailModal -->
	</section>

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/admin/leftbar.jsp"%>
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
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="button-demo flr">
										<button type="button" class="btn btn-primary setIcon"
											onclick="openExcelSync()">
											<span>SINCRONIZAR EXCEL</span><span><i
												class="material-icons m-t--2 ">update</i></span>
										</button>
									</div>
								</div>
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Proyecto</label>
					<div class="form-group">
						<select id="proyectFilter"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${projects}" var="project">
								<option id="${project.id }" value="${project.code }">${project.code }</option>
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
								<option id="${typeRequest.id }" value="${typeRequest.code }">${typeRequest.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="table-responsive m-b-20">
						<table
							class="table table-bordered table-striped table-hover dataTable"
							id="requestTable">
							<thead>
								<tr>
									<th>Código Soin</th>
									<th>Código ICE</th>
									<th>Descripción</th>
									<th>Proyecto</th>
									<th>Tipo requerimiento</th>
									<th class="actCol" style="text-align: center;">Activo</th>
									<th class="actCol" style="text-align: center;">Acciones</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<a id="buttonAddRequest" type="button"
			class="btn btn-primary btn-fab waves-effect fixedDown"
			onclick="addRequest()"> <i class="material-icons lh-1-8">add</i>
		</a>
	</section>

	<!-- Script Section -->
	<%@include file="../../plantilla/scriptSection.jsp"%>
	<!-- #END# Script Section -->
	<script
		src="<c:url value='/static/js/admin/request.js'/>"></script>
	<script
		src="<c:url value='/static/js/request/excelSync.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>

	<script
		src="<c:url value='/static/plugins/multi-select/js/jquery.multi-select.js'/>"></script>
</body>

</html>