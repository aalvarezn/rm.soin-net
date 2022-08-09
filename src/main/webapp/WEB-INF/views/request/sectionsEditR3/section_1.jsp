<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Solicitud de cambios en el servicio</h5>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label for="permission">Ambientes por cambiar</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
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
			<label id="ambient_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>

	</div>

	</div>
<div class="row clearfix" style="margin-top: 20px;">
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>Cambios de servicio</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='change' id="change"
					class="form-control"
					placeholder="Ingrese la informaci&oacute;n del cambio..." style="">${requestR5.changeService }</textarea>
			</div>
			<label id="change_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>Justificaci&oacute;n de cambio</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='justify' id="justify"
					class="form-control"
					placeholder="Ingrese la justificac&oacute;n del cambio..." style="">${requestR5.justify }</textarea>
			</div>
			<label id="justify_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
</div>




