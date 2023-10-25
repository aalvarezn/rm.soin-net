<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
<!-- TagInput Js -->
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css'/>" />
</head>

<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
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

	<section id="contentSummary" class="content">
		<div class="container-fluid">
			<div class="row clearfix">
				<%@include file="../rfc/changeStatusModal.jsp"%>
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
					<span class="col-blue-grey">RESUMEN RFC</span> <span
						class="flr font-bold col-cyan ">${rfc.numRequest}</span>
				</p>
			</div>
			<hr>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n general</h5>
				</div>

				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">RFC N°</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.numRequest}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha de actualizaci&oacute;n</label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>
									<fmt:formatDate value="${rfc.requestDate }"
										pattern="dd/MM/YYYY HH:mm:ss" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Sistema</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.systemInfo.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Estado</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.status.name}</p>
						</div>
					</div>
				</div>

			</div>
			<div class="row clearfix m-t-10">
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-t-10">
					<label for="email_address">Solicitado por</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.user.fullName}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Impacto</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.impact.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Tipo de cambio</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.typeChange.name}</p>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 p-t-10">
					<label for="email_address">Prioridad</label>
					<div class="form-group m-b-0i">
						<div class="form-line disabled">
							<p>${rfc.priority.name}</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-sm-12">
					<h5 class="titulares">Fecha y hora propuesta para ejecutar el
						cambio</h5>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha y hora Inicio </label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${rfc.requestDateBegin }</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6 p-t-10">
					<label for="email_address">Fecha y hora Fin </label>
					<div class="form-group m-b-0i">
						<div class="form-group m-b-0i">
							<div class="form-line disabled">
								<p>${rfc.requestDateFinish }</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 m-t-10">
					<label for="">Raz&oacute;n del cambio.</label>
					<textarea disabled class="areaWidth" rows="" cols="">${rfc.reasonChange }</textarea>
				</div>


				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Efecto si no se implementa el cambio.</label>
					<textarea disabled class="areaWidth disabled" rows="" cols="">${rfc.effect }</textarea>
				</div>
			</div>
			<div class="row clearfix m-t-10">
				<div class="col-sm-12">
					<h5 class="titulares">Informaci&oacute;n de Cambio</h5>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Sistemas impactados</label>

					<div id="listSystems">
						<ul class="nav nav-pills disabled">


							<c:forEach items="${systemsImplicated}" var="system">

								<li class="nav-item dependency m-r-10">${system}</li>

							</c:forEach>

						</ul>
					</div>
				</div>

				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
					<label for="">Releases a instalar en producci&oacute;n</label>

					<div id="listSystems">
						<ul class="nav nav-pills disabled">


							<c:forEach items="${rfc.releases}" var="release">

								<li class="nav-item dependency m-r-10">${release.releaseNumber}</li>

							</c:forEach>

						</ul>
					</div>
				</div>
				<c:if test="${totalObjects>0}">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
						<div class="row clearfix">
							<div class="col-sm-12">
								<h5 class="titulares">Releases y cantidad de objetos a
									instalar</h5>
							</div>

							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive m-b-20">
									<table
										class="table tableIni table-bordered table-striped table-hover dataTable"
										id="userTable">
										<thead>
											<tr>
												<th>N&uacute;mero Release</th>
												<th>Descripci&oacute;n</th>
												<th>Total de objetos</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${rfc.releases}" var="release">
												<tr>
													<td>${release.releaseNumber}</td>
													<td>${release.description}</td>
													<td>${release.objects.size()}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<label for="">Total de objetos a instalar:
										${totalObjects }</label>
								</div>

							</div>
						</div>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
						<div class="row clearfix">
							<div class="col-sm-12">
								<h5 class="titulares">Objetos a instalar</h5>
							</div>

							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="table-responsive m-b-20">
									<table
										class="table tableIni table-bordered table-striped table-hover dataTable"
										id="userTable">
										<thead>
											<tr>
												<th style="width: 338px;">Release al que pertenece</th>
												<th style="width: 338px;">Nombre objeto</th>
												<th>Descripci&oacute;n</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listObjects}" var="object">
												<tr>
													<td>${object.numRelease}</td>
													<td>${object.name}</td>
													<td>${object.description}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>

							</div>
						</div>
					</div>
				</c:if>
			</div>
			<div class="row clearfix">
				<div class="col-sm-12">
					<h5 class="titulares">Detalles de la implementaci&oacute;n</h5>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 disabled">
					<label for="">Detalles</label>
					<textarea disabled class="areaWidth" rows="" cols="">${rfc.detail }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 disabled">
					<label for="">Plan de retorno</label>
					<textarea disabled class="areaWidth" rows="" cols="">${rfc.returnPlan }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 disabled">
					<label for="">Evidencias</label>
					<textarea disabled class="areaWidth" disabled rows="" cols="">${rfc.evidence }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 disabled">
					<label for="">Requisitos especiales</label>
					<textarea disabled class="areaWidth" rows="" cols="">${rfc.requestEsp }</textarea>
				</div>
				<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10 disabled">
					<label for="">Requiere base de datos</label>
					<div class="switch">
						<c:choose>
							<c:when test="${rfc.requiredBD}">
								<label>No<input type="checkbox" checked="checked"
									disabled="disabled"><span class="lever"></span>S&iacute;
								</label>
							</c:when>
							<c:otherwise>
								<label>No<input type="checkbox" disabled="disabled"><span
									class="lever"></span>S&iacute;
								</label>
							</c:otherwise>
						</c:choose>

					</div>
				</div>
				<c:if test="${rfc.requiredBD}">
					<div class="col-md-12 col-lg-12 col-xs-12 col-sm-12 m-b-10">
						<label for="">Esquemas necesarios</label>
						<div class="form-line disabled">
							<p>${rfc.schemaDB }</p>
						</div>
					</div>
				</c:if>
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
										<th class="col-md-8 col-lg-8 col-xs-8 col-sm-8">Nombre</th>
										<th class="col-md-4 col-lg-4 col-xs-4 col-sm-4">Fecha de
											carga</th>
										<th class="col-md-1 col-lg-1 col-xs-12 col-sm-12">Acciones</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${rfc.files}" var="fileRFC">
										<tr id="${fileRFC.id}">
											<td>${fileRFC.name}</td>
											<td><fmt:formatDate value="${fileRFC.revisionDate}"
													type="both" /></td>
											<td><a
												href="<c:url value='/file/singleDownloadRFC-${fileRFC.id }'/>"
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

				<c:forEach items="${userInfo.authorities}" var="authority">
					<c:if test="${authority.name == 'Release Manager'}">
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
							<div class="m-b-20">
								<c:if test="${rfc.status.name ne 'Anulado'}">
									<button type="button" class="btn btn-default setIcon"
										onclick="confirmCancelRFC(${rfc.id})" title="Anular"
										style="background-color: #00294c !important; color: #fff; border: none !important;">
										<span>ANULAR</span><span style="margin-left: 10px;"><i
											class="material-icons m-t--2">highlight_off</i></span>
									</button>
								</c:if>
								<button type="button" class="btn btn-default setIcon"
									onclick="changeStatusRFC(${rfc.id}, '${rfc.numRequest}','${rfc.siges.emailTemplate.cc}' )"
									title="Borrador"
									style="background-color: #00294c !important; color: #fff; border: none !important;">
									<span>CAMBIAR ESTADO</span><span style="margin-left: 10px;"><i
										class="material-icons m-t--2">offline_pin</i></span>
								</button>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</section>

	<%@include file="../plantilla/footer.jsp"%>

	<script src="<c:url value='/static/js/rfc/rfcSummaryActions.js'/>"></script>
	<!-- Validate Core Js -->
	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

</body>
</html>