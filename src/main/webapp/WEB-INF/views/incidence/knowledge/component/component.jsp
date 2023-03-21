<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<%@include file="../../../plantilla/header.jsp"%>
<!-- Style Section -->
<%@include file="../../../plantilla/styleSection.jsp"%>
<!-- #END# Style Section -->
</head>
<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page Loader -->
	<%@include file="../../../plantilla/pageLoader.jsp"%>
	<!-- #END# Page Loader -->

	<!-- Overlay For Sidebars -->
	<div class="overlay"></div>
	<!-- #END# Overlay For Sidebars -->

	<!-- Top Bar -->
	<%@include file="../../../plantilla/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- EmailModal -->
		<%@include file="../../../incidence/knowledge/component/componentModal.jsp"%>
		<!-- #END# EmailModal -->
	</section>

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../../plantilla/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>
	<section class="content m-t-90I">
		<div class="container-fluid">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="" style="padding-top: -5pc;">
						<h2 class="title-Adm m-t-0">Componentes base de conocimiento</h2>
					</div>
					<hr>
				</div>
			</div>
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="table-responsive m-b-20">
						<table class="table table-bordered table-striped table-hover"
							id="statusRFCTable">
							<thead>
								<tr>
									<th>Nombre</th>
									<th>Sistema</th>
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
		</div>
		<a id="buttonAddSiges" type="button"
			class="btn btn-primary btn-fab waves-effect fixedDown"
			onclick="addStatusRFC()"> <i class="material-icons lh-1-8">add</i>
		</a>
	</section>

	<!-- Script Section -->
	<%@include file="../../../plantilla/scriptSection.jsp"%>
	<!-- #END# Script Section -->
	<script
		src="<c:url value='/static/js/baseKnowledge/component.js'/>"></script>
	

</body>

</html>