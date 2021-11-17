<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="emailModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Crear plantilla
					correo</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="emailModalForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="emailId"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name" name="name"
										placeholder="Ingrese un nombre" style="height: 60px;">
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t--10">
							<label for="name">Asunto</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="subject"
										name="subject" placeholder="Ingrese un nombre"
										style="height: 60px;">
								</div>
								<label id="subject_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeEmailModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="saveEmail()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>
