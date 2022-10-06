<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Detalles del ticket</h5>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
		<label for="email_address">Titulo a Asunto</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text"  id="title" name="title"
					value="${incidence.title}" class="form-control" placeholder="">
			
			</div>
			
		</div>
		<div class="form-group p-l-15 m-b-0i">
				<label id="title_error" class="error fieldError activeError"
					for="name" style="visibility: hidden;">Campo requerido.</label>
			</div>
	</div>
	
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
		
			<b>Tipo de ticket </b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<select class="form-control show-tick" id="tId"
					name="tId">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${typeincidences}" var="typeincidence">
						<c:choose>
							<c:when test="${typeincidence.id == incidence.typeIncidence.id}">
								<option selected="selected" value="${typeincidence.id }">${typeincidence.typeIncidence.code }</option>
							</c:when>
							<c:otherwise>
								<option value="${typeincidence.id }">${typeincidence.typeIncidence.code }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>

			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="tId_error" class="error fieldError activeError"
					for="name" style="visibility: hidden;">Campo requerido.</label>
			</div>
		</div>


	</div>


	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
			<b>Prioridad</b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="form-line">
					<select class="form-control show-tick" id="pId"
						name="pId">
						<option value="">-- Seleccione una opci&oacute;n --</option>
						<c:forEach items="${priorities}" var="priority">
							<c:choose>
								<c:when test="${priority.priority.id == incidence.priority.priority.id}">
									<option selected="selected" value="${priority.priority.id }">${priority.priority.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${priority.priority.id }">${priority.priority.name }</option>
								</c:otherwise>
							</c:choose>

						</c:forEach>
					</select>

				</div>

			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="pId_error" class="error fieldError activeError"
					for="name" style="visibility: hidden;">Campo requerido.</label>
			</div>
		</div>
	</div>
</div>
<div class="row clearfix activeSection">
	
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Detalle del problema</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='detail' id="detail"
					class="form-control"
					placeholder="Ingrese el detalle del problema..." style="">${incidence.detail}</textarea>
			</div>
			<label id="detail_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>

<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12"
		style="margin-bottom: 20px;">
		<label>Resultado esperado. </label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='result' id="result"
					name="result" class="form-control"
					placeholder="Ingrese el resultado deseado..." style="">${incidence.result}</textarea>
			</div>
			<label id="result_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12"
		style="margin-bottom: 20px;">
		<label>Notas adicionales. </label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='note' id="note"
					name="note" class="form-control"
					placeholder="Ingrese una nota adicional..." style="">${incidence.note}</textarea>
			</div>
			<label id="note_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
</div>



