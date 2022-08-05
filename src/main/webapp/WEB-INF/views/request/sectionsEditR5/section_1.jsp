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
	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="permission">Ambientes por cambiar</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
				<input type="checkbox" id="ambient1" name="ambient" value="Desarrollo"> <label for="ambient1">Desarrollo</label> <br>
				<input type="checkbox" id="ambient2" name="ambient" value="QA"> <label for="ambient2">QA</label><br>
				<input type="checkbox" id="ambient3" name="ambient" value="Pre-Produccion"> <label for="ambient3">Pre-Producci&oacute;n</label><br>
				<input type="checkbox" id="ambient4" name="ambient" value="Produccion"> <label for="ambient4">Producci&oacute;n</label><br>
				<input type="checkbox" id="ambient5" name="ambient" value="Otro" onchange="changeAttributte(this)"> <label for="ambient5">Otro(s):Indicar el nombre del ambiente</label><br>
				<input id="ambient6" maxlength="150"
					class="tagInit" name="tags-1" type="text" hidden >
				<br>
			</div>
			<label id="ambient_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>

	</div>
		<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label>Tipo del cambio</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
			<c:choose>
			<c:when test="${requestR5.typeChange == 'Ambiente'}">
			<input type="radio" id="type1" name="type" value="Ambiente" checked> <label	for="type1">Ambiente</label><br> 
			<input type="radio" id="type2" name="type" value="Aplicacion"> <label for="type2">Aplicaci&oacute;n</label><br>
			<input type="radio" id="type3" name="type" value="Base de datos"> <label for="type3">Base de datos</label><br> <br>
			</c:when>
			<c:when test="${requestR5.typeChange == 'Aplicacion'}">
			<input type="radio" id="type1" name="type" value="Ambiente" > <label for="type1">Ambiente</label><br> 
			<input type="radio" id="type2" name="type" value="Aplicacion" checked> <label for="type2">Aplicaci&oacute;n</label><br>
			<input type="radio" id="type3" name="type" value="Base de datos"> <label for="type3">Base de datos</label><br> <br>
			</c:when>
			<c:when test="${requestR5.typeChange == 'Base de datos'}">
			<input type="radio" id="type1" name="type" value="Ambiente" > <label for="type1">Ambiente</label><br> 
			<input type="radio" id="type2" name="type" value="Aplicacion"> <label for="type2">Aplicaci&oacute;n</label><br>
			<input type="radio" id="type3" name="type" value="Base de datos" checked> <label for="type3">Base de datos</label><br> <br>
			</c:when>
			<c:otherwise>
			<input type="radio" id="type1" name="type" value="Ambiente" > <label for="type1">Ambiente</label><br> 
			<input type="radio" id="type2" name="type" value="Aplicacion"> <label for="type2">Aplicaci&oacute;n</label><br>
			<input type="radio" id="type3" name="type" value="Base de datos"> <label for="type3">Base de datos</label><br> <br>
			</c:otherwise>
			</c:choose>	
				<br>
			</div>
			<label id="type_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
		<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
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
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
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



