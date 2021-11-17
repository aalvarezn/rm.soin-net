<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<%@include file="../plantilla/header.jsp"%>

    <!-- Bootstrap Core Css -->
    <link href="<c:url value='/static/plugins/bootstrap/css/bootstrap.css'/>" rel="stylesheet" type="text/css">

    <!-- Animation Css -->
    <link href="<c:url value='/static/plugins/animate-css/animate.css'/>" rel="stylesheet" type="text/css">

    <!-- Morris Chart Css-->
    <link href="<c:url value='/static/plugins/morrisjs/morris.css'/>" rel="stylesheet" type="text/css">

	<!-- JQuery DataTable Css -->
	<link href="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css'/>" rel="stylesheet" type="text/css">
	
	 <!-- Bootstrap Select Css -->
	 <link href="<c:url value='/static/bootstrap-select/css/bootstrap-select.css'/>" type="text/css">
	
    <!-- Sweetalert Css -->
    <link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>" rel="stylesheet" />
    
    <!-- Custom Css -->
    <link href="<c:url value='/static/css/style.css'/>" rel="stylesheet" type="text/css">

    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link href="<c:url value='/static/css/themes/all-themes.css'/>" rel="stylesheet" type="text/css">
</head>
<body class="four-zero-four">
	<div class="four-zero-four-container">
		<div class="error-code">403</div>
		<div class="error-message">ACCESO DENEGADO</div>
		<div class="button-place">
			<a href="<c:url value='/'/> "
				class="btn btn-primary waves-effect">IR A INICIO</a>
		</div>
	</div>

	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>

    <!-- Bootstrap Core Js -->
    <script src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

    <!-- Select Plugin Js -->
    <script src="<c:url value='/static/plugins/bootstrap-select/js/bootstrap-select.js'/>"></script>

    <!-- Slimscroll Plugin Js -->
    <script src="<c:url value='/static/plugins/jquery-slimscroll/jquery.slimscroll.js'/>"></script>
	
	<!-- Jquery CountTo Plugin Js -->
	<script src="<c:url value='/static/plugins/jquery-countto/jquery.countTo.js'/>"></script>
		
	<!-- Autosize Plugin Js -->
	<script src="<c:url value='/static/plugins/autosize/autosize.js'/>"></script>
	
	<!-- Jquery DataTable Plugin Js -->
	<script src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/dataTables.buttons.min.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.flash.min.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/jszip.min.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/pdfmake.min.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/vfs_fonts.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.html5.min.js'/>"></script>
	<script src="<c:url value='/static/plugins/jquery-datatable/extensions/export/buttons.print.min.js'/>"></script>
    
    <!-- Bootstrap Notify Plugin Js -->
    <script src="<c:url value='/static/plugins/bootstrap-notify/bootstrap-notify.js'/>"></script>

    <!-- SweetAlert Plugin Js -->
    <script src="<c:url value='/static/plugins/sweetalert/sweetalert.min.js'/>"></script>
</body>

</html>