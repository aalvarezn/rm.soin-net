<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>
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

<!-- Bootstrap Material Datetime Picker Css -->
<link
	href="<c:url value='/static/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css'/>"
	rel="stylesheet" type="text/css">

<!-- Morris Chart Css-->
<link href="<c:url value='/static/plugins/morrisjs/morris.css'/>"
	rel="stylesheet" type="text/css">

<!-- JQuery DataTable Css -->
<link
	href="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Bootstrap Select Css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.9/dist/css/bootstrap-select.min.css">

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" type="text/css">

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


	<!-- Vis Plugin Js -->
	<script src="<c:url value='/static/plugins/vis/vis-network.js'/>"></script>

<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css'/>" />

<!-- Linearicons -->
<link rel="stylesheet"
	href="<c:url value='/static/plugins/linearicons/css/linearicon.min.css'/>" />

<link rel="stylesheet"
	href="<c:url value='/static/plugins/jquery-file-upload/css/jquery.fileupload.css'/>" />
<link rel="stylesheet"
	href="<c:url value='/static/plugins/jquery-file-upload/css/jquery.fileupload-ui.css'/>" />




<style type="text/css">
.alert {
	width: 20%;
}

.dataTables_paginate {
	float: initial;
	font-size: 15px;
}

table.dataTable tbody tr.selected {
	background-color: #b0bed9;
}

tr.selected {
	background-color: #acbad4;
}
.noclick {
  pointer-events: none;
}
</style>

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

	<section>
		<%@include file="../release/trackingReleaseModal.jsp"%>
		<%@include file="../rfc/treeModal.jsp"%>
		<!-- addObjectModal -->
		<%@include file="../release/addObjectModal.jsp"%>
		<!-- #END# addObjectModal -->

		<!-- addObjectModal -->
		<%@include file="../rfc/previewRFCModal.jsp"%>
		<!-- #END# addObjectModal -->

		<!-- addFileModal -->
		<%@include file="../release/addFileModal.jsp"%>
		<!-- #END# addFileModal -->

		<!-- environmentModal -->
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<%@include file="../release/environmentActionModals.jsp"%>
		</div>
		<!-- #environmentModal -->

		<!-- addCSVModal -->
		<%@include file="../release/addCSVModal.jsp"%>
		<!-- #END# addCSVModal -->

		<!-- addDetailModal -->
		<%@include file="../release/crontabButton/addDetailModal.jsp"%>
		<!-- #END# addDetailModal -->
	</section>

	<section class="content">
		<form id="generateRFCForm" role="form">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

			<div class="container-fluid">
				<div class="row">
					<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
						<h5 class="font-20 greyLigth">Nuevo RFC</h5>
					</div>
					<div class="col-md-8 col-lg-10 col-sm-12 col-xs-12 setReleaseIcon">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<h5 class="rel_num">
								<span>RFC: </span>${rfc.numRequest} <input type="hidden"
									id="rfcId" value="${rfc.id}"> <input type="hidden"
									id="rfcNumber" value="${rfc.numRequest}">
									 <input type="hidden"
									id="systemInfoId" value="${rfc.systemInfo.id}">
									
							</h5>
						</div>
						<div
							class=" col-lg-6 col-md-6 col-sm-12 col-xs-12 align-right p-r-0">

							<button type="button" class="btn btn-default setIcon"
								onclick="previewRFC()" title="RESUMEN"
								style="background-color: #00294c !important; color: #ffffff; border: none !important;">
								<span>VER RESUMEN</span><span style="margin-left: 10px;"><i
									class="material-icons"
									style="font-size: 28px; margin-top: -3px;">pageview</i></span>
							</button>

							<button type="button" id="btnSave"
								class="btn btn-default setIcon" onclick="sendRFC()"
								title="GUARDAR"
								style="background-color: #00294c !important; color: #fff; border: none !important;">
								<span id="btnText">GUARDAR</span><span
									style="margin-left: 10px;"><i id="btnIcon"
									class="material-icons m-t--2">check_box</i></span>
							</button>
						</div>
					</div>
				</div>
				<div class="d-flex justify-content-center">
					<div class="spinner-border" role="status">
						<span class="sr-only">Loading...</span>
					</div>
				</div>
				<hr>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="row clearfix">
						<section>
							<div class="wizard">
								<div class="wizard-inner">
									<div class="connecting-line"></div>
									<ul class="nav nav-tabs stepper" role="tablist">

										<li id="1" role="presentation" class="active without-line"><a
											class="hideLineTab" href="#step1" data-toggle="tab"
											aria-controls="step1" role="tab" title=""> <span
												class="round-tab"> 1 </span> <span id="step1Errors"
												style="visibility: hidden;" class="labelCount_Error">
													<i class="material-icons spanError">warning</i>
											</span>
										</a></li>

										<li id="2" role="presentation" class="without-line "><a
											href="#step2" data-toggle="tab" aria-controls="step2"
											role="tab" title=""> <span class="round-tab"> 2 </span> <span
												id="step2Errors" style="visibility: hidden;"
												class="labelCount_Error"><i
													class="material-icons spanError">warning</i></span>
										</a></li>
										<li id="3" role="presentation" class="without-line "><a
											href="#step3" data-toggle="tab" aria-controls="step3"
											role="tab" title=""> <span class="round-tab"> 3 </span> <span
												id="step3Errors" style="visibility: hidden;"
												class="labelCount_Error"><i
													class="material-icons spanError">warning</i></span>
										</a></li>
										<li id="4" role="presentation" class="without-line "><a
											href="#step4" data-toggle="tab" aria-controls="step4"
											role="tab" title=""> <span class="round-tab"> 4 </span> <span
												id="step4Errors" style="visibility: hidden;"
												class="labelCount_Error"><i
													class="material-icons spanError">warning</i></span>
										</a></li>
										<li id="5" role="presentation" class="without-line"><a
											href="#step5" data-toggle="tab" aria-controls="step5"
											role="tab" title=""> <span class="round-tab"> 5 </span>
										</a></li>
									</ul>
								</div>
								<div class="tab-content">
									<!--Step_1 -->
									<div class="tab-pane animated fadeIn active" role="tabpanel"
										id="step1">
										<div class="body">
											<%@include file="../rfc/sectionsEditRFC/section_1.jsp"%>
										</div>
										<div class="button-demo flr">
											<button type="button" id="nextStep" class="btn btn-primary next-step"
												style="margin-bottom: 100px;">SIGUIENTE</button>
										</div>
									</div>
									<!--#Step_1 -->

									<!--Step_2 -->
									<div class="tab-pane animated fadeIn" role="tabpanel"
										id="step2">
										<div class="body">
											<%@include file="../rfc/sectionsEditRFC/section_2.jsp"%>
										</div>
										<div class="button-demo flr">
											<button type="button"  class="btn btn-default prev-step"
												style="margin-bottom: 100px;">ANTERIOR</button>
											<button type="button" class="btn btn-primary next-step"
												style="margin-bottom: 100px;">SIGUIENTE</button>
										</div>
									</div>
									<!--#Step_2 -->

									<!--Step_3 -->
									<div class="tab-pane animated fadeIn" role="tabpanel"
										id="step3">
										<div class="body">
											<%@include file="../rfc/sectionsEditRFC/section_3.jsp"%>

										</div>
										<div class="button-demo flr">
											<button type="button" class="btn btn-default prev-step"
												style="margin-bottom: 30px;">ANTERIOR</button>
											<button type="button" class="btn btn-primary next-step"
												style="margin-bottom: 30px;">SIGUIENTE</button>
										</div>
									</div>
									<!--#Step_3 -->

									<!--Step_4 -->
									<div class="tab-pane animated fadeIn" role="tabpanel"
										id="step4">
										<div class="body">
											<%@include file="../rfc/sectionsEditRFC/section_4.jsp"%>
										</div>
										<div class="button-demo flr">
											<button type="button" class="btn btn-default prev-step">ANTERIOR</button>
											<button type="button" class="btn btn-primary next-step">SIGUIENTE</button>
										</div>
									</div>
									<!--#Step_4 -->
									<!--Step_5 -->
									<div class="tab-pane animated fadeIn" role="tabpanel"
										id="step5">
										<div class="body">
											<%@include file="../rfc/sectionsEditRFC/section_5.jsp"%>
										</div>
										<div class="button-demo flr p-t-20">
											<button type="button" class="btn btn-default prev-step">ANTERIOR</button>
											<%-- 											<c:if test="${release.status.name == 'Borrador'}"> --%>
											<button id="applyFor" onclick="requestRFC()" type="button"
												class="btn btn-primary">SOLICITAR</button>
											<%-- 											</c:if> --%>
										</div>
									</div>
									<!--#Step_5 -->
									<div class="clearfix"></div>
								</div>
							</div>
						</section>
					</div>
				</div>
			</div>
		</form>
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
	<script src="<c:url value='/static/plugins/momentjs/moment.js'/>"></script>
	<script type="text/javascript"
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/locale/es.js"></script>
	<!-- Jquery DataTable Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/fnFindCellRowIndexes.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>


	<script
		src="<c:url value='/static/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js'/>"></script>
	<script src="<c:url value='/static/js/demo.js'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/forms/basic-form-elements.js'/>"></script>
	<script
		src="<c:url value='/static/js/pages/tables/jquery-datatable.js'/>"></script>

	<script src="<c:url value='/static/js/rfc/editRFC.js'/>"></script>
	<script src="<c:url value='/static/js/rfc/rfcFileUpload.js'/>"></script>

	<!-- Linearicons -->
	<script src="<c:url value='/static/plugins/linearicons/js/linearicons.min.js'/>"></script>


</body>

</html>