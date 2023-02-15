<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="priorityIncidenceModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Prioridad de
					ticket</h4>
			</div>
			<div class="modal-body">
				<form id="priorityIncidenceModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="id" value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Sistema</label>
							<div class="form-group">
								<select id="sId" name="sId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option value="${system.id}">${system.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Prioridad</label>
							<div class="form-group">
								<select id="priorityId" disabled name="priorityId" disabled
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
								</select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<div class="form-line">
									<label for="email_address">Tiene SLA</label>

									<div class="switch" style="margin-top: 20px;">
										<label>NO<input id="sla" type="checkbox"
											value="0" name="sla"><span class="lever"></span>S&Iacute;
										</label>
									</div>

								</div>
							</div>
						</div>
							<div id="timeDiv" hidden class='col-lg-6 col-md-6 col-sm-12 col-xs-12'>
							<label>Tiempo SLA</label>
							<div class="form-group">
								<div class="form-line disabled">
									<input required="required" type='text' style="margin-top: 10px;"
										class="form-control datetimepicker" id='time' />
								</div>
								<label id="dateChange_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closePriorityIncidence()">CANCELAR</button>
				<button id="save" disabled type="button" class="btn btn-primary waves-effect"
					onclick="savePriorityIncidence()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect"
					onclick="updatePriorityIncidence()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
