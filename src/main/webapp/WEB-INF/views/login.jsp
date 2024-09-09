<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
<%@include file="plantilla/header.jsp"%>

<!-- Bootstrap Core Css -->
<link
	href="<c:url value='/static/plugins/bootstrap/css/bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Animation Css -->
<link href="<c:url value='/static/plugins/animate-css/animate.css'/>"
	rel="stylesheet" type="text/css">

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">
</head>

<body class="login-page">

	<div class="img"
		style="width: 100%; background-image: url(<c:url value='/static/images/forma_login.png'/>); background-repeat: no-repeat; background-position: bottom; height: 25%; position: absolute; left: 0px; bottom: 35%; background-size: contain;"></div>
	<div
		style="width: 100%; height: 35%; background-color: #eaeaea; position: absolute; left: 0px; bottom: 0px;"></div>

	<div class="login-box">
		<div class="logo"></div>
		<c:if test="${not empty errorMessge}">
			<div class="alert bg-red alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">X</span>
				</button>
				${errorMessge}
			</div>
		</c:if>
		<c:if test="${not empty successMessge}">
			<div class="alert bg-blue alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">X</span>
				</button>
				${successMessge}
			</div>
		</c:if>
		<div class="card" style="border-radius: 5px;">
			<div
				style="position: absolute; height: 5px; width: 100%; border-top-left-radius: 5px; border-top-right-radius: 5px; background-color: #009fe4;"></div>
			<div class="body">
				<form name='loginForm' accept-charset="ISO-8859-1"
					action="<c:url value='/login' />" method='POST'>

					<div class="msg">Ingrese al Sistema</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="material-icons gris">person</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="username"
								maxlength="30" id="username" placeholder="USUARIO" required
								title="Campo requerido"
								oninvalid="this.setCustomValidity('Complete este campo')"
								oninput="setCustomValidity('')" autofocus>
						</div>
					</div>
					<div class="input-group" style="margin-bottom: 50px !important;">
						<span class="input-group-addon"> <i
							class="material-icons gris">lock</i>
						</span>
						<div class="form-line">
							<input type="password" class="form-control" name="password"
								maxlength="30" id="password"
								oninvalid="this.setCustomValidity('Complete este campo')"
								oninput="setCustomValidity('')" placeholder="CONTRASEÑA"
								required title="Campo requerido">
						</div>
					</div>
					<div class="row">

						<div class="col-xs-12">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /> <a href="index.html"><button
									class="btn btn-block bg-blue waves-effect" type="submit"
									name="submit">INGRESAR</button></a>
						</div>
						<div class="col-xs-12" style="">
							<p style="text-align: center">
								<a href="<c:url value='/forgetPassword' />" style="color: #00538d;">¿Olvidó su contraseña?</a>
							</p>
							<p style="text-align: center">
								<a href="<c:url value='/createUser' />" style="color: #00538d;">Crear cuenta</a>
							</p>
						</div>
					</div>

				</form>
			</div>
		</div>
	</div>

	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>

	<!-- Bootstrap Core Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

	<!-- Validation Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-validation/jquery.validate.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/pages/examples/sign-in.js'/>"></script>

</body>
</html>