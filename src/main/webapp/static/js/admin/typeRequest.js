var $dtTypeRequest;
var $mdTypeRequest = $('#typeRequestModal');
var $fmTypeRequest = $('#typeRequestModalForm');

$(function() {
	activeItemMenu("requestItem", true);
	initDataTable();
	initTypeRequestFormValidation();
});


function initDataTable() {
	$dtTypeRequest = $('#typeRequestTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/typeRequest/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'description'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showTypeRequest('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteTypeRequest('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showTypeRequest(index){
	$fmTypeRequest.validate().resetForm();
	$fmTypeRequest[0].reset();
	var obj = $dtTypeRequest.row(index).data();
	$fmTypeRequest.find('#tId').val(obj.id);
	$fmTypeRequest.find('#tCode').val(obj.code);
	$fmTypeRequest.find('#tDescription').val(obj.description);
	$mdTypeRequest.find('#update').show();
	$mdTypeRequest.find('#save').hide();
	$mdTypeRequest.modal('show');
}

function updateTypeRequest() {
	if (!$fmTypeRequest.valid())
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
				url : getCont() + "admin/typeRequest/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmTypeRequest.find('#tId').val(),
					code : $fmTypeRequest.find('#tCode').val(),
					description : $fmTypeRequest.find('#tDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeRequest.ajax.reload();
					$mdTypeRequest.modal('hide');
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


function saveTypeRequest() {
	if (!$fmTypeRequest.valid())
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
				url : getCont() + "admin/typeRequest/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmTypeRequest.find('#tCode').val(),
					description : $fmTypeRequest.find('#tDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeRequest.ajax.reload();
					$mdTypeRequest.modal('hide');
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

function deleteTypeRequest(index) {
	var obj = $dtTypeRequest.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/typeRequest/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeRequest.ajax.reload();
					$mdTypeRequest.modal('hide');
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

function addTypeRequest(){
	$fmTypeRequest.validate().resetForm();
	$fmTypeRequest[0].reset();
	$mdTypeRequest.find('#save').show();
	$mdTypeRequest.find('#update').hide();
	$mdTypeRequest.modal('show');
}

function closeTypeRequest(){
	$mdTypeRequest.modal('hide');
}

function initTypeRequestFormValidation() {
	$fmTypeRequest.validate({
		rules : {
			'tCode' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'tDescription' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
		},
		messages : {
			'tCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'tDescription' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 100 caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}
