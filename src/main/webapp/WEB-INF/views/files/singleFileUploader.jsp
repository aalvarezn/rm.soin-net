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

<script type="text/javascript">
	function upload() {
		var cont = getCont();
		var form = $('#fileUploadForm')[0];
		var formData = new FormData(form);

		// Ajax call for file uploaling
		var ajaxReq = $.ajax({
			url : cont + "/file/" + "singleUpload",
			type : 'POST',
			data : formData,
			cache : false,
			contentType : false,
			processData : false,
			success : function(response) {
				setTimeout(function() {
					$('#progressBar').text(0 + '%');
					$('#progressBar').css('width', 0 + '%');
				}, 2000);
			},
			xhr : function() {
				//Get XmlHttpRequest object
				var xhr = $.ajaxSettings.xhr();

				//Set onprogress event handler 
				xhr.upload.onprogress = function(event) {
					var perc = Math.round((event.loaded / event.total) * 100);
					$('#progressBar').text(perc + '%');
					$('#progressBar').css('width', perc + '%');
				};
				return xhr;
			},
			beforeSend : function(xhr) {
				//Reset alert message and progress bar
				$('#alertMsg').text('');
				$('#progressBar').text('');
				$('#progressBar').css('width', '0%');
			}
		});
	}
</script>
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
		<div class="container">
			<h2>Spring MVC - File Upload Example With Progress Bar</h2>
			<hr>
			<!-- File Upload From -->
			<form id="fileUploadForm" action="fileUpload" onsubmit="return false;" method="post"
				enctype="multipart/form-data">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
				<div class="form-group">
					<label>Select File</label> <input class="form-control" type="file"
						name="file">
				</div>
				<div class="form-group">
					<button class="btn btn-primary" onclick="upload();">Upload</button>
				</div>
			</form>
			<br />

			<!-- Bootstrap Progress bar -->
			<div class="progress">
				<div id="progressBar" class="progress-bar progress-bar-success"
					role="progressbar" aria-valuenow="0" aria-valuemin="0"
					aria-valuemax="100" style="width: 0%">0%</div>
			</div>

			<!-- Alert -->
			<div id="alertMsg" style="color: red; font-size: 18px;"></div>
		</div>
	</section>
	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>

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
		src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/dataTables.buttons.min.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.flash.min.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/jszip.min.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/pdfmake.min.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/vfs_fonts.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.html5.min.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.print.min.js'/>"></script>

	<!-- Bootstrap Notify Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-notify/bootstrap-notify.js'/>"></script>

	<!-- SweetAlert Plugin Js -->
	<script
		src="<c:url value='/static/plugins/sweetalert/sweetalert.min.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>
	<script src="<c:url value='/static/js/release/profile.js'/>"></script>

</body>

</html>