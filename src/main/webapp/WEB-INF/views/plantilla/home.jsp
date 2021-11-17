<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<%@include file="header.jsp"%>

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
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page Loader -->
	<%@include file="pageLoader.jsp"%>
	<!-- #END# Page Loader -->

	<!-- Overlay For Sidebars -->
	<div class="overlay"></div>
	<!-- #END# Overlay For Sidebars -->

	<!-- Top Bar -->
	<%@include file="topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>
	<section class="content m-t-80I">
		<div class="container-fluid"></div>
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
	-->

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
	<script
		src="<c:url value='/static/plugins/sweetalert/sweetalert.min.js'/>"></script>

	<!-- Moment Plugin Js -->
	<script src="<c:url value='/static/plugins/momentjs/moment.js'/>"></script>

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
	<script src="<c:url value='/static/js/release/release.js'/>"></script>
</body>

</html>