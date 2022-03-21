<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="userModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="modalTitle">Usuario</h4>
			</div>
			<div class="modal-body">
				<form id="userForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="userId"
						value="" />
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a
									href="#tabHome" data-toggle="tab">INFORMACIÓN</a></li>
								<li role="presentation"><a href="#tabRoles"
									data-toggle="tab">ROLES</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
							<div class="row m-t-20">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Nombre de usuario</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="30" class="form-control"
												id="username" name="username"
												placeholder="Ingrese un nombre" style="height: 60px;">
											<div class="help-info">Máx. 30 caracteres</div>
										</div>
										<label id="username_error" class="error fieldError"
											for="username" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Alias de usuario</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="30" class="form-control"
												id="shortName" name="shortName"
												placeholder="Ingrese un nombre" style="height: 60px;">
											<div class="help-info">Máx. 30 caracteres</div>
										</div>
										<label id="shortName_error" class="error fieldError"
											for="name" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Nombre Completo</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="100" class="form-control"
												id="fullName" name="fullName"
												placeholder="Ingrese un nombre" style="height: 60px;">
											<div class="help-info">Máx. 100 caracteres</div>
										</div>
										<label id="fullName_error" class="error fieldError" for="name"
											style="visibility: hidden">Campo Requerido.</label>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Correo</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="100" class="form-control"
												id="emailAddress" name="emailAddress"
												placeholder="Ingrese un correo" style="height: 60px;">
											<div class="help-info">Máx. 100 caracteres</div>
										</div>
										<label id="emailAddress_error" class="error fieldError"
											for="name" style="visibility: hidden">Campo
											Requerido.</label>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabRoles">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<select id='userGroups' multiple='multiple'>
										<c:forEach items="${listRoles}" var="role">
											<option id="${role.id}" value='${role.id}'>${role.name}</option>
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
					onclick="closeUserModal()">CANCELAR</button>
				<button type="button" id="btnSave" class="btn btn-primary waves-effect"
					onclick="saveUser()">GUARDAR</button>
				<button type="button" id="btnUpdate" class="btn btn-primary waves-effect"
					onclick="updateUser()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
