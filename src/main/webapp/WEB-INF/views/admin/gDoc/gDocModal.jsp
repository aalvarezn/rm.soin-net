<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="gDocModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">GDOC Configuración</h4>
			</div>
			<div class="modal-body">

				<form id="gDocModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="gId"
						value="" />
					<div class="row clearfix">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="name">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="gDescription"
										name="gDescription" placeholder="Ingrese una descripción"
										style="height: 48px;">
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="name">Hoja de Excel</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="gSpreadSheet"
										maxlength="100" name="gSpreadSheet" placeholder="Ingrese un estado"
										style="height: 48px;">
									<div class="help-info">Máx. 100 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label>Proyecto</label>
							<div class="form-group m-t-15">
								<select id="pId"
									name="pId"
									required="required"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${projects}" var="project">
										<option id="${project.id }" value="${project.id }">${project.code }</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Json Credenciales</label>
							<div class="form-group">
								<div class="form-line">
									<textarea rows="10" cols="" id="gCredentials"
										name="gCredentials" class="form-control"
										placeholder="Ingrese una credencial" style="">${release.technicalSolution}</textarea>
								</div>
		
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeGDoc()">CANCELAR</button>
				<button id="save" type="button"
					class="btn btn-primary waves-effect" onclick="saveGDoc()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateGDoc()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
