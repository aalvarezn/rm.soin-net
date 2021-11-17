<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="wflowModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Crear Flujo</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="wflowForm" action="">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name" name="name"
										placeholder="Ingrese un nombre" style="height: 60px;">
								</div>
								<label id="newPassword_field" class="error fieldError"
									for="name" style="display: none">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label>Sistema</label>
							<div class="form-group m-b-0">
								<select id="system_id" name="system_id"
									class="form-control show-tick">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option value="${system.code }">${system.code }</option>
									</c:forEach>
								</select> <label id="system_id_error" class="error" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeWflowModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="createWFlow()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>