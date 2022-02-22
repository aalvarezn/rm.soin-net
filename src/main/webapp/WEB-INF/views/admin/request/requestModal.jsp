<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="requestModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Requerimiento</h4>
			</div>
			<div class="modal-body">

				<form id="requestModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="requestId"
						value="" />
					<div class="row clearfix">
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Código Soin</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="code_soin"
										maxlength="100" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="code_soin_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Código ICE</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="code_ice"
										maxlength="100" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="code_ice_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="name">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description"
										name="name" placeholder="Ingrese una descripción"
										style="height: 60px;">
								</div>
								<label id="description_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
							<label for="name">Estado</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="status"
										maxlength="50" name="name" placeholder="Ingrese un estado"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="status_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</div>
					<div class="row clearfix">
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Gestor Soin</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="soinManagement"
										maxlength="200" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 200 caracteres</div>
								</div>
								<label id="soinManagement_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Gestor ICE</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="iceManagement"
										maxlength="200" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 200 caracteres</div>
								</div>
								<label id="iceManagement_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Leader Técnico Soin</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="leaderSoin"
										maxlength="200" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 200 caracteres</div>
								</div>
								<label id="leaderSoin_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label for="name">Leader Técnico ICE</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="liderIce"
										maxlength="200" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 200 caracteres</div>
								</div>
								<label id="liderIce_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

					</div>
					<div class="row clearfix">
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label>Proyecto</label>
							<div class="form-group">
								<select id="proyectId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${projects}" var="project">
										<option id="${project.id }" value="${project.id }">${project.code }</option>
									</c:forEach>
								</select> <label id="proyectId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
							<label>Tipo Requerimiento</label>
							<div class="form-group">
								<select id="typeRequestId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${typeRequests}" var="typeRequest">
										<option id="${typeRequest.id }" value="${typeRequest.id }">${typeRequest.code }</option>
									</c:forEach>
								</select> <label id="typeRequestId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeRequestModal()">CANCELAR</button>
				<button id="btnSaveRequest" type="button"
					class="btn btn-primary waves-effect" onclick="saveRequest()">GUARDAR</button>
				<button id="btnUpdateRequest" type="button"
					class="btn btn-primary waves-effect" onclick="updateRequest()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
