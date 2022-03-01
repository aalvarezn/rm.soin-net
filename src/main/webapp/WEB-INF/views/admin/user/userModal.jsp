<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="userModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="modalTitle">Usuario</h4>
			</div>
			<div class="modal-body">
				<form id="userModalForm" action="">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="uId"
						value="" />
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a
									href="#tabHome" data-toggle="tab">INFORMACIÓN</a></li>
								<li  role="presentation"><a href="#tabRoles"
									data-toggle="tab">ROLES</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
							<div class="row m-t-20">
								<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
									<label for="name">Usuario</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="30" class="form-control"
												id="uUserName" name="uUserName"
												placeholder="Ingrese un valor" style="height: 60px;">
											<div class="help-info">Máx. 30 caracteres</div>
										</div>
									</div>
								</div>
								<div class="col-lg-7 col-md-7 col-sm-12 col-xs-12">
									<label for="name">Nombre</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="200" class="form-control"
												id="uName" name="uName" 
												placeholder="Ingrese un valor" style="height: 60px;">
											<div class="help-info">Máx. 200 caracteres</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<label for="name">Correo</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="250" class="form-control"
												id="uEmail" name="uEmail"
												placeholder="Ingrese un valor" style="height: 60px;">
											<div class="help-info">Máx. 250 caracteres</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabRoles">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<select id='userGroups' class="multiselect" multiple='multiple'>
										<c:forEach items="${roles}" var="role">
											<option id="${role.code}" value='${role.code}'>${role.name}</option>
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
					onclick="closeUser()">CANCELAR</button>
				<button type="button" id="save" class="btn btn-primary waves-effect"
					onclick="saveUser()">GUARDAR</button>
				<button type="button" id="update" class="btn btn-primary waves-effect"
					onclick="updateUser()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
