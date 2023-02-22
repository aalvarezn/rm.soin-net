<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Informaci&oacute;n General</h5>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 m-t-20">
		<div class="clearfix m-l--15">
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">ERROR</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="releaseNumberTinySummary"
							name="systemCode" value="${baseKnowledge.numError}"
							class="form-control" placeholder="">
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Sistema</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="systemName" name="systemName"
							value="${baseKnowledge.system.name}" class="form-control"
							placeholder="">
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Componente</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="systemCodeTinySummary"
							name="systemCode" value="${baseKnowledge.component.name}"
							class="form-control" placeholder="">
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Solicitado por</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="userTinySummary" name="systemCode"
							value="${baseKnowledge.user.fullName}" class="form-control"
							placeholder="">
					</div>
				</div>
			</div>

		</div>
		<div class="clearfix m-t-20 m-l--15">
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Fecha de creaci&oacute;n</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="dateTinySummary" name="dateCreate"
							value='<fmt:formatDate
										value="${baseKnowledge.requestDate }" pattern="dd/MM/YYYY HH:mm:ss"  />'
							class="form-control" placeholder="">
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<label for="email_address">Estado</label>
				<div class="form-group m-b-0i">
					<div class="form-line disabled">
						<input type="text" disabled id="statusTinySummary"
							name="systemCode" value="${baseKnowledge.status.name }"
							class="form-control" placeholder="">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>