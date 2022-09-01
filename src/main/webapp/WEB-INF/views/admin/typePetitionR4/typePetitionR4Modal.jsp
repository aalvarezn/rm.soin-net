<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="typePetitionModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Tipo de cambio</h4>
			</div>
			<div class="modal-body">
				<form id="typePetitionModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Codigo</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sCode"
										maxlength="50" name="sCode" placeholder="Ingrese un codigo"
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
										maxlength="50" name="sDescription" placeholder="Ingrese una descripción"
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
					onclick="closeTypePetition()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveTypePetition()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateTypePetition()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
