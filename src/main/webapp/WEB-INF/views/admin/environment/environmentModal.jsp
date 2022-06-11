<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="environmentModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Tipo de Objeto</h4>
			</div>
			<div class="modal-body">
				<form id="environmentModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="environmentId"
						value="" />

					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a href="#tabHome"
									data-toggle="tab">INFORMACIÓN</a></li>
								<li role="presentation"><a href="#tabRoles"
									data-toggle="tab">SISTEMAS</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
		<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name"
										maxlength="50" name="name" placeholder="Ingrese un nombre"
										style="height: 60px;">
									<div class="help-info">Máx. 50 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="description">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description"
										name="description" placeholder="Ingrese un nombre"
										style="height: 60px;">
								</div>
								<label id="description_error" class="error fieldError"
									for="description" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
					</div>
					<div class="row clearfix">
						
						<div
							class="col-lg-6 col-md-6 col-sm-12 col-xs-12 align-left m-t-10">
							<div class="switch" style="margin-top: 20px;">
								<label>Externo<input id="external" type="checkbox"
									value="0"><span class="lever"></span>
								</label>
							</div>
						</div>
					</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabRoles">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<select id='userGroups' class="multiselect" multiple='multiple'>
										<c:forEach items="${systems}" var="system">
											<option id="${system.id}" value='${system.id}'>${system.name}</option>
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
					onclick="closeEnvironmentModal()">CANCELAR</button>
				<button id="btnSaveEnvironment" type="button"
					class="btn btn-primary waves-effect" onclick="saveEnvironment()">GUARDAR</button>
				<button id="btnUpdateEnvironment" type="button"
					class="btn btn-primary waves-effect" onclick="updateEnvironment()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
