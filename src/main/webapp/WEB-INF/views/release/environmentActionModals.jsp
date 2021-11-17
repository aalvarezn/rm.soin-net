<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="environmentActionModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Agregar
					observación</h4>
			</div>
			<div class="modal-body">

				<div class="row clearfix">
					<form action="" id="environmentActionForm">

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="demo-radio-button">
								<input name="group1" value="ANTES" type="radio" id="radio_1"
									checked=""> <label for="radio_1">ANTES</label> <input
									name="group1" value="DURANTE" type="radio" id="radio_2">
								<label for="radio_2">DURANTE</label> <input name="group1"
									value="DESPUES" type="radio" id="radio_3"> <label
									for="radio_3">DESPUÉS</label>

							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 p-t-20">
							<label>Entorno</label>
							<div class="row clearfix">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="form-group">
										<select class="form-control show-tick" id="environment"
											name="impactId">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${environments}" var="environment">
												<option value="${environment.id}">${environment.name}</option>
											</c:forEach>
										</select><label id="environment_error" class="error fieldError"
											for="name" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 p-t-20">
							<label>Accion</label>
							<div class="row clearfix">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="form-group">
										<select class="form-control show-tick" id="actionEnvironment"
											name="acctionId">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${actionEnvironments}"
												var="actionEnvironment">
												<option value="${actionEnvironment.id}">${actionEnvironment.name}</option>
											</c:forEach>
										</select> <label id="actionEnvironment_error" class="error fieldError"
											for="name" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-t-20">
							<label for="email_address">Observación</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" id="observation" class="form-control"
										maxlength="200"
										placeholder="Ingrese una observación para la bajada de ambientes"
										style="height: 60px;">
								</div>
								<label id="observation_error" class="error fieldError"
									for="name" style="visibility: hidden;">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeEnvironmentActionModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="addEnvironmentAction()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>