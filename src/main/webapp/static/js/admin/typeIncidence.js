
var $dtTypeIncidence;
var $mdTypeIncidence = $('#typeIncidenceModal');
var $fmTypeIncidence = $('#typeIncidenceModalForm');

$(function() {
	activeItemMenu("ticketsItem", true);
	initDataTable();
	initTypeIncidenceFormValidation();
});


function initDataTable() {
	$dtTypeIncidence = $('#typeIncidenceTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/typeIncidence/list",
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
								var options = '<div class="iconLineC">';

								options += '<a onclick="showTypeIncidence('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

			
								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function softDeleteRequest(index) {	
	console.log(index);
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea modificar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/typeIncidence/"+index ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeIncidence.ajax.reload();
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function deleteSoftRequest(index) {
			let cont = getCont();
			blockUI();
			$.ajax({
				async : false,
				type : "POST",
				url : cont + "admin/request/" + "softDelete",
				timeout : 60000,
				data : {
					requestId : index
				},
				success : function(response) {
					ajaxDeleteSoftRequest(response, index);
				},
				error : function(x, t, m) {
					notifyAjaxError(x, t, m);
				}
			});
}

function ajaxDeleteSoftRequest(response, index) {
 switch (response.status) {
 case 'success':
	$('#requestTable').find('#softDeleteRequest_'+index).attr("onclick",'softDeleteRequest('+index+','+response.obj+')');
		if(response.obj == true){
		$('#requestTable').find('#softDeleteRequest_'+index).text('check_circle');
		swal("Correcto!", "Requerimiento activado correctamente.", "success", 2000)
		}else{
		$('#requestTable').find('#softDeleteRequest_'+index).text('cancel');
		swal("Correcto!", "Requerimiento desactivado correctamente.", "success", 2000)
		}
		break;
		case 'fail':
		swal("Error!", response.exception, "error")
		break;
		case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
		default:
		location.reload();
		}
}
function showTypeIncidence(index){
	$fmTypeIncidence.validate().resetForm();
	$fmTypeIncidence[0].reset();
	var obj = $dtTypeIncidence.row(index).data();
	$fmTypeIncidence.find('#sId').val(obj.id);
	$fmTypeIncidence.find('#sCode').val(obj.code);
	$fmTypeIncidence.find('#sDescription').val(obj.description);
	$mdTypeIncidence.find('#update').show();
	$mdTypeIncidence.find('#save').hide();
	$mdTypeIncidence.modal('show');
}

function updateTypeIncidence() {
	if (!$fmTypeIncidence.valid())
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
				url : getCont() + "admin/typeIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmTypeIncidence.find('#sId').val(),
					code : $fmTypeIncidence.find('#sCode').val(),
					description:$fmTypeIncidence.find('#sDescription').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeIncidence.ajax.reload();
					$mdTypeIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveTypeIncidence() {
	
	if (!$fmTypeIncidence.valid())
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
				url : getCont() + "admin/typeIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmTypeIncidence.find('#sCode').val(),
					description:$fmTypeIncidence.find('#sDescription').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeIncidence.ajax.reload();
					$mdTypeIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteTypeIncidence(index) {
	var obj = $dtTypeIncidence.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/typeIncidence/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeIncidence.ajax.reload();
					$mdTypeIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addTypeIncidence(){
	$fmTypeIncidence.validate().resetForm();
	$fmTypeIncidence[0].reset();
	$mdTypeIncidence.find('#save').show();
	$mdTypeIncidence.find('#update').hide();
	$mdTypeIncidence.modal('show');
}

function closeTypeIncidence(){
	$mdTypeIncidence.modal('hide');
}

function initTypeIncidenceFormValidation() {
	$fmTypeIncidence.validate({
		rules : {

			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},

			'sDescription' : {
				required : true,
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
			'sDescription' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}