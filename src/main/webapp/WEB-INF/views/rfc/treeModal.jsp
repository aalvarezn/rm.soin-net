<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="modal fade" id="treeModal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-body">

			<!-- 			<div class="row clearfix"> -->
			<!-- 				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"> -->
			<!-- 					<div class="" style="padding-top: -5pc;"> -->
			<!-- 						<h2 class="title-Adm m-t-0">ARBOL DE DEPENDENCIAS</h2> -->
			<!-- 					</div> -->
			<!-- 					<hr> -->
			<!-- 				</div> -->
			<!-- 			</div> -->
			<div class="row clearfix">
				<form action="" id="treeForm">
					<div class="form-group ">
						<label for="to" class="col-sm-2 col-form-label lbtxt m-t-10">Numero
							Release:</label>
					
							<label id="releaseNumber" class="col-sm-6 col-form-label lbtxt m-t-10 col-cyan">Número Release</label>
						
						<label for="to" class="col-sm-2 col-form-label lbtxt m-t-10 ">Profundidad: 2</label>
					</div>
				</form>
			</div>
			<div class="row clearfix">
				<div class="col-sm-12 m-t-15 m-l-10">
					<button type="button" class="btn bg-light-green btn-xs">Producción</button>
					<button type="button" class="btn bg-orange btn-xs">Certificación</button>
					<button type="button" class="btn bg-green btn-xs">Solicitado</button>
					<button type="button" class="btn bg-normal-blue btn-xs">Borrador</button>
					<button type="button" class="btn bg-pink btn-xs">Anulado</button>
				</div>
				<div id="mynetwork"
					style="width: 97%; height: 450px; border: 1px solid lightgray; margin-left: 15px;">
					<div class="vis-network"
						style="position: relative; overflow: hidden; touch-action: pan-y; user-select: none; width: 100%; height: 100%;"
						tabindex="900">
						<canvas
							style="position: relative; touch-action: none; user-select: none; width: 100%; height: 100%;"
							width="600" height="600"></canvas>
					</div>
				</div>
			</div>
			<ul class='node-menu'>
				<li id="summary" data-id="" data-release="">Resumen</li>
				<li id="clipboard" data-id="" data-release="">Copiar número de release</li>
			</ul>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeTreeModal()">SALIR</button>
			</div>
		</div>
	</div>
</div>