<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div class="row clearfix">
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
		<label for="email_address">Sistema</label>
		<div class="form-group m-b-0i">
			<div class="form-line disabled">
				<input type="text" disabled id="systemCode" name="systemCode"
					value="${release.system.code}" class="form-control" placeholder="">
			</div>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
			<b>Impacto</b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<select class="form-control show-tick" id="impactId" name="impactId">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${impacts}" var="impact">
						<c:choose>
							<c:when test="${impact.id == release.impact}">
								<option selected="selected" value="${impact.id }">${impact.name }</option>
							</c:when>
							<c:otherwise>
								<option value="${impact.id }">${impact.name }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="impactId_error" class="error fieldError activeError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
			<b>Riesgo</b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<select class="form-control show-tick" id="riskId" name="riskId">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${risks}" var="risk">
						<c:choose>
							<c:when test="${risk.id == release.risk}">
								<option selected="selected" value="${risk.id }">${risk.name }</option>
							</c:when>
							<c:otherwise>
								<option value="${risk.id }">${risk.name }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="riskId_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
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
					<select class="form-control show-tick" id="priorityId"
						name="priorityId">
						<option value="">-- Seleccione una opci&oacute;n --</option>
						<c:forEach items="${priorities}" var="priority">
							<c:choose>
								<c:when test="${priority.id == release.priority}">
									<option selected="selected" value="${priority.id }">${priority.name }</option>
								</c:when>
								<c:otherwise>
									<option value="${priority.id }">${priority.name }</option>
								</c:otherwise>
							</c:choose>

						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="priorityId_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
		<label for="email_address">Descripci&oacute;n</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<textarea rows="2" cols="" id="description" name="description"
					maxlength="4000" class="form-control"
					placeholder="Ingrese una descripci&oacute;n..." style="">${release.description}</textarea>
			</div>
			<label id="description_error" class="error fieldError" for="name"
				style="visibility: hidden;">Campo Requerido.</label>
		</div>
	</div>
	<c:if test="${systemConfiguration.solutionInfo}">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="email_address">Soluci&oacute;n T&eacute;cnica</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="technicalSolution"
						name="technicalSolution" class="form-control"
						placeholder="Ingrese una solucion..." style="">${release.technicalSolution}</textarea>
				</div>
				<label id="technicalSolution_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="email_address">Soluci&oacute;n Funcional</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="functionalSolution"
						name="functionalSolution" class="form-control"
						placeholder="Ingrese una solucion..." style="">${release.functionalSolution}</textarea>
				</div>
				<label id="functionalSolution_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="email_address">Consecuencias si no se instala</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="notInstalling"
						name="notInstalling" class="form-control"
						placeholder="Consecuencias..." style="">${release.notInstalling}</textarea>
				</div>
				<label id="notInstalling_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<label for="email_address">Observaciones</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="observations" name="observations"
					 class="form-control"
						placeholder="Ingrese una observaci&oacute;n..." style="">${release.observations}</textarea>
				</div>
				<label id="observations_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</c:if>
</div>

<c:if test="${systemConfiguration.applicationVersion}">
	<div class="row clearfix">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<label for="email_address">Versi&oacute;n</label>
			<div class="form-group m-b-0i">
				<div class="form-line">
					<textarea rows="2" cols="" id="versionNumber" name="versionNumber"
						maxlength="20" class="form-control"
						placeholder="Ingrese una versi&oacute;n" style="">${release.versionNumber}</textarea>
						<div class="help-info">M&aacute;x. 20 caracteres</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
