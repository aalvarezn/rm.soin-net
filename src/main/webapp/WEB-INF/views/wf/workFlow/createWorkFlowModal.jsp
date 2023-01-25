<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="workFlowModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Crear flujo</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="workFlowModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="moduleId"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name"
										maxlength="50" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Sistema</label>
							<div class="form-group">
								<select id="systemId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option id="${system.id }" value="${system.id }">${system.code }</option>
									</c:forEach>
								</select> <label id="systemId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Tipo</label>
							<div class="form-group">
								<select id="typeId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${types}" var="type">
										<option id="${type.id }" value="${type.id }">${type.name }</option>
									</c:forEach>
								</select> <label id="systemId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeWorkFlowModal()">CANCELAR</button>
				<button id="btnSaveModule" type="button"
					class="btn btn-primary waves-effect" onclick="saveWorkFlow()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>
