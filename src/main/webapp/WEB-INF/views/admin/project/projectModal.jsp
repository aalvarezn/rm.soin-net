<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="projectModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Proyecto</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="projectModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="projectId"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="code">Código</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="code"
										maxlength="50" name="code" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="code_error" class="error fieldError" for="code"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--20 m-b--20">
							<label for="description">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description"
										name="description" placeholder="Ingrese un nombre"
										style="height: 60px;">
								</div>
								<label id="description_error" class="error fieldError"
									for="description" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--20 m-b--20">
							<div class="switch" style="margin-top: 20px;">
								<label>Permitir repetir código proyecto<input id="isAllow" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeProjectModal()">CANCELAR</button>
				<button id="btnSaveProject" type="button"
					class="btn btn-primary waves-effect" onclick="saveProject()">GUARDAR</button>
				<button id="btnUpdateProject" type="button"
					class="btn btn-primary waves-effect" onclick="updateProject()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
