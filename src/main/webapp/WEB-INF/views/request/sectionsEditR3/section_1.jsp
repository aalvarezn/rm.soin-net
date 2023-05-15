<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Registro de herramienta y acceso a usuarios</h5>
		
	</div>
	
	<div class="form-group m-b-0i">
	<div class="form-line">
	<div role="tabpanel" class="tab-pane" id="tabRoles">
		<div class="row m-t-20">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<label>Lista de colaboradores con acceso a la herramienta</label>
			</div>
				<select class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id='userGroups' multiple='multiple'>
					<c:forEach items="${usersRM}" var="user">
						<option id="${user.id}" value='${user.id}'>${user.fullName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<label id="userRM_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo requerido.</label>
	</div>
	</div>
	
</div>
</div>
<div class="row clearfix" style="margin-top: 20px;">
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<label>M�todo de conexi&oacute;n</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='connectionMethod' id="connectionMethod"
					class="form-control"
					placeholder="Ingrese la informaci&oacute;n del metodo de conexi&oacute;n..."
					style="">${requestR3.connectionMethod }</textarea>
			</div>
			<label id="connectionMethod_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo requerido.</label>
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


