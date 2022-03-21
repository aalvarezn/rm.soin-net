<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="ambientModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Ambiente</h4>
			</div>
			<div class="modal-body">
				<form id="ambientModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="aId"
							value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Código</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="aCode"
										maxlength="50" name="aCode" placeholder="Ingrese un código"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="description">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="aName" name="aName"
										maxlength="100" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeAmbient()">CANCELAR</button>
				<button id="save" type="button"
					class="btn btn-primary waves-effect" onclick="saveAmbient()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateAmbient()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
