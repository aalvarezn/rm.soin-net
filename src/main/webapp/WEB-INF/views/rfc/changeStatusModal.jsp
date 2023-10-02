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
							value="${_csrf.token}" /> <input type="hidden" id="idRFC"
							value="" />

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-b-10">
							<label>N&uacute;mero RFC</label>
							<div class="form-group">
								<div class="form-line disabled">
									<input type="text" readonly="" id="rfcNumRequest"
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
										<option data-motive="${status.reason }" value="${status.id }">${status.name }</option>
									</c:forEach>
								</select> <label id="statusId_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div id="divError" hidden
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<label for="email_address">Tipo de error</label> <select
									id="errorId" class="form-control show-tick selectpicker"
									data-live-search="true" required="required">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${errors}" var="error">
										<option value="${error.id }">${error.name }</option>
									</c:forEach>
								</select> <label id="errorId_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="email_address">Motivo</label>
							<div class="form-group m-b-0i">
								<div class="form-line">
									<textarea required="required" rows="2" cols="" id="motive"
										name="motive" class="form-control"
										placeholder="Ingrese un motivo..." style="height: 67px;"
										maxlength="250"></textarea>
								</div>
								<label id="motive_error" class="error fieldError" for="name"
									style="visibility: hidden;">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="email_address">¿Quieres enviar correo?</label>
							<div class="form-group m-b-0i" style="margin-top: 3%">
								<div class="switch">
									<label>NO<input id="sendMail" type="checkbox"
										name="sendMail"><span class="lever"></span>S&Iacute;
									</label>
								</div>
							</div>
						</div>
						<div id="divEmail" hidden>
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"
								style="margin-top: 5%">
								<label for="email_address">Correos a enviar</label>
								<div class="form-group m-b-0i">
									<div class="form-line">
										<input type="text" id="senders" name="senders"
											required="required" class="form-control tagInitMail"
											placeholder="">
									</div>
									<label id="senders_error" class="error fieldError" for="name"
										style="visibility: hidden;">Campo Requerido.</label>
								</div>
							</div>
							<div class="col-lg-12 col-md-12 col-sm-6 col-xs-6">
								<label for="email_address">Nota</label>
								<div class="form-group m-b-0i">
									<div class="form-line">
										<textarea rows="2" cols="" id="note" name="note"
											class="form-control"
											placeholder="Ingrese una nota adicional al correo..."
											style="height: 67px;" maxlength="200"></textarea>
									</div>

								</div>
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