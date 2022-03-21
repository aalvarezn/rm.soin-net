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
		<!-- UserModal -->
		<%@include file="../../admin/user/userModal.jsp"%>
		<!-- #END# UserModal -->
	</section>
	<section>
		<!-- changePassword -->
		<%@include file="../../admin/user/changePassword.jsp"%>
		<!-- #END# changePassword -->
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
						<h2 class="title-Adm m-t-0">USUARIOS</h2>
					</div>
					<hr>
				</div>
			</div>
			<div id="tableFilters" class="row clearfix m-t-20">
				<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
					<label>Estado</label>
					<div class="form-group m-b-0">
						<select id="fStatus" class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Todos --</option>
							<option value="1">Activo</option>
							<option value="0">Inactivo</option>
						</select>
					</div>
				</div>
			</div>
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="table-responsive m-b-20">
						<table
							class="table tableIni table-bordered table-striped table-hover dataTable"
							id="userTable">
							<thead>
								<tr>
									<th>Nombre</th>
									<th>Usuario</th>
									<th>Correo</th>
									<th>Últ. Logueo</th>
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
		<a id="buttonAddUser" type="button"
			class="btn btn-primary btn-fab waves-effect fixedDown"
			onclick="createUser()"> <i class="material-icons lh-1-8">add</i>
		</a>
	</section>

	<!-- Script Section -->
	<%@include file="../../plantilla/scriptSection.jsp"%>
	<!-- #END# Script Section -->

	<script src="<c:url value='/static/js/admin/user.js'/>"></script>

</body>

</html>