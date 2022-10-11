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
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>Tipo del cambio</label>
		<div class="form-group m-b-0i">
			<div class="form-line" style="height: 110.667px;">
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
	
</div>
<div class="row clearfix">
	<div class="col-sm-12">
	<h5>* Nota si se desea adjuntar im�genes subirlas por favor en el siguiente paso</h5>
	</div>
	</div>
<div class="row clearfix" style="margin-top: 20px;">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
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
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
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

<div class="button-demo flr">
	<button type="button" class="btn btn-primary setIcon"
		onclick="openAddFileModal()">
		<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
	</button>
</div>
<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<div class="table-responsive"
			style="margin-top: 20px; margin-bottom: 20px;">
			<table
				class="table table-bordered table-striped table-hover dataTable"
				id="attachedFilesTable">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Fecha Carga</th>
						<th class="actCol"
							style="text-align: center; padding-left: 0px; padding-right: 0px;">Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${request.files}" var="fileRequest">
						<tr id="${fileRequest.id}">
							<td>${fileRequest.name}</td>
							<td><fmt:formatDate value="${fileRequest.revisionDate}"
									type="both" /></td>
							<td>
								<div style="text-align: center">
									<div class="iconLine">
										<a onclick="deleteReleaseFile(${fileRequest.id})" download
											class=""> <i class="material-icons gris"
											style="font-size: 30px;">delete</i>
										</a> <a href="<c:url value='/file/singleDownload-${fileRequest.id}'/>"
											download class=""> <i class="material-icons gris"
											style="font-size: 30px;">cloud_download</i>
										</a>
									</div>
								</div>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<%@include file="../../plantilla/mail.jsp"%>


