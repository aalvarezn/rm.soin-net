<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_3" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>

<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Detalles de la implementaci&oacute;n</h5>
	</div>
	

	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label for="email_address">Detalles</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" id="detailRFC"
					class="form-control"
					name="detailRFC"
					placeholder="Ingrese los detalles de implementaci&oacute;n..."
					style="">${rfc.detail}</textarea>
			</div>
			<label id="detailRFC_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo Requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label for="email_address">Plan de retorno </label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" id="returnPlanRFC"
					name="returnPlanRFC" class="form-control"
					placeholder="Ingrese el plan de retorno..."
					style="">${rfc.returnPlan}</textarea>
			</div>
			<label id="returnPlanRFC_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo Requerido.</label>
		</div>
	</div>
	
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Evidencias</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" id="evidenceRFC"
					class="form-control"
					name="evidenceRFC"
					placeholder="Ingrese las evidencias..."
					style="">${rfc.evidence}</textarea>
			</div>
			<label id="evidenceRFC_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo Requerido.</label>
		</div>
		
	</div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label>Requisitos especiales</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" id="requestEspRFC"
					class="form-control"
					name="requestEspRFC"
					placeholder="Ingrese los requisitos especiales..."
					style="">${rfc.requestEsp}</textarea>
			</div>
			<label id="requestEspRFC_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo requerido.</label>
		</div>
		
	</div>
	
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Requiere base de datos</label>
				<c:choose>
					<c:when test="${rfc.requiredBD}">
						<div class="switch" style="margin-top: 20px;">
							<label>NO<input id="requiredBD" type="checkbox" value="1"
								name="requiredBD" checked="checked"><span class="lever"></span>S&Iacute;
							</label>
						</div>
					</c:when>
					<c:otherwise>
						<div class="switch" style="margin-top: 20px;">
							<label>NO<input id="requiredBD" name="requiredBD"
								type="checkbox" value="0"><span class="lever"></span>S&Iacute;
							</label>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<div id="tagShow" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<label for="email_address">Ingrese nombre de BD</label>
				<input id="bd" maxlength="150"
					class="tagInit" name="tags-1" type="text"
					value="${rfc.schemaDB}">
			</div>
</div>
