<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="wfpackageModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Paquete</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="wfpackageForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="idpackage"
							value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name" name="name"
										placeholder="Ingrese un nombre" style="height: 60px;">
								</div>
								<label id="name_field" class="error fieldError" for="name"
									style="display: none">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description"
										name="description" placeholder="Ingrese un nombre"
										style="height: 60px;">
								</div>
								<label id="description_field" class="error fieldError"
									for="name" style="display: none">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closewfpackageModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="packageModal()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>