<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="changeUserModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Cambiar
					Desarrollador</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="changeUserForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="idRelease"
							value="" />

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label>Número Release</label>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-b-10">
							<div class="form-group">
								<div class="form-line disabled">
									<input type="text" readonly="" id="releaseNumber"
										class="form-control">
								</div>
							</div>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-10">
							<label>Usuario</label>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

							<div class="form-group">
								<select id="userId" class="form-control show-tick">
									<option value="">-- Seleccione una opci&oacute;n --</option>
								</select> <label id="userId_error" class="error" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>




					</form>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeChangeUserModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="saveChangeUserModal()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>