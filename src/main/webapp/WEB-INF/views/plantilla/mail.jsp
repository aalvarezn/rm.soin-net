<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Configuraciones extra del envio de correos</h5>
	</div>

	<c:choose>
		<c:when test="${ccs.size() >0}">
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<label for="to" class="col-sm-2 col-form-label lbtxt m-t-11">Destinatarios:
				</label>
				<div class="col-sm-6" style="margin-left: 29px;">
					<div class="form-line">
						<ul class="nav nav-pills">
							<c:forEach items="${ccs}" var="cc">
								<li class="nav-item dependency m-r-10">${cc}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<label for="to" class="col-sm-4 col-form-label lbtxt m-t-11"
					style="width: 165px; margin-bottom: 20px;">Otros
					destinatarios: </label>
				<div class="col-sm-6">
					<div class="form-group m-b-0i">
						<div class="form-line">
							<input type="text" id="senders" name="senders" value="${senders}"
								class="form-control tagInitMail" placeholder="">
						</div>
						<label id="senders_error" class="error fieldError" for="name"
							style="visibility: hidden;">Campo Requerido.</label>
					</div>
				</div>
			</div>

		</c:when>
		<c:otherwise>
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<label for="to" class="col-sm-4 col-form-label lbtxt m-t-11"
					style="width: 165px;">Destinatarios: </label>
				<div class="col-sm-6">
					<div class="form-group m-b-0i">
						<div class="form-line">
							<input type="text" id="senders" name="senders" value="${senders}"
								class="form-control tagInitMail" placeholder="">
						</div>
						<label id="senders_error" class="error fieldError" for="name"
							style="visibility: hidden;">Campo Requerido.</label>
					</div>
				</div>
			</div>
			<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<label>Mensaje personalizado</label>
					<div class="form-group m-b-0i">
						<div class="form-line">
							<textarea rows="2" cols="" id="messagePer" name="messagePer"
								class="form-control" placeholder="Ingrese un mensaje..."
								style="">${message}</textarea>
						</div>
						<label id="messagePer_error" class="error fieldError" for="name"
							style="visibility: hidden;">Campo Requerido.</label>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>
<c:if test="${ccs.size()> 0}">
	<div class="row clearfix">
		<div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"
				style="margin-top: 20px;">
				<label>Mensaje personalizado</label>
				<div class="form-group m-b-0i">
					<div class="form-line">
						<textarea rows="2" cols="" id="messagePer" name="messagePer"
							class="form-control" placeholder="Ingrese un mensaje..." style="">${message}</textarea>
					</div>
					<label id="messagePer_error" class="error fieldError" for="name"
						style="visibility: hidden;">Campo Requerido.</label>
				</div>
			</div>
		</div>
	</div>
</c:if>