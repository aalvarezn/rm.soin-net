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
				<form id="systemModalForm" action="" novalidate>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs tab-nav-right" role="tablist">
								<li role="presentation" class="active"><a href="#tabHome"
									data-toggle="tab">INFORMACIÓN</a></li>
								<li role="presentation"><a href="#tabTeam"
									data-toggle="tab">EQUIPO DE TRABAJO</a></li>
								<li role="presentation"><a href="#tabManagements"
									data-toggle="tab">GESTORES</a></li>
							</ul>
						</div>
					</div>
					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="tabHome">
							<div class="row m-t-20">
								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<label for="name">Código</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="10" class="form-control"
												id="sCode" name="sCode" placeholder="Ingrese un valor"
												style="height: 49px;">
											<div class="help-info">Máx. 10 caracteres</div>
										</div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<label for="name">Nombre</label>
									<div class="form-group">
										<div class="form-line">
											<input type="text" maxlength="50" class="form-control"
												id="sName" name="sName" placeholder="Ingrese un valor"
												style="height: 49px;">
											<div class="help-info">Máx. 50 caracteres</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row m-t-20">
								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<label>Líder Técnico</label>
									<div class="form-group m-t-15">
										<select id="sLeader" name="sLeader" required="required"
											class="form-control show-tick selectpicker"
											data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${users}" var="user">
												<option value="${user.userName }">${user.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<label>Proyecto</label>
									<div class="form-group m-t-15">
										<select id="sProject" name="sProject" required="required"
											class="form-control show-tick selectpicker"
											data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${projects}" var="project">
												<option value="${project.code }">${project.description }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<label>Plantilla correo</label>
									<div class="form-group m-t-15">
										<select id="sEmail"
											class="form-control show-tick selectpicker"
											data-live-search="true">
											<option value="">-- Seleccione una opci&oacute;n --</option>
											<c:forEach items="${emails}" var="email">
												<option value="${email.id }">${email.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="row m-t-20">
								<div
									class="demo-checkbox col-lg-3 col-md-3 col-sm-12 col-xs-12 align-left">
									<input type="checkbox" id="sImportObjects" class="filled-in">
									<label for="sImportObjects">Importar objetos</label>
								</div>
								<div
									class="demo-checkbox col-lg-3 col-md-3 col-sm-12 col-xs-12 align-left">
									<input type="checkbox" id="sCustomCommands" class="filled-in">
									<label for="sCustomCommands">Comandos personalizados</label>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabTeam">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='sTeam' multiple='multiple'>
										<c:forEach items="${users}" var="user">
											<option value="${user.userName }">${user.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="tabManagements">
							<div class="row m-t-20">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 multSelect">
									<select id='sManagers' multiple='multiple'>
										<c:forEach items="${users}" var="user">
											<option value="${user.userName }">${user.name}</option>
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
					onclick="closeSystem()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveSystem()">GUARDAR</button>
				<button id="update" type="button"
					class="btn btn-primary waves-effect" onclick="updateSystem()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>
