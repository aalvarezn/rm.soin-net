<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Solicitud de Usuario</h5>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-8 col-xs-8">
		<p>
			<b>Tipo</b>
		</p>
		<div class="form-group m-b-0i">
			<div class="form-line">

				<select id="typeId" name="typeId" required="required"
					class="form-control show-tick selectpicker" data-live-search="true">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${typesPetition}" var="typePetition">
						<option value="${typePetition.id }">${typePetition.code }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
		<label for="permission">Permisos</label>
		<div class="form-group m-b-0i m-t-10">
			<div class="form-line disabled">
				<input type="checkbox" id="permission1" name="permission" value="Lectura"> <label for="permission1">Lectura</label>
				<input type="checkbox" id="permission2" name="permission" value="Escritura"> <label for="permission2">Escritura</label>
				<input type="checkbox" id="permission3" name="permission" value="Ejecucion"> <label for="permission3">Ejecución</label>
				<input type="checkbox" id="permission4" name="permission" value="Acceso"> <label for="permission4">Acceso</label>
				<br>
			</div>
		</div>

	</div>
	

<div hidden>
<select id="sgrmId" name="sgrmId"  
					class="form-control show-tick selectpicker" data-live-search="true">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${SGRMList}" var="sgrm">
						<option value="${sgrm.id }">${sgrm.name }</option>
					</c:forEach>
				</select></div>
	
</div>
<div class="row clearfix" style="margin-top: 50px;">
	<div class="col-lg-3 col-md-3 col-sm-8 col-xs-8">
		<p>
			<b>Ambiente</b>
		</p>
		<div class="form-group m-b-0i">
			<div class="form-line"  style="margin-top: 32px;">

				<select id="ambientId" name="ambientId" required="required" 
					class="form-control show-tick selectpicker" data-live-search="true">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${ambients}" var="ambient">
						<option value="${ambient.id }">${ambient.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
		

	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
		<label for="espec">Especificación</label>
		<div class="form-group m-b-0i">
		<div class="form-line" >
				<textarea rows="1" cols="" name='espec' id="espec"
					class="form-control" 
					placeholder="Indique algún dato adicional que se deba saber." style=""></textarea>
			</div>
		</div>
	</div>
</div>
<div class="row clearfix" style="margin-top: 50px;">

<div class="col-lg-3 col-md-3 col-sm-10 col-xs-10">
		<label for="name" >Nombre</label>
		<div class="form-group m-b-0i">
			<div class="form-line" style="height: 40px;">
				<input type="text" id="user" name="user" class="form-control"
					placeholder="Ingrese el nombre">
			</div>
		</div>
	</div>


	<div class="col-lg-3 col-md-3 col-sm-10 col-xs-10">
		<label for="email_address">Correo</label>
		<div class="form-group m-b-0i">
			<div class="form-line" style="height: 40px;">
				<input type="text" id="email" name="email" class="form-control"
					placeholder="Ingrese el correo">
			</div>
		</div>
	</div>

	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
	
		<div class="row clearfix">
		
			<div class="alig_btn">
			<button id="update" type="button" class="btn btn-primary setIcon"
					onclick="modUser()">
					<span>MODIFICAR</span>
				</button>
				<button type="button" class="btn btn-primary setIcon"
					onclick="addUser()">
					<span>AGREGAR</span>
				</button>
			</div>
		</div>
	</div>
	</div>
	
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="table-responsive m-b-20">
					<table
						class="table tableIni table-bordered table-striped table-hover dataTable"
						id="userTable">
						<thead>
							<tr>
								<th></th>
								<th>Nombre</th>
								<th>Correo</th>
								<th>Tipo</th>
								<th>Permisos</th>
								<th>Ambiente</th>
								<th>Especificación</th>
								<th class="actCol" style="text-align: center;">Acciones</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
<div class="form-group p-l-15 m-b-0i">
				<label id="requiredUser_error" class="error fieldError activeError"
					for="name" style="visibility: hidden;">Campo requerido.</label>
	</div>
	</div>
		
</div>

<%@include file="../../plantilla/mail.jsp"%>


