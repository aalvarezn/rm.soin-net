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

<style>
.tagsinput {
	border: none;
}

.alert {
	width: 20%;
}
</style>

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
	<section class="content m-t-70I">
		<div class="container-fluid">

			<form id="emailForm" role="form">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="hidden" id="emailId"
					value="${email.id}" />
				<div class="row clearfix">
					<div class="form-group ">
						<label for="to" class="col-sm-1 col-form-label lbtxt m-t-10">Nombre:</label>
						<div class="col-sm-7">
							<div class="form-line">
								<input type="text" id="name" name="name" value="${email.name}"
									class="form-control" placeholder="">
							</div>
							<label id="name_error" class="error fieldError" for="name"
								style="visibility: hidden">Campo Requerido.</label>
						</div>
						<div class="col-sm-4 m-t-10">
							<div class="button-demo flr">
								<button type="button" class="btn btn-default prev-step"
									onclick="updateEmail()">
									<i class="material-icons iconbtn">save</i> ACTUALIZAR
								</button>
								<button type="button" class="btn btn-primary next-step"
									onclick="sendEmailTest()">
									<i class="material-icons iconbtn">email</i> ENVIAR
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="form-group">
						<label for="subject" class="col-sm-1 col-form-label lbtxt m-t-10">Para:</label>
						<div class="col-sm-5">
							<div class="form-line">
								<input type="text" id="to" name="to" value="${email.to}"
									class="form-control tagInit" placeholder="">
							</div>
							<label id="to_error" class="error fieldError activeError"
								for="name" style="visibility: hidden;">Valor requerido.</label>
						</div>
						<label for="to" class="col-sm-1 col-form-label lbtxt m-t-10">CC:</label>
						<div class="col-sm-5 ">
							<div class="form-line">
								<input type="text" id="cc" name="cc" value="${email.cc}"
									class="form-control tagInit" placeholder="">
							</div>
							<label id="cc_error" class="error fieldError activeError"
								for="name" style="visibility: hidden;">Valor requerido.</label>
						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="form-group">
						<label for="subject" class="col-sm-1 col-form-label lbtxt m-t-10">Asunto:</label>
						<div class="col-sm-11">
							<div class="form-line">
								<input type="text" id="subject" name="subject"
									value="${email.subject}" class="form-control" placeholder="">
							</div>
							<label id="subject_error" class="error fieldError activeError"
								for="name" style="visibility: hidden;">Valor requerido.</label>
						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-sm-12">
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{releaseNumber}}">Número
							Release</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))"
							data-type="{{functionalSolution}}">Solución Funcional</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))"
							data-type="{{technicalSolution}}">Solución Técnica</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{ambient}}">Ambientes</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{dependencies}}">Dependencias</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{objects}}">Objetos</button>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<textarea id="htmlBody">${email.html}</textarea>
					</div>
				</div>
			</form>


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

	<!-- summernote -->
	<script
		src="<c:url value='/static/plugins/summernote/summernote.min.js'/>"></script>

	<!-- summernote -->
	<script
		src="<c:url value='/static/plugins/summernote/lang/summernote-es-ES.js'/>"></script>

	<!-- TagInput Js -->
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js?v=${jsVersion}'/>"></script>
	<script src="<c:url value='/static/js/admin/email.js?v=${jsVersion}'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>

</body>

</html>