<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="nodeModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Estado
					tr&aacute;mite a Saltar</h4>
			</div>
			<div class="modal-body">
				<form id="nodeModalForm" action="">
					<div class="row clearfix">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="id" value="" />
					</div>
					<div class="row clearfix">

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label>Estado a saltar</label>
							<div class="form-group m-t-25">
								<select id="statusSkipId" name="statusSkipId" required="required"
									class="form-control show-tick selectpicker"
									data-live-search="true">
									<option value="">-- Ninguno --</option>
								</select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<label for="name">Motivo</label>
							<div class="form-group">
								<div class="form-line">
									<input type="text" class="form-control" id="motiveSkip"
										name="motiveSkip" placeholder="Ingrese un motivo"
										style="height: 60px;">
								</div>

							</div>
						</div>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeNode()">CANCELAR</button>
				<button id="save" type="button"
					class="btn btn-primary waves-effect"
					onclick="updateRequest()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>
