<style>
.loaderInput {
	border: 4px solid #f3f3f3;
	border-radius: 50%;
	border-top: 4px solid #3498db;
	width: 20px;
	height: 20px;
	-webkit-animation: spin 2s linear infinite; /* Safari */
	animation: spin 2s linear infinite;
}

/* Safari */
@
-webkit-keyframes spin { 0% {
	-webkit-transform: rotate(0deg);
}

100%
{
-webkit-transform


:

 

rotate


(360
deg
);

 

}
}
@
keyframes spin { 0% {
	transform: rotate(0deg);
}

100%
{
transform


:

 

rotate


(360
deg
);

 

}
}
.dropzone {
	width: 200px;
	height: 200px;
	border: 2px dashed #ccc;
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	font-size: 24px;
	cursor: pointer;
}

.icon {
	font-size: 48px;
	margin-bottom: 10px;
}
</style>
<div class="modal fade" id="addFileModal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="largeModalLabel">Agregar un archivo</h4>
			</div>
			<div class="modal-body">

				<div class="row clearfix">

					<!-- File Upload From -->
					<form id="fileUploadForm" action="fileUpload"
						onsubmit="return false;" method="post"
						enctype="multipart/form-data">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 text-center" >
							<span class="btn btn-primary btn-block btn-lg fileinput-button"> <span>Selecccionar</span>
								<input id="files" type="file" name="files[]" multiple>
							</span>
						</div>

						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 p-t-20 fileListTable">
							<table id="tableFiles" class="table table-hover">
								<tbody>
								</tbody>
							</table>
						</div>

					</form>
					<br>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"
						style="display: none">
						<!-- Bootstrap Progress bar -->
						<div class="progress" style="margin-top: 15px;">
							<div id="progressBar"
								class="progress-bar progress-bar-success progress-bar-striped active"
								role="progressbar" aria-valuenow="0" aria-valuemin="0"
								aria-valuemax="100" style="width: 0%; color: black;">0%</div>
						</div>
					</div>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default waves-effect"
					onclick="closeAddFileModal()">CANCELAR</button>
				<button type="button" class="btn btn-primary waves-effect"
					onclick="upload()">GUARDAR</button>
			</div>
		</div>
	</div>
</div>