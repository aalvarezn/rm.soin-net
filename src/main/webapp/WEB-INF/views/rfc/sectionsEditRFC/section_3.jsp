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
			<label id="installationInstructions_error" class="error fieldError"
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
			<label id="verificationInstructions_error" class="error fieldError"
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
					placeholder="Ingrese los detalles de implementaci&oacute;n..."
					style="">${rfc.evidence}</textarea>
			</div>
			<label id="installationInstructions_error" class="error fieldError"
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
					placeholder="Ingrese los detalles de implementaci&oacute;n..."
					style="">${rfc.requestEsp}</textarea>
			</div>
			<label id="installationInstructions_error" class="error fieldError"
				for="name" style="visibility: hidden;">Campo Requerido.</label>
		</div>
		
	</div>
</div>
