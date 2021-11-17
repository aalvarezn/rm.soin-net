<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<link rel="icon" href="<c:url value='/static/images/favicon.ico'/>"
	type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SGRM Info</title>

<!-- Bootstrap Core Css -->
<link
	href="<c:url value='/static/plugins/bootstrap/css/bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- JQuery DataTable Css -->
<link
	href="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" type="text/css">

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">
</head>
<body>
	<input type="hidden" id="systemContent" name="systemContent"
		value="<c:url value='/'/>" />
	<div class="container-fluid m-t-20">

		<section>
			<div class="row clearfix">
				<div class="col-lg-2 col-md-2 col-xs-12 col-sm-12">
					<strong>RELEASE VERSION:</strong> <br> SGRM.RM001.001_2.20200211
					<br>
				</div>
				<div class="col-lg-10 col-md-10 col-xs-12 col-sm-12">
					<a href="<c:url value='/'/> " class="btn btn-primary waves-effect">IR
						A INICIO</a>
				</div>
			</div>
			<br>
			<div class="row clearfix">
				<div class="col-lg-6 col-md-6">
					<table class="table table-bordered  table-hover dataTable">

						<tr>
							<td class="bg-cyan" style="color: black;" colspan="3"><strong>SGRM
									Base Datos</strong></td>
						</tr>
						<tr>
							<td>Estado</td>
							<td>${ds_sgrm}</td>
						</tr>
						<tr>
							<td>ds_username</td>
							<td>${ds_username}</td>
						</tr>
						<tr>
							<td class="bg-cyan" style="color: black;" colspan="3"><strong>CORP
									Base Datos</strong></td>
						</tr>
						<tr>
							<td>Estado</td>
							<td>${ds_corp}</td>
						</tr>
						<tr>
							<td>ds_corp_username</td>
							<td>${ds_corp_username}</td>
						</tr>
					</table>
				</div>
			</div>
			<br>
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12">
					<table class="table table-bordered  table-hover dataTable">
						<tr>
							<td class="bg-cyan" style="color: black;" colspan="3"><strong>Parámetros</strong></td>
						</tr>
						<tr>
							<td class="col-lg-2">Ruta de Archivos</td>
							<td>${fileStore_path}</td>
						</tr>
					</table>
				</div>
			</div>
			<br>
			<div class="row clearfix">
				<div class="col-lg-6 col-md-6 col-xs-12 col-sm-12">
					<table class="table table-bordered  table-hover dataTable">
						<tr>
							<td class="bg-cyan" style="color: black;" colspan="3"><strong>Email
									Propiedades</strong> <!-- 								<button type="button" onclick="testEmail()" -->
								<!-- 									class="btn btn-default flr">PRUEBA</button> --></td>
						</tr>
						<c:forEach var="property" items="${emailProperties}">
							<tr>
								<td>${property.key}</td>
								<td>${property.value}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="col-lg-6 col-md-6 col-xs-12 col-sm-12">
					<table class="table table-bordered  table-hover dataTable">
						<tr>
							<td class="bg-cyan" style="color: black;" colspan="3"><strong>Logs
									del Sistema</strong></td>
						</tr>
						<tr>
							<td>SYSTEM_ERROR</td>
							<td>${systemErrorLog}</td>
							<td><a href="<c:url value='/logDownload-SYSTEM_ERROR'/>"
								download class=""> Descargar </a></td>
						</tr>

						<tr>
							<td>WEBSERVICE</td>
							<td>${webserviceLog}</td>
							<td><a href="<c:url value='/logDownload-WEBSERVICE'/>"
								download class=""> Descargar </a></td>
						</tr>

						<tr>
							<td>RELEASE_ERROR</td>
							<td>${releaseErrorLog}</td>
							<td><a href="<c:url value='/logDownload-RELEASE_ERROR'/>"
								download class=""> Descargar </a></td>
						</tr>

						<tr>
							<td>FILE_READ</td>
							<td>${fileReadLog}</td>
							<td><a href="<c:url value='/logDownload-FILE_READ'/>"
								download class=""> Descargar </a></td>
						</tr>
						<tr>
							<td>ADMIN_ERROR</td>
							<td>${fileAdminLog}</td>
							<td><a href="<c:url value='/logDownload-ADMIN_ERROR'/>"
								download class=""> Descargar </a></td>
						</tr>
					</table>
				</div>
			</div>
		</section>
		<section>
			<!-- Jquery Core Js -->
			<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>
			<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

			<script type="text/javascript"
				src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>

			<!-- Bootstrap Core Js -->
			<script
				src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

			<!-- Jquery DataTable Plugin Js -->
			<script
				src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
			<script
				src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>

			<!-- SweetAlert Plugin Js -->
			<script
				src="<c:url value='/static/plugins/sweetalert/sweetalert.min.js'/>"></script>

			<!-- Input Mask Plugin Js -->
			<script
				src="<c:url value='/static/plugins/jquery-inputmask/jquery.inputmask.bundle.js'/>"></script>
		</section>
	</div>

	<script type="text/javascript">
		function testEmail() {
			swal({
				html : true,
				title : "Prueba de Correo!",
				text : "Ingrese un correo de prueba:",
				type : "input",
				className : "email",
				showCancelButton : true,
				closeOnConfirm : false,
				animation : "slide-from-top",
				inputPlaceholder : "correo@gmail.com"
			}, function(inputValue) {
				if (inputValue === false)
					return false;
				if (inputValue === "") {
					swal.showInputError("Ingrese un correo!");
					return false
				}
				swal("Excelente!", "Ingrese al correo " + inputValue
						+ " para validar la prueba", "success");
			});
		}
	</script>
</body>
</html>