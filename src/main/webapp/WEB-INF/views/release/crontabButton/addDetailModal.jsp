<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="addDetailModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="font-20 ">
					<span>Agregar detalle: </span>
				</h5>

			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="addDetailForm" action="">
						<input type="hidden" id="formToAdd" value="" />
						<div class="col-lg-7 col-md-7 col-sm-12 col-xs-12">
							<label for="">Nombre</label>
							<div class="form-group m-b-0i">
								<div class="form-line">
									<input type="text" id="name" name="name" maxlength="50"
										value="" class="form-control" placeholder="Ingrese un valor..">
										<div class="help-info">M&aacute;x. 50 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden;">Valor requerido.</label>
							</div>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
							<label for="">¿Es requerido?</label>
							<div class="switch" style="margin-top: 20px;">
								<label>No<input id="isRequired" type="checkbox"
									value="0"><span class="lever"></span>Si
								</label>
							</div>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-12 col-xs-12 m-t-20">
							<label>Tipo de detalle</label>
							<div class="form-group">
								<select id="typeDetail" class="form-control show-tick">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${typeDetailList}" var="typeDetail">
										<option id="${typeDetail.name }" value="${typeDetail.name }">${typeDetail.name }</option>
									</c:forEach>
								</select> <label id="typeDetail_error" class="error fieldError"
									for="typeDetail" style="visibility: hidden;">Valor
									requerido.</label>
							</div>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12 m-t-20">
							<label for="">&nbsp;</label>
							<div class="form-group m-b-0i">
								<div class="form-line">
									<input type="text" id="typeText" name="typeText" maxlength="50"
										value="" class="form-control" placeholder="Ingrese un valor..">
										<div class="help-info">M&aacute;x. 50 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="">¿Par&aacute;metro entre comillas?</label>
							<div class="switch" style="margin-top: 20px;">
								<label>No<input id="quotationMarks" type="checkbox"
									value="0"><span class="lever"></span>Si
								</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
							<label for="">Descripci&oacute;n</label>
							<div class="form-group m-b-0i">
								<div class="form-line">
									<input type="text" id="description" name="description"
										maxlength="200" value="" class="form-control"
										placeholder="Ingrese un valor..">
										<div class="help-info">M&aacute;x. 200 caracteres</div>
								</div>
								<label id="description_error" class="error fieldError"
									for="description" style="visibility: hidden;">Valor
									requerido.</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeAddDetailModal()">CANCELAR</button>
				<button id="addRow" type="button" data-type=""
					class="btn btn-primary waves-effect" onclick="addButtonRow()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>