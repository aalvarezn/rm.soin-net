<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="statusModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Estado</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="statusModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="statusId"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name"
										maxlength="25" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">M�x. 25 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--20 m-b--20">
							<label for="description">Descripci�n</label>
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
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeStatusModal()">CANCELAR</button>
				<button id="btnSaveStatus" type="button"
					class="btn btn-primary waves-effect" onclick="saveStatus()">GUARDAR</button>
				<button id="btnUpdateStatus" type="button"
					class="btn btn-primary waves-effect" onclick="updateStatus()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
