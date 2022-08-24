<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>


<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Configuraciones extra del envio de correos</h5>
	</div>
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="to" class="col-sm-2 col-form-label lbtxt m-t-11">Destinatarios:</label>
			<div class="col-sm-5 ">
				<div class="form-line">
					<input type="text" id="senders" name="senders"  value="${request.senders}"
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
					placeholder="Ingrese un mensaje..." style="">${request.message}</textarea>
			</div>
			<label id="messagePer_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo requerido.</label>
		</div>
		</div>
		</div>
	</div>

</div>