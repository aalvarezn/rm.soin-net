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
							value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Código</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sCode" name="sCode"
										maxlength="30" placeholder="Ingrese un valor">
									<div class="help-info">Máx. 30 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sName" name="sName"
										maxlength="25" placeholder="Ingrese un valor">
									<div class="help-info">Máx. 25 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
							<label for="description">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sDescription"
										maxlength="150" name="sDescription"
										placeholder="Ingrese un valor" style="height: 40px;">
									<div class="help-info">Máx. 150 caracteres</div>
								</div>
							</div>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="description">Motivo</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sReason"
										maxlength="200" name="sReason" placeholder="Ingrese un valor"
										style="height: 40px;">
									<div class="help-info">Máx. 200 caracteres</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeStatus()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveStatus()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateStatus()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
