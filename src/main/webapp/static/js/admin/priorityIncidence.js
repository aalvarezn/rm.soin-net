
var $dtPriorityIncidence;
var $mdPriorityIncidence = $('#priorityIncidenceModal');
var $fmPriorityIncidence = $('#priorityIncidenceModalForm');

$(function() {
	activeItemMenu("releaseItem", true);
	initDataTable();
	initPriorityIncidenceFormValidation();
});


function initDataTable() {
	$dtPriorityIncidence = $('#priorityIncidenceTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/priorityIncidence/list",
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

								options += '<a onclick="showPriorityIncidence('
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
				url : getCont() + "admin/priorityIncidence/"+index ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
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
function showPriorityIncidence(index){
	$fmPriorityIncidence.validate().resetForm();
	$fmPriorityIncidence[0].reset();
	var obj = $dtPriorityIncidence.row(index).data();
	$fmPriorityIncidence.find('#sId').val(obj.id);
	$fmPriorityIncidence.find('#sCode').val(obj.name);
	$fmPriorityIncidence.find('#sDescription').val(obj.description);
	$mdPriorityIncidence.find('#update').show();
	$mdPriorityIncidence.find('#save').hide();
	$mdPriorityIncidence.modal('show');
}

function updatePriorityIncidence() {
	if (!$fmPriorityIncidence.valid())
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
				url : getCont() + "admin/priorityIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmPriorityIncidence.find('#sId').val(),
					name : $fmPriorityIncidence.find('#sCode').val(),
					description:$fmPriorityIncidence.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function savePriorityIncidence() {
	
	if (!$fmPriorityIncidence.valid())
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
				url : getCont() + "admin/priorityIncidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmPriorityIncidence.find('#sCode').val(),
					description:$fmPriorityIncidence.find('#sDescription').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deletePriorityIncidence(index) {
	var obj = $dtPriorityIncidence.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/priorityIncidence/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addPriorityIncidence(){
	$fmPriorityIncidence.validate().resetForm();
	$fmPriorityIncidence[0].reset();
	$mdPriorityIncidence.find('#save').show();
	$mdPriorityIncidence.find('#update').hide();
	$mdPriorityIncidence.modal('show');
}

function closePriorityIncidence(){
	$mdPriorityIncidence.modal('hide');
}

function initPriorityIncidenceFormValidation() {
	$fmPriorityIncidence.validate({
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
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}