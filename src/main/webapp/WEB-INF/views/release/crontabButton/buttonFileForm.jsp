<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Informaci&oacute;n del bot&oacute;n</h5>
		<input type="hidden" id="button_id" value="0">
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Descripci&oacute;n</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="description" maxlength="100" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 100 caracteres</div>
			</div>
			<label id="description_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Descripci&oacute;n html</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="descriptionHtml" maxlength="200" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 200 caracteres</div>
			</div>
			<label id="descriptionHtml_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="module">M&oacute;dulo</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="module" maxlength="100" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 100 caracteres</div>
			</div>
			<label id="module_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
</div>

<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Archivo a editar</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="fileEdit" maxlength="100" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 100 caracteres</div>
			</div>
			<label id="fileEdit_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Propietario</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="owner" maxlength="20" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 20 caracteres</div>
			</div>
			<label id="owner_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Permisos</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="permissions" maxlength="20" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 20 caracteres</div>
			</div>
			<label id="permissions_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-sm-12 m-t-20">
		<h5 class="titulares">Detalle del comando</h5>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Sustituir variables de entorno en
			el nombre de archivo</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="replaceVariables" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Disponibilidad en usermin</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="userminAvailability" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Si Ruta
			</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Comando antes de editar</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="commandBeforeEditing" maxlength="200"
					value="" class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 200 caracteres</div>
			</div>
			<label id="commandBeforeEditing_error" class="error fieldError"
				for="name" style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Comando antes de salvar</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="commandBeforeSaving" maxlength="200" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 200 caracteres</div>
			</div>
			<label id="commandBeforeSaving_error" class="error fieldError"
				for="name" style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Comando antes de ejecutar</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="commandBeforeExecuting" maxlength="200"
					value="" class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 200 caracteres</div>
			</div>
			<label id="commandBeforeExecuting_error" class="error fieldError"
				for="name" style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>

</div>

<div class="row clearfix">
	<div class="col-sm-12 m-t-20">
		<h5 class="titulares">Detalle del bot&oacute;n de comando</h5>
	</div>
</div>
<div class="row clearfix">
	<div class="button-demo flr m-t-20">
		<button type="button" class="btn btn-primary setIcon"
			onclick="openAddDetailModal($(this))">
			<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
		</button>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<div class="table-responsive"
			style="margin-top: 20px; margin-bottom: 20px;">
			<table
				class="table table-bordered table-striped table-hover dataTable"
				id="buttonFileRowsTable" style="width: 100%">
				<thead>
					<tr>
						<th>ID</th>
						<th>Nombre</th>
						<th>Descipci&oacute;n</th>
						<th>Tipo</th>
						<th>&nbsp;</th>
						<th>¿Pongo parámetros entre comillas?</th>
						<th>¿Requerido?</th>
						<th class="actCol" style="text-align: center;">Acciones</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</div>