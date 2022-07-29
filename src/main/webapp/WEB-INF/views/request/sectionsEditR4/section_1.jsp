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



	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="name">Nombre</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="name" name="name" class="form-control"
					placeholder="Ingrese el nombre">
			</div>
		</div>
	</div>


	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="email_address">Correo</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
				<input type="text" id="email" name="email" class="form-control"
					placeholder="Ingrese el correo">
			</div>
		</div>
	</div>

	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="type">Tipo</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
					<input type="radio" id="type1" name="type" value="Ambiente"> <label	for="type1">Ambiente</label><br> 
					<input type="radio" id="type2" name="type" value="Aplicacion"> <label for="type2">Aplicacion</label><br>
					<input type="radio" id="type3" name="type" value="SGRM"> <label for="type3">SGRM</label><br> 
					<input type="radio" id="type4" name="type" value="Base de datos"> <label for="type4">Base de datos</label><br> <br>
			</div>
		</div>

	</div>


	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="permission">Permisos</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
				<input type="checkbox" id="permission1" name="permission" value="Lectura"> <label for="permission1">Lectura</label> <br>
				<input type="checkbox" id="permission2" name="permission" value="Escritura"> <label for="permission2">Escritura</label><br>
				<input type="checkbox" id="permission3" name="permission" value="Ejecucion"> <label for="permission3">Ejecucion</label><br>
				<input type="checkbox" id="permission4" name="permission" value="Acceso"> <label for="permission4">Acceso</label><br>
				<br>
			</div>
		</div>

	</div>


	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<p>
			<b>Ambiente</b>
		</p>
		<div class="form-group m-b-0i">
			<div class="form-line">

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

	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<label for="espec">Especificacion</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
				<textarea cols="18" id="espec" name="espec"></textarea>
			</div>
		</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
		<div class="row clearfix">
			<div class="alig_btn">
				<button type="button" class="btn btn-primary setIcon"
					onclick="addUser()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
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
								<th>Especificacion</th>
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




