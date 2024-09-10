<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="sigesModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Creaci&oacute;n
					Sistema</h4>
			</div>
			<div class="modal-body">
				<form id="sigesModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="sId" value="" />
					</div>
					<div class="row clearfix">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">C&oacute;digo</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sCode"
										onpaste="return false" onkeypress="return verifyCode(event)"
										maxlength="50" name="sCode"
										placeholder="Ingrese un c&oacute;digo de sistema"
										style="height: 60px;">
									
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">C&oacute;digo Proyecto</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="sigesCode"
										maxlength="50" name="sigesCode" onpaste="return false" onkeypress="return verifyCode(event)"
										placeholder="Ingrese un c&oacute;digo siges"
										style="height: 60px;">
									
								</div>
							</div>
						</div>

					</div>
					<div class="row clearfix " style="margin-top: 20px;">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
							<label for="description">Proyecto</label>
							<div class="form-group m-t-12">
								<select id="proyectId" name="proyectId" required="required"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${proyects}" var="proyect">
										<option value="${proyect.id }">${proyect.code }</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
							<label for="description">L&iacute;der T&eacute;cnico</label>
							<div class="form-group m-t-12">
								<select id="userId" name="userId"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${users}" var="user">
										<option value="${user.id }">${user.fullName }</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeSiges()">CANCELAR</button>
				<button id="save" type="button" class="btn btn-primary waves-effect"
					onclick="saveSystem()">CREAR SOLICITUD</button>
			</div>
		</div>
	</div>
</div>
