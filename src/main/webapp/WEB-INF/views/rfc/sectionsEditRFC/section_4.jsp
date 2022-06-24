<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_4" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>

<div class="button-demo flr">
	<button type="button" class="btn btn-primary setIcon"
		onclick="openAddFileModal()">
		<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
	</button>
</div>
<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Archivos adjuntos</h5>
	</div>
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
					<c:forEach items="${rfc.files}" var="fileRFC">
						<tr id="${fileRFC.id}">
							<td>${fileRFC.name}</td>
							<td><fmt:formatDate value="${fileRFC.revisionDate}"
									type="both" /></td>
							<td>
								<div style="text-align: center">
									<div class="iconLine">
										<a onclick="deleteReleaseFile(${fileRFC.id})" download
											class=""> <i class="material-icons gris"
											style="font-size: 30px;">delete</i>
										</a> <a href="<c:url value='/file/singleDownload-${fileRFC.id}'/>"
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

<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Detalles de envio</h5>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="to" class="col-sm-2 col-form-label lbtxt m-t-11">Destinatarios:</label>
			<div class="col-sm-5 ">
				<div class="form-line">
					<input type="text" id="senders" name="senders"  value="${rfc.senders}"
						class="form-control tagInit" placeholder="">
				</div>
			</div>
		</div>
		<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<label>Mensaje personalizado</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='messagePer' id="messagePer"
					class="form-control"
					placeholder="Ingrese un mensaje..." style="">${rfc.message}</textarea>
			</div>
			<label id="messagePer_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
		</div>
		</div>
	</div>

</div>