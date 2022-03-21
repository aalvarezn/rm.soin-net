<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="userPasswordModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Cambio de
					contraseña</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="userPasswordModalForm" action="" autocomplete="off">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="uId" value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Contraseña</label>
							<div class="form-group">
								<div class="form-line">
									<input type="password" class="form-control" id="newPassword"
										maxlength="12" name="newPassword"
										placeholder="Ingrese un valor" style="height: 60px;">
								</div>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Confirmar Contraseña</label>
							<div class="form-group">
								<div class="form-line">
									<input type="password" class="form-control"
										id="confirmPassword" name="confirmPassword" maxlength="12"
										placeholder="Ingrese un valor" style="height: 60px;">
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 blueBgc">
						<p class="p-t-10">
							No debe contener más de 12 caractéres. <br /> La contraseña no
							puede ser completamente numérica.<br />
						</p>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeUserPassword()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="updateUserPassword()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>
