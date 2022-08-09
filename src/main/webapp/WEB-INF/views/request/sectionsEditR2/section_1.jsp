<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Solicitud de nuevos servicios</h5>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
		<label>Tipo de servicio</label>
		<div class="form-group m-b-0i">
			<div class="form-line" style="height: 110.667px;">
			<c:choose>
			<c:when test="${requestR2.typeService == 'Estandar'}">
			<input type="radio" id="type1" name="type" value="Estandar" checked> <label	for="type1">Estandar</label><br> 
			<input type="radio" id="type2" name="type" value="Completo"> <label for="type2">Completo</label><br>
			</c:when>
			<c:when test="${requestR2.typeService == 'Completo'}">
			<input type="radio" id="type1" name="type" value="Estandar" > <label	for="type1">Estandar</label><br> 
			<input type="radio" id="type2" name="type" value="Completo" checked> <label for="type2">Completo</label><br>
			</c:when>	
			<c:otherwise>
			<input type="radio" id="type1" name="type" value="Estandar" > <label	for="type1">Estandar</label><br> 
			<input type="radio" id="type2" name="type" value="Completo" > <label for="type2">Completo</label><br>
			</c:otherwise>
			</c:choose>	
				<br>
			</div>
			<label id="type_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label for="permission">Ambientes por utilizar</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
				<input type="checkbox" id="ambient1" name="ambient" value="Desarrollo"> <label for="ambient1">Desarrollo</label> <br>
				<input type="checkbox" id="ambient2" name="ambient" value="QA"> <label for="ambient2">QA</label><br>
				</div>
				<input type="checkbox" id="ambient3" name="ambient" value="Pre-Produccion"> <label for="ambient3">Pre-Producci&oacute;n</label><br>
				<input type="checkbox" id="ambient4" name="ambient" value="Produccion"> <label for="ambient4">Producci&oacute;n</label><br>
				
				<input type="checkbox" id="ambient5" name="ambient" value="Otro" onchange="changeAttributte(this)" > <label for="ambient5" style="margin-left: 15px;">Otro(s):Indicar el nombre del ambiente</label><br>
				<input id="ambient6" maxlength="150" class="tagInit" name="tags-2" type="text"  >
				<br>
			</div>
			<label id="ambient_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>

	</div>
		
	</div>
<div class="row clearfix" style="margin-top: 20px;">
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>Jeraquia de ambientes</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='change' id="change"
					class="form-control"
					placeholder="Ingrese la jeraquia de ambientes..." style="">${requestR2.hierarchy }</textarea>
			</div>
			<label id="change_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>Requerimientos</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='justify' id="justify"
					class="form-control"
					placeholder="Ingrese los requerimientos ..." style="">${requestR2.requeriments }</textarea>
			</div>
			<label id="justify_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
</div>




