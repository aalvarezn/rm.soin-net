<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="ambientModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Ambiente</h4>
			</div>
			<div class="modal-body">
				<form id="ambientModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="ambientId"
							value="" />
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Sistema</label>
							<div class="form-group">
								<select id="systemId" class="form-control show-tick selectpicker">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option id="${system.id }" value="${system.id }">${system.name }</option>
									</c:forEach>
								</select> <label id="systemId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Tipo de ambiente</label>
							<div class="form-group">
								<select id="typeAmbientId" class="form-control show-tick selectpicker">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${typeAmbients}" var="typeAmbient">
										<option id="${typeAmbient.id }" value="${typeAmbient.id }">${typeAmbient.name }</option>
									</c:forEach>
								</select> <label id="typeAmbientId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Código</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="code"
										maxlength="50" name="code" placeholder="Ingrese un código"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="code_error" class="error fieldError" for="code"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="description">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name" name="name"
										maxlength="100" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="detail">Detalle</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="details"
										name="details" placeholder="Ingrese un detalle"
										style="height: 60px;">

								</div>
								<label id="details_error" class="error fieldError" for="code"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 ">
							<label for="serverName">Nombre Servidor</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="serverName"
										name="serverName" maxlength="100"
										placeholder="Ingrese un nombre" style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="serverName_error" class="error fieldError"
									for="serverName" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeAmbientModal()">CANCELAR</button>
				<button id="btnSaveAmbient" type="button"
					class="btn btn-primary waves-effect" onclick="saveAmbient()">GUARDAR</button>
				<button id="btnUpdateAmbient" type="button"
					class="btn btn-primary waves-effect" onclick="updateAmbient()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
