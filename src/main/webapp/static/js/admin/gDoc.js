var $dtGDoc;
var $mdGDoc = $('#gDocModal');
var $fmGDoc = $('#gDocModalForm');
$(document).ready(function () {
	
	activeItemMenu("documentItem", true);
	initDataTable();
	initGDoc();
});



function initDataTable() {
	$dtGDoc = $('#gDocTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/gDoc/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'project.description'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showGDoc('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteGDoc('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showGDoc(index){
	$fmGDoc.validate().resetForm();
	$fmGDoc[0].reset();
	var obj = $dtGDoc.row(index).data();
	$fmGDoc.find('#gId').val(obj.id);
	$fmGDoc.find('#gDescription').val(obj.name);
	$fmGDoc.find('#gSpreadSheet').val(obj.spreadSheet);
	$fmGDoc.find('#gCredentials').val(obj.credentials);
	$fmGDoc	.find('#pId').selectpicker('val',obj.project.id);
	$mdGDoc.find('#update').show();
	$mdGDoc.find('#save').hide();
	$mdGDoc.modal('show');
}

function updateGDoc() {
	if (!$fmGDoc.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea actualizar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/gDoc/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					 id : $fmGDoc.find('#gId').val(),
					 name:	$fmGDoc.find('#gDescription').val(),
					 spreadSheet: $fmGDoc.find('#gSpreadSheet').val(),
					 credentials: $fmGDoc.find('#gCredentials').val(),
					 projectId: $fmGDoc.find('#pId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtGDoc.ajax.reload();
					$mdGDoc.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}


function saveGDoc() {
	if (!$fmGDoc.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea crear el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "POST",
				url : getCont() + "admin/gDoc/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					 name:	$fmGDoc.find('#gDescription').val(),
					 spreadSheet: $fmGDoc.find('#gSpreadSheet').val(),
					 credentials: $fmGDoc.find('#gCredentials').val(),
					 projectId: $fmGDoc.find('#pId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtGDoc.ajax.reload();
					$mdGDoc.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}

function deleteGDoc(index) {
	var obj = $dtGDoc.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/gDoc/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtGDoc.ajax.reload();
					$mdGDoc.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}

function addGDoc(){
	$fmGDoc.validate().resetForm();
	$fmGDoc[0].reset();
	$fmGDoc	.find('#pId').selectpicker('val',"");
	$mdGDoc.find('#save').show();
	$mdGDoc.find('#update').hide();
	$mdGDoc.modal('show');
}

function closeGDoc(){
	$mdGDoc.modal('hide');
}

function initGDoc() {
	$fmGDoc.validate({
		rules : {
			'gDescription' : {
				required : true,
				minlength : 1,
				maxlength : 200,
			},
			'gSpreadSheet' : {
				required : true,
				minlength : 1,
				maxlength : 500
			},
			'pId' : {
				required : true,
			},
			'gCredentials' : {
				required : true,
				minlength : 1
			},
		},
		messages : {
			'gDescription' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'gSpreadSheet' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'pId' : {
				required : "Ingrese un valor",
			},
			'gCredentials' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}
