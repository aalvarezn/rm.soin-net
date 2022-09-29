<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="systemModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Sistema</h4>
			</div>
			<div class="modal-body">
				<form id="systemModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="systemId"
						value="" />
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a href="#tabHome"
									data-toggle="tab">INFORMACIÓN</a></li>
								<li role="presentation"><a href="#tabTeam"
									data-toggle="tab">USUARIOS EXTERNOS</a></li>
								<li role="presentation"><a href="#tabManagements"
									data-toggle="tab">GESTORES TICKETS</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
							<div class="row m-t-20">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Nombre</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="50" class="form-control"
												id="name" name="name" placeholder="Ingrese un nombre"
												style="height: 49px;">
											<div class="help-info">Máx. 50 caracteres</div>
										</div>
										<label id="username_error" class="error fieldError" for="name"
											style="visibility: hidden">Campo Requerido.</label>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Código</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="10" class="form-control"
												id="code" name="code" placeholder="Ingrese un código"
												style="height: 49px;">
											<div class="help-info">Máx. 10 caracteres</div>
										</div>
										<label id="code_error" class="error fieldError" for="name"
											style="visibility: hidden">Campo Requerido.</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label>Líder Técnico</label>
									<div class="form-group">
										<select id="leaderId" disabled
											class="form-control show-tick selectpicker" data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${users}" var="user">
												<option id="${user.id }" value="${user.id }">${user.fullName }</option>
											</c:forEach>
										</select> <label id="leaderId_error" class="error fieldError"
											for="system" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label>Proyecto</label>
									<div class="form-group">
										<select id="proyectId" disabled
											class="form-control show-tick selectpicker" data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${projects}" var="project">
												<option id="${project.id }" value="${project.id }">${project.code }</option>
											</c:forEach>
										</select> <label id="proyectId_error" class="error fieldError"
											for="system" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
							</div>
							
						</div>
						<div role="tabpanel" class="tab-pane" id="tabTeam">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='team' multiple='multiple'>
										<c:forEach items="${userManager}" var="user">
											<option id="${user.id}" value='${user.id}'>${user.fullName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabManagements">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='managers' multiple='multiple'>
										<c:forEach items="${userIncidence}" var="user">
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
					onclick="closeSystemModal()">CANCELAR</button>
				<button id="btnSaveSystem" type="button"
					class="btn btn-primary waves-effect" onclick="saveSystem()">GUARDAR</button>
				<button id="btnUpdateSystem" type="button"
					class="btn btn-primary waves-effect" onclick="updateSystem()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
