<div id="addReleaseSection">
	<form id="formAddReleaseDraft" action="changePassword" method="post">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" /> <input type="hidden" id="idRelease"
			value="" />
		<div class="m-l-15">

			<div id="system_idDiv" class=" row clearfix ">
				<div
					class=" col-lg-12 col-md-12 col-sm-12 col-xs-10 align-left p-l-10 m-b-10">
					<a onclick="closeAddReleaseSection()"><i
						class="material-icons active_icons font-50">
							keyboard_arrow_left</i> </a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
					<label>Sistema</label>
					<div class="form-group m-b-0">
						<select id="system_id" name="system_id"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${systems}" var="system">
								<option value="${system.code }">${system.code }</option>
							</c:forEach>
						</select> <label id="system_id_error" class="error" for="name"
							style="visibility: hidden">Campo Requerido.</label>
					</div>

				</div>
			</div>


			<div class=" row clearfix ">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<label>Descripci&oacute;n</label>
					<div class="form-group m-b-0">
						<div class="form-line">
							<textarea rows="2" class="form-control no-resize"
								id="description" name="description"
								placeholder="Ingrese una descripción..."></textarea>
							<div class="help-info">Min. 2 caracteres</div>

						</div>
						<label id="description_error" class="error" for="name"
							style="visibility: hidden">Campo Requerido.</label>
					</div>
				</div>

				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<label>Observaciones</label>
					<div class="form-group m-b-0">
						<div class="form-line">
							<textarea rows="2" class="form-control no-resize"
								id="observations" name="observations"
								placeholder="Ingrese una observación..."></textarea>
							<div class="help-info">Min. 2 caracteres</div>

						</div>
						<label id="observations_error" class="error" for="name"
							style="visibility: hidden">Campo Requerido.</label>
					</div>

				</div>

				<div class="col-sm-12">
					<h5 class="titulares">
						Tipo de Requerimiento <span class="text-danger">*</span>
					</h5>
				</div>

				<div class="col-sm-12">
					<div class="form-group m-b-0">
						<div class="demo-radio-button" id="requirement" name="requirement">
							<input name="group1" type="radio" id="tpo" value="TPO/BT" checked />
							<label for="tpo">TPO / BT</label> <input name="group1"
								type="radio" id="incident" value="IN" /> <label for="incident">Incidente</label>
							<input name="group1" type="radio" id="problem" value="PR" /> <label
								for="problem">Problema</label> <input name="group1" type="radio"
								id="ss" value="SS" /> <label for="ss">SS</label> <input
								name="group1" type="radio" id="so" value="SO-ICE" /> <label
								for="so">SO-ICE</label> <input name="group1" type="radio"
								id="infra" value="INFRA" /> <label for="infra">INFRA</label>
						</div>
					</div>
				</div>
				<div class="col-sm-12">
					<div class="form-group m-b-0">
						<div class="form-line">
							<textarea rows="1" class="form-control no-resize"
								id="requirement_name" name="requirement_name" maxlength="30"
								placeholder="Ingrese una búsqueda..." onkeypress="return verifyLetters(event)"></textarea>
							<div class="help-info">Min. 2 caracteres y Max.30 caracteres</div>
						</div>
						<label id="requirement_name_error" class="error" for="name"
							style="visibility: hidden">Campo Requerido.</label>
					</div>
				</div>
				<div id="listRequirement" class="col-sm-12">
					<ul class="list-group">
					</ul>
				</div>

				<div id="divAddObjectOption" class="col-sm-12">
					<label>Incluir Objetos</label>
					<div class="switch">
						<label>NO<input id="addObjectOption" type="checkbox"
							value="0"><span class="lever"></span>S&Iacute;
						</label>
					</div>
				</div>
			</div>

		</div>
	</form>

	<div class="button-demo flr">
		<button type="button" class="btn btn-default"
			onclick="closeAddReleaseSection()">CANCELAR</button>
		<button id="copyRelease" type="button"
			class="btn btn-primary waves-effect" onclick="createCopyRelease()">CREAR
			COPIA</button>
		<button id="createRelease" type="button"
			class="btn btn-primary waves-effect" onclick="createRelease()">CREAR
			RELEASE</button>
	</div>

</div>