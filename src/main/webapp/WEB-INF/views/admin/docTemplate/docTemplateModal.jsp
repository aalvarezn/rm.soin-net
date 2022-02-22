<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="docTemplateModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Plantilla release</h4>
			</div>
			<div class="modal-body">

				<form id="docTemplateModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="docTemplateId"
						value="" />
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name"
										maxlength="100" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Plantilla</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="templateName"
										maxlength="100" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="templateName_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

					</div>
					<div class="row clearfix">
						<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
							<label for="name">Componente generador</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="componentGenerator"
										maxlength="100" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
								<label id="componentGenerator_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="name">Sufijo</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sufix"
										maxlength="50" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="sufix_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</div>
					<div class="row clearfix">
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
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeDocTemplateModal()">CANCELAR</button>
				<button id="btnSaveDocTemplate" type="button"
					class="btn btn-primary waves-effect" onclick="saveDocTemplate()">GUARDAR</button>
				<button id="btnUpdateDocTemplate" type="button"
					class="btn btn-primary waves-effect" onclick="updateDocTemplate()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
