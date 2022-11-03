<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>



<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Detalles del error</h5>
	</div>


	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Descripci&oacute;n del error</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='description' id="description"
					class="form-control"
					placeholder="Ingrese una pequeña descripci&oacute;n del error..." style="">${baseKnowledge.description}</textarea>
			</div>
			<label id="description_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12"
		style="margin-bottom: 20px;">
		<label>Datos requeridos para escalar. </label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='data' id="data"
					name="data" class="form-control"
					placeholder="Ingrese datos requeridos para escalar..." style="">${baseKnowledge.dataRequired}</textarea>
			</div>
			<label id="data_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12"
		style="margin-bottom: 20px;">
		<label>Notas. </label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" name='note' id="note"
					name="note" class="form-control"
					placeholder="Ingrese alguna nota adicional..." style="">${baseKnowledge.note}</textarea>
			</div>
			<label id="note_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
	</div>
	
		<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Publicado</label>
				<c:choose>
					<c:when test="${baseKnowledge.publicate}">
						<div class="switch" style="margin-top: 20px;">
							<label>NO<input id="publicate" type="checkbox" value="0"
								name="publicate" checked="checked"><span class="lever"></span>S&Iacute;
							</label>
						</div>
					</c:when>
					<c:otherwise>
						<div class="switch" style="margin-top: 20px;">
							<label>NO<input id="publicate" name="publicate"
								type="checkbox" value="1"><span class="lever"></span>S&Iacute;
							</label>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
	
</div>



