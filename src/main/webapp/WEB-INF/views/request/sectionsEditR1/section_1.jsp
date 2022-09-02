<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Solicitud de estimacion inicial</h5>
	</div>

	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Requerimientos iniciales del cliente</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='requeriments' id="requeriments"
					class="form-control"
					placeholder="Se deben indicar los requerimientos iniciales del cliente..."
					style="">${requestR1.initialRequeriments }</textarea>
			</div>
			<label id="requeriments_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Observaciones Generales</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='observations' id="observations"
					class="form-control"
					placeholder="Ingrese en esta sección observaciones generales sobre los requerimientos o solicitud del proyecto ..."
					style="">${requestR1.observations }</textarea>
			</div>
			<label id="observations_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>

</div>
<div class="row clearfix" style="margin-top: 20px;margin-bottom: 30px">
	<div class='col-lg-3 col-md-3 col-sm-12 col-xs-12'>
		<label>Tiempo de respuesta</label>
		<div class="form-group">
			<div class="form-line disabled">
				<input required="required" type='text'
					class="form-control datetimepicker" id='timeAnswer'
					name='timeAnswer' value="${requestR1.timeAnswer }" />
			</div>
			<label id="timeAns_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
</div>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Archivos adjuntos</h5>
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



