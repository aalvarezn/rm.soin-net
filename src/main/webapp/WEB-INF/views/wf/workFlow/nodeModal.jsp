<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="nodeModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Actividad</h4>
			</div>
			<div class="modal-body">
				<form id="nodeForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="id" value="" />
					<input type="hidden" id="x" value="" /> <input type="hidden"
						id="y" value="" />
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a href="#tabHome"
									data-toggle="tab">ACCI&Oacute;N</a></li>
								<li role="presentation"><a href="#tabActors"
									data-toggle="tab">ACTORES</a></li>
								<li role="presentation"><a href="#tabSecurity"
									data-toggle="tab">NOTIFICAR</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
							<div class="row clearfix m-t-20">
								<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
									<label for="name">Nombre</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" class="form-control" id="name" name="name"
												placeholder="Ingrese un nombre" style="height: 60px;">
										</div>
										<label id="newPassword_field" class="error fieldError"
											for="name" style="display: none">Campo Requerido.</label>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
									<label>Estado</label>
									<div class="form-group m-t-25">
										<select id="statusId"
											class="form-control show-tick selectpicker"
											data-live-search="true">
											<option value="">-- Ninguno --</option>
											<c:forEach items="${statuses}" var="status">
												<option value="${status.id }">${status.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
									<label>Tipo</label>
									<div class="form-group m-t-25">
										<select id="typeGroup"
											class="form-control show-tick selectpicker"
											data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<option value="start">Inicio</option>
											<option value="action">Actividad</option>
											<option value="finish">Fin</option>
										</select>
									</div>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
									<label for="name">Enviar Correo</label>
									<div class="switch" style="margin-top: 20px;">
										<label>No<input id="sendEmail" type="checkbox"><span
											class="lever"></span>Si
										</label>
									</div>
								</div>


							</div>
							<div class="row clearfix m-t-20">
								<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
									<label for="name">Saltar tr&aacute;mite </label>
									<div class="switch" style="margin-top: 20px;">
										<label>No<input id="skipNode" type="checkbox"><span
											class="lever"></span>Si
										</label>
									</div>
								</div>
								<div id="skipDiv">
									<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
										<label>Estado a saltar</label>
										<div class="form-group m-t-25">
											<select id="statusSkipId"
												class="form-control show-tick selectpicker"
												data-live-search="true">
												<option value="">-- Ninguno --</option>
											</select>
										</div>
									</div>
									<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
										<label>Estado a continuar</label>
										<div class="form-group m-t-25">
											<select id="statustoSkipId"
												class="form-control show-tick selectpicker"
												data-live-search="true" disabled>
												<option value="">-- Ninguno --</option>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabActors">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='actors' multiple='multiple'>
										<c:forEach items="${users}" var="user">
											<option id="${user.id}" value='${user.id}'>${user.fullName}</option>
										</c:forEach>
									</select>
								</div>

							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabSecurity">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='users' multiple='multiple'>
										<c:forEach items="${users}" var="user">
											<option id="${user.id}" value='${user.id}'>${user.fullName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeNodeModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="updateNodeModal()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>