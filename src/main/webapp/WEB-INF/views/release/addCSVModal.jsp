<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="addCSVModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Carga masiva de
					objetos por CSV</h4>
			</div>
			<div class="modal-body">

				<div class="row clearfix">
					<form id="csvForm" action="">
						<div class=" col-lg-12 col-md-12 col-xs-12 col-sm-12">
							<div class="form-group">
								<div class="form-line">
									<textarea rows="4" cols="" id="csv" name="csv"
										class="form-control"
										placeholder="Ingrese un c&oacute;digo CSV..." style=""></textarea>
								</div>
								<p class="p-t-5" style="font-size: 12px;">
									Nombre,Descripción,CódigoRevisiónSVN,FechaRevisionSVN(dd-mm-YYYY),NombreItemConfiguración,NombreTipo
								</p>
							</div>
						</div>
						<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 p-l-10">
							<div class="footer_perfil">
								<p>
									* No deje lineas en blanco, ni espacios entre las comas.<br>
									* Procure no dejar una línea en blanco al final.<br>
								</p>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeCSVModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="uploadCSV()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>