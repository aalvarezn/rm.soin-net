<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_2" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>
<div class="row clearfix activeSection">
	<div class="col-sm-12">
		<h5 class="titulares">Informaci&oacute;n de Cambio</h5>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 p-b-20">
		<p>
			<b>Sistema</b>
		</p>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<select class="form-control show-tick" id="systemId" name="systemId">
					<option value="">-- Seleccione una opci&oacute;n --</option>
					<c:forEach items="${systems}" var="systems">
						<option value="${systems.id }">${systems.name }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group p-l-15 m-b-0i">
				<label id="riskId_error" class="error fieldError" for="name"
					style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>
	</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
	<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="table-responsive m-b-20">
						<table
							class="table tableIni table-bordered table-striped table-hover dataTable"
							id="releaseTable">
							<thead>
								<tr>
									<th>Numero Release</th>
									<th class="actCol" style="text-align: center;">Acciones</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>

	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="row clearfix">
				<div class="alig_btn" style="margin-top: 10px;">
					<button type="button" class="btn btn-primary setIcon"
						onclick="addDataToTable()">
						<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
					</button>
				</div>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="table-responsive"
					style="margin-top: 20px; margin-bottom: 20px;">
					<div class="help-info">
						<b>Releases a instalar Prod </b>
					</div>
					<table
						class="table table-bordered table-striped table-hover dataTable"
						id="releaseTableAdd">
						<thead>
							<tr>
								<th>Numero Release</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-40">
		<c:if test="${empty  release.functionalDescription}">
			<input type="checkbox" id="requiredFunctionalDes" class="filled-in">
		</c:if>

		<c:if test="${not empty  release.functionalDescription}">
			<input type="checkbox" id="requiredFunctionalDes" class="filled-in"
				checked="checked">
		</c:if>
		<label for="requiredFunctionalDes">&iquest;Requiere BD?</label>
	</div>
</div>



