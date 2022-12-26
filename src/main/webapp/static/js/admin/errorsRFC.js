
var $dtStatusRFC;
var $mdStatusRFC = $('#statusRFCModal');
var $fmStatusRFC = $('#statusRFCModalForm');

$(function() {
	activeItemMenu("releaseItem", true);
	initDataTable();
	initStatusRFCFormValidation();
});


function initDataTable() {
	$dtStatusRFC = $('#statusRFCTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/error/rfc/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'description'
						},
						
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showStatusRFC('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteStatusRFC('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showStatusRFC(index){
	$fmStatusRFC.validate().resetForm();
	$fmStatusRFC[0].reset();
	var obj = $dtStatusRFC.row(index).data();
	$fmStatusRFC.find('#sId').val(obj.id);
	$fmStatusRFC.find('#sName').val(obj.name);
	$fmStatusRFC.find('#sDescription').val(obj.description);
	$mdStatusRFC.find('#update').show();
	$mdStatusRFC.find('#save').hide();
	$mdStatusRFC.modal('show');
}

function updateStatusRFC() {
	if (!$fmStatusRFC.valid())
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
				url : getCont() + "admin/error/rfc" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmStatusRFC.find('#sId').val(),
					name : $fmStatusRFC.find('#sName').val(),
					description:$fmStatusRFC.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRFC.ajax.reload();
					$mdStatusRFC.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveStatusRFC() {
	
	if (!$fmStatusRFC.valid())
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
				url : getCont() + "admin/error/rfc" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmStatusRFC.find('#sName').val(),
					description:$fmStatusRFC.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRFC.ajax.reload();
					$mdStatusRFC.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteStatusRFC(index) {
	var obj = $dtStatusRFC.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/error/rfc/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRFC.ajax.reload();
					$mdStatusRFC.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addStatusRFC(){
	$fmStatusRFC.validate().resetForm();
	$fmStatusRFC[0].reset();
	$mdStatusRFC.find('#save').show();
	$mdStatusRFC.find('#update').hide();
	$mdStatusRFC.modal('show');
}

function closeStatusRFC(){
	$mdStatusRFC.modal('hide');
}

function initStatusRFCFormValidation() {
	$fmStatusRFC.validate({
		rules : {
			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},
			'sDescription' : {
				minlength : 1,
				maxlength : 100,
			}
		},
		messages : {

			'sName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sDescription' : {
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}