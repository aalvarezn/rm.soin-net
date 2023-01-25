<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_3" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>
<c:if test="${release.system.customCommands}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Crontabs</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="alig_btn" style="margin-top: 10px;">
				<button type="button" class="btn btn-primary setIcon"
					onclick="openCrontabForm()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="table-responsive"
				style="margin-top: 20px; margin-bottom: 20px;">
				<table
					class="table table-bordered table-striped table-hover dataTable"
					id="crontabTable">
					<thead>
						<tr>
							<th>Comando</th>
							<th>Usuario</th>
							<th>Descripci&oacute;n</th>
							<th>Entrada del comando</th>
							<th class="actCol" style="text-align: center;">Acciones</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${release.crontabs}" var="crontab">
							<tr id="${crontab.id}">
								<td>${crontab.commandCron}</td>
								<td>${crontab.user}</td>
								<td>${crontab.descriptionCron}</td>
								<td>${crontab.commandEntry}</td>
								<td><div style="text-align: center">
										<i onclick="editCrontab(${crontab.id})"
											class="material-icons gris" style="font-size: 30px;">mode_edit</i>
										<i onclick="deleteCrontab(${crontab.id})"
											class="material-icons gris" style="font-size: 30px;">delete</i>
									</div></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Botones de comandos</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="alig_btn" style="margin-top: 10px;">
				<button type="button" class="btn btn-primary setIcon"
					onclick="openButtonForm()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="table-responsive"
				style="margin-top: 20px; margin-bottom: 20px;">
				<table
					class="table table-bordered table-striped table-hover dataTable"
					id="buttonsTable" style="width: 100%">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Descripci&oacute;n</th>
							<th>Comando</th>
							<th>Object</th>
							<th class="actCol" style="text-align: center;">Acciones</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${release.buttons}" var="button">
							<tr id="${button.id}">
								<td>${button.name}</td>
								<td>${button.description}</td>
								<td>${button.command}</td>
								<td><input id="button-${button.id}" value="${button}"></td>
								<td><div style="text-align: center">
										<i onclick="editButton(${button.id})"
											class="material-icons gris" style="font-size: 30px;">mode_edit</i>
										<i onclick="deleteButton(${button.id})"
											class="material-icons gris" style="font-size: 30px;">delete</i>
									</div></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Botones de edici&oacute;n de archivos</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="alig_btn" style="margin-top: 10px;">
				<button type="button" class="btn btn-primary setIcon"
					onclick="openButtonFileForm()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="table-responsive"
				style="margin-top: 20px; margin-bottom: 20px;">
				<table
					class="table table-bordered table-striped table-hover dataTable"
					id="buttonFilesTable">
					<thead>
						<tr>
							<th>Descripci&oacute;n</th>
							<th>Descripci&oacute;n html</th>
							<th>Archivo</th>
							<th class="actCol" style="text-align: center;">Acciones</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${release.buttonsFile}" var="buttonFile">
							<tr id="${buttonFile.id}">
								<td>${buttonFile.description}</td>
								<td>${buttonFile.descriptionHtml}</td>
								<td>${buttonFile.fileEdit}</td>
								<td><div style="text-align: center">
										<i onclick="editButtonFile(${buttonFile.id})"
											class="material-icons gris" style="font-size: 30px;">mode_edit</i>
										<i onclick="deleteButtonFile(${buttonFile.id})"
											class="material-icons gris" style="font-size: 30px;">delete</i>
									</div></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${systemConfiguration.configurationItems}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Items de Configuraci&oacute;n y Objetos</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-b-20">
			<div class="alig_btn" style="margin-top: 10px;">
				<c:if test="${release.system.importObjects}">
					<button type="button" class="btn btn-default setIcon"
						onclick="synchronizeObjects()">
						<span>SINCRONIZAR</span><span style="margin-left: 10px"><i
							class="material-icons m-t--2 ">autorenew</i></span>
					</button>
				</c:if>
				<button type="button" class="btn btn-default setIcon"
					onclick="openCSVModal()">
					<span>SUBIR CSV</span><span style="margin-left: 10px"><i
						class="material-icons m-t--2 ">cloud_upload</i></span>
				</button>
				<button type="button" class="btn btn-primary setIcon"
					onclick="openObjectItemModal()">
					<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
				</button>
			</div>
		</div>
	</div>

	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

			<div class="help-info">
				Agregados: <span id="countObject"></span>
			</div>

			<table id="tableTest4"
				class="table table-bordered table-striped table-hover dataTable no-footer">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Fecha Revisión</th>
						<th>Número Revisión</th>
						<th>Tipo</th>
						<th>Item Configuración</th>
						<th class="actCol"
							style="text-align: center; padding-left: 0px; padding-right: 0px;">Acciones</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div style="margin-top: -15px;" class="form-group m-t-10 m-b-0i">
				<label id="configurationItemsTable_error" class="error fieldError"
					for="name" style="visibility: hidden;">Campo Requerido.</label>
			</div>
		</div>

	</div>

	<div class="row clearfix">

		<div class="col-sm-12">
			<h5 class="titulares">Objetos SQL</h5>
		</div>
	</div>

	<div class="row clearfix">
		<div class="col-lg-12 col-md-6 col-sm-12 col-xs-12">
			<div class="table-responsive"
				style="margin-top: 20px; margin-bottom: 20px; overflow-x: auto;">
				<table
					class="table tableIni table-bordered table-striped table-hover dataTable"
					id="sqlObjectTable">
					<thead>
						<tr>
							<th>Nombre del Objeto</th>
							<th>Ocupa Ejecutar</th>
							<th>Esquema</th>
							<th>Requiere Plan de ejecuci&oacute;n</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${releaseObject}" var="releaseObjects">
							<c:if test="${releaseObjects.objects.isSql == 1}">
								<tr id="${releaseObjects.objects.id}">
									<td>${releaseObjects.objects.name}</td>
									<td><c:choose>
											<c:when test="${releaseObjects.objects.execute == 1}">
												<div class="switch">
													<label>NO<input
														id="obj_sql_exec_${releaseObjects.objects.id}" type="checkbox"
														checked="checked" value="1"><span class="lever"></span>S&Iacute;
													</label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="switch">
													<label>NO<input
														id="obj_sql_exec_${releaseObjects.objects.id}" type="checkbox"
														value="0"><span class="lever"></span>S&Iacute;
													</label>
												</div>
											</c:otherwise>
										</c:choose></td>
									<td><input id="form-tags-${releaseObjects.objects.id}"
										maxlength="150" class="tagInit" name="tags-1" type="text"
										value="${releaseObjects.objects.dbScheme}">
										<div class="form-group m-b-0i">
											<label id="form-tags-${releaseObjects.objects.id}_error"
												class="error fieldError" for="name"
												style="visibility: hidden;">Campo Requerido.</label>
										</div></td>
									<td><c:choose>
											<c:when test="${releaseObjects.objects.executePlan == 1}">
												<div class="switch">
													<label>NO<input id="obj_sql_rp_${releaseObjects.objects.id}"
														type="checkbox" checked="checked" value="1"><span
														class="lever"></span>S&Iacute;
													</label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="switch">
													<label>NO<input id="obj_sql_rp_${releaseObjects.objects.id}"
														type="checkbox" value="0"><span class="lever"></span>S&Iacute;
													</label>
												</div>
											</c:otherwise>
										</c:choose></td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${systemConfiguration.dependencies}">
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Dependencias del Release</h5>
		</div>
	</div>
	<div class="row clearfix">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<!-- Nav tabs -->
			<ul class="nav nav-tabs tab-nav-right" role="tablist">
				<li role="presentation" class="active"><a href="#technicals"
					data-toggle="tab">T&Eacute;CNICAS</a></li>
				<li role="presentation"><a href="#functionals"
					data-toggle="tab">FUNCIONALES</a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="technicals"
					style="padding-top: 0px;">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group m-b-0i">
								<div class="form-line">
									<input type="text" id="dependencyTechnical" maxlength="50"
										name="dependencyTechnical" class="form-control"
										placeholder="Ingrese las dependencias..."
										style="height: 60px;">
									<div class="help-info">M&aacute;x. 50 caracteres</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div id="listTechnicalsDependencies" class="col-sm-12 m-t-20">
							<ul class="nav nav-pills">
								<c:forEach items="${release.dependencies}" var="dependency">
									<c:if test="${not dependency.isFunctional}">
										<li id="${dependency.to_release.id}"
											value="${dependency.to_release.id}"
											class="nav-item dependency">
											${dependency.to_release.releaseNumber} <c:if
												test="${not dependency.mandatory }">
												<span class="flr m-b--10" style="margin-top: -3px;">
													<a
													onclick="deleteDependency(${dependency.to_release.id}, false)"
													title="Borrar"><i class="gris"><span
															class="lnr lnr-cross-circle p-l-5"></span></i></a>
												</span>
											</c:if>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
						<div class="form-group m-b-0i p-l-15">
							<label id="dependency_error" class="error fieldError" for="name"
								style="visibility: hidden;">Valor requerido.</label>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="functionals"
					style="padding-top: 0px;">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group m-b-0i">
								<div class="form-line">
									<input type="text" id="dependencyFunctional" maxlength="50"
										name="dependencyFunctional" class="form-control"
										placeholder="Ingrese las dependencias..."
										style="height: 60px;">
									<div class="help-info">M&aacute;x. 50 caracteres</div>
								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 m-t-40">
							<c:if test="${empty  release.functionalDescription}">
								<input type="checkbox" id="requiredFunctionalDes"
									class="filled-in">
							</c:if>

							<c:if test="${not empty  release.functionalDescription}">
								<input type="checkbox" id="requiredFunctionalDes"
									class="filled-in" checked="checked">
							</c:if>
							<label for="requiredFunctionalDes">&iquest;No se
								encuentra la dependencia?</label>
						</div>
					</div>
					<div class="row">
						<div id="listFunctionalsDependencies" class="col-sm-12 m-t-20">
							<ul class="nav nav-pills">
								<c:forEach items="${release.dependencies}" var="dependency">
									<c:if test="${dependency.isFunctional}">
										<li id="${dependency.to_release.id}"
											value="${dependency.to_release.id}"
											class="nav-item dependency">
											${dependency.to_release.releaseNumber} <span
											class="flr m-b--10" style="margin-top: -3px;"> <a
												onclick="deleteDependency(${dependency.to_release.id}, true)"
												title="Borrar"><i class="gris"><span
														class="lnr lnr-cross-circle p-l-5"></span></i></a>
										</span>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
						<div class="form-group m-b-0i p-l-15">
							<label id="dependency_error" class="error fieldError" for="name"
								style="visibility: hidden;">Valor requerido.</label>
						</div>
					</div>
					<div class="row">
						<c:if test="${empty  release.functionalDescription}">
							<div id="divRequiredFunctionalDes" class="col-sm-12 m-t-20"
								style="display: none;">
								<label for="email_address">Descripci&oacute;n de la
									dependencia</label>
								<div class="form-group m-b-0i">
									<div class="form-line">
										<textarea rows="2" cols="" id="functionalDescription"
											maxlength="500" name="functionalDescription"
											class="form-control"
											placeholder="Ingrese las dependencias no encontradas..."
											style="">${release.functionalDescription}</textarea>
										<div class="help-info">M&aacute;x. 500 caracteres</div>
									</div>
								</div>
							</div>
						</c:if>

						<c:if test="${not empty  release.functionalDescription}">
							<div id="divRequiredFunctionalDes" class="col-sm-12 m-t-20"
								style="display: block;">
								<label for="email_address">Descripcion de la dependencia</label>
								<div class="form-group m-b-0i">
									<div class="form-line">
										<textarea rows="2" cols="" id="functionalDescription"
											maxlength="500" name="functionalDescription"
											class="form-control"
											placeholder="Ingrese las dependencias no encontradas..."
											style="">${release.functionalDescription}</textarea>
										<div class="help-info">M&aacute;x. 500 caracteres</div>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
