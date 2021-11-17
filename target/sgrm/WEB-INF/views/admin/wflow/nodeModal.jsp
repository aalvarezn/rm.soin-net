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

				<div class="row clearfix">
					<form id="nodeForm" action="#">
						<input type="hidden" id="idActivity" value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
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
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Final</label>
							<div class="switch" style="margin-top: 20px;">
								<label>No<input id="isFinal" type="checkbox"><span
									class="lever"></span>Si
								</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Usuarios</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="users" name="users"
										placeholder="Ingrese un nombre" style="height: 60px;">
								</div>
								<label id="newPassword_field" class="error fieldError"
									for="name" style="display: none">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>
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