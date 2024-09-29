<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="buttonModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Agregar Boton
					Infra</h4>
			</div>
			<div class="modal-body">
				<form id="buttonModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sName"
										maxlength="50" name="sName" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 20 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Ruta</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sRute"
										maxlength="50" name="sRute" placeholder="Ingrese una ruta valida"
										style="height: 60px;">
									<div class="help-info">Máx. 20 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sDescription"
										maxlength="50" name="sDescription"
										placeholder="Ingrese una descripción" style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
							<label for="name">Sistema</label>
							<div class="form-group">
								<select id="systemId" name="systemId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option value="${system.id}">${system.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closebutton()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="savebutton()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updatebutton()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
