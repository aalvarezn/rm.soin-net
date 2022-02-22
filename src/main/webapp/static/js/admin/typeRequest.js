$(function() {
	activeItemMenu("requestItem", true);
});
var $typeRequestTable = $('#typeRequestTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $typeRequestModal = $('#typeRequestModal');
var $typeRequestModalForm = $('#typeRequestModalForm');

function openTypeRequestModal() {
	resetErrors();
	$typeRequestModalForm[0].reset();
	$('#btnUpdateTypeRequest').hide();
	$('#btnSaveTypeRequest').show();
	$typeRequestModal.modal('show');
}

function saveTypeRequest() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeRequest/" + "saveTypeRequest",
		data : {
			// Informacion tipo requerimientos
			id : 0,
			code : $typeRequestModalForm.find('#code').val(),
			description : $typeRequestModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveTypeRequest(response)
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

function ajaxSaveTypeRequest(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo requerimiento creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeRequestErrors(response.errors, $typeRequestModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateTypeRequestModal(index) {
	resetErrors();
	$('#btnUpdateTypeRequest').show();
	$('#btnSaveTypeRequest').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "typeRequest/findTypeRequest/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$typeRequestModalForm.find('#typeRequestId').val(index);
			$typeRequestModalForm.find('#code').val(response.code);
			$typeRequestModalForm.find('#description').val(response.description);
			$typeRequestModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeTypeRequestModal() {
	$typeRequestModalForm[0].reset();
	$typeRequestModal.modal('hide');
}

function updateTypeRequest() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeRequest/" + "updateTypeRequest",
		data : {
			// Informacion tipo requerimiento
			id : $typeRequestModalForm.find('#typeRequestId').val(),
			code : $typeRequestModalForm.find('#code').val(),
			description : $typeRequestModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateTypeRequest(response)
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

function ajaxUpdateTypeRequest(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo requerimiento modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeRequestErrors(response.errors, $typeRequestModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteTypeRequest(element) {
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
				deleteTypeRequest(element);
			}		
		});
}

function deleteTypeRequest(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "typeRequest/deleteTypeRequest/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteTypeRequest(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteTypeRequest(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El tipo requerimiento ha sido eliminado exitosamente.",
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

function showTypeRequestErrors(error, $form) {
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
