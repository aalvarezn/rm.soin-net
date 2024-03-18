<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="systemConfigModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Configurar
					secciones del sistema</h4>
			</div>
			<div class="modal-body">
				<div class="row clearfix">
					<form id="systemConfigForm" action="">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden"
							id="systemConfigId" value="" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 m-b-10">
							<label>Sistema</label>
							<div class="form-group">
								<select id="systemId"
									class="form-control show-tick selectpicker" data-live-search="true">
									<option value="">-- Seleccione una opci&oacute;n --</option>
									<c:forEach items="${systems}" var="system">
										<option id="${system.id }" value="${system.id }">${system.name }</option>
									</c:forEach>
								</select> <label id="systemId_error" class="error fieldError"
									for="system" style="visibility: hidden">Campo
									Requerido.</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Observaciones<input
									id="observations" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Informaci&oacute;n de la soluci&oacute;n<input
									id="solutionInfo" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Definici&oacute;n de ambientes<input
									id="definitionEnvironment" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Datos de instalaci&oacute;n<input
									id="instalationData" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Instrucciones de base de datos<input
									id="dataBaseInstructions" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Bajar ambientes<input id="downEnvironment"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Observaciones de ambientes<input
									id="environmentObservations" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Pruebas sugeridas<input id="suggestedTests"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Items de configuraci&oacute;n<input
									id="configurationItems" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Dependencias<input id="dependencies"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Archivos adjuntos<input id="attachmentFiles"
									type="checkbox" value="0"><span class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Versi&oacute;n de aplicaci&oacute;n<input
									id="applicationVersion" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 align-right">
							<div class="switch" style="margin-top: 20px;">
								<label>Bugs<input
									id="bugs" type="checkbox" value="0"><span
									class="lever"></span>
								</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeSystemConfigModal()">CANCELAR</button>
				<button id="btnSaveSystemConfig" type="button"
					class="btn btn-primary waves-effect" onclick="saveSystemConfig()">GUARDAR</button>
				<button id="btnUpdateSystemConfig" type="button"
					class="btn btn-primary waves-effect" onclick="updateSystemConfig()">ACTUALIZAR</button>
			</div>
		</div>
	</div>
</div>