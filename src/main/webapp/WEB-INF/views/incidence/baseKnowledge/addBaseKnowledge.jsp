
<div id="addRFCSection">
	<form id="formAddRFC" action="changePassword" method="post">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" /> <input type="hidden" id="idRFC" value="" />

		<div class="m-l-15">

			<div class=" row clearfix ">

				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Sistema</label>
					<div class="form-group m-b-0">
						<select id="sId" name="sId"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${system}" var="systems">
								<option value="${systems.id }">${systems.name }</option>
							</c:forEach>
						</select>

					</div>

				</div>
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Componente</label>
					<div class="form-group m-b-0">
						<select id="cId" name="cId" disabled
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
						</select>

					</div>

				</div>
			</div>

		</div>
	</form>

	<div class="button-demo flr">
		<button type="button" class="btn btn-default"
			onclick="closeRFCSection()">CANCELAR</button>
		<button id="createRFC" type="button"
			class="btn btn-primary waves-effect" onclick="createRFC()">CREAR
			ERROR</button>
	</div>

</div>