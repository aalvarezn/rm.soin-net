var $dtStatus;
var $mdStatus = $('#statusModal');
var $fmStatus = $('#statusModalForm');

$(function() {
	activeItemMenu("catalogueItem", true);
	initDataTable();
	initStatusFormValidation();
});


function initDataTable() {
	$dtStatus = $('#statusTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/status/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'description'
						},
						{
							"mDataProp" : 'reason'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showStatus('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteStatus('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showStatus(index){
	$fmStatus.validate().resetForm();
	$fmStatus[0].reset();
	var obj = $dtStatus.row(index).data();
	$fmStatus.find('#sId').val(obj.id);
	$fmStatus.find('#sCode').val(obj.code);
	$fmStatus.find('#sName').val(obj.name);
	$fmStatus.find('#sDescription').val(obj.description);
	$fmStatus.find('#sReason').val(obj.reason);
	$mdStatus.find('#update').show();
	$mdStatus.find('#save').hide();
	$mdStatus.modal('show');
}

function updateStatus() {
	if (!$fmStatus.valid())
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
				url : getCont() + "admin/status/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmStatus.find('#sId').val(),
					code : $fmStatus.find('#sCode').val(),
					name : $fmStatus.find('#sName').val(),
					description : $fmStatus.find('#sDescription').val(),
					reason : $fmStatus.find('#sReason').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatus.ajax.reload();
					$mdStatus.modal('hide');
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


function saveStatus() {
	if (!$fmStatus.valid())
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
				url : getCont() + "admin/status/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmStatus.find('#sCode').val(),
					name : $fmStatus.find('#sName').val(),
					description : $fmStatus.find('#sDescription').val(),
					reason : $fmStatus.find('#sReason').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatus.ajax.reload();
					$mdStatus.modal('hide');
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

function deleteStatus(index) {
	var obj = $dtStatus.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/status/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatus.ajax.reload();
					$mdStatus.modal('hide');
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

function addStatus(){
	$fmStatus.validate().resetForm();
	$fmStatus[0].reset();
	$mdStatus.find('#save').show();
	$mdStatus.find('#update').hide();
	$mdStatus.modal('show');
}

function closeStatus(){
	$mdStatus.modal('hide');
}

function initStatusFormValidation() {
	$fmStatus.validate({
		rules : {
			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 30,
			},
			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 25
			},
			'sDescription' : {
				required : true,
				minlength : 1,
				maxlength : 150
			},
			'sReason' : {
				required : true,
				minlength : 1,
				maxlength : 200
			},
		},
		messages : {
			'sCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 30 caracteres"
			},
			'sName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 25 caracteres"
			},
			'sDescription' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 150 caracteres"
			},
			'sReason' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 200 caracteres"
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}