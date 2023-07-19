
<div id="addRequestSection">
	<form id="formAddRequest" action="changePassword" method="post">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" /> <input type="hidden" id="idRequest"
			value="" />

		<div class="m-l-15">

			<div class=" row clearfix ">
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Tipo de solicitud</label>
					<div class="form-group m-b-0">
						<select id="tId" name="tId"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${typePetitionsFilter}" var="typePetition">
								<option  data-content="<span data-toggle='tooltip' title='${typePetition.code}'>${typePetition.code}-${typePetition.description}</span>" value="${typePetition.id }">${typePetition.code }</option>
							</c:forEach>
						</select>

					</div>

				</div>
				<div class="col-lg-2 col-md-3 col-sm-12 col-xs-12 m-t-40" id="checkShow" hidden>

					<input type="checkbox" id="requiredFunctionalDes" class="filled-in">

					<label for="requiredFunctionalDes">&iquest;Desea crear un
						sistema nuevo?</label>
				</div>
				<div id="dropShow" >
					<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
						<label>Sistema</label>
						<div class="form-group m-b-0">
							<select id="sId" name="sId"
								class="form-control show-tick selectpicker"
								data-live-search="true">
								<option value="">-- Seleccione una opci&oacute;n --</option>
								<c:forEach items="${systems}" var="system">
									<option value="${system.id }">${system.code }</option>
								</c:forEach>
							</select>

						</div>

					</div>
					<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
						<label>Codigo proyecto</label>
						<div class="form-group m-b-0">
							<select id="sigesId" name="sigesId" disabled
								class="form-control show-tick selectpicker"
								data-live-search="true">
								<option value="">-- Seleccione una opci&oacute;n --</option>

							</select>
						</div>

					</div>
				</div>
			</div>
		</div>
	</form>
	<form id="formAddR1" hidden action="changePassword" method="post">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" /> <input type="hidden" id="idRequest"
			value="" />

		<div class="m-l-15">

			<div class=" row clearfix ">
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Tipo de solicitud</label>
					<div class="form-group m-b-0">
						<select id="tId2" name="tId2"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${typePetitionsFilter}" var="typePetition">
								<option  data-content="<span data-toggle='tooltip' title='${typePetition.code}'>${typePetition.code}-${typePetition.description}</span>" value="${typePetition.id }">${typePetition.code }</option>
							</c:forEach>
						</select>

					</div>

				</div>
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Sistema</label>
					<div class="form-group m-b-0">
						<select id="sId2" name="sId2"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${systems}" var="system">
								<option value="${system.id }">${system.code }</option>
							</c:forEach>
						</select>

					</div>

				</div>

				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>C&oacute;digo de oportunidad</label>
					<div class="form-group m-b-0">
						<input type="text" class="form-control" id="sCode" maxlength="50"
							name="sCode" onpaste="return false"
							placeholder="Escriba un texto corto '_' para separarlo"
							style="height: 60px; padding-bottom: 20px;"
							onkeypress="return verifyLetters(event)">
						<div class="help-info">Máx. 50 caracteres</div>
					</div>

				</div>
			</div>
		</div>
	</form>

	<div class="button-demo flr">
		<button type="button" title="cancelar" class="btn btn-default"
			onclick="closeRequestSection()">CANCELAR</button>
		<button id="createRequest" type="button" disabled
			class="btn btn-primary waves-effect"  onclick="createRequest()">CREAR
			SOLICITUD</button>
		<button id="createR1" type="button"
			class="btn btn-primary waves-effect" onclick="createRequestR1()">CREAR
			SOLICITUD</button>
			<button id="createR2" type="button"
			class="btn btn-primary waves-effect" onclick="openCreate()">CREAR
			SISTEMA</button>
			
	</div>

</div>