<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="sigesModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Código Siges</h4>
			</div>
			<div class="modal-body">
				<form id="sigesModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Código</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sCode"
										maxlength="50" name="sCode" placeholder="Ingrese un código"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="description">Sistema</label>
							<div class="form-group m-t-12">
								<select id="sSystemId" name="sSystemId" required="required"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option value="${system.id }">${system.code }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="description">Plantilla Correo</label>
							<div class="form-group m-t-12">
								<select id="sEmailId" name="sEmailId" required="required"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${emailTemplates}" var="emailTemplate">
										<option value="${emailTemplate.id }">${emailTemplate.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeSiges()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveSiges()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateSiges()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
