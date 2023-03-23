<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="trackingRFCModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title col-blue-grey" id="largeModalLabel">Seguimiento
					Ticket</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="trackingRFCForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="idRFC"
							value="" />

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label id="rfcNumber" class="col-cyan">Número Ticket</label>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 table-responsive">
							<table id="trackingTable"
								class="table table-bordered table-hover">
								<thead>
									<tr>
										<th colspan="2" style="text-align: center;">Estado</th>
										<th>Fecha del estado</th>
										<th>Responsable</th>
										<th>Motivo</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</form>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeTrackingRFCModal()">SALIR</button>

			</div>
		</div>
	</div>
</div>

