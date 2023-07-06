
var $dtStatusRequest;
var $mdStatusRequest = $('#statusRequestModal');
var $fmStatusRequest = $('#statusRequestModalForm');

$(function() {
	activeItemMenu("releaseItem", true);
	initDataTable();
	initStatusRequestFormValidation();
});


function initDataTable() {
	$dtStatusRequest = $('#statusRequestTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/statusRequest/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'reason'
						},
						{
							"mDataProp" : 'description'
						},
						
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showStatusRequest('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteStatusRequest('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showStatusRequest(index){
	$fmStatusRequest.validate().resetForm();
	$fmStatusRequest[0].reset();
	var obj = $dtStatusRequest.row(index).data();
	$fmStatusRequest.find('#sId').val(obj.id);
	$fmStatusRequest.find('#sName').val(obj.name);
	$fmStatusRequest.find('#sCode').val(obj.code);
	$fmStatusRequest.find('#sMotive').val(obj.reason);
	$fmStatusRequest.find('#sDescription').val(obj.description);
	$mdStatusRequest.find('#update').show();
	$mdStatusRequest.find('#save').hide();
	$mdStatusRequest.modal('show');
}

function updateStatusRequest() {
	if (!$fmStatusRequest.valid())
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
				url : getCont() + "admin/statusRequest/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmStatusRequest.find('#sId').val(),
					name : $fmStatusRequest.find('#sName').val(),
					code : $fmStatusRequest.find('#sCode').val(),
					reason :$fmStatusRequest.find('#sMotive').val(),
					description:$fmStatusRequest.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRequest.ajax.reload();
					$mdStatusRequest.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveStatusRequest() {
	
	if (!$fmStatusRequest.valid())
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
				url : getCont() + "admin/statusRequest/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmStatusRequest.find('#sName').val(),
					code : $fmStatusRequest.find('#sCode').val(),
					reason :$fmStatusRequest.find('#sMotive').val(),
					description:$fmStatusRequest.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRequest.ajax.reload();
					$mdStatusRequest.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteStatusRequest(index) {
	var obj = $dtStatusRequest.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/statusRequest/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusRequest.ajax.reload();
					$mdStatusRequest.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addStatusRequest(){
	$fmStatusRequest.validate().resetForm();
	$fmStatusRequest[0].reset();
	$mdStatusRequest.find('#save').show();
	$mdStatusRequest.find('#update').hide();
	$mdStatusRequest.modal('show');
}

function closeStatusRequest(){
	$mdStatusRequest.modal('hide');
}

function initStatusRequestFormValidation() {
	$fmStatusRequest.validate({
		rules : {
			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},
			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},
			'sMotive' : {
				minlength : 1,
				maxlength : 50,
			},
			'sDescription' : {
				minlength : 1,
				maxlength : 100,
			}
		},
		messages : {
			'sCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sMotive' : {
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