<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<!-- Bootstrap Select Css -->
<link
	href="<c:url value='/static/bootstrap-select/css/bootstrap-select.css'/>"
	type="text/css">

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" />

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">
</head>

<body class="theme-grey">
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

	<section class="content">
		<div class="container-fluid">
			<div class="block-header">
				<h2>PERFIL DE USUARIO</h2>
			</div>
		</div>

		<div class="col-xs-12 col-sm-3">
			<div class="card profile-card">
				<div class="profile-header">&nbsp;</div>
				<div class="profile-body">
					<div class="image-area">
						<img src="<c:url value='/static/images/persona2.png'/>"
							alt="AdminBSB - Profile Image"
							style="width: 128px; height: 128px;">
					</div>
					<div class="content-area">
						<h3>${user.fullName }</h3>
						<p style="color: #00b5d4">
							<strong>${user.username }</strong>
						</p>
					</div>
				</div>
				<div class="profile-footer" style="height: 128px;">
					<p>${user.email }</p>
				</div>
			</div>
		</div>

		<div class="col-xs-12 col-sm-9">
			<div class="card profile-card" style="padding: 10px 10px 0px 10px;">
				<form id="formChangePassword" action="changePassword" method="post">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="row clearfix">
						<div class="col-sm-12">
							<label>Contrase&ntilde;a <span class="text-danger">*</span></label>
							<div class="form-group">
								<div class="form-line">
									<input type="password" class="form-control" id="oldPassword"
										oninput="myEvent('oldPassword',false)" name="oldPassword"
										placeholder="Ingrese la antigua contraseña">
										
								</div>
							</div>
						</div>
					</div>

					<div class="row clearfix">

						<div class="col-sm-6">
							<label>Nueva Contrase&ntilde;a <span class="text-danger">*</span></label>
							<div class="form-group">
								<div class="form-line">
									<input type="password" max class="form-control" id="newPassword"
										oninput="myEvent('newPassword',true)" name="newPassword"
										placeholder="Ingrese una nueva contraseña" maxlength="32" minlength="8">
										<div class="help-info">Min. 8, Max. 32 caracteres</div>
										<label
										id="mensajePass_error" class="control-label col-md-12 text-danger"
										style="display: none;">Las contraseñas no coinciden</label>
								</div>
							</div>
						</div>

						<div class="col-sm-6">
							<label>Confirmaci&oacute;n de Contrase&ntilde;a <span
								class="text-danger">*</span></label>
							<div class="form-group">
								<div class="form-line">
									<input type="password" class="form-control"
										id="confirmPassword" name="confirmPassword"
										oninput="myEvent('confirmPassword',true)"
										placeholder="Verifique la nueva contraseña" maxlength="32" minlength="8">
										<div class="help-info">Min. 8, Max. 32 caracteres</div> <label
										id="mensaje_error" class="control-label col-md-12 text-danger"
										style="display: none;">Las contraseñas no coinciden</label>
								</div>
							</div>
						</div>

					</div>
				</form>
				<div class="form-group">
					<label>Pertenece a los grupos</label>
				</div>

				<div class="row clearfix">
					<c:forEach items="${details.authorities}" var="authority">
						<div class="col-sm-3">
							<div class="form-group">
								<p>${authority.name }</p>
							</div>
						</div>
					</c:forEach>
				</div>

				<div class="row clearfix">
					<div class="footer_perfil">
						<p>
							Su contraseña no puede asemejarse tanto a su otra información
							personal. <br />Debe contener más de 8 caractéres. <br />Su
							contraseña no puede ser completamente numérica.<br /> (*) Campo
							requerido.
						</p>
					</div>
				</div>


			</div>

			<div class="alig_btn">
				<button type="button" onclick="confirmChangePassword()" disabled
					id="change" class="btn btn-primary waves-effect">GUARDAR
					CAMBIOS</button>
			</div>

		</div>

	</section>
	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<!-- Bootstrap Core Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

	<!-- Select Plugin Js -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.9/dist/js/bootstrap-select.min.js"></script>

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

	<!-- Bootstrap Material Datetime Picker Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js'/>"></script>

	<!-- TagInput Js -->
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

	<!-- Input Mask Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-inputmask/jquery.inputmask.bundle.js'/>"></script>

	<!-- BlockUI Plugin Js -->
	<script src="<c:url value='/static/plugins/blockPage/blockUI.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script src="<c:url value='/static/js/profile/profile.js'/>"></script>

</body>

</html>