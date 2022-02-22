<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="docFileModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Plantilla Archivo</h4>
			</div>
			<div class="modal-body">
				<form id="docFileModalForm" action="fileUpload"
					onsubmit="return false;" method="post"
					enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="row clearfix">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<label for="name">Archivo</label>
							<div class="form-group">
								<input type="file" id="inputFile" class="form-control inputFile">
								<label class="custom-file-label" for="customFileLang"
									style="">Seleccionar archivo</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeDocFileModal()">CANCELAR</button>
				<button id="btnUpdateDocFile" type="button"
					class="btn btn-primary waves-effect" onclick="uploadInputFile()">CARGAR</button>
			</div>
		</div>
	</div>
</div>
