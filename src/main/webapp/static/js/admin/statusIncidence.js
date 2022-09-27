
var $dtStatusIncidence;
var $mdStatusIncidence = $('#statusIncidenceModal');
var $fmStatusIncidence = $('#statusIncidenceModalForm');

$(function() {
	activeItemMenu("incidenceManagementItem", true);
	initDataTable();
	initStatusIncidenceFormValidation();
});


function initDataTable() {
	$dtStatusIncidence = $('#statusIncidenceTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "statusIncidence/list",
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

								options += '<a onclick="showStatusIncidence('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteStatusIncidence('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showStatusIncidence(index){
	$fmStatusIncidence.validate().resetForm();
	$fmStatusIncidence[0].reset();
	var obj = $dtStatusIncidence.row(index).data();
	$fmStatusIncidence.find('#sId').val(obj.id);
	$fmStatusIncidence.find('#sName').val(obj.name);
	$fmStatusIncidence.find('#sCode').val(obj.code);
	$fmStatusIncidence.find('#sMotive').val(obj.reason);
	$fmStatusIncidence.find('#sDescription').val(obj.description);
	$mdStatusIncidence.find('#update').show();
	$mdStatusIncidence.find('#save').hide();
	$mdStatusIncidence.modal('show');
}

function updateStatusIncidence() {
	if (!$fmStatusIncidence.valid())
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
				url : getCont() + "statusIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmStatusIncidence.find('#sId').val(),
					name : $fmStatusIncidence.find('#sName').val(),
					code : $fmStatusIncidence.find('#sCode').val(),
					reason :$fmStatusIncidence.find('#sMotive').val(),
					description:$fmStatusIncidence.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusIncidence.ajax.reload();
					$mdStatusIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveStatusIncidence() {
	
	if (!$fmStatusIncidence.valid())
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
				url : getCont() + "statusIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmStatusIncidence.find('#sName').val(),
					code : $fmStatusIncidence.find('#sCode').val(),
					reason :$fmStatusIncidence.find('#sMotive').val(),
					description:$fmStatusIncidence.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusIncidence.ajax.reload();
					$mdStatusIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteStatusIncidence(index) {
	var obj = $dtStatusIncidence.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "statusIncidence/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusIncidence.ajax.reload();
					$mdStatusIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addStatusIncidence(){
	$fmStatusIncidence.validate().resetForm();
	$fmStatusIncidence[0].reset();
	$mdStatusIncidence.find('#save').show();
	$mdStatusIncidence.find('#update').hide();
	$mdStatusIncidence.modal('show');
}

function closeStatusIncidence(){
	$mdStatusIncidence.modal('hide');
}

function initStatusIncidenceFormValidation() {
	$fmStatusIncidence.validate({
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