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
					value="${_csrf.token}" /> <input type="hidden" id="eId"
					value="${email.id}" />
				<div class="row clearfix">
					<div class="form-group ">
						<label for="to" class="col-sm-1 col-form-label lbtxt m-t-10">Nombre:</label>
						<div class="col-sm-7">
							<div class="form-line">
								<input type="text" id="eName" name="eName" value="${email.name}"
									class="form-control" placeholder="">
							</div>
						</div>
						<div class="col-sm-4 m-t-10">
							<div class="button-demo flr">
								<button id="save" type="button"
									class="btn btn-default prev-step" onclick="saveEmail()">
									<i class="material-icons iconbtn">save</i> GUARDAR
								</button>
								<button id="update" type="button"
									class="btn btn-default prev-step" onclick="updateEmail()">
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
								<input type="text" id="eTo" name="eTo" value="${email.to}"
									class="form-control tagInit" placeholder="">
							</div>
						</div>
						<label for="to" class="col-sm-1 col-form-label lbtxt m-t-10">CC:</label>
						<div class="col-sm-5 ">
							<div class="form-line">
								<input type="text" id="eCc" name="eCc" value="${email.cc}"
									class="form-control tagInit" placeholder="">
							</div>

						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="form-group">
						<label for="subject" class="col-sm-1 col-form-label lbtxt m-t-10">Asunto:</label>
						<div class="col-sm-11">
							<div class="form-line">
								<input type="text" id="eSubject" name="eSubject"
									value="${email.subject}" class="form-control" placeholder="">
							</div>

						</div>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-sm-12">
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{userName}}">Solicitante</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{releaseNumber}}">Número
							Release</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{priority}}">Prioridad</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{risk}}">Riesgo</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{impact}}">Impacto</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{description}}">Descripción</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{observation}}">Observación</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))"
							data-type="{{functionalSolution}}">Solución Funcional</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))"
							data-type="{{technicalSolution}}">Solución Técnica</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{ambient}}">Ambientes</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))"
							data-type="{{minimalEvidence}}">Pruebas</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{dependencies}}">Dependencias</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{objects}}">Objetos</button>
						<button type="button" class="btn btn-primary btn-xs"
							onclick="copyToClipboard($(this))" data-type="{{version}}">Versión</button>
					</div>
				</div>
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
						<textarea id="eBody">${email.body}</textarea>
					</div>
				</div>
			</form>


		</div>
	</section>
	<!-- Script Section -->
	<%@include file="../../plantilla/scriptSection.jsp"%>
	<!-- #END# Script Section -->

	<script
		src="<c:url value='/static/js/admin/editEmail.js'/>"></script>

</body>

</html>