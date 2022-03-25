var $dtRequest;
var $mdRequest = $('#requestModal');
var $fmRequest = $('#requestModalForm');

$(function() {
	activeItemMenu("requestItem", true);
	initDataTable();
	initRequestFormValidation();
});


function initDataTable() {
	$dtRequest = $('#requestTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/request/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'codeSoin'
						},
						{
							"mDataProp" : 'codeIce'
						},
						{
							"mDataProp" : 'description'
						},
						{
							"mDataProp" : 'project.code'
						},
						{
							"mDataProp" : 'typeRequest.code'
						},
						{
							render:function(data,type,row,meta){
								var option='<div class="iconLine align-center">';
								if(!row.active){
									option+='<i id="softDeleteRequest_'
										+meta.row
										+'"onclick="softDeleteRequest('
										+meta.row+',' 
										+row.active
										+')"class="material-icons gris" style="font-size: 30px;">cancel</i>';
									option+='</div>';
								}else{
									option+='<i id="softDeleteRequest_'
										+meta.row
										+'"onclick="softDeleteRequest('
										+meta.row+',' 
										+row.active
										+')"class="material-icons gris" style="font-size: 30px;">check_circle</i>';
									option+='</div>';
								}

								return option;
							}	
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showRequest('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteRequest('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showRequest(index){
	$fmRequest.validate().resetForm();
	$fmRequest[0].reset();
	var obj = $dtRequest.row(index).data();
	$fmRequest.find('#rId').val(obj.id);
	$fmRequest.find('#rCode_soin').val(obj.codeSoin);
	$fmRequest.find('#rCode_ice').val(obj.codeIce);
	$fmRequest.find('#rDescription').val(obj.description);
	$fmRequest.find('#rStatus').val(obj.status);
	$fmRequest.find('#rSoinManagement').val(obj.managerSoin);
	$fmRequest.find('#rIceManagement').val(obj.managerIce);
	$fmRequest.find('#rLeaderSoin').val(obj.leaderTecSoin);
	$fmRequest.find('#rLiderIce').val(obj.leaderTecIce);
	$fmRequest.find('#rProjectId').selectpicker('val',obj.project.id);
	$fmRequest.find('#rTypeRequestId').selectpicker('val',obj.typeRequest.id);
	$mdRequest.find('#update').show();
	$mdRequest.find('#save').hide();
	$mdRequest.modal('show');
}

function updateRequest() {
	if (!$mdRequest.valid())
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
				url : getCont() + "admin/request/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmRequest.find('#rId').val(),
					codeSoin : $fmRequest.find('#rCode_soin').val(),
					codeIce : $fmRequest.find('#rCode_ice').val(),
					description : $fmRequest.find('#rDescription').val(),
					status : $fmRequest.find('#rStatus').val(),
					managerSoin:$fmRequest.find('#rSoinManagement').val(),
					managerIce:$fmRequest.find('#rIceManagement').val(),
					leaderTecSoin:$fmRequest.find('#rLeaderSoin').val(),
					leaderTecIce:$fmRequest.find('#rLiderIce').val(),
					projectId:$fmRequest.find('#rProjectId').val(),
					typeRequestId:$fmRequest.find('#rTypeRequestId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRequest.ajax.reload();
					$mdRequest.modal('hide');
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


function saveRequest() {
	if (!$fmRequest.valid())
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
				url : getCont() + "admin/request/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmRequest.find('#rId').val(),
					codeSoin : $fmRequest.find('#rCode_soin').val(),
					codeIce : $fmRequest.find('#rCode_ice').val(obj.codeIce),
					description : $fmRequest.find('#rDescription').val(),
					status : $fmRequest.find('#rStatus').val(),
					managerSoin:$fmRequest.find('#rSoinManagement').val(),
					managerIce:$fmRequest.find('#rIceManagement').val(),
					leaderTecSoin:$fmRequest.find('#rLeaderSoin').val(),
					leaderTecIce:$fmRequest.find('#rLiderIce').val(),
					projectId:$fmRequest.find('#rProjectId').val(),
					typeRequestId:$fmRequest.find('#rTypeRequestId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRequest.ajax.reload();
					$mdRequest.modal('hide');
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

function deleteRequest(index) {
	var obj = $dtRequest.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/request/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRequest.ajax.reload();
					$mdRequest.modal('hide');
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

//------ Soft-Delete Request ------
function softDeleteRequest(index, active) {	
	let textTittle = (active) ? 'desactivar':'activar';
	let textMessage = (active) ? 'activado':'desactivado';
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea '+textTittle+' el requerimiento?',
		text: "El requerimiento puede ser "+textMessage+" nuevamente.",
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/request/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRequest.ajax.reload();
					$mdRequest.modal('hide');
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


function addRequest(){
	$fmRequest.validate().resetForm();
	$fmRequest[0].reset();
	$fmRequest.find('#rProjectId').selectpicker('val',"");
	$fmRequest.find('#rTypeRequestId').selectpicker('val',"");
	$mdRequest.find('#save').show();
	$mdRequest.find('#update').hide();
	$mdRequest.modal('show');
}

function closeRequestModal(){
	$mdRequest.modal('hide');
}

function initRequestFormValidation() {
	$fmRequest.validate({
		rules : {
			'iName' : {
				required : true,
				minlength : 1,
				maxlength : 50
			},
			'iDescription' : {
				required : true,
				minlength : 1,
				maxlength : 250,
			},
		},
		messages : {
			'iName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'iDescription' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 250 caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}


/*
$(function() {
	activeItemMenu("requestItem", true);
});
var $requestTable = $('#requestTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $requestModal = $('#requestModal');
var $requestModalForm = $('#requestModalForm');

$('#proyectFilter').on( 'change', function () {
    $requestTable
        .columns( 3 )
        .search( this.value )
        .draw();
} );

$('#typeRequestFilter').on( 'change', function () {
    $requestTable
        .columns( 4 )
        .search( this.value )
        .draw();
} );


function openRequestModal() {
	resetErrors();
	$requestModalForm[0].reset();
	$requestModalForm.find("#proyectId").selectpicker('val', '');
	$requestModalForm.find("#typeRequestId").selectpicker('val', '');
	$('#btnUpdateRequest').hide();
	$('#btnSaveRequest').show();
	$requestModal.modal('show');
}

function saveRequest() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/request/" + "saveRequest",
		data : {
			// Informacion requerimientos
			id : 0,
			code_soin: $requestModalForm.find('#code_soin').val(),
			code_ice: $requestModalForm.find('#code_ice').val(),
			description: $requestModalForm.find('#description').val(),
			status: $requestModalForm.find('#status').val(),
			soinManagement: $requestModalForm.find('#soinManagement').val(),
			iceManagement: $requestModalForm.find('#iceManagement').val(),
			leaderSoin: $requestModalForm.find('#leaderSoin').val(),
			liderIce: $requestModalForm.find('#liderIce').val(),
			proyectId: $requestModalForm.find("#proyectId").children("option:selected").val(),
			typeRequestId: $requestModalForm.find("#typeRequestId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveRequest(response)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			unblockUI();
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSaveRequest(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Requerimiento creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showRequestErrors(response.errors, $requestModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateRequestModal(index) {
	resetErrors();
	$('#btnUpdateRequest').show();
	$('#btnSaveRequest').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "request/findRequest/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$requestModalForm.find('#requestId').val(index);
			$requestModalForm.find('#code_soin').val(response.code_soin);
			$requestModalForm.find('#code_ice').val(response.code_ice);
			$requestModalForm.find('#description').val(response.description);
			$requestModalForm.find('#status').val(response.status);
			$requestModalForm.find('#soinManagement').val(response.soinManagement);
			$requestModalForm.find('#iceManagement').val(response.iceManagement);
			$requestModalForm.find('#leaderSoin').val(response.leaderSoin);
			$requestModalForm.find('#liderIce').val(response.liderIce);
			$requestModalForm.find("#typeRequestId").selectpicker('val', response.typeRequest.id);
			$requestModalForm.find("#proyectId").selectpicker('val', response.proyect.id);
			$requestModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeRequestModal() {
	$requestModalForm[0].reset();
	$requestModal.modal('hide');
}

function updateRequest() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/request/" + "updateRequest",
		data : {
			// Informacion requerimiento
			id: $requestModalForm.find('#requestId').val(),
			code_soin: $requestModalForm.find('#code_soin').val(),
			code_ice: $requestModalForm.find('#code_ice').val(),
			description: $requestModalForm.find('#description').val(),
			status: $requestModalForm.find('#status').val(),
			soinManagement: $requestModalForm.find('#soinManagement').val(),
			iceManagement: $requestModalForm.find('#iceManagement').val(),
			leaderSoin: $requestModalForm.find('#leaderSoin').val(),
			liderIce: $requestModalForm.find('#liderIce').val(),
			proyectId: $requestModalForm.find("#proyectId").children("option:selected").val(),
			typeRequestId: $requestModalForm.find("#typeRequestId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateRequest(response)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			unblockUI();
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxUpdateRequest(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Requerimiento modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showRequestErrors(response.errors, $requestModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteRequest(element) {
	Swal.fire({
		  title: '\u00BFEst\u00e1s seguro que desea eliminar?',
		  text: "Esta acci\u00F3n no se puede reversar.",
		  icon: 'question',
		  showCancelButton: true,
		  customClass: 'swal-wide',
		  cancelButtonText: 'Cancelar',
		  cancelButtonColor: '#f14747',
		  confirmButtonColor: '#3085d6',
		  confirmButtonText: 'Aceptar',
		}).then((result) => {
			if(result.value){
				deleteRequest(element);
			}		
		});
}

function deleteRequest(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "request/deleteRequest/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteRequest(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteRequest(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El requerimiento ha sido eliminado exitosamente.",
				"success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
}

function showRequestErrors(error, $form) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$form.find(" #" + error[i].key + "_error").text(error[i].message);
		$form.find(" #" + error[i].key + "_error").css("visibility", "visible");
		$form.find(" #" + error[i].key + "_error").attr("class",
				"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($form.find(" #" + error[i].key).is("input")
				|| $form.find(" #" + error[i].key).is("textarea")) {
			$form.find(" #" + error[i].key).parent().attr("class",
					"form-line error focused fieldErrorLine");
		}
	}
}

//------ Soft-Delete Request ------
function softDeleteRequest(index, active) {	
	let textTittle = (active) ? 'desactivar':'activar';
	let textMessage = (active) ? 'activado':'desactivado';
	
	Swal.fire({
		  title: '\u00BFEst\u00e1s seguro que desea '+textTittle+' el requerimiento?',
		  text: "El requerimiento puede ser "+textMessage+" nuevamente.",
		  icon: 'question',
		  showCancelButton: true,
		  customClass: 'swal-wide',
		  cancelButtonText: 'Cancelar',
		  cancelButtonColor: '#f14747',
		  confirmButtonColor: '#3085d6',
		  confirmButtonText: 'Aceptar',
		}).then((result) => {
			if(result.value){
				deleteSoftRequest(index)
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
}*/
// ------ #Soft-Delete Request ------
