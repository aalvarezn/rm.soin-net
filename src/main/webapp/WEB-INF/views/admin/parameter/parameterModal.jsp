<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="parameterModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Modificar
					Par�metro</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="parameterModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="paramId"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="description">Descripci�n</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description"
										maxlength="50" name="description"
										placeholder="Ingrese un nombre" style="height: 60px;">
									<div class="help-info">M�x. 50 caracteres</div>
								</div>
								<label id="description_error" class="error fieldError"
									for="description" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--20 m-b--20">
							<label for="paramValue">Valor</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="paramValue"
										maxlength="100" name="paramValue"
										placeholder="Ingrese un nombre" style="height: 60px;">
									<div class="help-info">M�x. 100 caracteres</div>
								</div>
								<label id="paramValue_error" class="error fieldError"
									for="paramValue" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeParameterModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="updateParameter()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
