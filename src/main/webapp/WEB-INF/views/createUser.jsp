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
		<div class="card" style="border-radius: 5px;">
			<div
				style="position: absolute; height: 5px; width: 100%; border-top-left-radius: 5px; border-top-right-radius: 5px; background-color: #009fe4;"></div>
			<div class="body">
				<form name='loginForm' accept-charset="ISO-8859-1"
					action="<c:url value='/createUserNew' />" method='POST'>

					<div class="msg">Crear usuario</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="material-icons gris">person</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="fullName"
								value="${user.fullName }" maxlength="30" id="fullName"
								placeholder="Nombre completo" required title="Campo requerido"
								oninvalid="this.setCustomValidity('Complete este campo')"
								oninput="setCustomValidity('')" autofocus>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="material-icons gris">email</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control email" name="emailAddress"
								value="${user.emailAddress }" maxlength="30" id="emailAddress"
								placeholder="usuario@soin.co.cr" required
								title="Campo requerido"
								
								oninput="validateEmail(this)" autofocus>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="fab fa-github material-icons gris"></i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="gitusername"
								title="Ingrese el nombre de usuario de github(opcional)"
								value="${user.gitusername }" maxlength="30" id="gitusername"
								placeholder="Usuario git(opcional)"
								oninvalid="this.setCustomValidity('Complete este campo')"
								oninput="setCustomValidity('')" autofocus>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="material-icons gris">work</i>
						</span>
						<div class="form-line">
							<select class="form-control" name="projectId" id="projectId"
								oninvalid="this.setCustomValidity('Seleccione una opción')"
								title="Seleccione un proyecto para desplegar los sistemas al cual puede pertenecer(opcional)"
								oninput="setCustomValidity('')" autofocus>
								<option value="0">Seleccione un proyecto</option>
								<c:forEach items="${projects}" var="project">
									<option value="${project.id }">${project.code}</option>
								</c:forEach>
								<!-- Agrega más opciones aquí según sea necesario -->
							</select>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="material-icons gris">settings</i>
						</span>
						<div class="form-line">
							<select class="form-control" disabled name="systemId"
								id="systemId"
								title="Seleccione el sistema al cual quieres pertenecer(opcional)"
								oninvalid="this.setCustomValidity('Seleccione una opción')"
								oninput="setCustomValidity('')" autofocus>
								<option value="0">Seleccione un sistema</option>
								<!-- Agrega más opciones aquí según sea necesario -->
							</select>
						</div>
					</div>
					<div class="row">

						<div class="col-xs-12">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /> <a href="index.html"><button
									class="btn btn-block bg-blue waves-effect" type="submit"
									name="submit">REGISTRARSE</button></a>
						</div>

						<div class="col-xs-12" style="">
							<p style="text-align: center">
								<a href="<c:url value='/' />" style="color: #00538d;">¿Regresar
									al inicio?</a>
							</p>
						</div>
					</div>

				</form>
			</div>
		</div>
	</div>
	<script>
    function validateEmail(input) {
        const email = input.value;
        const domainRegex = /^[a-zA-Z0-9._%+-]+@soin.co.cr$/; // Valida que termine con @soin.co.cr
        const generalEmailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // Valida formato general de correo
        const noInvalidSymbolsRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // No permite espacios ni símbolos raros

        if (email === "") {
            input.setCustomValidity('El correo no puede estar vacío');
        } else if (!generalEmailRegex.test(email)) {
            input.setCustomValidity('Correo inválido. Asegúrese de que el correo tiene un formato válido');
        } else if (!domainRegex.test(email)) {
            input.setCustomValidity('El correo no pertenece a un dominio permitido');
        } else if (!noInvalidSymbolsRegex.test(email)) {
            input.setCustomValidity('El correo no debe contener espacios ni símbolos no permitidos');
        } else {
            input.setCustomValidity(''); // Email válido
        }
    }
	</script>
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