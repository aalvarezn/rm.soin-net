<div class="row clearfix">
	<div class="col-sm-12">
		<h5 class="titulares">Creaci&oacute;n de Botones</h5>
		<input type="hidden" id="button_id" value="0">
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Nombre del Bot&oacute;n</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="name" value="" maxlength="50"
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="name_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-20">
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
</div>
<div class="row clearfix">
	<div class="col-sm-12 m-t-20">
		<h5 class="titulares">Detalle del comando</h5>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Comando</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="command" maxlength="200" value=""
					class="form-control" placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 200 caracteres</div>
			</div>
			<label id="command_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Ejecutar en directorio</label>
		<div class="switch" style="margin-top: 20px;">
			<label>Ruta por defecto<input id="executeDirectory"
				type="checkbox" value="0"><span class="lever inputSelect"></span>Otra
				Ruta
			</label>
		</div>
	</div>
	<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">&nbsp;</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="directoryName" maxlength="50" value=""
					class="form-control" disabled="disabled"
					placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="directoryName_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Ejecutar como usuario</label>
		<div class="switch" style="margin-top: 20px;">
			<label>Usuario Webmin<input id="executeUser" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Otro
			</label>
		</div>
	</div>
	<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">&nbsp;</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="userName" maxlength="50" value=""
					class="form-control" disabled="disabled"
					placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="userName_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Utilizo entorno de usuario?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="useUserEnvironment" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">El comando tiene salidad HTML?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="haveHTML" type="checkbox" value="0"><span
				class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Ocultar al ejecutar?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="hideExecute" type="checkbox" value="0"><span
				class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Disponible en Usermin?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="userminAvailability" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Limpiar variables de entorno?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>No<input id="clearVariables" type="checkbox" value="0"><span
				class="lever inputSelect"></span>Si
			</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Tiempo máximo de espera a un
			comando?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>Por Defecto<input id="waitCommand" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Definir el
				tiempo
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">&nbsp;</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="timeCommand" name="input" maxlength="50"
					value="" class="form-control" disabled="disabled"
					placeholder="20 Segundos">
				<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="timeCommand_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
		</div>
	</div>
</div>
<div class="row clearfix">
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">Ordenado en la página principal?</label>
		<div class="switch" style="margin-top: 20px;">
			<label>Por Defecto<input id="principalPage" type="checkbox"
				value="0"><span class="lever inputSelect"></span>Otro
			</label>
		</div>
	</div>
	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 m-t-20">
		<label for="email_address">&nbsp;</label>
		<div class="form-group m-b-0i">
			<div class="form-line">
				<input type="text" id="pageName" name="input" maxlength="50"
					value="" class="form-control" disabled="disabled"
					placeholder="Ingrese un valor..">
				<div class="help-info">M&aacute;x. 50 caracteres</div>
			</div>
			<label id="pageName_error" class="error fieldError" for="name"
				style="visibility: hidden;">Valor requerido.</label>
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
				id="buttonRowsTable" style="width: 100%">
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