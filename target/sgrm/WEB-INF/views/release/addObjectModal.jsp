<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="addObjectItemModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Agregar un objeto</h4>
			</div>
			<div class="modal-body">

				<div class="row clearfix">
					<form id="objectItemForm" action="">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="name">Nombre</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="name" name="name"
										maxlength="500" placeholder="Ingrese un nombre"
										style="height: 60px;">
										<div class="help-info">M&aacute;x. 500 caracteres</div>
								</div>
								<label id="name_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="email_address">Revisión SVN</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="revision"
										name="revision" maxlength="75"
										placeholder="Ingrese una revisión" style="height: 60px;">
										<div class="help-info">M&aacute;x. 75 caracteres</div>
								</div>
								<label id="revision_error" class="error fieldError" for="name"
									style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<label for="email_address">Fecha de revisión</label>
							<div class="form-group">
								<div class="form-line">
									<%
										java.text.DateFormat df = new java.text.SimpleDateFormat("YYYY-MM-dd");
									%>
									<input type="date" class="form-control" id="revisionDate"
										name="revisionDate"
										value="<%=df.format(new java.util.Date())%>"
										placeholder="Please choose a date..." style="height: 60px;">
								</div>
								<label id="revisionDate_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="email_address">Descripción</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="description" maxlength="1024"
										name="description" placeholder="Ingrese una descripción..."
										style="height: 60px;">
										<div class="help-info">M&aacute;x. 1024 caracteres</div>
								</div>
								<label id="description_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Tipo de Objetos</label>
							<div class="form-group">
								<select id="typeObject"
									onchange="changeSelectOption('typeObject','itemConfiguration')"
									class="form-control show-tick">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${typeObjects}" var="typeObject">
										<option id="${typeObject.id }" value="${typeObject.id }">${typeObject.name }</option>
									</c:forEach>
								</select> <label id="typeObject_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Item de Configuración</label>
							<div class="form-group">
								<select id="itemConfiguration"
									onchange="changeSelectOption('itemConfiguration','typeObject')"
									class="form-control show-tick">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${configurationItems}"
										var="configurationItem">
										<option id="${configurationItem.id }"
											value="${configurationItem.id }">${configurationItem.name }</option>
									</c:forEach>
								</select> <label id="itemConfiguration_error" class="error fieldError"
									for="name" style="visibility: hidden">Campo Requerido.</label>
							</div>
						</div>
					</form>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeObjectItemModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="addObjectItem()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>