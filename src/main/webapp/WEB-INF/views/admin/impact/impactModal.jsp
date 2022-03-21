<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="impactModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Impacto</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="impactModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="iId" value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="iName" name="iName"
										maxlength="50" placeholder="Ingrese un valor"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--20 m-b--20">
							<label for="description">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="iDescription"
										maxlength="250" name="iDescription"
										placeholder="Ingrese un nombre" style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeImpact()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveImpact()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateImpact()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
