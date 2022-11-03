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

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">

<link rel="stylesheet" type="text/css"
	href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
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
	<%@include file="../../plantilla/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>

	<section id="contentSummary" class="content">
		<div class="container-fluid">
			<div class="row clearfix">
				<%@include file="../../rfc/changeStatusModal.jsp"%>
			</div>
			<span class="topArrow"> <i class="material-icons pointer">keyboard_arrow_up</i>
			</span>
			<div class="row clearfix">
				<div class="button-demo flr">
					<a href="<c:url value='/homeRFC'/> " class="btn btn-default">IR
						A INICIO</a>
				</div>
			</div>
			<div class="block-header">
				<p class="font-20">
					<span class="col-blue-grey">RESUMEN ERROR</span> <span
						class="flr font-bold col-cyan ">${baseKnowledge.numError}</span>
				</p>
			</div>
			<hr>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n general</h5>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Codigo error</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${baseKnowledge.numError}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de creacion</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${baseKnowledge.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Componente</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${baseKnowledge.component.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${baseKnowledge.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${baseKnowledge.user.fullName}</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-sm-12">
				<h5 class="titulares">Informacion sobre el error</h5>
			</div>
			<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
				<label for="">Descripci&oacute;n del error.</label>
				<textarea class="areaWidth" rows="" cols="">${baseKnowledge.description }</textarea>
			</div>


			<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
				<label for="">Datos requeridos para escalar.</label>
				<textarea class="areaWidth" rows="" cols="">${baseKnowledge.dataRequired }</textarea>
			</div>
			<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
				<label for="">Notas adicionales.</label>
				<textarea class="areaWidth" rows="" cols="">${baseKnowledge.note }</textarea>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-sm-12">
				<h5 class="titulares">Archivos adjuntos</h5>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
				<div class="clearfix">
					<div class="body table-responsive">
						<table
							class="table tableIni table-bordered table-striped table-hover dataTable no-footer">
							<thead>
								<tr>
									<th class="col-md-8 col-lg-8 col-xs-12 col-sm-12">Nombre</th>
									<th class="col-md-4 col-lg-4 col-xs-12 col-sm-12">Fecha de
										carga</th>
									<th class="col-md-4 col-lg-4 col-xs-12 col-sm-12">Acciones</th>
								</tr>
							</thead>
							<tbody>

								<c:forEach items="${baseKnowledge.files}"
									var="baseKnowledgefile">
									<tr id="${baseKnowledgefile.id}">
										<td>${baseKnowledgefile.name}</td>
										<td><fmt:formatDate
												value="${baseKnowledgefile.revisionDate}" type="both" /></td>
										<td><a
											href="<c:url value='/file/singleDownloadBaseKnowledge-${baseKnowledgefile.id }'/>"
											download class=""> <i class="material-icons col-cyan"
												style="font-size: 30px;">cloud_download</i>
										</a></td>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</div>
				</div>
			</div>


		</div>

		<c:forEach items="${userInfo.authorities}" var="authority">
			<c:if test="${authority.name == 'Gestion Incidencia'}">
				<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
					<div class="m-b-20">
						<c:if test="${baseKnowledgefile.status.name ne 'Obsoleto'}">
							<button type="button" class="btn btn-default setIcon"
								onclick="confirmCancelRFC(${baseKnowledgefile.id})" title="Anular"
								style="background-color: #00294c !important; color: #fff; border: none !important;">
								<span>ANULAR</span><span style="margin-left: 10px;"><i
									class="material-icons m-t--2">highlight_off</i></span>
							</button>
						</c:if>
						<button type="button" class="btn btn-default setIcon"
							onclick="changeStatusRFC(${baseKnowledgefile.id}, '${baseKnowledgefile.numError}' )"
							title="Borrador"
							style="background-color: #00294c !important; color: #fff; border: none !important;">
							<span>CAMBIAR ESTADO</span><span style="margin-left: 10px;"><i
								class="material-icons m-t--2">offline_pin</i></span>
						</button>
					</div>
				</div>
			</c:if>
		</c:forEach>
	
	</section>

	<%@include file="../../plantilla/footer.jsp"%>

	<script src="<c:url value='/static/js/rfc/rfcSummaryActions.js'/>"></script>
	<!-- Validate Core Js -->
	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#contentSummary textarea").parent().removeClass('focused');
			$("#contentSummary textarea").attr("disabled", true);
			autosize($('textarea'));
		});
	</script>

</body>
</html>
