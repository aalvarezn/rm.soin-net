<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="changeStatusModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Cambiar Estado</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="changeStatusForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="idRelease"
							value="" />

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
							<label>Número Release</label>
							<div class="form-group">
								<div class="form-line disabled">
									<input type="text" readonly="" id="releaseNumber"
										class="form-control">
								</div>
							</div>
						</div>

						<div class='col-lg-6 col-md-6 col-sm-12 col-xs-12'>
							<label>Fecha</label>
							<div class="form-group">
								<div class="form-line disabled">
									<input required="required" type='text'
										class="form-control datetimepicker" id='dateChange' />
								</div>
								<label id="dateChange_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label>Estado</label>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<select id="statusId"
									class="form-control show-tick selectpicker"
									data-live-search="true" required="required">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${statuses}" var="status">
										<option data-motive="${status.motive }" value="${status.id }">${status.name }</option>
									</c:forEach>
								</select> <label id="statusId_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-6">
							<label for="email_address">Motivo</label>
							<div class="form-group m-b-0i">
								<div class="form-line">
									<textarea required="required" rows="2" cols="" id="motive"
										name="motive" class="form-control"
										placeholder="Ingrese un motivo..." style="height: 67px;"
										maxlength="50"></textarea>
								</div>
								<label id="motive_error" class="error fieldError" for="name"
									style="visibility: hidden;">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeChangeStatusModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="saveChangeStatusModal()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>